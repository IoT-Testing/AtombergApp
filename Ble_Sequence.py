import asyncio
import json
from bleak import BleakClient

command = {
    "routine": "S",
    "seq": "B10B10B10B10B10B10B10B10B10B10B10B10B10B10B10B10B10B10B10B10B10B10"
}
payload = json.dumps(command).encode('utf-8')

device_address = "28:37:2F:6E:D6:02"        # MAC/BLE address or name
write_characteristic_uuid = "e29ee02c-af3d-11ec-b909-0242ac120002" 

async def send_ble():
    async with BleakClient(device_address) as client:
        await client.write_gatt_char(write_characteristic_uuid, payload, response=True)
        print(f"✓ Sent {len(payload)} bytes via BLE")

asyncio.run(send_ble())