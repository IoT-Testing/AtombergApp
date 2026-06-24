import json
import os

def load_config(config_file):
    """Load configuration settings from a JSON file."""
    if not os.path.exists(config_file):
        raise FileNotFoundError(f"Configuration file not found: {config_file}")
    
    with open(config_file, 'r') as file:
        return json.load(file)

def get_listen_timeout(config):
    """Get the listen timeout from the configuration."""
    return config.get("listen_timeout", 10)

def get_retry_limit(config):
    """Get the retry limit from the configuration."""
    return config.get("retry_limit", 3)

def get_mac_ids_file_path():
    """Get the path to the MAC IDs file."""
    return os.path.join("data", "mac_ids.txt")

def get_results_file_path():
    """Get the path to the results CSV file."""
    return os.path.join("data", "results.csv")