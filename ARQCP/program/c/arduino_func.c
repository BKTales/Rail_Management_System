#include "program_header.h"

int get_active_led_index(t_light_signs *s)
{
    if (s->red == 1) {
        s->red = 0;
        return RED_ON_RESPONSE - 3;
    }
    if (s->yellow == 1){
        s->yellow = 0;
        return YELLOW_ON_RESPONSE - 3;
    }    
    if (s->green == 1){
        s->green = 0;
        return GREEN_ON_RESPONSE;
    } 
    return -1; // nenhum LED ligado
}


void exec_function(t_program *prog, char *cmd, int data)
{
    update_sensors_data(prog);
    if(strncmp(cmd, "GTH", 3) == 0){
        print_moving_median_string(prog);
    } else {
        control_leds(prog, cmd, data);
    }
}

/**
 * @brief Reads data from sensors multiple times and updates circular buffers.
 *
 * For each iteration, it reads the sensor string, extracts temperature and
 * humidity values, and updates the corresponding circular buffers.
 *
 * @param prog Pointer to the main program structure.
 */
void update_sensors_data(t_program *prog){
    for (int i = 0; i < 20; i++) // change 10 for a constant number(to be defined) doesnt represent number of readings
    //with n = 100 it gave me 50 readings(>1min) and with n = 10, it gave me 5(1sexit)
    {
        int temp = -1;
        int hum = -1;
        char *str = read_from_sensors();
        extract_data_to_struct(str, &temp, &hum);
        update_circular_buffers(prog, &temp, &hum);
    }

}

/**
 * @brief Prints the internal state of a circular buffer.
 *
 * This function prints the buffer size, count, head, tail, and optionally
 * the first element for debugging purposes.
 *
 * @param cb Pointer to the circular buffer structure.
 * @param name Name of the buffer for labeling the output.
 */
void print_circular_buffer(circularBuffer *cb, const char *name) {
    if (!cb) {
        printf("%s: NULL\n", name);
        return;
    }

    printf("%s:\n", name);
    printf("  Size: %d\n", cb->size);
    printf("  Count: %d\n", cb->count);
    printf("  Head: %d\n", cb->head);
    printf("  Tail: %d\n", cb->tail);

    printf("\n");
}


/**
 * @brief Updates the temperature and humidity circular buffers with new readings.
 *
 * If both temperature and humidity values are -1, nothing is done. Otherwise,
 * each valid value is added to the corresponding buffer using enqueue_value.
 *
 * @param prog Pointer to the main program structure containing the buffers.
 * @param temperature Pointer to the latest temperature value.
 * @param humidity Pointer to the latest humidity value.
 */
void update_circular_buffers(t_program *prog, int *temperature, int *humidity){
    if (prog == NULL || prog->temperature_buffer == NULL || prog->humidity_buffer == NULL)
        return;

    if ((temperature && *temperature == -1) && (humidity && *humidity == -1))
        return;


    if (*temperature != -1) {
        enqueue_value(
            prog->temperature_buffer->buffer,
            prog->temperature_buffer->size,
            &prog->temperature_buffer->count,
            &prog->temperature_buffer->tail,
            &prog->temperature_buffer->head,
            *temperature
        );
    }

    if (*humidity != -1) {
        enqueue_value(
            prog->humidity_buffer->buffer,
            prog->humidity_buffer->size,
            &prog->humidity_buffer->count,
            &prog->humidity_buffer->tail,
            &prog->humidity_buffer->head,
            *humidity
        );
    }
}

int calculate_moving_median(t_program *prog)
{
    int res_temperature = 0;
    int res_humidity = 0;

    int* temperature_vec = malloc(sizeof(int) * MOVING_MEDIAN_N_VALUES);
    int* humidity_vec = malloc(sizeof(int) * MOVING_MEDIAN_N_VALUES);

    // move_n_to_array should return 1 if it succeeds, 0 otherwise (if there is no n elements to move)
    if(temperature_vec != NULL){
        res_temperature = move_n_to_array(prog->temperature_buffer->buffer, 
            prog->temperature_buffer->size, 
            &(prog->temperature_buffer->count),
            &(prog->temperature_buffer->tail),
            &(prog->temperature_buffer->head),
            MOVING_MEDIAN_N_VALUES,
            temperature_vec
        );
        if(res_temperature){
            median(temperature_vec, MOVING_MEDIAN_N_VALUES, &(prog->temperature));
        }
        free(temperature_vec);
    }

    if(humidity_vec != NULL){
        res_humidity = move_n_to_array(prog->humidity_buffer->buffer, 
            prog->humidity_buffer->size, 
            &(prog->humidity_buffer->count),
            &(prog->humidity_buffer->tail),
            &(prog->humidity_buffer->head),
            MOVING_MEDIAN_N_VALUES,
            humidity_vec
        );
        if(res_humidity){
            median(humidity_vec, MOVING_MEDIAN_N_VALUES, &(prog->humidity));
        }
        free(humidity_vec);
    }

    if(!res_temperature || !res_humidity) return 0;   

    return 1;
}

void print_moving_median_string(t_program *prog){
    int res = calculate_moving_median(prog);
    if(res){
        printf("\nTEMP&unit:celsius&value:%d#HUM&unit:percentage&value:%d\n", prog->temperature, prog->humidity);
    } else {
        printf("There was an error trying to calculate the moving median of the temperature and humidity!\n");
    }
}

/**
 * @brief Extracts temperature and humidity values from a sensor string.
 *
 * This function parses a formatted string received from sensors and fills the
 * corresponding temperature and humidity variables. The expected string format is:
 * "TOKEN&unit:xxxx&value:xx#TOKEN&unit:xxxx&value:xx"
 * where TOKEN can be "TEMP" or "HUM".
 *
 * @param prog Pointer to the program structure (can be used for context, if needed).
 * @param str Pointer to the input string read from the sensors. This string
 *            will be freed by this function.
 * @param temperature Pointer to an integer where the extracted temperature value
 *                    will be stored if present.
 * @param humidity Pointer to an integer where the extracted humidity value
 *                 will be stored if present.
 */
void extract_data_to_struct(char *str, int *temperature, int *humidity){
    char unit[20];
    int value;


    if (extract_data(str, "TEMP", unit, &value))
        *temperature = value; 

    if (extract_data(str, "HUM", unit, &value))
        *humidity = value;    

    free(str);
}

void control_leds(t_program *prog, char *cmd, int data){
    if (data < 1 || data > 2) {
        printf("Número de track inválido\n");
        return;
    }

    int ledOn = -1;


    if (strncmp(cmd, "RE", 2) == 0) {
        ledOn = 0; // Índice para o LED vermelho
        prog->track[data - 1].signs->red = 1;
    }
    else if (strncmp(cmd, "YE", 2) == 0) {
        ledOn = 1; // Índice para o LED amarelo
        prog->track[data - 1].signs->yellow = 1;
    }
    else if (strncmp(cmd, "GE", 2) == 0) {
        ledOn = 2; // Índice para o LED verde
        prog->track[data - 1].signs->green = 1;
    }
    else if (strncmp(cmd, "RB", 2) == 0) {
        ledOn = 3; // Índice para o LED vermelho
    }

    // Enviar o índice do LED para o Arduino
    if (ledOn != -1) {
        send_led_data_to_arduino(data - 1, ledOn); // Envia o índice e o estado "ligado"
    }
}
