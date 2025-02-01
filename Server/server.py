import socket
import json
import os
#import serial
from datetime import datetime
import hashlib
import threading
#from twilio.rest import Client  # Import Twilio client

# Notificaciones de WA
account_sid = ''
auth_token = ''
#twilio_client = Client(account_sid, auth_token)

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
                    "expiracionM": hash_data(user_data.get("expiracionM", "")),
                    "expiracionA": hash_data(user_data.get("expiracionA", "")),
                    "cuentaIban": hash_data(user_data.get("cuentaIban", "")),
                    "tarjeta": hash_data(user_data.get("tarjeta", "")),
                    "Pin": hash_data(user_data.get("Pin", "")),
                    "hospital": hash_data(user_data.get("hospital", "")),
                    "lugarFavorito": hash_data(user_data.get("lugarFavorito", "")),
                    "mascota": hash_data(user_data.get("mascota", "")),
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

class PropertyDatabase:
    def __init__(self, filename="properties.txt"):
        self.filename = filename
        self.ensure_file_exists()

    def ensure_file_exists(self):
        if not os.path.exists(self.filename):
            with open(self.filename, "w") as f:
                f.write("# Property Database - Created on {}\n".format(datetime.now()))

    def save_property(self, property_data):
        try:
            property_id = str(sum(1 for line in open(self.filename) if line.strip() and not line.startswith("#")))
            
            # Convert numeric values to strings before hashing
            hashed_property_data = {
                "propertyId": property_id,
                "propertyData": {
                    "nombrePropiedad": hash_data(str(property_data.get("nombrePropiedad", ""))),
                    "mascotas": hash_data(str(property_data.get("mascotas", ""))),
                    "precio": hash_data(str(property_data.get("precio", ""))),
                    "contacto": hash_data(str(property_data.get("contacto", ""))),
                    "maxPersonas": hash_data(str(property_data.get("maxPersonas", ""))),
                    "amenidad1": hash_data(str(property_data.get("amenidad1", ""))),
                    "amenidad2": hash_data(str(property_data.get("amenidad2", ""))),
                    "amenidad3": hash_data(str(property_data.get("amenidad3", ""))),
                    "amenidad4": hash_data(str(property_data.get("amenidad4", ""))),
                    "latitud": hash_data(str(property_data.get("latitud", ""))),
                    "longitud": hash_data(str(property_data.get("longitud", ""))),
                    "createdAt": datetime.now().isoformat()
                }
            }
            
            with open(self.filename, "a") as f:
                f.write(json.dumps(hashed_property_data, ensure_ascii=False) + "\n")
            return True, property_id
        except Exception as e:
            print(f"Error saving property: {e}")
            return False, None

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

def send_twilio_notification():
    """Send a personalized Twilio notification"""
    try:
        # Get the current time and date
        now = datetime.now()
        current_time = now.strftime("%H:%M:%S")
        current_date = now.strftime("%d/%m/%Y")
        
        # Construct the message body
        message_body = (
            f"Estimad@ usuario :\n"
            "Está ocurriendo un incendio.\n"
            f"Hora: {current_time}\n"
            f"Día: {current_date}"
        )
        
        # Send the message
        #message = twilio_client.messages.create(
        #    from_='whatsapp:+14155238886',
        #    body=message_body,
        #    to='whatsapp:+50684086287'
        #)
        #print(f"Twilio notification sent: {message.sid}")
    except Exception as e:
        print(f"Error sending Twilio notification: {e}")

#def monitor_serial_port():
#   """Monitor the serial port for the fire alarm flag"""
#    while True:
#        if arduino_serial and arduino_serial.is_open:
#            try:
#                line = arduino_serial.readline().decode('utf-8').strip()
#                if line == "FIRE_ALARM":  
#                    print("Sending Twilio notification...")
#                    send_twilio_notification()
#                    break
#            except Exception as e:
#                print(f"Error reading from serial port: {e}")

def process_request(request_data, db, property_db):
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
                "OnGaraje", "OffGaraje", "OnCocina", "OffCocina"
            ]
            if command in valid_commands:
                # Print the command to the terminal for testing
                print(f"Received LUCES command: {command}")
                if arduino_serial:  # Only send to Arduino if it's connected
                    send_to_arduino(command + "\n")
            else:
                print(f"Invalid LUCES command: {command}")
            return ""
        
        elif action == "REGISTER_PROPERTY":
            success, property_id = property_db.save_property(payload)
            if success:
                response = {
                    "action": "REGISTER_PROPERTY",
                    "status": "success",
                    "message": "Property registration successful",
                    "payload": {
                        "propertyId": property_id
                    }
                }
            else:
                response = {
                    "action": "REGISTER_PROPERTY",
                    "status": "error",
                    "message": "Property registration failed"
                }
            return json.dumps(response) + "\n"

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

def start_server(host="0.0.0.0", port=1717, baud_rate=9600):
    global arduino_serial
    db = UserDatabase()
    property_db = PropertyDatabase()

    # Initialize Arduino serial connection
    #try:
    #    arduino_serial = serial.Serial(serial_port, baud_rate, timeout=1)
    #    print(f"Connected to Arduino on {serial_port}")
    #except Exception as e:
    #    print(f"Failed to connect to Arduino: {e}")
    #    arduino_serial = None

    # Start a thread to monitor the serial port for the fire alarm flag
    #serial_monitor_thread = threading.Thread(target=monitor_serial_port)
    #serial_monitor_thread.daemon = True  
    #serial_monitor_thread.start()

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server_socket:
        server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)  
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
                        response = process_request(data, db, property_db)
                        print(f"Sending: {response}")
                        client_socket.sendall(response.encode('utf-8'))

                except Exception as e:
                    print(f"Error handling connection: {e}")

if __name__ == "__main__":
    start_server()
