.section .text
    .global string_compare

# Compares two null-terminated strings
# a0 = pointer to first string
# a1 = pointer to second string
# Returns:
#   a0 = 1 if strings are equal
#   a0 = 0 if strings are different
string_compare:
    lbu t0, 0(a0)            # load byte from first string 
    lbu t1, 0(a1)            # load byte from second string 
    bne t0, t1, not_equal    
    beqz t0, equal           
    addi a0, a0, 1           
    addi a1, a1, 1           
    j string_compare          

equal:
    li a0, 1                 
    ret                       

not_equal:
    li a0, 0                 
    ret                       
