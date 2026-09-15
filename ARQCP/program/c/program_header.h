#ifndef PROGRAM_HEADER
# define PROGRAM_HEADER

# include "./arduino_comm.h"
# include <time.h>
# include <stdio.h>
# include <fcntl.h>
# include <string.h>
# include <stdlib.h>
# include <stdint.h>
# include <unistd.h>
# include <signal.h>
# include <sys/time.h>
# include "../../USAC/USAC01/asm.h"
# include "../../USAC/USAC02/asm.h"
# include "../../USAC/USAC03/asm.h"
# include "../../USAC/USAC04/asm.h"
# include "../../USAC/USAC05/asm.h"
# include "../../USAC/USAC06/asm.h"
# include "../../USAC/USAC07/asm.h"
// # include "../../USAC/USAC08/asm.h"
# include "../../USAC/USAC09/asm.h"
# include "./get_next_line/get_next_line.h"
# include "./arduino_comm.h"  
# include <ctype.h>

# define ASK_FOR_INPUT "=> "
# define ASK_FOR_USERNAME "Enter username: "
# define ASK_FOR_PASSWORD "Enter password: "

# define ARDUINO_PORT "/dev/ttyACM0"

// ANSI color codes
#define RED     "\x1b[31m"
#define GREEN   "\x1b[32m"
#define YELLOW  "\x1b[33m"
#define BLUE    "\x1b[34m"
#define MAGENTA "\x1b[35m"
#define CYAN    "\x1b[36m"
#define WHITE   "\x1b[37m"
#define BRIGHT_RED "\x1b[91m"
#define BRIGHT_GREEN "\x1b[92m"
#define BRIGHT_YELLOW "\x1b[93m"
#define BRIGHT_BLUE "\x1b[94m"
#define RESET   "\x1b[0m"
#define BOLD    "\x1b[1m"
#define CLEAR   "\x1b[2J\x1b[H"
#define CLEAR_LINE "\x1b[2K\r"

// Response codes
# define ERROR_RESPONSE 0					// command do not exist response
# define INFORMATION_RECEIVED_RESPONSE 1	// GTH success
# define GREEN_ON_RESPONSE 2				// GE success
# define RED_ON_RESPONSE 3					// RE success
# define YELLOW_ON_RESPONSE 4				// YE success
# define RED_BLINK_RESPONSE 5				// RB success
# define EXIT_RESPONSE 6					// RB success

# define USER_DEFINITION 1
# define TRACK_DEFINITION 2
# define LINE_BAD_FORMATTED 3

# define TRUE 1
# define FALSE 0
# define SUCCESS 1
# define ERROR -1
# define MIN_VALID_LINE_LEN 8

# define CIRCULAR_BUFFER_LENGTH 50
# define MAX_LINE_LENGTH 64
# define MOVING_MEDIAN_N_VALUES 9

typedef struct s_user {
	int		cypher;
	char	*name;
	char	*username;
	char	*password;
}				t_user;

typedef struct s_light_signs
{
	int	yellow;
	int	green;
	int	red;
}				t_light_signs;

typedef struct s_track
{
	int				track_id;
	int				train_id;
	t_light_signs	*signs;
}				t_track;

typedef struct {
    int 			*buffer; 
    int 			head;             
    int 			tail;             
    int 			count;            
    int 			size;            
} 		circularBuffer;

typedef struct s_program
{
	char			*log_filename;
	t_user			*user;
	t_track			*track;
	circularBuffer  *temperature_buffer;
	circularBuffer  *humidity_buffer;
	int 			humidity;
	int 			temperature;
	int				many_track;
}				t_program;

void	setting_signals(void);
void	toggleRedLed(int track);
void	write_funny_response(int which);
void	setOnLed(int pinIndex, int track);
void	setOffLed(int pinIndex, int track);
void	allocate_sign(t_program *prog, char *line);
void	splitStr(const char *input, char **textPart, char **number, int comaIndex);
void	exec_function(t_program *prog, char *cmd, int data);
void    print_moving_median_string(t_program *prog);
void 	extract_data_to_struct(char *str, int *temperature, int *humidity);
void 	control_leds(t_program *prog, char *cmd, int data);
void 	update_circular_buffers(t_program *prog,int *temperature,int *humidity);
void 	update_sensors_data(t_program *prog);
void	manage_railway_tracks(t_program *prog);
void 	replace_input(char **input, char *formatted);

char	*get_time(void);
char	*ft_itoa(int n);
char	*copy_to_new_string(char *line, int i, int many_char);
char 	*clean_pass(char *pass);

int		is_exit(char *input);
int		login(t_program *prog);
int		get_which_action(char *line);
int		get_cypher(char *line, int i);
int		is_valid_instruction(char **input, int *data);
int		read_file(t_program *prog, int file_fd);
int		count_until_next_space(char *line, int i);
int		allocate_user(t_program *prog, int i, char *line);
int		encrypt_data(char* in, int key, char* out);
int		decrypt_data(char* in, int key, char* out);
int		write_log(t_program *program, char *message);
int		clean_finish(t_program *prog, int to_return);
int		start_program(t_program *prog, char *filename);
int		contains_caracter(const char *cmd, char target);
int		loop_instruction_treatment(t_program *prog, char *input);
int		count_until_next_hyphen(char *line, int i);
int 	calculate_moving_median(t_program *prog);

/**
 * @brief Concatenates Program User Info + Message + Time
 * Defined in concatenate.s
 */
char *concatenate(t_program *prog, char *msg);
char *lower_string(char *str);

#endif

