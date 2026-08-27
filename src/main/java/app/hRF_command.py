import os
import requests
import time
import urllib3

# Disable SSL warnings
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

# Configuration
CONTENT_TYPE = "application/json"
BASE_URL = "https://api.developer.atomberg-iot.com"
X_API_KEY = os.environ["ATOMBERG_API_KEY"]
REFRESH_TOKEN = os.environ["ATOMBERG_REFRESH_TOKEN"]
TARGET_MAC_ID = os.environ.get("ATOMBERG_TARGET_MAC_ID", "")

def get_access_token():
    """Generate access token using refresh token."""
    url = f"{BASE_URL}/v1/get_access_token"
    headers = {
        "x-api-key": X_API_KEY,
        "Authorization": f"Bearer {REFRESH_TOKEN}"
    }
    try:
        response = requests.get(url, headers=headers, verify=False)
        response.raise_for_status()
        data = response.json()
        # print(f"Access token response: {data}")  # Debug print
        return data.get('message', {}).get('access_token')
    except requests.exceptions.RequestException as e:
        print(f"Error getting access token: {e}")
        if hasattr(e.response, 'text'):
            print(f"Response content: {e.response.text}")
        return None

def get_device_list(access_token):
    """Get list of devices using access token."""
    url = f"{BASE_URL}/v1/get_list_of_devices"  # Updated endpoint
    headers = {
        "x-api-key": X_API_KEY,
        "Authorization": f"Bearer {access_token}",
        "Content-Type": CONTENT_TYPE
    }
    try:
        response = requests.get(url, headers=headers, verify=False)
        response.raise_for_status()
        data = response.json()
        print(f"Device list response: {data}")  # Debug print
        return data
    except requests.exceptions.RequestException as e:
        print(f"Error getting device list: {e}")
        if hasattr(e.response, 'text'):
            print(f"Response content: {e.response.text}")
        return None
    
def get_device_state(access_token, device_mac):
    """Get the Present state of the device"""
    url = f"{BASE_URL}/v1/get_device_state?device_id={device_mac}"
    headers = {
     "x-api-key": X_API_KEY,
      "Authorization": f"Bearer {access_token}",
      "Content-Type": CONTENT_TYPE
    }
    response = requests.get(url, headers=headers, verify=False)
    response.raise_for_status()
    return response.json()


def send_factory_reset(access_token, device_mac):
    """Send hard factory reset command to specific device."""
    url = f"{BASE_URL}/v1/send_command"
    headers = {
        "x-api-key": X_API_KEY,
        "Authorization": f"Bearer {access_token}",
        "Content-Type": CONTENT_TYPE
    }
    payload = {
        "device_id": device_mac,
        "command": {
            "hardFactoryReset": True
        }
    }
    
    print(f"\nSending command with:")
    print(f"URL: {url}")
    print(f"Headers: {headers}")
    print(f"Payload: {payload}")
    
    try:
        response = requests.post(url, headers=headers, json=payload, verify=False)
        print(f"Response Status Code: {response.status_code}")
        print(f"Response Headers: {response.headers}")
        
        if response.status_code == 403:
            print("Authorization error - token might be expired")
            return {"error": "auth_failed"}
            
        response.raise_for_status()
        response_data = response.json()
        print(f"Factory reset response: {response_data}")
        return response_data
    except requests.exceptions.RequestException as e:
        print(f"Error sending factory reset command: {e}")
        if hasattr(e.response, 'text'):
            print(f"Response content: {e.response.text}")
        return None

def send_power(access_token, device_mac, status):
    """Send hard factory reset command to specific device."""
    url = f"{BASE_URL}/v1/send_command"
    headers = {
        "x-api-key": X_API_KEY,
        "Authorization": f"Bearer {access_token}",
        "Content-Type": CONTENT_TYPE
    }
    payload = {
        "device_id": device_mac,
        "command": {
            "power": status
        }
    }
    
    print("\nSending command with:")
    print(f"URL: {url}")
    print(f"Headers: {headers}")
    print(f"Payload: {payload}")
    
    try:
        response = requests.post(url, headers=headers, json=payload, verify=False)
        print(f"Response Status Code: {response.status_code}")
        print(f"Response Headers: {response.headers}")
        
        if response.status_code == 403:
            print("Authorization error - token might be expired")
            return {"error": "auth_failed"}
            
        response.raise_for_status()
        response_data = response.json()
        print(f"Factory reset response: {response_data}")
        return response_data
    except requests.exceptions.RequestException as e:
        print(f"Error sending factory reset command: {e}")
        if hasattr(e.response, 'text'):
            print(f"Response content: {e.response.text}")
        return None


def main():
    print("Starting Atomberg Device Reset Process...")
    device_found = True
    i= 0
    for i in range(2):  # Try up to 2 times
    # while device_found:
        # Get new access token
        access_token = get_access_token()
        if not access_token:
            print("Failed to get access token. Retrying in 5 seconds...")
            time.sleep(5)
            continue
            
        # Get device list
        response = get_device_list(access_token)
        if not response or response.get('status') != 'Success':
            print("Failed to get device list. Retrying in 5 seconds...")
            time.sleep(5)
            continue
            
        devices = response.get('message', {}).get('devices_list', [])
        # Check if target device exists in the list
        device_found = False
        for device in devices:
            if device.get("device_id") == TARGET_MAC_ID:
                device_found = True
                print(f"Target device found: {TARGET_MAC_ID}")
                print(f"Device details: Name: {device.get('name')}, Room: {device.get('room')}, Model: {device.get('model')}")
                print(get_device_state(access_token,TARGET_MAC_ID))
                # Send factory reset command
                # result = send_factory_reset(access_token, TARGET_MAC_ID)
                # if result:
                #     print("Factory reset command sent successfully")
                # else:
                #     print("Failed to send factory reset command")
                # break
        
        if not device_found:
            print(f"Device {TARGET_MAC_ID} not found in the list. Process complete.")
        else:
            print("Waiting 10 seconds before checking device list again...")
            time.sleep(10)

if __name__ == "__main__":
    main()
