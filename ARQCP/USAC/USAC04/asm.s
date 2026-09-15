.section .data 
    .global str1, str2, str3, str4, str5, array 
    str1: .asciz "YE" 
    str2: .asciz "RE" 
    str3: .asciz "GE" 
    str4: .asciz "RB" 
    str5: .asciz "GTH" 
    array: .word str1,str2,str3,str4,str5 
.section .bss 
    .comm buffer, 16 # global array of 16 bytes 
.section .text
    .global format_command

# format_command(a0 = pointer to input_string, a1 = number, a2 = pointer to output comand)
# Returns:
#   a0 = 1 if command is valid, 0 otherwise
#   a2 = pointer to output_buffer if valid

format_command:
    beq a0, zero, invalid_pointer   # input_string NULL
    beq a2, zero, invalid_pointer   # output buffer NULL

    # Prologue
    addi sp, sp, -32
    sw ra, 28(sp)
    sw s0, 24(sp)
    sw s1, 20(sp)
    sw s2, 16(sp)
    sw s3, 12(sp)
    sw s4, 8(sp)

    mv s0, a0          # save input pointer
    la s1, buffer      # buffer for trimmed+uppercased command
    la s2, array       # pointer to command array
    li s3, 5           # number of commands
    mv s4, a1
    mv a4, a2          # saves pointer to array[0]

    
    call trim

    
    mv a0, s1
    call initialize_auxiliar_variables
    li a3, 0

loop_array:
    bge a3, s3, invalid_command   # reached end of array

    lw  a5, 0(s2)                 # t1 = array[i]

    
    mv  a0, s1
    mv  a1, a5
    call string_compare

    bgtz a0, verify_command        # match found

    # next entry
    addi a3, a3, 1
    addi s2, s2, 4
    j loop_array


verify_command:
    mv a1, s4                    # a1 = number argument
    mv a0, a5                    # a0 = command string pointer

    call comand_construction

    beqz a0, invalid_command       # construction returned 0 → invalid

    # valid command
    mv a2, a4
    li a0, 1
    j  end_format


invalid_pointer:
    li a0, 0      # retorno = 0
    ret           # termina imediatamente, não mexe em a2
    
# clear output buffer
invalid_command:
    mv a2, a4               
    sb zero, 0(a2)               # output buffer = \0
    li a0, 0

# Epilogue
end_format:
    lw s4, 8(sp)
    lw s3, 12(sp)
    lw s2, 16(sp)
    lw s1, 20(sp)
    lw s0, 24(sp)
    lw ra, 28(sp)
    addi sp, sp, 32
    ret

