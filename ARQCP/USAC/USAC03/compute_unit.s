# int compute_unit_value(char *str, char *save_unit, chat expected_operator)
# Compute the value found until expected operator, checking if it does not
# pass the memory pre allocate from the variables(always 20 bytes), if the
# declaration od the value passes the amount of memory allocated it will
# return a error(0)
#
# RETURN (1) - Value stored succesfully
# RETURN (0) - Could not compute the value

.section .data
	.equ ALLOCATED_BYTES, 19
	# remember that last char needs to be '\0' so instead of 20 it is 19

.section .text
	.global compute_value

# USES:
	# t0 = str (value)
	# t1 = size_control
	# t2 = size_max

# RECEIVES:
	# a0 = char *str
	# a1 = char *save_unit
	# a2 = chat expected_operator
compute_value:
	# t0 = str (value)
	# t1 = size_control
	# t2 = size_max
	addi t0, zero , 0
	addi t1, zero , 0
	li t2, ALLOCATED_BYTES

loop:
	lb t0, 0(a0)  # t0 = str (value)
	# if (*str == expected_operator) -> end_success
	beq t0, a2, end_success

	# if (size_control == size_max) -> end_error
	beq t1, t2, end_error

verify_zero:
# This validation exists becuase if the separator is not '\0' we need to see if
# we are not going to go beyond the boundaries of the string.
# Since it is already being check up in the function if the separator is '\0'
# in case it comes here it's becuase it is an error(spearator was not found)!
	# if (*str == '\0') -> end_error
	beq t0, a2, end_error

copy_char:
	sb t0, 0(a1) # *save_unit = *str

increment:
	addi a0, a0 , 1 # str++
	addi a1, a1 , 1 # save_unit++
	addi t1, t1 , 1 # size_control++
	j loop

end_error: # clean the save_unit in main function if this is returned!
	addi a0, zero, 0
	j end

end_success:
	addi a0, zero, 1

end:
	sb zero, 0(a1) # save_unit[size] = '\0'
	ret
