.section .text
    .global decrypt_data

decrypt_data:
    # Function arguments:
    # a0 = char* in (input string - only uppercase A-Z)
    # a1 = int key (range [1, 26])
    # a2 = char* out (output string)

    # Validate key is in range [1, 26]
    li t1, 1
    li t2, 26
    blt a1, t1, invalid_input  # If key < 1, fail
    bgt a1, t2, invalid_input  # If key > 26, fail

    # Check if input string is empty (empty string is valid)
    lb t0, 0(a0)
    beqz t0, empty_string_success

process_loop:
    # Load current character from input
    lb t0, 0(a0)
    beqz t0, success_done      # End of string reached
    
    # Validate character is uppercase A-Z
    li t3, 'A'
    li t4, 'Z'
    blt t0, t3, invalid_input  # Character < 'A' - invalid
    bgt t0, t4, invalid_input  # Character > 'Z' - invalid
    
    # Apply Caesar cipher decryption: subtract key
    sub t0, t0, a1
    
    # Handle wrap-around if result went below 'A'
    bge t0, t3, store_char    # If still >= 'A', no wrap needed
    
wrap_around:
    # Character wrapped below 'A', add 26 to wrap to end of alphabet
    addi t0, t0, 26
    
store_char:
    # Store decrypted character in output
    sb t0, 0(a2)
    
    # Move to next character
    addi a0, a0, 1
    addi a2, a2, 1
    j process_loop

empty_string_success:
    # Empty input string with valid key - output empty string
    sb zero, 0(a2)
    li a0, 1
    ret

success_done:
    # Successfully processed all characters
    sb zero, 0(a2)            # Null terminate output string
    li a0, 1                  # Return success
    ret

invalid_input:
    # Invalid key or non-uppercase character found
    sb zero, 0(a2)            # Set output as empty string
    li a0, 0                  # Return failure
    ret