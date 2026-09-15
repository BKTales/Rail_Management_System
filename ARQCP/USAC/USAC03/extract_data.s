# int		extract_data(char* str, char* token, char*unit, int* value);
# It receives as input two strings, str and token, and as output it receives two pointers, one for a string, unit, and another for an integer, value. The content of the
# str is formatted as follows:
#TOKEN&unit:xxxxxxx&value:xx#TOKEN&unit:xxxxxxxx&value:xx
# where TOKEN could be TEMP or HUM for temperature or humidity, respectively.
#
# This function extracts value and unit data from str according to the token. It
# should return 1 if it succeeds, 0 otherwise (in this case, it should set value to zero
# and unit to an empty string, "" )
.section .data
unit_token:
	.asciz "unit"
value_token:
	.asciz "value"
#separator_token_value:
#	.byte '&'
#separator_declaration:
#	.byte '#'


.section .text
	.global extract_data

# a0 = char *str
# a1 = char *token
# a2 = char *unit
# a3 = int *value
extract_data:
	addi sp, sp, -32
	sw a0, 4(sp)  # sp 0 - 4 = char *str
	sw a1, 8(sp)  # sp 4 - 8 = char *token
	sw a2, 12(sp)  # sp 8 - 12 = char *unit
	sw a3, 16(sp)  # sp 12 - 16 = int *value
	sw ra, 20(sp)  # sp 16 - 20 = ra
	#  if (var == NULL)
	beqz a0, end_error
	beqz a1, end_error
	beqz a2, end_error
	beqz a3, end_error
	# add check (token == TEMP) or (token == HUM)
	lw a0, 8(sp) # a0 = char *token
	call is_valid_token
	beqz a0, end_error

#checked
call_check_initial_token:
	# a0 = str
	lw a0, 4(sp) # a0 = char *token
	# a1 = token
	lw a1, 8(sp) # a0 = char *token
	# a2 = expected_separator
	addi a2, zero, '&' # a2 == '&'
	call check_token
	# checking if the tokens match, if it dont go to next cycle/end it just need to check if the return is 0
	# if (a0 == 0) -> loop_until_end_or_hashtag
	beqz a0, restore_before_loop

	lw a0, 4(sp) # a0 = char *str
	lw a1, 8(sp) # a0 = char *token
	addi t0, zero, 0
	addi t1, zero, '#'
	j call_walk_initial_token

restore_before_loop:
	lw a0, 4(sp) # a0 = char *str
	lw a1, 8(sp) # a0 = char *token
	addi t0, zero, 0
	addi t1, zero, '#'

loop_until_end_or_hashtag:
	lb t0, 0(a0) # a0 = str (value)
	addi a0, a0, 1 # str++
	# if (*str == '#') -> call_check_initial_token
	sw a0, 4(sp)  # sp 0 - 4 = char *str
	beq t0, t1, call_check_initial_token
	# if (*str == '\0') -> end_error
	beqz t0, end_error
	j loop_until_end_or_hashtag


#checked
call_walk_initial_token:
	# reloading adress to a0
	lw a0, 4(sp) # a0 = char *str
	# reloading token to a1
	lw a1, 8(sp) # a1 = token
	call walk_with_str
	# saving on stack
	addi a0, a0, 1 # str++ # jumping separator
	sw a0, 4(sp) # 4(sp)  = modified returned str

# ============== Next token =============== #

#checked
call_check_unit_token:
	# a0 = str (already)
	# loading address to a1
	la a1, unit_token # a1 = unit_token
	# a2 = expected_separator
	addi a2, zero, ':'
	call check_token
	# checking if the tokens match, if it dont go to next cycle/end it just need to check if the return is 0
	# if (a0 == 0) -> end_error
	beqz a0, end_error

#checked
call_walk_unit_token:
	# reloading adress to a0
	lw a0, 4(sp) # a0 = char *str
	# loading address to a1
	la a1, unit_token # a1 = unit_token
	call walk_with_str
	addi a0, a0, 1 # jumping separator
	# saving on stack
	sw a0, 4(sp) # 4(sp)  = modified returned str

# ============== Next token =============== #

#checked
call_compute_unit:
	# a0 = str (already)
	# loading address to a1
	lw a1, 12(sp) # a1 = save_unit
	# a2 = expected_separator'&')
	addi a2, zero, '&'
	call compute_value
	# if (a0 == 0) -> end_error
	beqz a0, end_error
#checked
call_walk_unit:
	# reloading adress to a0
	lw a0, 4(sp) # a0 = char *str
	# loading address to a1
	lw a1, 12(sp) # a1 = save_unit
	call walk_with_str
	addi a0, a0, 1 # jumping separator
	# saving on stack
	sw a0, 4(sp) # 4(sp)  = modified returned str

# ============== Next token =============== #s

#checked
call_check_value_token:
	# a0 = str (already)
	# loading address to a1
	la a1, value_token # a1 = value_token
	# a2 = expected_separator
	addi a2, zero, ':'
	call check_token
	# checking if the tokens match, if it dont go to next cycle/end it just need to check if the return is 0
	# if (a0 == 0) -> end_error
	beqz a0, end_error

#checked
call_walk_value_token:
	# reloading adress to a0
	lw a0, 4(sp) # a0 = char *str
	# loading address to a1
	la a1, value_token # a1 = value_token
	call walk_with_str
	addi a0, a0, 1 # jumping separator
	# saving on stack
	sw a0, 4(sp) # 4(sp)  = modified returned str

# ============== Next token =============== #s

#checked
call_atoi:
	# a0 = str (already)
	# a1 = *value
	lw a1,16(sp) # loading address to a1
	call atoiS
	# if (a0 == 0) -> end_error
	beqz a0, end_error
	# else
	j end_success

# ============== End Functions =============== #s

# will erase the unit(put a '\0' in its begining and end_error)
end_error:
	addi a0, zero, 0 # a0 = 0
	lw a2, 12(sp)   
	sb a0, 0(a2) # save_unit[0] = '\0'
	lw a1, 16(sp) # a1 = *value
	sw a0, 0(a1) # value = 0
	j end

end_success:
	addi a0, zero, 1 # a0 = 1

# clean stack pointer and change the return address to the original one!
end:
	# reload return address
	lw ra, 20(sp) # ra = original return address
	# clean the stack
	addi sp, sp, 32
	ret
