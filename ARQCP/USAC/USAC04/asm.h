#ifndef ASM4_H
#define ASM4_H

/**
 * @brief Formats a command string based on the given inputs.
 *
 * This function takes an operation string, a numeric argument,
 * and an output buffer. It validates the operation and, if valid,
 * writes the formatted command into the output buffer.
 *
 * @param op   Pointer to the input operation string.
 * @param n    Integer argument for the command.
 * @param cmd  Pointer to the buffer where the formatted command is stored.
 *
 * @return
 *   1 if the command is valid.
 *   0 if the command is invalid.
 */
int format_command(char *op, int n, char *cmd);

/**
 * @brief Compares two null-terminated strings.
 *
 * Returns 1 if the strings are equal and 0 otherwise.
 *
 * @param s1   Pointer to the first string.
 * @param s2   Pointer to the second string.
 *	
 * @return
 *   1 if the strings are identical.
 *   0 if they differ.
 */
int string_compare(const char *s1, const char *s2);

/**
 * @brief Initializes auxiliary variables used internally in the assembly routines.
 *
 * The assembly implementation does not take parameters and performs
 * internal setup (such as loading constant bounds for letter checks).
 */
void initialize_auxiliar_variables(void);

/**
 * @brief Trims leading and trailing spaces from a string.
 *
 * The trimmed result is written into a global buffer defined in assembly.
 *
 * @param input   Pointer to the string that will be trimmed.
 */
void trim(char *input);



#endif
