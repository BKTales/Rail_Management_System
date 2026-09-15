.section .text
    .global comand_construction

# a0 = pointer to command string
# a1 = number argument
# a2 = pointer to output_buffer
# Returns:
#   a0 = 0 if invalid number (for GTH always valid)
#   a2 points to output_buffer (written command)
comand_construction:
    lbu t1, 0(a0)         # load first character of command string
    li t2, 'G'             
    beq t1, t2, comand_construction_g 


copy_string:
    lbu t1, 0(a0)         # load character from input string
    beq t1, zero, add_comma  # if null terminator, jump to add comma
    sb t1, 0(a2)          # store character in output buffer
    addi a0, a0, 1        
    addi a2, a2, 1       
    j copy_string

add_comma:
    li t1, ','            
    sb t1, 0(a2)          # append comma to output buffer
    addi a2, a2, 1
    j process_number      

# Special command "G" -> check second character
comand_construction_g:
    lbu t1, 1(a0)         
    li t2, 'T'
    beq t1, t2, comand_construction_gth 
    j copy_string         

comand_construction_gth:
    lbu t1, 0(a0)
    sb t1, 0(a2)
    lbu t1, 1(a0)
    sb t1, 1(a2)
    lbu t1, 2(a0)
    sb t1, 2(a2)
    li t1, 0
    sb t1, 3(a2)          # \0
    ret                   


process_number:
    li t2, 99
    bgt a1, t2, invalid_number  # number > 99 invalid
    li t2, 0
    blt a1, t2, invalid_number  # number < 0 invalid
    li t2, 9
    bgt a1, t2, two_Digits_Number
    j one_Digit_Number


two_Digits_Number:
    li t2, 10
    div t3, a1, t2           # t3 = a1 / 10
    addi t3, t3, 48          # convert to ASCII
    sb t3, 0(a2)
    addi a2, a2, 1
    rem t3, a1, t2           # remainder = a1 % 10
    addi t3, t3, 48          
    sb t3, 0(a2)
    addi a2, a2, 1
    j finish_comand

# Handle numbers < 10
one_Digit_Number:
    li t3, 48
    sb t3, 0(a2)             # leading 0
    addi a2, a2, 1
    addi t3, a1, 48
    sb t3, 0(a2)
    addi a2, a2, 1
    j finish_comand

finish_comand:
    li t3, 0
    sb t3, 0(a2)             # \0
    ret                      

invalid_number:
    mv a0, zero               # return 0
    ret    