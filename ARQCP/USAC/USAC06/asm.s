.section .text
    .global  dequeue_value
    
#-------------------------------------------------------Loop Logic-----------------------------------------------------------------#
/**
 * Removes the oldest element from the circular buffer
 * 
 * RISC-V register mapping for inputs:
 * a0 - buffer: pointer to the buffer array
 * a1 - length: maximum capacity of the buffer
 * a2 - nelem: pointer to current number of elements in buffer
 * a3 - tail: pointer to tail index (removal position)
 * a4 - head: pointer to head index (insertion position)  
 * a5 - value: pointer to store the dequeued value
 */
#Loads number of elemens , if equals to 0, jumps to no_success, if not it load index of tail
dequeue_value:
    lw t0, 0(a2)
    blez t0, no_success
    lw t1, 0(a3)

#Accesses buffer[index], loads the integer, stores it in *value (a5), and replaces the buffer slot with zero.
remove_element:
    slli t2, t1, 2      #(tail * 4)
    add t3, a0, t2      #(&buffer[tail])
    lw t4, 0(t3)        #(buffer[tail])
    sw t4, 0(a5)

    
#Increments index of tail, if > length, returns to 0
change_tail:
    addi t1, t1, 1
    rem t1, t1, a1
    sw t1, 0(a3)

#Decrements number of elements
decrement_nelem:
    addi t0, t0, -1
    sw t0, 0(a2)

    
success:
    li a0, 1               
    ret


no_success:
    mv a0, zero
    ret

