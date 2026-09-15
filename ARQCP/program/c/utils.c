
# include "program_header.h"


/**
 * @brief Function will check if the given input has the right/valid command
 * Valid options:
 *
 * GTH, RE, YE,GE or RB followed by the number of which sensor/ to use
 *
 * @param input address of the inputed string
 * @return (TRUE - Valid Input && input = ["cmd,which"])
 * @return (FALSE - Invalid Input)
 */
int is_valid_instruction(char **input, int *data)
{
    if (input == NULL || *input == NULL) return FALSE;

    int comaIndex = contains_caracter(*input, ',');

    if(comaIndex == -1){
        return FALSE;
    }

    int len = strlen(*input); 
    char *textPart = malloc(comaIndex + 1);
    char *number = malloc(len - comaIndex);
	char *formatted_input = malloc(strlen(*input)); 	
    
    if (!textPart || !number || !formatted_input) {
        if(textPart) free(textPart);
        if(number) free(number);
        if(formatted_input) free(formatted_input);
        return FALSE;
    }

    splitStr(*input, &textPart, &number, comaIndex);
    *data = atoi(number);
    int response = format_command(textPart,*data,formatted_input);
    replace_input(input,formatted_input);
    free(textPart);
    free(number);
    free(formatted_input);

    return response;
}

void replace_input(char **input, char *formatted)
{
    free(*input);
    *input = malloc(strlen(formatted) + 1);
    if (!*input) return;

    strcpy(*input, formatted);
}


void splitStr(const char *input, char **textPart, char **number, int comaIndex){
	strncpy(*textPart, input, comaIndex);
    (*textPart)[comaIndex] = '\0';
    strcpy(*number, input + comaIndex + 1);
}


/**
 *@brief Returns index of first char ocurrence or 0
 @param cmd string
 @param target character to search for
 @return (i = index)
*/
int contains_caracter(const char *cmd, char target)
{
	if (cmd == NULL) return -1;

	int len = strlen(cmd);
    int count = 0;
	int index = -1;


    for (int i = 0; i < len; i++) {
        if (cmd[i] == target) {
            count++;
			if (count > 1)
			{
				return -1;
			}
			index = i;

        }
    }

    return index;
}

/**
 * @brief Set the signals actions in the program
 * so we do not have to deal with signals
 */
void	setting_signals(void)
{
	struct sigaction	sa;

	sigemptyset(&sa.sa_mask);
	sa.sa_flags = SA_SIGINFO;
	signal(SIGINT, SIG_IGN);
	signal(SIGQUIT, SIG_IGN);
}

char *copy_to_new_string(char *line, int i, int many_char)
{
	char	*copy;
	int		j = -1;

	copy = malloc(sizeof(char) * (many_char + 1));
	if (!copy)
		return (NULL);
	while (++j < many_char && line[i + j] != '\0')
		copy[j] = line[i + j];
	copy[j] = 0;
	return (copy);
}

int count_until_next_space(char *line, int i)
{
	int count = 0;
	while (line[i + count] != ' ')
	{
		if (line[i + count] == '\0')
			return (ERROR);
		count++;
	}
	return (count);
}

int count_until_next_hyphen(char *line, int i)
{
	int count = 0;
	while (line[i + count] != '-')
	{
		if (line[i + count] == '\0')
			return (ERROR);
		count++;
	}
	return (count);
}

char *lower_string(char *str){
    int length = strlen(str);
    char* lower_string;

    lower_string = malloc(sizeof(char) * (length + 1));
    if(!lower_string) return NULL;

    for(int i = 0; i < length; i++){
        *(lower_string + i) = tolower(*(str + i));
    }
    lower_string[length] = '\0'; 

    return lower_string;
}

char *clean_pass(char *pass)
{
    int i = 0;
    while(pass[i] != '\0')
    {
        if (pass[i] == '\n')
            pass[i] = '\0';
        i++;
    }
    return pass;
}