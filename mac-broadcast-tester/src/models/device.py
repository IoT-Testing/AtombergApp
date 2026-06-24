class Device:
    def __init__(self, mac_id, ip_address=None):
        self.mac_id = mac_id
        self.ip_address = ip_address

    def set_ip_address(self, ip_address):
        self.ip_address = ip_address

    def get_mac_id(self):
        return self.mac_id

    def get_ip_address(self):
        return self.ip_address

    def __repr__(self):
        return f"Device(mac_id='{self.mac_id}', ip_address='{self.ip_address}')"