.section .text
    .global enqueue_value

# int enqueue_value(int* buffer, int length, int* nelem, int* tail, int* head, int value)
# a0 = buffer, a1 = length, a2 = nelem, a3 = tail, a4 = head, a5 = value
# Returns: 1 if buffer is full after insertion, 0 otherwise

enqueue_value:
    # Save registers that might be modified
    addi sp, sp, -12
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)

    # Load current values
    lw t0, 0(a2)        # t0 = *nelem (number of elements)
    lw t1, 0(a3)        # t1 = *tail
    lw t2, 0(a4)        # t2 = *head

check_full:
    # Check if buffer is full
    bne t0, a1, not_full
    
    # Buffer is full - remove oldest element (at tail)
    # Calculate tail index in buffer
    slli t3, t1, 2      # t3 = tail * 4 (sizeof(int))
    add t3, a0, t3      # t3 = &buffer[tail]
    
    # Move tail forward (circular)
    addi t1, t1, 1      # tail++
    blt t1, a1, tail_updated
    mv t1, zero         # wrap around if tail >= length
tail_updated:
    sw t1, 0(a3)        # update *tail
    
    # Decrement nelem since we removed one
    addi t0, t0, -1
    sw t0, 0(a2)

not_full:
    # Insert new value at head position
    slli t3, t2, 2      # t3 = head * 4 (sizeof(int))
    add t3, a0, t3      # t3 = &buffer[head]
    sw a5, 0(t3)        # buffer[head] = value
    
    # Move head forward (circular)
    addi t2, t2, 1      # head++
    blt t2, a1, head_updated
    mv t2, zero         # wrap around if head >= length
head_updated:
    sw t2, 0(a4)        # update *head
    
    # Increment nelem
    addi t0, t0, 1
    sw t0, 0(a2)        # update *nelem
    
    # Check if buffer is full after insertion for return value
    li a0, 0            # assume not full
    beq t0, a1, is_full # if nelem == length, buffer is full
    j return

is_full:
    li a0, 1            # return 1 if full

return:
    # Restore saved registers
    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    addi sp, sp, 12
    ret
