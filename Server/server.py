import socket
import json
import os
import serial
from datetime import datetime
import hashlib

def hash_data(data):
    """Hash the given data using SHA-256."""
    return hashlib.sha256(data.encode('utf-8')).hexdigest()

class UserDatabase:
    def __init__(self, filename="users.txt"):
        self.filename = filename
        self.ensure_file_exists()

    def ensure_file_exists(self):
        if not os.path.exists(self.filename):
            with open(self.filename, "w") as f:
                f.write("# User Database - Created on {}\n".format(datetime.now()))
    def save_user(self, user_data):
        try:
            user_id = str(sum(1 for line in open(self.filename) if line.strip() and not line.startswith("#")))
            
            # Hash all sensitive user information
            hashed_user_data = {
                "userId": user_id,
                "username": hash_data(user_data["username"]),
                "password": hash_data(user_data["password"]),  # Hash the password
                "userData": {
                    "nombre": hash_data(user_data.get("nombre", "")),
                    "apellidos": hash_data(user_data.get("apellidos", "")),
                    "email": hash_data(user_data.get("email", "")),
                    "fechaNacimiento": hash_data(user_data.get("fechaNacimiento", "")),
                    "gender": hash_data(user_data.get("gender", "")),
                    "nacionalidad": hash_data(user_data.get("nacionalidad", "")),
                    "pasatiempos": hash_data(user_data.get("pasatiempos", "")),
                    "expiracionM": user_data.get("expiracionM", ""),
                    "expiracionA": user_data.get("expiracionA", ""),
                    "cuentaIban": user_data.get("cuentaIban", ""),
                    "tarjeta": user_data.get("tarjeta", ""),
                    "Pin": user_data.get("Pin", ""),
                    "hospital": user_data.get("hospital", ""),
                    "lugarFavorito": user_data.get("lugarFavorito", ""),
                    "mascota": user_data.get("mascota", ""),

                    "createdAt": datetime.now().isoformat()
                }
            }
            
            with open(self.filename, "a") as f:
                f.write(json.dumps(hashed_user_data, ensure_ascii=False) + "\n")
            return True, user_id
        except Exception as e:
            print(f"Error saving user: {e}")
            return False, None

    def verify_credentials(self, username, password, email=None):
        try:
            with open(self.filename, "r") as f:
                for line in f:
                    if line.strip() and not line.startswith("#"):
                        try:
                            user_data = json.loads(line.strip())
                            # Hash the input credentials
                            hashed_username = hash_data(username) if username else None
                            hashed_password = hash_data(password)
                            hashed_email = hash_data(email) if email else None

                            # Check for username/password OR email/password match
                            if ((hashed_username and user_data["username"] == hashed_username and user_data["password"] == hashed_password) or 
                                (hashed_email and user_data["userData"]["email"] == hashed_email and user_data["password"] == hashed_password)):
                                return True, user_data["userId"], user_data
                        except json.JSONDecodeError:
                            continue
            return False, None, None
        except FileNotFoundError:
            return False, None, None
        
        
def send_to_arduino(command):
    """Send command to Arduino without waiting for response"""
    try:
        if arduino_serial and arduino_serial.is_open:
            arduino_serial.write(command.encode())
            return True
        return False
    except Exception as e:
        print(f"Error sending to Arduino: {e}")
        return False

def process_request(request_data, db):
    """Process incoming client requests"""
    try:
        request = json.loads(request_data)
        action = request.get("action", "")
        payload = request.get("payload", {})

        if action == "REGISTER":
            success, user_id = db.save_user(payload)
            if success:
                response = {
                    "action": "REGISTER",
                    "status": "success",
                    "message": "Registration successful",
                    "payload": {
                        "userId": user_id,
                        "username": payload["username"]
                    }
                }
            else:
                response = {
                    "action": "REGISTER",
                    "status": "error",
                    "message": "Registration failed"
                }
            return json.dumps(response) + "\n"
        
        elif action == "LOGIN":
            email = payload.get("email")
            username = payload.get("username")
            password = payload.get("password", "")

            success, user_id, user_data = db.verify_credentials(
                username or "", 
                password,
                email
            )
            if success:
                response = {
                    "action": "LOGIN",
                    "status": "success",
                    "message": "Login successful",
                    "payload": {
                        "userId": user_id,
                        "username": user_data["username"],
                        "userData": user_data["userData"]
                    }
                }
            else:
                response = {
                    "action": "LOGIN",
                    "status": "error",
                    "message": "Invalid credentials"
                }
            return json.dumps(response) + "\n"

        elif action == "LUCES":
            command = payload.get("command", "")
            valid_commands = [
                "OnHabitacion1", "OffHabitacion1",
                "OnHabitacion2", "OffHabitacion2",
                "OnHabitacion3", "OffHabitacion3",
                "OnSala", "OffSala",
                "OnBanoPrincipal", "OffBanoPrincipal",
                "OnBanoHabitacion", "OffBanoHabitacion",
                "OnGaraje", "OffGaraje"
            ]
            if command in valid_commands:
                #Print the command to the terminal for testing
                print(f"Received LUCES command: {command}")
                if arduino_serial:  #Only send to Arduino if it's connected
                    send_to_arduino(command + "\n")
            else:
                print(f"Invalid LUCES command: {command}")
            return ""

        else:
            response = {
                "action": action,
                "status": "error",
                "message": "Unknown action"
            }
            return json.dumps(response) + "\n"

    except json.JSONDecodeError:
        return json.dumps({
            "action": "error",
            "status": "error",
            "message": "Invalid JSON format"
        }) + "\n"

def start_server(host="0.0.0.0", port=1717, serial_port="/dev/ttyUSB0", baud_rate=9600):
    global arduino_serial
    db = UserDatabase()

    #Initialize Arduino serial connection
    try:
        arduino_serial = serial.Serial(serial_port, baud_rate, timeout=1)
        print(f"Connected to Arduino on {serial_port}")
    except Exception as e:
        print(f"Failed to connect to Arduino: {e}")
        arduino_serial = None

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server_socket:
        server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)  # Enable address reuse
        server_socket.bind((host, port))
        server_socket.listen(5)
        print(f"Server listening on {host}:{port}...")        

        while True:
            client_socket, address = server_socket.accept()
            print(f"Connection established with {address}")

            with client_socket:
                try:
                    while True:
                        data = client_socket.recv(1024).decode('utf-8')
                        if not data:
                            print("Client disconnected")
                            break

                        print(f"Received: {data}")
                        response = process_request(data, db)
                        print(f"Sending: {response}")
                        client_socket.sendall(response.encode('utf-8'))

                except Exception as e:
                    print(f"Error handling connection: {e}")

if __name__ == "__main__":
    start_server()
