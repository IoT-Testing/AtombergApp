import socket
import json
import time

class Unicast:
    def __init__(self, ip_address, mac_id, unicast_port=5600):
        self.ip_address = ip_address
        self.mac_id = mac_id
        self.unicast_port = unicast_port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    def send_power_command(self, power_state):
        """Send power command to device (True=ON, False=OFF)."""
        message = json.dumps({"power": power_state})
        try:
            self.sock.sendto(message.encode(), (self.ip_address, self.unicast_port))
            print(f"  Sent to {self.mac_id} ({self.ip_address}): {message}")
            return True
        except Exception as e:
            print(f"  Error sending command to {self.mac_id}: {e}")
            return False

    def toggle_power_state(self, listener, expected_power_state, max_retries=10, retry_delay=2):
        """
        Toggle power state with retry logic until state is verified.
        
        Args:
            listener: Listener instance for state verification
            expected_power_state: Boolean (True=ON, False=OFF)
            max_retries: Maximum number of attempts
            retry_delay: Delay between retries in seconds
        
        Returns:
            dict with success, attempts, time_first_command, time_state_matched
        """
        time_first_command = None
        time_state_matched = None
        
        for attempt in range(1, max_retries + 1):
            print(f"  Attempt {attempt}/{max_retries}: Setting power={'ON' if expected_power_state else 'OFF'}")
            
            # Send command
            if self.send_power_command(expected_power_state):
                if time_first_command is None:
                    time_first_command = time.time()
                
                # Wait for state verification
                if listener.wait_for_state(self.mac_id, expected_power_state, timeout=retry_delay):
                    time_state_matched = time.time()
                    print(f"  ✓ State verified for {self.mac_id} after {attempt} attempt(s)")
                    return {
                        "success": True,
                        "attempts": attempt,
                        "time_first_command": time_first_command,
                        "time_state_matched": time_state_matched
                    }
            
            if attempt < max_retries:
                time.sleep(0.5)  # Small delay before retry
        
        print(f"  ✗ Failed to verify state for {self.mac_id} after {max_retries} attempts")
        return {
            "success": False,
            "attempts": max_retries,
            "time_first_command": time_first_command,
            "time_state_matched": None
        }

    def close(self):
        """Close the socket."""
        self.sock.close()