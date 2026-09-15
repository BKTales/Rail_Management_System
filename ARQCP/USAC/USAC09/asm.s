.section .text   
.global sort_array

sort_array:
    # Prólogo - salvar registos
    addi sp, sp, -32
    sw ra, 28(sp)
    sw s0, 24(sp)
    sw s1, 20(sp)
    sw s2, 16(sp)
    sw s3, 12(sp)
    sw s4, 8(sp)

    # a0 = vec (ponteiro para array)
    # a1 = length
    # a2 = order (1 = ascending, 0 = descending)

    # Verificar se length <= 0 primeiro (antes de modificar a0)
    blez a1, return_zero
    
    # Salvar argumentos
    mv s0, a0             # s0 = vec (salvar ponteiro)
    mv s2, a2             # s2 = order

    # Inicializar variáveis
    li t0, 0              # t0 = índice atual
    addi s1, a1, -1       # s1 = length - 1 (último índice)

    # Verificar ordem
    li t1, 1
    beq s2, t1, ascending_sort_loop
    j descending_sort_loop

ascending_sort_loop:
    li s3, 1000           # s3 = contador de iterações

ascending_loop_inner:
    addi s3, s3, -1       # Decrementar contador
    beqz s3, end_sort     # Se contador = 0, terminar
    beq t0, s1, end_sort  # Se índice = length-1, terminar

    # Carregar vec[t0] e vec[t0+1]
    slli t2, t0, 2        # t2 = t0 * 4 (offset em bytes)
    add t3, s0, t2        # t3 = endereço de vec[t0]
    lw t4, 0(t3)          # t4 = vec[t0]
    lw t5, 4(t3)          # t5 = vec[t0+1]

    addi t0, t0, 1        # Incrementar índice

    # Comparar: se vec[t0+1] >= vec[t0], continuar (já está ordenado)
    bge t5, t4, ascending_loop_inner

    # Trocar elementos (t5 < t4, então trocar)
    sw t4, 4(t3)          # vec[t0+1] = t4 (antigo vec[t0])
    sw t5, 0(t3)          # vec[t0] = t5 (antigo vec[t0+1])

    # Reiniciar índice
    li t0, 0
    j ascending_loop_inner

descending_sort_loop:
    li s4, 1000           # s4 = contador de iterações

descending_loop_inner:
    addi s4, s4, -1       # Decrementar contador
    beqz s4, end_sort     # Se contador = 0, terminar
    beq t0, s1, end_sort  # Se índice = length-1, terminar

    # Carregar vec[t0] e vec[t0+1]
    slli t2, t0, 2        # t2 = t0 * 4 (offset em bytes)
    add t3, s0, t2        # t3 = endereço de vec[t0]
    lw t4, 0(t3)          # t4 = vec[t0]
    lw t5, 4(t3)          # t5 = vec[t0+1]

    addi t0, t0, 1        # Incrementar índice

    # Comparar: se vec[t0+1] <= vec[t0], continuar (já está ordenado)
    ble t5, t4, descending_loop_inner

    # Trocar elementos (t5 > t4, então trocar)
    sw t4, 4(t3)          # vec[t0+1] = t4 (antigo vec[t0])
    sw t5, 0(t3)          # vec[t0] = t5 (antigo vec[t0+1])

    # Reiniciar índice
    li t0, 0
    j descending_loop_inner

end_sort:
    li a0, 1              # Retornar 1 (sucesso)
    j end0_sort

return_zero:
    li a0, 0              # Retornar 0 (falha - length <= 0)

end0_sort:
    # Epílogo - restaurar registos
    lw ra, 28(sp)
    lw s0, 24(sp)
    lw s1, 20(sp)
    lw s2, 16(sp)
    lw s3, 12(sp)
    lw s4, 8(sp)
    addi sp, sp, 32

    ret
