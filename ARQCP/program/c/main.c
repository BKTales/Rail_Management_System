#include "program_header.h"


int clean_finish(t_program *prog, int to_return)
{
	if (prog != NULL)
	{
		if (prog->many_track != 0)
		{
			if (prog->track != NULL)
				free(prog->track);
		}
		if (prog->user != NULL)
		{
			if (prog->user->name != NULL)
				free(prog->user->name);
			if (prog->user->username != NULL)
				free(prog->user->username);
			if (prog->user->password != NULL)
				free(prog->user->password);
			free(prog->user);
		}
		if (prog->log_filename != NULL)
			free(prog->log_filename);
		if (prog->temperature_buffer != NULL) {
			if (prog->temperature_buffer->buffer != NULL)
				free(prog->temperature_buffer->buffer);
    		free(prog->temperature_buffer);           
		}

		if (prog->humidity_buffer != NULL) {
			if (prog->humidity_buffer->buffer != NULL)
				free(prog->humidity_buffer->buffer);   
			free(prog->humidity_buffer);              
		}
		
	}
	return (to_return);
}

int loop_instruction_treatment(t_program *prog, char *input)
{
	int	data;
	if (input != NULL)
	{
		
		// if input is "exit" or "EXIT" or "ExIt", etc it's the same (safety reason)
		char* lower_input = lower_string(input);
		int result = -1;

		if(lower_input != NULL){
			result = strcmp(lower_input, "exit");
			free(lower_input);
		}

		if (result == 0)
			return (write_funny_response(EXIT_RESPONSE), FALSE);

		int which_to_do = is_valid_instruction(&input, &data);
		if (which_to_do)
		{
			exec_function(prog, input, data);
			write_funny_response(which_to_do);
		}
		else
			write_funny_response(ERROR_RESPONSE);
		write_log(prog, input);
	}
	return (TRUE);
}

/**
 * Function will try 3 times to login the user's info compared to the one in the file
 * if succed the program will be started, otherwise, the program should end!
 *
 * @param prog program informations
 * @param input line inputed by the user
 * @return (SUCCESS - the login was made succefuly)
 * @return (ERROR - the user could not login)
 */
int login(t_program *prog)
{

	//return SUCCESS;
	// some issue I cant fix. BK

	char	*login;
	char	*pass;
	char	*decrypt_pass;
	int		i = -1;

	login = malloc(sizeof(char) * 100);
	pass = malloc(sizeof(char) * 100);
	while (++i < 3)
	{
		
		write(1, ASK_FOR_USERNAME, 16);
		scanf("%99s", login);
		write(1, ASK_FOR_PASSWORD, 16);
		scanf("%99s", pass);

		decrypt_pass = malloc(sizeof(char) * (strlen(pass) + 1));
		if (decrypt_data(prog->user->password, prog->user->cypher, decrypt_pass))
		{
			pass = clean_pass(pass);
			login = clean_pass(login);
			if (strcmp(login, prog->user->username) == 0 &&
					strcmp(pass, decrypt_pass) == 0)
			{
				free(pass);
				free(login);
				free(decrypt_pass);
				return (SUCCESS);
			}
		}
		if (decrypt_pass != NULL)
			free(decrypt_pass);
	}
	write(1, "You've exceeded the available attempts!", 39);
	free(login);
	free(pass);
	return (ERROR);
}

int main(int ac, char **av)
{
	t_program	*prog = NULL;
	char		*input = NULL;
	int			is_to_run = TRUE;

	if (ac == 2)
	{
		prog = malloc(sizeof(t_program));
		if (!prog)
			return (write(1, "Malloc failed\n", 14));
		if (!start_program(prog, av[1]))
			return (1);
		if (login(prog) != SUCCESS)
			return (clean_finish(prog, 1));
		manage_railway_tracks(prog);

		input = malloc(sizeof(char) * 100);
		if (!input)
			return (write(1, "input Malloc failed\n", 20), clean_finish(prog, 1));

		// LOOP PRINCIPAL
		while (is_to_run)
		{
			write(1, ASK_FOR_INPUT, 3);
			scanf("%99s", input);
			is_to_run = loop_instruction_treatment(prog, input);
		}
		free(input);

		send_exit_command_to_arduino();
	}
	else
		write(1, "You need to enter a setup file on execution call!\n", 50);
	return (clean_finish(prog, 0));
}
