CC = riscv32-linux-gcc
CFLAGS = -Wall -Wextra -fanalyzer -g
ARCHFLAGS = -march=rv32imd -mabi=ilp32d
LIBRV32 = ${HOME}/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot

# Assembly files usados por todos os testes
ASM_FILES = asm.s trim.s to_upper_case.s string_compare.s comand_construction.s callfunc.S
ASM_OBJS  = $(ASM_FILES:.s=.o)
ASM_OBJS  := $(ASM_OBJS:.S=.o)

# Unity
UNITY_OBJ = ../../unity.o

# Alvo principal
TARGET = main.elf

# Default target
all: $(TARGET)

# Compilar ELF principal
$(TARGET): main.c $(ASM_OBJS) $(UNITY_OBJ)
	$(CC) $(CFLAGS) $(ARCHFLAGS) $^ -o $@

# Compilar C
%.o: %.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -I. -c $< -o $@

# Compilar Assembly
%.o: %.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c $< -o $@

%.o: %.S
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c $< -o $@

# Executar
run: $(TARGET)
	qemu-riscv32 -L $(LIBRV32) ./$(TARGET)

# Debug com GDB
debug: $(TARGET)
	@echo "On another terminal, run:"
	@echo "riscv32-linux-gdb-tui ./$(TARGET)"
	qemu-riscv32 -L $(LIBRV32) -g 1237 ./$(TARGET)

# Limpeza
clean:
	rm -f *.o *.elf

full-clean: clean
	rm -f ../../unity.o
