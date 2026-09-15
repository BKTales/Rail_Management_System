.section .text
    .global trim
    
# a0 = pointer to input string
# Writes trimmed string to global buffer
trim:
    la t0, buffer             # t0 = buffer pointer
    
    # Skip leading spaces
skip_leading:
    lbu t1, 0(a0)
    beqz t1, end_trim         # end of string
    li t2, ' '
    bne t1, t2, copy_chars    # found non-space, start copying
    addi a0, a0, 1            # skip space
    j skip_leading

copy_chars:
    lbu t1, 0(a0)
    beqz t1, end_trim         # end of string
    li t2, ' '
    beq t1, t2, end_trim      # found trailing space, stop
    sb t1, 0(t0)              # copy char to buffer
    addi t0, t0, 1
    addi a0, a0, 1
    j copy_chars

end_trim:
    sb zero, 0(t0)            # null terminate
    ret