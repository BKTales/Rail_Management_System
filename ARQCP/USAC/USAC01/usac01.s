.section .text
    .global encrypt_data

encrypt_data:
    li t0, 0 # input string size
    li t1, 'A' # load 'A' into t1
    li t2, 'Z' # load 'Z' into t2
    li t3, 1 # lower bound
    li t4, 26 # alphabet size
    blt a1, t3, return_error # if key is not in [1, 26]
    bgt a1, t4, return_error # if key is not in [1, 26]

check_valid_string: # verifies beforehand if the string is valid, to avoid unnecessary writes to memory later on (because of the test sentinels)
    lb t5, 0(a0) # load char pointed by a0 (*a0)
    beqz t5, verify_end_string # verify if t5 is a null character (could be either an empty string or end of string)
    blt t5, t1, return_error # char not between A and Z (< A)
    bgt t5, t2, return_error # char not between A and Z (> Z)
    addi a0, a0, 1
    addi t0, t0, 1
    j check_valid_string

verify_end_string:
    sub a0, a0, t0 # return input string pointer to its original position
    li t3, 0 # "initialize" output string size

loop_string:
    lb t5, 0(a0) # load char pointed by a0 (*a0)
    beqz t5, return_success # if character is '\0' return sucess
    add t6, t5, a1 # add offset (key) to the char value
    ble t6, t2, add_encrypted_char_to_output_string # if (char value + key <= 'Z') then it didn't overflow else, it overflowed

handle_alphabet_overflow:
    sub t5, t6, t2 # overflowed by t5 (e.g if t6 = 'Z' + 2, means that overflowed by 2 (('Z' + 2) - 'Z'))
    addi t5, t5, -1 # subtract 1 to the overflow amount to lineup correctly after adding to 'A'
    add t6, t1, t5 # add the overflow amount to 'A'

add_encrypted_char_to_output_string:
    sb t6, 0(a2) # store the offsetted char

    addi a0, a0, 1 # cycle to next char
    addi a2, a2, 1 # after adding transformed char to out, increment out pointer

    addi t3, t3, 1 # increment string output size

    j loop_string

return_success:
    sb zero, 0(a2) # add null terminator to end of output string
    sub a2, a2, t3 # return output pointer to its original state

    li a0, 1 # return 1
    ret

return_error:
    sb zero, 0(a2) # add null terminator to set an empty string
    # no need to return pointer to its original state because it never moved in the first place,
    # and becuase we already verified that it's an invalid string we will just add a null terminator and move on
    # gg

    mv a0, zero # return 0
    ret
