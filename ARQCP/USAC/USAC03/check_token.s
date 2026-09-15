# int check_token(char* str, char* token, char separator)
# verify if the token is found in the beggining of the str,
# and if it is followed by the given separtor
#
# RETURN (1) - token found
# RETURN (0) - token not found or token == "" or
#				the given token/string has spaces(' ' or '\t')
.section .text
	.global check_token

# USES:
	# t0 = val (val ptr1)
	# t1 = val (val token)
	# a5 = ' '
	# a6 = '\t'

# RECEIVES:
	# a0 = char* str
	# a1 = char* token
	# a2 = char separtor
check_token:
	# t0 = val (val ptr1)
	# t1 = val (val token)
	addi t0, zero, 0
	addi t1, zero, 0
	# a5 = ' '
	# a6 = '\t'
	addi a5, zero, ' '
	addi a6, zero, '\t'

check_empty_token:
	beqz a0, end_diff # checking if a0 == NULL
	beqz a1, end_diff # checking if a1 == NULL
	lb t1, 0(a1) # t1 = val (val token)
	beq t1, zero, end_diff # checking a1 = ""

	loop:
		lb t0, 0(a0) # t0 = val (val str)
		lb t1, 0(a1) # t1 = val (val token)

		checking_separators:
			# if (t1 == ' ') -> end
			beq t1, a5, end_diff
			# if (t1 == '\t') -> end
			beq t1, a6, end_diff

		checking_end:
			# if (t1 == '/0') -> end
			beq t1, zero, end_token
			# if (t0 == '/0') -> end
			beq t0, zero, end_diff # means that str is shorter than token, so it cannot be token!

		checking_equals:
			# if (t0 == t1) -> increment
			beq t0, t1, increment
			# else -> end_diff
			j end_diff


	increment:
		addi a0, a0, 1 # str++;
		addi a1, a1, 1 # token++;
		j loop


# when ended token verify if the str still have something
# and if that something is the character '&'
end_token:
	# if (t0 == '/0') -> end
	beq t0, zero, end_diff

	# if (*str != separator) -> end_diff
	bne t0, a2, end_diff

	# else
	addi a0, zero, 1
	j end

end_diff:
	addi a0, zero, 0

end:
	ret
