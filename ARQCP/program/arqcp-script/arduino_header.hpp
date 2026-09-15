#ifndef ARDUINO_HEADER_H
# define ARDUINO_HEADER_H

# include "./DHT_sensor_library/DHT.h"
# include "./arduino-timer/src/arduino-timer.h"

# include <stdbool.h>

# define DHTPIN 16
# define DHTTYPE DHT11 // O teu sensor é o DHT11

# define REDPIN1 12
# define REDPIN2 8
# define YELLOWPIN1 11
# define YELLOWPIN2 7
# define GREENPIN1 10
# define GREENPIN2 6

# define TRACK1_PINS { REDPIN1, YELLOWPIN1, GREENPIN1 }
# define TRACK2_PINS { REDPIN2, YELLOWPIN2, GREENPIN2 }

# define NUM_LEDS 3
// Sensor Count Definitions
# define NR_ATMOSPHERIC_HUMIDITY_SENSORS 4
# define NR_ATMOSPHERIC_TEMPERATURE_SENSORS 4

// Buffer Length Definitions
# define DATA_LENGTH 100
# define TYPE_NAME_LENGTH 30
# define UNIT_NAME_LENGTH 30

// Timing Period Definitions (milliseconds)
# define TOGGLE_LED_PERIOD 1000
# define ATMOSPHERIC_HUMIDITY_PERIOD 2000
# define ATMOSPHERIC_TEMPERATURE_PERIOD 2000

// Sensor Type Names
char sensor_types[][TYPE_NAME_LENGTH] = {
    "atmospheric_humidity",
    "atmospheric_temperature"
};

// Sensor Type Enumeration
enum sensor_type {
    ATMOSPHERIC_HUMIDITY,
    ATMOSPHERIC_TEMPERATURE
};

// Sensor Unit Names
char sensor_units[][UNIT_NAME_LENGTH] = {
    "percentage",
    "celsius",
};

// Sensor Unit Enumeration
enum sensor_unit {
    PERCENTAGE = 0,
    CELSIUS,
};

// Global Objects
DHT dht(DHTPIN, DHTTYPE);
auto timer = timer_create_default();

// Indica se o LED vermelho da track deve piscar
bool redLedBlink[2] = {false, false};

static const int LED_PINS[2][3] = {
    TRACK1_PINS,
    TRACK2_PINS
};

int		get_sensor_id(int min, int max);

void	loop();
void	setup();
void	stopRedLed(int track);
void	toggleRedLed(int track);
void	setOnLed(int pinNumberIndex, int track);
void	setOffLed(int pinNumberIndex, int track);
void	send_data(sensor_type t, float r, sensor_unit u);

bool	toggle_led(void *);
void	handleRedLedBlink();
bool	read_atmospheric_data(void *);
bool	read_atmospheric_humidity(void *);
bool	read_atmospheric_temperature(void *);

#endif
