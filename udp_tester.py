import socket
import threading
import time

class UDPHandler:
    def __init__(self, broadcast_port=12345, listen_port=12346):
        self.broadcast_port = broadcast_port
        self.listen_port = listen_port
        self.running = True

    def start_broadcast(self):
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        i = 0
        while self.running:
            # message = f"Broadcast message at {time.strftime('%H:%M:%S')}"
            message = '{"hardFactoryReset":true}'
            if time.time() % 10 < 1:  # Send message every 10 seconds
                sock.sendto(message.encode(), ('<broadcast>', self.broadcast_port))
                print(f"Broadcasting: {message}")
                i += 1
                if(i >=5):
                    print(f"Exiting code")
                    break
            time.sleep(1)

    def start_listening(self):
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        sock.bind(('', self.listen_port))
        
        while self.running:
            data, addr = sock.recvfrom(1024)
            print(f"Received message from {addr}: {data.decode()}")

    def start(self):
        # Create threads for broadcasting and listening
        broadcast_thread = threading.Thread(target=self.start_broadcast)
        listen_thread = threading.Thread(target=self.start_listening)
        
        # Start both threads
        # broadcast_thread.start()
        listen_thread.start()

        try:
            # Keep the main thread alive
            while True:
                time.sleep(1)
        except KeyboardInterrupt:
            print("\nShutting down...")
            self.running = False
            broadcast_thread.join()
            listen_thread.join()

if __name__ == "__main__":
    handler = UDPHandler(broadcast_port=8080, listen_port=5625)
    handler.start()
    exit