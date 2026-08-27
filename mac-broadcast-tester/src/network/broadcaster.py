import socket
import time

class Listener:
    def __init__(self, mac_ids, listen_port=5625, timeout=10):
        self.mac_ids = mac_ids
        self.listen_port = listen_port
        self.timeout = timeout
        self.ip_mapping = {}

    def listen(self):
        """Listen for device broadcasts and map MAC IDs to IP addresses"""
        print(f"Listening on port {self.listen_port} for device broadcasts...")
        
        # Create UDP socket
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        sock.bind(('', self.listen_port))
        sock.settimeout(self.timeout)
        
        start_time = time.time()
        found_devices = set()
        
        try:
            while len(found_devices) < len(self.mac_ids):
                # Check if timeout exceeded
                if time.time() - start_time > self.timeout:
                    print(f"Timeout reached. Found {len(found_devices)}/{len(self.mac_ids)} devices.")
                    break
                
                try:
                    # Receive broadcast message
                    data, addr = sock.recvfrom(1024)
                    message = data.decode('utf-8').strip()
                    ip_address = addr[0]
                    
                    print(f"Received: '{message}' from {ip_address}")
                    
                    # Extract MAC ID from message (format: macid_seriesname)
                    # Assuming MAC ID is the first part before underscore
                    if '_' in message:
                        mac_id = message.split('_')[0]
                    else:
                        mac_id = message
                    
                    # Check if this MAC ID is in our list
                    if mac_id in self.mac_ids and mac_id not in found_devices:
                        self.ip_mapping[mac_id] = ip_address
                        found_devices.add(mac_id)
                        print(f"✓ Mapped {mac_id} -> {ip_address} ({len(found_devices)}/{len(self.mac_ids)})")
                    
                except socket.timeout:
                    continue
                except Exception as e:
                    print(f"Error receiving broadcast: {e}")
                    continue
        
        finally:
            sock.close()
        
        # Report missing devices
        missing = set(self.mac_ids) - found_devices
        if missing:
            print(f"\nWarning: Did not receive broadcasts from: {', '.join(missing)}")
        
        return self.ip_mapping

    def get_ip_mapping(self):
        return self.ip_mapping