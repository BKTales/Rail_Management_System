# int atoi(char *str, int *value)

#check max and min int
# check only sign
.section .text
	.global atoiS


# USES:
	# t0 = total_val
	# t1 = sign
	# t2 = *str (value)
	# t3 = auxiliar

# RECEIVES:
	# a0 = char *str
	# a1 = int *value
atoiS:
	# t0 = total_val
	# t1 = sign
	# t2 = *str (value)
	# t3 = auxiliar
	addi t0, zero, '-' # t0 = '-'
	addi t1, zero, '+' # t1 = '+'
	addi t2, zero, 0

	lb t2, 0(a0) # t2 = *str (value)

	# if (str == '/0') -> set_neg_sign
	beqz t2, end_error
	# if (str == '-') -> set_neg_sign
	beq t2, t0, set_neg_sign
	# if (str != '+') -> loop
	bne t2, t1, reset_t0

set_positive_sign:
	addi a0, a0, 1 # str++
	# setting t0 and t1 to the wanted values
	addi t0, zero, 0 # total_val = 0
	addi t1, zero, 1 # sign = 1
	j check_only_sign

set_neg_sign:
	addi a0, a0, 1 # str++
	# setting t0 and t1 to the wanted values
	addi t0, zero, 0 # total_val = 0
	addi t1, zero, -1 # sign = -1
	j check_only_sign

check_only_sign:
	lb t2, 0(a0) # t2 = *str (value)
	# if (str == '\0') -> end_error
	beqz t2, end_error
	j loop

reset_t0: # error 1
	addi t1, zero, 1 # sign = 1
	addi t0, zero, 0 # total_val = 0

# add function to check if there is only signs in here
loop:
	lb t2, 0(a0) # t2 = *str (value)
	# if (str == '\0') -> end
	beqz t2, end_success
	addi t3, zero, '#'
	# if (str == '#') -> end
	beq t2, t3, end_success

	is_num:
		addi t3, zero, '9' # t3 = '9'
		# if (str > '9') -> end_error
		bgt t2, t3, end_error

		addi t3, zero, '0' # t3 = '0'
		# if (str < '0') -> end_error
		blt t2, t3, end_error

	add_value:
		sub t2, t2, t3 # val_now = val_now - '0'
		addi t3, zero, 10 # t3 = 10
		mul t0, t0, t3 # total_val = total_val * 10
		add t0, t0, t2 # total_val = total_val + val_now

	increment:
		addi a0, a0, 1 # str++
		j loop

end_error:
	addi a0, zero, 0 # a0 = 0
	ret

end_success:
	mul t0, t0, t1
	sw t0, 0(a1) # value = total_val
	addi a0, zero, 1 # a0 = 1
	ret
