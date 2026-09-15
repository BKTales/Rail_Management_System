# int is_valid_token(char *token)

.section .text
	.global is_valid_token

# valid tokens:
# - TEMP
# - HUM

# a0 = *token
is_valid_token:
	# t1 = token (value)
	# t2 = wanted value
	addi t1, zero, 0
	addi t2, zero, 'T'
	lb t1, 0(a0) # t1 = token (value)
	# if (token != 'T') -> check_hum
	bne t2, t1, check_hum
	addi a0, a0, 1 # token++;
	lb t1, 0(a0) # t1 = token (value)
	addi t2, zero, 'E'
	# if (token != 'E') -> end_wrong
	bne t2, t1, end_wrong
	addi a0, a0, 1 # token++;
	lb t1, 0(a0) # t1 = token (value)
	addi t2, zero, 'M'
	# if (token != 'M') -> end_wrong
	bne t2, t1, end_wrong
	addi a0, a0, 1 # token++;
	lb t1, 0(a0) # t1 = token (value)
	addi t2, zero, 'P'
	# if (token != 'P') -> end_wrong
	bne t2, t1, end_wrong
	j end_success


check_hum:
	addi t2, zero, 'H'
	# if (token != 'H') -> check_hum
	bne t2, t1, end_wrong
	addi a0, a0, 1 # token++;
	lb t1, 0(a0) # t1 = token (value)
	addi t2, zero, 'U'
	# if (token != 'U') -> end_wrong
	bne t2, t1, end_wrong
	addi a0, a0, 1 # token++;
	lb t1, 0(a0) # t1 = token (value)
	addi t2, zero, 'M'
	# if (token != 'M') -> end_wrong
	bne t2, t1, end_wrong
	j end_success


end_wrong:
	addi a0, zero, 0 # a0 = 0
	j end

end_success:
	addi a0, a0, 1
	lb t1, 0(a0) # t1 = token (value)
	addi t2, zero, 0  # t2 = '\0'
	# if (token != '\0') -> end wrond
	bne t2, t1, end_wrong
	addi a0, zero, 1 # a0 = 1

end:
	ret

