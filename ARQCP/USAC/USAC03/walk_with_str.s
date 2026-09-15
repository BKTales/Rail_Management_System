# char *walk_with_str(char *str, char *token)
# walking with the pointer in the str the token size
# in this point it already  ensured that the first
# characters are the same in str and token
#
# RETURN - adrress in which the string is after walking
#			token in it.

.section .text
	.global walk_with_str

# a0 = char *str
# a1 = char *token
walk_with_str:
	# t0 = token (value)
	addi t0, zero, 0

loop:
	lb t0, 0(a1)
	# if(*token == '\0') -> end
	beqz t0, end

increment:
	addi a0, a0, 1 # str++;
	addi a1, a1, 1 # token++;
	j loop

end:
	ret
