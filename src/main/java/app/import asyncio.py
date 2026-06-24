import asyncio
import json
import re
from typing import Optional

import aiohttp
from fastapi import FastAPI, WebSocket, WebSocketDisconnect, Query

# ===============================
# CONFIG
# ===============================

SERVER_API_KEY = "fan-ui-dev-key"

OLLAMA_URL = "http://127.0.0.1:11434/api/generate"
OLLAMA_MODEL = "phi"

ATOMBERG_API_KEY = "dH4I3OyCAENYmPkUja7MwHCncyiSuBCpdbB4HVmy"
ATOMBERG_REFRESH_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Imdvb2dsZV8xMDE5MDk5MTY0OTU5OTg5NjM1MzciLCJ0eXBlIjoicmVmcmVzaCIsImlzcyI6ImRldmVsb3Blci5hdG9tYmVyZy1pb3QuY29tIiwiZGV2ZWxvcGVyX2lkIjoiaVRPZndSYWxveiIsImp0aSI6IjI3ZDEwY2ViLTM1MzEtNGZjZS1iNzgzLTdhOWRhMmQ0Yzg0MSIsImlhdCI6MTc3MTQwMzg3MSwiZXhwIjoyMDg2NzYzODcxfQ.8swqkFmZa3LwQBhof5IveaTwH22MgKARrPeygYd2mYs"

ATOMBERG_TOKEN_URL = "https://api.developer.atomberg-iot.com/v1/get_access_token"
ATOMBERG_CONTROL_URL = "https://api.developer.atomberg-iot.com/v1/send_command"

ROOM_TO_DEVICE_ID = {
    "living room": "3030f9c92258",
    "bedroom": "DEVICE_ID_2",
}

DEFAULT_ROOM = "living room"

# ===============================
# APP
# ===============================

app = FastAPI()

# ===============================
# TOKEN CACHE
# ===============================

_atomberg_token: Optional[str] = None
_token_lock = asyncio.Lock()


async def get_atomberg_access_token(session: aiohttp.ClientSession) -> str:
    global _atomberg_token

    async with _token_lock:
        if _atomberg_token:
            return _atomberg_token

        async with session.get(
            ATOMBERG_TOKEN_URL,
            headers={
                "x-api-key": ATOMBERG_API_KEY,
                "Authorization": f"Bearer {ATOMBERG_REFRESH_TOKEN}",
            },
            ssl=True,
            timeout=aiohttp.ClientTimeout(total=10),
        ) as resp:
            if resp.status in (401, 403):
                raise RuntimeError("Atomberg refresh token rejected")

            resp.raise_for_status()
            data = await resp.json()

            token = data.get("message", {}).get("access_token")
            if not token:
                raise RuntimeError(f"Invalid token response: {data}")

            _atomberg_token = token
            return token


# ===============================
# FAN CONTROL (FIXED)
# ===============================

async def control_fan(
    session: aiohttp.ClientSession,
    room: str,
    power: Optional[bool],
    speed: Optional[int],
):
    global _atomberg_token

    device_id = ROOM_TO_DEVICE_ID.get(room)
    if not device_id:
        raise ValueError(f"No device mapped for room '{room}'")

    token = await get_atomberg_access_token(session)

    command = {}

    if power is not None:
        command["power"] = power

    if speed is not None:
        command["speed"] = speed
        command.setdefault("power", True)  # REQUIRED by Atomberg

    payload = {
        "device_id": device_id,
        "command": command,   # 🔥 FIXED (NOT list)
    }

    async with session.post(
        ATOMBERG_CONTROL_URL,
        headers={
            "x-api-key": ATOMBERG_API_KEY,
            "Authorization": f"Bearer {token}",
            "Content-Type": "application/json",
        },
        json=payload,
        ssl=True,
        timeout=aiohttp.ClientTimeout(total=10),
    ) as resp:

        if resp.status == 401:
            _atomberg_token = None
            return await control_fan(session, room, power, speed)

        if resp.status == 403:
            raise RuntimeError("Atomberg forbidden")

        resp.raise_for_status()
        return await resp.json()


# ===============================
# INTENT PARSING
# ===============================

def regex_parse(text: str):
    text = text.lower()
    room = DEFAULT_ROOM
    for r in ROOM_TO_DEVICE_ID:
        if r in text:
            room = r

    power = None
    speed = None

    if "turn on" in text:
        power = True
    elif "turn off" in text:
        power = False

    m = re.search(r"speed\s*(to)?\s*(\d)", text)
    if m:
        speed = int(m.group(2))

    return {"room": room, "power": power, "speed": speed}


async def parse_intent(command: str, session: aiohttp.ClientSession):
    payload = {
        "model": OLLAMA_MODEL,
        "prompt": command,
        "stream": False,
        "options": {"temperature": 0},
    }

    try:
        async with session.post(
            OLLAMA_URL,
            json=payload,
            timeout=aiohttp.ClientTimeout(total=6),
        ) as resp:
            resp.raise_for_status()
            raw = (await resp.json()).get("response", "")
            return json.loads(raw)
    except Exception:
        return regex_parse(command)


# ===============================
# WEBSOCKET
# ===============================

@app.websocket("/ws")
async def websocket_endpoint(ws: WebSocket, token: str = Query(None)):
    if token != SERVER_API_KEY:
        await ws.close(code=1008)
        return

    await ws.accept()
    await ws.send_text("🟢 Connected to Atomberg Smart Fan Controller")

    async with aiohttp.ClientSession() as session:
        try:
            while True:
                text = await ws.receive_text()
                await ws.send_text("🤖 Executing...")

                intent = await parse_intent(text, session)
                await ws.send_text("⏳ Sending command...")

                await control_fan(
                    session=session,
                    room=intent["room"],
                    power=intent["power"],
                    speed=intent["speed"],
                )

                await ws.send_text(f"✅ Executed: {intent}")

        except WebSocketDisconnect:
            pass
        except Exception as e:
            await ws.send_text(f"❌ Error: {e}")