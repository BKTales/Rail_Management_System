#ifndef ARDUINO_COMM_H
# define ARDUINO_COMM_H
# define MAX_SENSOR_DATA_LENGTH 64

int arduino_init(const char *port);
void arduino_close(void);
int send_led_data_to_arduino(int track, const int ledOn);
int send_exit_command_to_arduino();
void processCommand(int track, int ledOn);
char *read_from_sensors();
#endif