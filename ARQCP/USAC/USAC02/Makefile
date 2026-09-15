CC=riscv32-linux-gcc
CFLAGS=-Wall -Wextra -fanalyzer -g
ARCHFLAGS=-march=rv32imd -mabi=ilp32d
LIBRV32=${HOME}/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot

# Source files - specify them explicitly instead of using find
SRC_C := $(wildcard *.c)
SRC_ASM := $(wildcard *.s)

# Remove main.c from the source list
OTHER_C_SRC := $(filter-out main.c,$(SRC_C))
OTHER_ASM_SRC := $(filter-out callfunc.s,$(SRC_ASM))

# Object files
OTHER_C_OBJ := $(OTHER_C_SRC:.c=.o)
OTHER_ASM_OBJ := $(OTHER_ASM_SRC:.s=.o)
OTHER_OBJ := $(OTHER_C_OBJ) $(OTHER_ASM_OBJ)

prog.elf: main.o callfunc.o ../../unity.o $(OTHER_OBJ)
	$(CC) $(CFLAGS) $(ARCHFLAGS) main.o callfunc.o ../../unity.o $(OTHER_OBJ) -o prog.elf

main.o: main.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -I ../.. -c $< -o $@

callfunc.o: callfunc.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -I ../.. -c $< -o $@

../../unity.o: ../../unity.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -I ../.. -c $< -o $@

# Pattern rules for other source files
%.o: %.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c $< -o $@

%.o: %.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c $< -o $@

run: prog.elf
	qemu-riscv32 -L $(LIBRV32) ./prog.elf

debug: prog.elf
	@echo "Starting QEMU in debug mode and attaching GDB..."
	qemu-riscv32 -L $(LIBRV32) -g 1234 ./prog.elf & \
	sleep 1; \
		riscv32-linux-gdb prog.elf \
		-ex "target remote :1234" \
		-ex "layout split" \
		-ex "set disassemble-next-line on" \

clean:
	rm -f prog.elf *.o

full-clean:
	rm -f prog.elf *.o ../../unity.o

.PHONY: run clean full-clean