.section .text
.global median

# ============================================
# Função: median
# Parâmetros: a0 = vec, a1 = length, a2 = me
# Retorno: a0 = 1 (sucesso) ou 0 (falha)
# ============================================
median:
    # Prólogo
    addi sp, sp, -32
    sw ra, 28(sp)
    sw s0, 24(sp)
    sw s1, 20(sp)
    sw s2, 16(sp)
    sw s3, 12(sp)

    # Salvar parâmetros primeiro
    mv s0, a0             # s0 = vec
    mv s1, a1             # s1 = length
    mv s2, a2             # s2 = me (ponteiro para resultado)

    # Verificar se length <= 0
    blez a1, end_failure

    # Chamar sort_array(vec, length, 1)
    mv a0, s0             # restaurar a0 = vec
    mv a1, s1             # restaurar a1 = length
    li a2, 1              # order = 1 (ascending)
    jal ra, sort_array

    # Verificar se sort_array teve sucesso
    beqz a0, end_failure

    # Calcular índice da mediana: length / 2
    srli s3, s1, 1        # s3 = length >> 1 (divisão por 2)

    # Verificar se length é ímpar (bit 0 = 1)
    andi t0, s1, 1
    bnez t0, odd

    # Par: mediana = (vec[s3-1] + vec[s3]) / 2
    slli t1, s3, 2        # t1 = s3 * 4
    add t2, s0, t1        # t2 = endereço de vec[s3]
    lw t3, 0(t2)          # t3 = vec[s3]
    lw t4, -4(t2)         # t4 = vec[s3-1]
    add t3, t3, t4        # t3 = vec[s3-1] + vec[s3]
    srai t3, t3, 1        # t3 = t3 / 2 (shift aritmético)
    sw t3, 0(s2)          # *me = mediana
    j end_success

odd:
    # Ímpar: mediana = vec[s3]
    slli t1, s3, 2        # t1 = s3 * 4
    add t2, s0, t1        # t2 = endereço de vec[s3]
    lw t3, 0(t2)          # t3 = vec[s3]
    sw t3, 0(s2)          # *me = mediana

end_success:
    li a0, 1              # Retornar 1 (sucesso)
    j end_median

end_failure:
    li a0, 0              # Retornar 0 (falha)
    li t0, -1
    sw t0, 0(s2)          # *me = -1

end_median:
    # Epílogo
    lw ra, 28(sp)
    lw s0, 24(sp)
    lw s1, 20(sp)
    lw s2, 16(sp)
    lw s3, 12(sp)
    addi sp, sp, 32
    ret
