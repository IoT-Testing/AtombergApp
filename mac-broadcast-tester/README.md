# mac-broadcast-tester

## Overview
The mac-broadcast-tester project is designed to facilitate the broadcasting of MAC IDs, retrieve corresponding IP addresses, and send unicast commands to toggle the power states of devices. The results of these operations are logged in a CSV file for further analysis.

## Project Structure
```
mac-broadcast-tester
├── src
│   ├── main.py                # Entry point of the application
│   ├── network
│   │   ├── __init__.py        # Initializes the network module
│   │   ├── listener.py        # Listens for device broadcasts and maps MAC IDs to IP addresses
│   │   └── unicast.py         # Manages sending unicast commands and logging results
│   ├── utils
│   │   ├── __init__.py        # Initializes the utils module
│   │   ├── csv_logger.py       # Handles logging results to a CSV file
│   │   └── config.py          # Configuration settings and utility functions
│   └── models
│       ├── __init__.py        # Initializes the models module
│       └── device.py          # Defines the Device class with MAC ID and IP address properties
├── data
│   ├── mac_ids.txt            # Contains a list of MAC IDs to be processed
│   └── results.csv            # Stores results of power state toggling operations
├── config
│   └── settings.json          # Configuration settings for the application
├── requirements.txt           # Lists dependencies required for the project
└── README.md                  # Documentation for the project
```

## Installation
1. Clone the repository:
   ```
   git clone <repository-url>
   cd mac-broadcast-tester
   ```

2. Install the required dependencies:
   ```
   pip install -r requirements.txt
   ```

## Usage
1. Prepare a text file (`data/mac_ids.txt`) containing the MAC IDs you wish to process, one per line.

2. Run the application:
   ```
   python src/main.py
   ```

3. The results of the power state toggling operations will be logged in `data/results.csv`.

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for details.