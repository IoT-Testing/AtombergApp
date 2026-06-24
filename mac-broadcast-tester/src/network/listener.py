import socket
import time
import threading
import json

class Listener:
    def __init__(self, mac_ids, listen_port=5625, timeout=10):
        self.mac_ids = mac_ids
        self.listen_port = listen_port
        self.timeout = timeout
        self.ip_mapping = {}
        self.running = False
        self.listener_thread = None
        self.sock = None
        self.state_received = threading.Event()
        self.last_state_info = None
        self.filter_device_id = None

    def hex_to_ascii(self, hex_string):
        """Convert hex string to ASCII characters."""
        try:
            bytes_data = bytes.fromhex(hex_string)
            return bytes_data.decode('ascii')
        except (ValueError, UnicodeDecodeError):
            return ""

    def parse_state_string(self, state_string):
        """Parse state string and extract timer information."""
        state_array = state_string.split(",")
        
        if state_array and state_array[-1].lower() == "end":
            state_array.pop()
        
        if not state_array:
            return {"error": "Empty state array"}
        
        try:
            value = int(state_array[0])
            
            power = (0x10 & value) > 0
            led = (0x20 & value) > 0
            boost = (0x40 & value) > 0
            sleep = (0x80 & value) > 0
            speed = 0x07 & value
            
            legacy_timer_hours = ((0x0F0000 & value) // 65536)
            legacy_timer_elapsed_mins = ((0xFF000000 & value) * 4 // 16777216)
            legacy_timer_remaining_mins = (legacy_timer_hours * 60) - legacy_timer_elapsed_mins
            
            if legacy_timer_remaining_mins < 0:
                legacy_timer_remaining_mins = 0
            
            return {
                "value": value,
                "power": power,
                "led": led,
                "boost": boost,
                "sleep": sleep,
                "speed": speed,
                "legacy_timer_hours": legacy_timer_hours,
                "legacy_timer_elapsed_mins": legacy_timer_elapsed_mins,
                "legacy_timer_remaining_mins": legacy_timer_remaining_mins
            }
        except (ValueError, IndexError) as e:
            return {"error": f"Failed to parse state string: {str(e)}"}

    def parse_udp_message(self, data):
        """Parse UDP message data and return a dictionary with message details."""
        if isinstance(data, bytes):
            data_string = data.decode('utf-8', errors='ignore')
        elif isinstance(data, list):
            data_string = ''.join(chr(b) for b in data)
        else:
            data_string = str(data)
        
        # Case 1: Discovery message (length <= 15, only device ID)
        if len(data_string) <= 15:
            device_id = data_string[:12]
            return {
                "type": "discover",
                "device_id": device_id,
                "message_id": "",
                "state_string": ""
            }
        
        # Case 2: Message with underscore (macid_series format)
        if "_" in data_string:
            parts = data_string.split("_", 1)
            if len(parts) > 1:
                data_string = parts[1]
                if len(data_string) > 2:
                    data_string = data_string[2:]
        
        decoded_text = self.hex_to_ascii(data_string)
        
        if not decoded_text:
            return {
                "type": "unknown",
                "device_id": "",
                "message_id": "",
                "state_string": ""
            }
        
        try:
            decoded_json = json.loads(decoded_text)
        except (json.JSONDecodeError, Exception):
            return {
                "type": "unknown",
                "device_id": "",
                "message_id": "",
                "state_string": ""
            }
        
        if not all(key in decoded_json for key in ["device_id", "message_id", "state_string"]):
            return {
                "type": "unknown",
                "device_id": "",
                "message_id": "",
                "state_string": ""
            }
        
        message_id = decoded_json["message_id"]
        device_id = decoded_json["device_id"]
        state_string = decoded_json["state_string"]
        
        if message_id == "internet_query":
            message_type = "internet_query"
        else:
            message_type = "state_update"
        
        return {
            "type": message_type,
            "device_id": device_id,
            "message_id": message_id,
            "state_string": state_string
        }

    def _listen_thread(self):
        """Thread function that continuously listens for UDP messages."""
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sock.bind(('', self.listen_port))
        # self.sock.settimeout(1.0)
        
        print(f"Listener thread started on port {self.listen_port}")
        
        while self.running:
            try:
                data, addr = self.sock.recvfrom(1024)
                ip_address = addr[0]
                
                parsed = self.parse_udp_message(data)
                device_id = parsed['device_id']
                
                # Discovery phase: map MAC ID to IP
                if parsed['type'] == 'discover' and device_id in self.mac_ids:
                    if device_id not in self.ip_mapping:
                        self.ip_mapping[device_id] = ip_address
                        print(f"✓ Discovered {device_id} -> {ip_address}")
                
                # State verification phase: check if message is from filtered device
                if self.filter_device_id and device_id == self.filter_device_id:
                    if parsed['type'] == 'state_update' and parsed['state_string']:
                        self.last_state_info = self.parse_state_string(parsed['state_string'])
                        print(f"  State received from {device_id}: power={self.last_state_info.get('power')}")
                        self.state_received.set()
                
            except socket.timeout:
                continue
            except Exception as e:
                if self.running:
                    print(f"Error in listener thread: {e}")
        
        if self.sock:
            self.sock.close()
        print("Listener thread stopped")

    def start(self):
        """Start the listener thread."""
        if not self.running:
            self.running = True
            self.listener_thread = threading.Thread(target=self._listen_thread, daemon=True)
            self.listener_thread.start()

    def stop(self):
        """Stop the listener thread."""
        self.running = False
        if self.listener_thread:
            self.listener_thread.join(timeout=2)

    def listen(self):
        """Listen for device broadcasts and map MAC IDs to IP addresses."""
        print(f"Listening on port {self.listen_port} for device broadcasts...")
        
        self.start()
        start_time = time.time()
        found_devices = set()
        
        while len(found_devices) < len(self.mac_ids):
            if time.time() - start_time > self.timeout:
                print(f"Timeout reached. Found {len(found_devices)}/{len(self.mac_ids)} devices.")
                break
            
            found_devices = set(self.ip_mapping.keys())
            time.sleep(0.1)
        
        missing = set(self.mac_ids) - found_devices
        if missing:
            print(f"\nWarning: Did not receive broadcasts from: {', '.join(missing)}")
        
        return self.ip_mapping

    def wait_for_state(self, device_id, expected_power_state, timeout=5):
        """Wait for a state update from a specific device."""
        self.filter_device_id = device_id
        self.state_received.clear()
        self.last_state_info = None
        
        if self.state_received.wait(timeout):
            if self.last_state_info and 'power' in self.last_state_info:
                return self.last_state_info['power'] == expected_power_state
        
        return False

    def get_ip_mapping(self):
        return self.ip_mapping