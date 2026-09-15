.section .rodata
str_sep: .string ": "
str_nl:  .string "\n"

.section .text
.global concatenate
.extern malloc
.extern ft_strlen
.extern get_time

# ==============================================================================
# Função: concatenate
# Objetivo: Juntar (Nome + ": " + Msg + ": " + Time + "\n") numa nova string.
# Entrada:
#   a0 = struct program*
#   a1 = char* message
# Retorno:
#   a0 = char* (Endereço da nova string completa alocada no Heap)
# ==============================================================================
concatenate:
    # 1. Prólogo: Salvar registadores
    addi sp, sp, -32
    sw   ra, 28(sp)
    sw   s0, 24(sp)       # Ponteiro: NOME
    sw   s1, 20(sp)       # Ponteiro: MENSAGEM
    sw   s2, 16(sp)       # Ponteiro: TEMPO
    sw   s3, 12(sp)       # Ponteiro: INÍCIO DA NOVA STRING (Resultado)
    sw   s4, 8(sp)        # Ponteiro: CURSOR (Onde estamos a escrever)

    # --- FASE 1: REUNIR OS INGREDIENTES (Ponteiros) ---
    
    # Guardar Mensagem em s1
    mv   s1, a1

    # Obter Nome (program->user->name) e guardar em s0
    lw   t0, 4(a0)        # t0 = program->user
    lw   s0, 4(t0)        # s0 = user->name 

    # Obter Tempo (get_time()) e guardar em s2
    call get_time
    mv   s2, a0

    # --- FASE 2: CALCULAR O TAMANHO TOTAL ---
    # Usaremos s4 temporariamente para acumular o tamanho total
    li   s4, 0

    # Tamanho do Nome
    mv   a0, s0
    call ft_strlen
    add  s4, s4, a0

    # Tamanho da Mensagem
    mv   a0, s1
    call ft_strlen
    add  s4, s4, a0

    # Tamanho do Tempo
    mv   a0, s2
    call ft_strlen
    add  s4, s4, a0

    # Tamanho dos Separadores
    # ": " (2) + ": " (2) + "\n" (1) + "\0" (1) = 6 bytes
    addi s4, s4, 6

    # --- FASE 3: ALOCAR MEMÓRIA ---
    mv   a0, s4           # Tamanho total
    call malloc           # Retorna endereço em a0
    
    mv   s3, a0           # s3 = RESULTADO (Guardamos para o final)
    mv   s4, a0           # s4 = CURSOR (Vamos mexer neste)

    # --- FASE 4: COPIAR TUDO  ---

    # 1. Copiar Nome
    mv   a1, s0
    jal  ra, append_str

    # 2. Copiar ": "
    la   a1, str_sep
    jal  ra, append_str

    # 3. Copiar Mensagem
    mv   a1, s1
    jal  ra, append_str

    # 4. Copiar ": "
    la   a1, str_sep
    jal  ra, append_str

    # 5. Copiar Tempo
    mv   a1, s2
    jal  ra, append_str

    # 6. Copiar "\n"
    la   a1, str_nl
    jal  ra, append_str

    # --- FINALIZAÇÃO ---
    mv   a0, s3           # Coloca o endereço do início em a0 para retornar
    
    # Restaurar registadores
    lw   ra, 28(sp)
    lw   s0, 24(sp)
    lw   s1, 20(sp)
    lw   s2, 16(sp)
    lw   s3, 12(sp)
    lw   s4, 8(sp)
    addi sp, sp, 32
    ret

# ==============================================================================
# Sub-rotina Auxiliar: append_str
# Copia string de (a1) para o Cursor (s4)
# Avança o s4 automaticamente
# ==============================================================================
append_str:
    lb   t0, 0(a1)        # Lê byte da origem
    beqz t0, end_append   # Se for \0, paramos
    
    sb   t0, 0(s4)        # Escreve byte no cursor
    
    addi a1, a1, 1        # Avança origem
    addi s4, s4, 1        # Avança cursor
    j    append_str

end_append:
    sb   zero, 0(s4)      # Garante terminador nulo a cada passo (segurança)
    ret
    