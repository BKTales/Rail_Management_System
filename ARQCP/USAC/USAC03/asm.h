#ifndef ASM3_H
#define ASM3_H


/**
 * Function will read the value until found the expected_operator.
 * If the expected_operator is not found until (ALLOCATED_BYTES - 1)(fixed 20 - 1)
 * or the size exceds the size_max(ALLOCATED_BYTES) it will be returned 0, and the string
 * will be still allocated, need to be cleande by the function using this one!
 *
 * @param str str to read value from
 * @param save_unit str to save found unit in
 * @param expected_separtor separator to read until it
 * @return (1) - Value Computed
 * @return (0) - Value Not computed, some error found in the definition
 */
int		compute_value(char *str, char *save_unit, char expected_separtor);

/**
 * Verify if the token is found in the beggining of the str,
 * and if it is followed by the given separtor
 *
 * @param str str to check tocken on
 * @param token token to find in str
 * @param separator separator to read until it
 * @return (1) - token found
 * @return (0) - token not found or token == "" or the given token/string has spaces(' ' or '\t')
 */
int		check_token(char* str, char* token, char separator);

/**
 * Walks with the pointer in the str the token size
 *
 * OBS: in this point it already ensured that the first
 * characters are the same in str and token!
 *
 * @param str str to be walked
 * @param token token to walk
 * @return (pointer to new start of the string)
 */
char*	walk_with_str(char *str, char *token);

/**
 * Function will read the value until found the expected_operator.
 * If the expected_operator is not found until (ALLOCATED_BYTES - 1)(fixed 20 - 1)
 * or the size exceds the size_max(ALLOCATED_BYTES) it will be returned 0, and the string
 * will be still allocated, need to be cleande by the function using this one!
 *
 * @param str str to read value from
 * @param save_unit str to save found unit in
 * @param expected_separtor separator to read until it
 * @return (1) - Value Computed
 * @return (0) - Value Not computed, some error found in the definition
 */
int		atoiS(char *str, int *value);

int		extract_data(char* str, char* token, char*unit, int* value);

int	is_valid_token(char *token);


// int extract_data (char* input, char * token, char * unit, int * value);
// char *  find_token (char* input, char * token);
// int to_num ( char* str);
#endif

