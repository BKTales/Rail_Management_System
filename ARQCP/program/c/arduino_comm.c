#include "program_header.h"
#include <termios.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>

static int arduino_fd = -1;

int arduino_init(const char *port) {
    arduino_fd = open(port, O_RDWR | O_NOCTTY);
    if (arduino_fd < 0) {
        perror("Erro open serial");
        return FALSE;
    }
    
    struct termios tty;
    tcgetattr(arduino_fd, &tty);
    
    cfsetospeed(&tty, B9600);
    cfsetispeed(&tty, B9600);
    
    tty.c_cflag &= ~PARENB;
    tty.c_cflag &= ~CSTOPB;
    tty.c_cflag &= ~CSIZE;
    tty.c_cflag |= CS8;
    tty.c_cflag |= CREAD | CLOCAL;
    
    tty.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);
    tty.c_oflag &= ~OPOST;

    tcsetattr(arduino_fd, TCSANOW, &tty);
    
    usleep(2000000); 
    tcflush(arduino_fd, TCIOFLUSH);

    return TRUE;
}


void arduino_close() {
    if (arduino_fd >= 0) {
        close(arduino_fd);
        arduino_fd = -1;
    }
}

int send_led_data_to_arduino(int track, const int ledOn) {
    if (arduino_fd < 0) return FALSE;  // Verifica se o arquivo do Arduino está aberto corretamente

    uint8_t buffer[8];

    memcpy(buffer, &track, 4);
    memcpy(buffer+4, &ledOn, 4);

    // Envia o buffer para o Arduino
    int written = write(arduino_fd, buffer, sizeof(buffer));
    if (written != sizeof(buffer)) {
        perror("Erro de escrita no Arduino");
        return FALSE;
    }

    return TRUE;
}

char* read_from_sensors() {
    // Verifica se os ponteiros de saída são válidos
    if (arduino_fd < 0) {
        return NULL; 
    }
    
   
    char *buffer = (char*)malloc(MAX_LINE_LENGTH);
    if (!buffer) {
        perror("Erro de alocação de memória");
        return NULL;
    }

   
    size_t bytes_received = 0;
    char c;
    while (bytes_received < MAX_LINE_LENGTH - 1) {
        // Tenta ler um único byte (com timeout implícito ou explícito na configuração do serial)
        int n = read(arduino_fd, &c, 1);
        
        if (n < 0) {
            // Erro de leitura
            perror("Erro ao ler da porta serial");
            free(buffer);
            return NULL;
        } else if (n == 0) {
            // Nenhum byte lido. Tentativa de otimização/pausa.
            usleep(100); 
            continue;
        }
        
        // Byte lido com sucesso
        buffer[bytes_received++] = c;
        
        // Verifica se é o final da linha
        if (c == '\n') {
            break;
        }
    }
   
    if (bytes_received > 0 && buffer[bytes_received - 1] == '\n') {
        buffer[bytes_received - 1] = '\0'; // Remove o '\n' e termina a string
    } else {
        buffer[bytes_received] = '\0'; // Termina a string
    }
    // Se o buffer estiver cheio e não vimos '\n', algo deu errado.
    if (bytes_received == MAX_LINE_LENGTH - 1) {
         fprintf(stderr, "Aviso: Linha serial muito longa ou parcial (MAX_LINE_LENGTH atingido).\n");
         free(buffer);
         return NULL;
    }
    
    return buffer;
}

int send_exit_command_to_arduino() {
    if (arduino_fd < 0) return FALSE;  // Verifica se o arquivo do Arduino está aberto corretamente

    char buffer[1];
    char exit = 0;

    memcpy(buffer, &exit, 1);

    // Envia o buffer para o Arduino
    int written = write(arduino_fd, buffer, sizeof(buffer));
    if (written != sizeof(buffer)) {
        perror("Erro de escrita no Arduino");
        return FALSE;
    }

    return TRUE;
}