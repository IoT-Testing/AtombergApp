import time
import csv
from network.listener import Listener
from network.unicast import Unicast
from utils.csv_logger import CSVLogger

def main():
    # Load MAC IDs from file
    with open('data/mac_ids.txt', 'r') as file:
        mac_ids = [line.strip() for line in file.readlines() if line.strip()]

    # Initialize logger
    logger = CSVLogger('data/results.csv')

    # Initialize listener and start thread
    listener = Listener(mac_ids)

    # Listen for device broadcasts and get MAC ID to IP mapping
    ip_mapping = listener.listen()
    
    if not ip_mapping:
        print("No devices found. Exiting.")
        return

    # Run 10 iterations (each iteration: OFF then ON)
    iterations = 10
    
    for iteration in range(1, iterations + 1):
        print(f"\n{'='*60}")
        print(f"ITERATION {iteration}/{iterations}")
        print(f"{'='*60}")
        
        # Iterate through each MAC ID
        for mac_id in mac_ids:
            ip_address = ip_mapping.get(mac_id)
            if not ip_address:
                print(f"Skipping {mac_id}: No IP address found")
                continue
            
            print(f"\nProcessing {mac_id} at {ip_address}")
            unicast_handler = Unicast(ip_address, mac_id)
            
            # Power OFF
            print(f"  → Setting power to OFF")
            result_off = unicast_handler.toggle_power_state(listener, expected_power_state=False)
            
            if result_off['success']:
                total_time_off = result_off['time_state_matched'] - result_off['time_first_command']
                logger.log_result(
                    iteration=iteration,
                    power_state_change_to="OFF",
                    mac_id=mac_id,
                    attempts=result_off['attempts'],
                    time_first_command=time.strftime('%H:%M:%S', time.localtime(result_off['time_first_command'])),
                    time_state_matched=time.strftime('%H:%M:%S', time.localtime(result_off['time_state_matched'])),
                    total_time=f"{total_time_off:.2f}s",
                    status="SUCCESS"
                )
            else:
                logger.log_result(
                    iteration=iteration,
                    power_state_change_to="OFF",
                    mac_id=mac_id,
                    attempts=result_off['attempts'],
                    time_first_command=time.strftime('%H:%M:%S', time.localtime(result_off['time_first_command'])) if result_off['time_first_command'] else "0",
                    time_state_matched="0",
                    total_time="0",
                    status="FAIL"
                )
            
            time.sleep(2)  # Small delay between OFF and ON
            
            # Power ON
            print(f"  → Setting power to ON")
            result_on = unicast_handler.toggle_power_state(listener, expected_power_state=True)
            
            if result_on['success']:
                total_time_on = result_on['time_state_matched'] - result_on['time_first_command']
                logger.log_result(
                    iteration=iteration,
                    power_state_change_to="ON",
                    mac_id=mac_id,
                    attempts=result_on['attempts'],
                    time_first_command=time.strftime('%H:%M:%S', time.localtime(result_on['time_first_command'])),
                    time_state_matched=time.strftime('%H:%M:%S', time.localtime(result_on['time_state_matched'])),
                    total_time=f"{total_time_on:.2f}s",
                    status="SUCCESS"
                )
            else:
                logger.log_result(
                    iteration=iteration,
                    power_state_change_to="ON",
                    mac_id=mac_id,
                    attempts=result_on['attempts'],
                    time_first_command=time.strftime('%H:%M:%S', time.localtime(result_on['time_first_command'])) if result_on['time_first_command'] else "0",
                    time_state_matched="0",
                    total_time="0",
                    status="FAIL"
                )
            time.sleep(2)  # Small delay between OFF and ON
            unicast_handler.close()
    
    # Stop listener thread
    listener.stop()
    print(f"\n{'='*60}")
    print("Testing complete! Results saved to data/results.csv")
    print(f"{'='*60}")

if __name__ == "__main__":
    main()