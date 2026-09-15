.section .text
    .global initialize_auxiliar_variables

# a0 = pointer to string
# Converts lowercase letters to uppercase in-place
initialize_auxiliar_variables:
    li t0, 'a'
    li t1, 'z'

to_upper_case:
    lb t2, 0(a0)             # load char
    beq t2, zero, end_upper 
    blt t2, t0, skip          # if t2 < 'a', it is not lowercase, skip conversion
    bgt t2, t1, skip          # if t2 > 'Z', it is not lowercase, skip conversion  
    addi t2, t2, -32          # convert lowercase to uppercase
    sb t2, 0(a0)

skip:
    addi a0, a0, 1
    j to_upper_case

end_upper:
    ret   