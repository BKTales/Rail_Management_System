    #include <DHT.h>
    #include <DHT_U.h>
    #include "arduino_header.hpp"

    unsigned long lastBlinkTime = 0;
    bool blinkState = false;
    const long SENSOR_READ_INTERVAL = 2000; 
    unsigned long lastSensorReadTime = 0;

    void setup() {
        Serial.begin(9600);
        
        for (int track = 0; track < 2; track++) {
            for (int i = 0; i < 3; i++) {
                pinMode(LED_PINS[track][i], OUTPUT);
                digitalWrite(LED_PINS[track][i], LOW);
            }
        }
        
        dht.begin();

    }

    void loop() {
        if (Serial.available() >= 8) {
            byte buffer[8];
            Serial.readBytes(buffer, 8);  // Lê 8 bytes para o buffer

            // Converte os bytes de volta para inteiros
            int track = *((int*)(&buffer[0]));      // Track (4 bytes)
            int ledOn = *((int*)(&buffer[4]));      // LED a ligar (4 bytes)

            // Processa os comandos
            processCommand(track, ledOn);
              // Passa track (0 ou 1)
        }
        
        handleRedLedBlink();
        if (Serial.available() == 1) {
            byte buffer[1];
            Serial.readBytes(buffer, 1);  // Lê 1 bytes para o buffer
            turnOffAllLeds();
        }

        unsigned long currentMillis = millis();
        if (currentMillis - lastSensorReadTime >= SENSOR_READ_INTERVAL) {
            lastSensorReadTime = currentMillis;
            read_atmospheric_data(NULL);
        }
    }

    int get_sensor_id(int min, int max) {
        return random(min, max);
    }

    bool read_atmospheric_data(void *) {
        int hum_min = NR_ATMOSPHERIC_HUMIDITY_SENSORS + 1;
        int hum_max = hum_min + NR_ATMOSPHERIC_HUMIDITY_SENSORS;
        int hum_id = get_sensor_id(hum_min, hum_max);
        
        int temp_min = NR_ATMOSPHERIC_HUMIDITY_SENSORS + NR_ATMOSPHERIC_HUMIDITY_SENSORS + 1;
        int temp_max = temp_min + NR_ATMOSPHERIC_TEMPERATURE_SENSORS;
        int temp_id = get_sensor_id(temp_min, temp_max);
        
        // Read both sensors
        float humidity = dht.readHumidity();
        float temperature = dht.readTemperature();
        
    
        // Prepare combined output
        char buffer[DATA_LENGTH];

        if (!isnan(temperature) && !isnan(humidity)) {
            snprintf(buffer, sizeof(buffer),
                    "TEMP&unit:celsius&value:%.0f#HUM&unit:percentage&value:%.0f",
                    temperature, humidity);
            Serial.println(buffer);
        }
        else if (!isnan(temperature)) {
            snprintf(buffer, sizeof(buffer),
                    "TEMP&unit:celsius&value:%.0f#HUM&unit:percentage&value:ERR",
                    temperature);
            Serial.println(buffer);
        }
        else if (!isnan(humidity)) {
            snprintf(buffer, sizeof(buffer),
                    "TEMP&unit:celsius&value:ERR#HUM&unit:percentage&value:%.0f",
                    humidity);
            Serial.println(buffer);
        }
        else {
            Serial.println("Couldn't read both sensors!");
        }
        
        return true;
    }


    void toggleRedLed(int track) {
        if (track < 0 || track > 1) return;
        redLedBlink[track] = !redLedBlink[track];
    }

    void stopRedLed(int track) {
        if (track < 1 || track > 2) return;

        redLedBlink[track-1] = false; // para o piscar
        setOffLed(0, track);
    }

    void handleRedLedBlink() {
        unsigned long currentMillis = millis();
        
        if (currentMillis - lastBlinkTime >= 500) { 
            lastBlinkTime = currentMillis;
            blinkState = !blinkState;
            
            for(int t = 0; t < 2; t++) {
                if(redLedBlink[t]) { 
                    digitalWrite(LED_PINS[t][0], blinkState ? HIGH : LOW);
                }
            }
        }
    }


    void setOffTrackLeds(int track){
        if (track < 0 || track > 1) return;

        for (int i = 0; i < 3; i++) {
            digitalWrite(LED_PINS[track][i], LOW);
        }
    }

    void setOnLed(int pinNumberIndex, int track){
        if (track < 0 || track > 1) return;
        if (pinNumberIndex < 0 || pinNumberIndex >= NUM_LEDS) return;

        int pin = LED_PINS[track][pinNumberIndex];

        if (!digitalRead(pin))
            digitalWrite(pin, HIGH);
    }

    void turnOffAllLeds(){
        for (int track = 0; track < 2; track++) {
            for (int i = 0; i < 3; i++) {
                digitalWrite(LED_PINS[track][i], LOW);
            }
            redLedBlink[track] = false;
        }
    }

    void processCommand(int track, int ledOn) {
        if (ledOn == 3) {
            setOffTrackLeds(track);
            redLedBlink[track] = true;
        }else{
            if (redLedBlink[track]) {
                digitalWrite(LED_PINS[track][0], LOW);
                redLedBlink[track] = false; 
            }
            setOffTrackLeds(track);
            setOnLed(ledOn, track);
        }
    }


    bool toggle_led(void *) { return true; }
