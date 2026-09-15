#include "program_header.h"

int is_valid_pass(char *password)
{
	while (*password != '\0')
	{
		if (*password < 'A' || *password > 'Z')
			return (FALSE);
		password++;
	}
	return (TRUE);
}

int start_program(t_program *prog, char *filename)
{
	int	file_fd;

	setting_signals();
	if (!arduino_init(ARDUINO_PORT))
        return (write(1, "Error on startArduino!\n", 23), clean_finish(prog, 0));

	file_fd = open(filename, O_RDONLY);
	if (file_fd < 0)
		return (write(1, "Invalid Filename! Try again\n", 28));

	prog->user = malloc(sizeof(t_user) * 1);
	if (!prog->user)
		return (write(1, "Could not malloc the program!\n", 30));

	prog->track = malloc(sizeof(t_track) * 2);
	if (!prog->track)
		return (write(1, "Could not malloc the program!\n", 30));
	prog->many_track = 2;

	prog->temperature_buffer = malloc(sizeof(circularBuffer));
    if (!prog->temperature_buffer)
        return (write(1, "Could not malloc temperature buffer!\n", 38), clean_finish(prog, 0));

    prog->temperature_buffer->head = 0;
    prog->temperature_buffer->tail = 0;
    prog->temperature_buffer->count = 0;
    prog->temperature_buffer->size = CIRCULAR_BUFFER_LENGTH;
	prog->temperature = 0;

    prog->temperature_buffer->buffer = malloc(sizeof(int) * CIRCULAR_BUFFER_LENGTH);
    if (!prog->temperature_buffer->buffer)
        return (write(1, "Could not malloc temperature buffer array!\n", 44), clean_finish(prog, 0));

    prog->humidity_buffer = malloc(sizeof(circularBuffer));
    if (!prog->humidity_buffer)
        return (write(1, "Could not malloc humidity buffer!\n", 35), clean_finish(prog, 0));

    prog->humidity_buffer->head = 0;
    prog->humidity_buffer->tail = 0;
    prog->humidity_buffer->count = 0;
    prog->humidity_buffer->size = CIRCULAR_BUFFER_LENGTH;
	prog->humidity = 0;

    prog->humidity_buffer->buffer = malloc(sizeof(int) * CIRCULAR_BUFFER_LENGTH);
    if (!prog->humidity_buffer->buffer){
        return (write(1, "Could not malloc humidity buffer array!\n", 41), clean_finish(prog, 0));
	}

	return (read_file(prog, file_fd));
}

int close_parsing(t_program* prog, char *line, int file_fd)
{
	clean_finish(prog, FALSE);
	free(line);
	close(file_fd);
	return (FALSE);
}

int allocate_track(t_program *prog, char *line, int which_track)
{
	int track_id;
	int train_id;
	int	i = 6; // "TRACK: " starting at ' ' or first char

	train_id = 0;
	if (line[i] == ' ')
		i++;

	track_id = atoi(&line[i]);
	while (line[i] != ' ' && line[i] != '\0' && line[i] != '\n' && line[i] != '\r')
	{
		if (line[i] < '0' || line[i] > '9'){
			return (ERROR);
		}
		i++;
	}

	if (line[i] == ' ')
		i++;

	if (line[i] != '\0' &&  line[i] != '\n' && line[i] != '\r')
	{
		train_id = atoi(&line[i]);
		if (line[i] == '+')
			i++;
		else if (line[i] == '-')
			i++;
		while (line[i] != '\0' && line[i] != '\n' && line[i] != '\r')
		{
			if (line[i] < '0' || line[i] > '9'){
				return (ERROR);
			}
			i++;
		}
	}
	prog->track[which_track].track_id = track_id;
	prog->track[which_track].train_id = train_id;
	prog->track[which_track].signs = malloc(sizeof(t_light_signs) * 1);
	if (prog->track[which_track].signs == NULL)
		return (ERROR);
	return (SUCCESS);
}

int allocate_user(t_program *prog, int i, char *line)
{
	int	many_char;

	if (line[i] == ' ')
		i++;

	many_char = count_until_next_hyphen(line, i);
	if (many_char == ERROR)
		return (ERROR);
	prog->user->name = copy_to_new_string(line, i, many_char);
	i += many_char + 1;

	many_char = count_until_next_hyphen(line, i);
	if (many_char == ERROR)
		return (ERROR);
	prog->user->username = copy_to_new_string(line, i, many_char);
	i += many_char + 1;

	many_char = count_until_next_hyphen(line, i);
	if (many_char == ERROR)
		return (ERROR);
	prog->user->password = copy_to_new_string(line, i, many_char);
	i += many_char + 1;

	prog->user->cypher = get_cypher(line, i);
	if (prog->user->cypher == ERROR)
		return (ERROR);

	if (!is_valid_pass(prog->user->password))
		return (ERROR);

	if (!encrypt_data(prog->user->password, prog->user->cypher, prog->user->password))
		return (ERROR);
	return (SUCCESS);
}

