.section .text
    .global move_n_to_array

move_n_to_array:

    # NOTA: SÓ PUDE USAR OS REGISTOS TEMPORÁRIOS t5 e t6 PORQUE OS OUTROS TODOS (t0-t4) SÃO USADOS NA FUNÇÃO DEQUEUE_VALUE, E IRIA GERAR CONFLITO SE AS DUAS FUNÇÕES USASSEM OS MESMOS TEMPORÁRIOS
    # PARA RESOLVER ISSO SIMPLESMENTE CRIEI VARIÁVEIS LOCAIS NA STACK

    # a0 - int* buffer (circular buffer pointer)
    # a1 - int length (constant length of buffer)
    # a2 - int* nelem (points to the number of elements currently in the buffer)
    # a3 - int* tail (points to the index of the oldest elemtn)
    # a4 - int* head (points to the index of the insertion of the next element)
    # a5 - int n (number of oldest members to move to output array)
    # a6 - int* array (points to the output array)

    # prologue
    addi sp, sp, -32 # allocate space in the stack (create stack frame)
    sw ra, 28(sp) # copy/save return adress because we will be calling another function inside this one


    # --- LOCAL VARIABLES ---
    sw a0, 0(sp) # local variable - copy/save buffer pointer

    sw zero, 4(sp) # local variable - i for the loop

    sw a5, 8(sp) # local variable - copy number of elements to move to output array, we need to do this because the function that we call inside this one changes the a5 register

    sw zero, 12(sp) # local variable - where we will store the dequeued value from the other function
    # --- END OF LOCAL VARIABLES ---


    # --- FIRST CHECKS ---
    lw t5, 0(a2) # t5 - number of elements currently in the buffer

    beqz a5, return_fail # ERROR: you can't remove 0 elements
    bgt a5, t5, return_fail # ERROR: if we want to move more elements than we currently have on the circular buffer, fail
    # --- END OF FIRST CHECKS


    lw t6, 8(sp) # load number of elements to move to output array from stack

move_loop:
    lw t5, 4(sp) # load i from stack
    bge t5, t6, return_success 

    addi a5, sp, 12 # points to the local variable that stores the pointer so that the dequeue_value function can set this local variable to the element that was dequeued

    call dequeue_value

    beqz a0, return_fail # check if dequeue_value function returned success or not

    lw a0, 0(sp) # restore original buffer pointer
    lw t5, 12(sp) # load the value that was dequeued
    sw t5, 0(a6) # store the dequeued value in the output array

    addi a6, a6, 4 # increment output array pointer

    lw t5, 4(sp) # load i from stack to increment
    addi t5, t5, 1 # increment i
    sw t5, 4(sp) # save i to stack

    j move_loop

return_success:
    li a0, 1
    j epilogue

return_fail:
    mv a0, zero

epilogue:
    lw t6, 8(sp) # number of elements moved to output array / output array size
    slli t5, t6, 2 # output array size * 4
    sub a6, a6, t5 # return output pointer to its original position

    # epilogue
    lw ra, 28(sp) # restore original return adress to the "previous" function
    addi sp, sp, 32 # delete stack frame

    ret
