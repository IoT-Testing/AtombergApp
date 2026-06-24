import csv

class CSVLogger:
    def __init__(self, file_path):
        self.file_path = file_path
        self.header = [
            "Iteration",
            "PowerStateChangeto",
            "MacID",
            "No of Attempts",
            "Time at first unicast command sent",
            "Time when State matched",
            "Total time elapsed",
            "Status"
        ]
        self.initialize_csv()

    def initialize_csv(self):
        with open(self.file_path, mode='w', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(self.header)

    def log_result(self, iteration, power_state_change_to, mac_id, attempts, time_first_command, time_state_matched, total_time, status):
        with open(self.file_path, mode='a', newline='') as file:
            writer = csv.writer(file)
            writer.writerow([
                iteration,
                power_state_change_to,
                mac_id,
                attempts,
                time_first_command,
                time_state_matched,
                total_time,
                status
            ])