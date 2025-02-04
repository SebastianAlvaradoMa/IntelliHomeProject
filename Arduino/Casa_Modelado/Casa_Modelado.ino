#define Sala 2
#define Cocina 3
#define Habitacion1 4
#define Habitacion2 5
#define Habitacion3 6
#define BanoPrincipal 7
#define BanoHabitacion 8
#define Garaje 9
#define Flame 10
#define Sismo 11

bool flameSensor;
bool fire;

void setup() {

  // Configurar los pines como salida
  pinMode(Sala, OUTPUT); // Sala
  pinMode(Cocina, OUTPUT); // Cocina
  pinMode(Habitacion1, OUTPUT); // Habitación 1
  pinMode(Habitacion2, OUTPUT); // Habitación 2
  pinMode(Habitacion3, OUTPUT); // Habitación 3
  pinMode(BanoPrincipal, OUTPUT); // Baño Principipal
  pinMode(BanoHabitacion, OUTPUT); // Baño Habitación
  pinMode(Garaje, OUTPUT); // Garaje
  pinMode(Flame,INPUT);  // Sensor de fuego 
  pinMode(Sismo,INPUT); //Sensor de inclinación

  Serial.begin(9600); // Iniciar comunicación serial
  while (!Serial) {
    // Esperar hasta que el Serial esté listo (opcional)
  }

}

void loop() {
   
  //SENSOR DE FUEGO
  flameSensor = digitalRead (Flame);   //Lectura del pin del sensor flame
  if (flameSensor && !fire){
    Serial.println("Llama detectada");
    fire = true;
  }
  if (!flameSensor && fire){
    Serial.println("Llama apagada");
    fire = false;
  }
  delay(200);

  
  //SENSOR DE INCLINACIÓN (SISMO)

 int SismoSensor = digitalRead(Sismo);  // Leer el estado del sensor
 if (SismoSensor == HIGH) {
   Serial.println("Se detuvo actividad sísmica");  //HIFG no hay inclinación
 } else { 
   Serial.println("Sismo detectado");
 }
 delay(500);

 //ILUMINACIÓN 

  if (Serial.available() > 0) {
    // Leer mensaje recibido
    String receivedMessage = Serial.readStringUntil('\n');
    receivedMessage.trim(); // Eliminar posibles espacios o caracteres extra

    Serial.print("Arduino recibe: ");
    Serial.println(receivedMessage);

    // Asociar nombres con LEDs
    if (receivedMessage == "OnSala") {
      digitalWrite(Sala, HIGH); // Encender LED del pin 2
    } else if (receivedMessage == "OffSala") {
      digitalWrite(Sala, LOW); // Apagar LED del pin 2
    } else if (receivedMessage == "OnCocina") {
      digitalWrite(Cocina, HIGH); // Encender LED del pin 3
    } else if (receivedMessage == "OffCocina") {
      digitalWrite(Cocina, LOW); // Apagar LED del pin 3
    } else if (receivedMessage == "OnHabitacion1") {
      digitalWrite(Habitacion1, HIGH); // Encender LED del pin 4
    } else if (receivedMessage == "OffHabitacion1") {
      digitalWrite(Habitacion1, LOW); // Apagar LED del pin 4
    } else if (receivedMessage == "OnHabitacion2") {
      digitalWrite(Habitacion2, HIGH); // Encender LED del pin 5
    } else if (receivedMessage == "OffHabitacion2") {
      digitalWrite(Habitacion2, LOW); // Apagar LED del pin 5
    } else if (receivedMessage == "OnHabitacion3") {
      digitalWrite(Habitacion3, HIGH); // Encender LED del pin 6
    } else if (receivedMessage == "OffHabitacion3") {
      digitalWrite(Habitacion3, LOW); // Apagar LED del pin 6
    } else if (receivedMessage == "OnBanoPrincipal") {
      digitalWrite(BanoPrincipal, HIGH); // Encender LED del pin 7
    } else if (receivedMessage == "OffBanoPrincipal") {
      digitalWrite(BanoPrincipal, LOW); // Apagar LED del pin 7
    } else if (receivedMessage == "OnBanoHabitacion") {
      digitalWrite(BanoHabitacion, HIGH); // Encender LED del pin 8
    } else if (receivedMessage == "OffBanoHabitacion") {
      digitalWrite(BanoHabitacion, LOW); // Apagar LED del pin 8
    } else if (receivedMessage == "OnGaraje") {
      digitalWrite(Garaje, HIGH); // Encender LED del pin 9
    } else if (receivedMessage == "OffGaraje") {
      digitalWrite(Garaje, LOW); // Apagar LED del pin 9 
    } else {
      Serial.println("Comando no reconocido.");
    }
  }
}