int get_cypher(char *line, int i)
{
	int val = atoi(&line[i]);

	while ((line[i] >= '0' && line[i] <= '9')){
		i++;
	}
	if ((line[i + 1] != '\0' && line[i + 1] != '\n') || val < 0)
		return (ERROR);
	return (val);
}

int get_which_action(char *line)
{
	if (strlen(line) < MIN_VALID_LINE_LEN) return (ERROR_RESPONSE);

	if (line[0] == 'U' && line[1] == 'S' &&
			line[2] == 'E' && line[3] == 'R' && line[4] == ':' )
		return (USER_DEFINITION);
	// LIGHT_SIGN
		if (line[0] == 'T' && line[1] == 'R' &&
			line[2] == 'A' && line[3] == 'C' &&
			line[4] == 'K' &&  line[5] == ':')
		return (TRACK_DEFINITION);
	return (LINE_BAD_FORMATTED);
}

char *concatenate_log_file(char *time, char *username)
{
	int i = -1;

	char *temp = malloc(sizeof(char) * ft_strlen(username) + 10);
	if (temp == NULL)
		return (write(1, "malloc error for log_filename\n", 30), NULL);

	while (username[++i] != '\0')
		temp[i] = username[i];
	temp[i] = time[11];
	temp[i+1] = time[12];
	temp[i+2] = '_';
	temp[i+3] = time[14];
	temp[i+4] = time[15];
	temp[i+5] = '.';
	temp[i+6] = 'l';
	temp[i+7] = 'o';
	temp[i+8] = 'g';
	temp[i+9] = '\0';
	return (temp);
}

int read_file(t_program *prog, int file_fd)
{
	char	*line;
	int count_track = 0;

	line = get_next_line(file_fd);
	while (line != NULL)
	{
		// process data in line
		if (line[0] != '\n' && line[0] != '\0')
		{
			switch (get_which_action(line))
			{
				case USER_DEFINITION:
					if (allocate_user(prog, 5, line) == -1){
						return (write(1, "USER_BAD_FORMATTED\n", 19), close_parsing(prog, line, file_fd));
					}
					break;

				case TRACK_DEFINITION:
					int returned = allocate_track(prog, line, count_track);
					if (count_track == 2 || returned == ERROR)
						return (write(1, "TRCK_BAD_FORMATTED\n", 19), close_parsing(prog, line, file_fd));
					count_track++;
					break;

				case LINE_BAD_FORMATTED:
					return (write(1, "LINE_BAD_FORMATTED\n", 19), close_parsing(prog, line, file_fd));

				default:
					break;
			}
		}
		free(line);
		line = get_next_line(file_fd);
	}
	prog->log_filename = concatenate_log_file(get_time(), prog->user->username);
	if (prog->log_filename == NULL)
		return (close_parsing(prog, line, file_fd));

	close(file_fd);
	return (TRUE);
}

void manage_railway_tracks(t_program *prog)
{
	int i;

	if (prog == NULL || prog->track == NULL)
		return;

	write(1, "\n" BOLD CYAN "=== Railway Track Management System ===" RESET "\n", 55);

	// Iterate through all tracks
	for (i = 0; i < prog->many_track; i++)
	{
		// Initialize all light signs to OFF
		prog->track[i].signs->red = 0;
		prog->track[i].signs->yellow = 0;
		prog->track[i].signs->green = 0;

		// Check if track is pending (train_id < 0)
		if (prog->track[i].train_id < 0)
		{
			prog->track[i].train_id = prog->track[i].train_id * (-1);
			// Track PENDING - Set YELLOW light
			prog->track[i].signs->yellow = 1;
			send_led_data_to_arduino(i, 1); // 1 = YELLOW LED

			printf(BRIGHT_YELLOW "Track %d PENDING (Train %d) - YELLOW LIGHT ON\n" RESET,
				prog->track[i].track_id, prog->track[i].train_id);

			sleep(2);

			prog->track[i].signs->yellow = 0;
			prog->track[i].signs->red = 1;
			send_led_data_to_arduino(i, 0); // 0 = RED LED

			printf(BRIGHT_RED "Track %d OCCUPIED by Train %d - RED LIGHT ON\n" RESET,
				prog->track[i].track_id, prog->track[i].train_id);
		}
		// Check if track is occupied (train_id >= 1)
		else if (prog->track[i].train_id >= 1)
		{
			// Track OCCUPIED - Set RED light
			prog->track[i].signs->red = 1;
			send_led_data_to_arduino(i, 0); // 0 = RED LED

			printf(BRIGHT_RED "Track %d OCCUPIED by Train %d - RED LIGHT ON\n" RESET,
				prog->track[i].track_id, prog->track[i].train_id);
		}
		// Track is free (train_id == 0)
		else
		{
			prog->track[i].signs->green = 1;
			send_led_data_to_arduino(i, 2); // 2 = GREEN LED

			printf(BRIGHT_GREEN "Track %d FREE - GREEN LIGHT ON\n" RESET,
				prog->track[i].track_id);
		}
	}

	write(1, BOLD CYAN "========================================\n" RESET "\n", 56);
}

