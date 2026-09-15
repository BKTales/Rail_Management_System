# --------------------------------------------------
#  RISC-V Unit Test Makefile (local, no REPO needed)
# --------------------------------------------------

CC = riscv32-linux-gcc
CFLAGS = -Wall -Wextra -fanalyzer -g
ARCHFLAGS = -march=rv32imd -mabi=ilp32d
LIBRV32 = ${HOME}/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot

# Automatically detect source files in the current directory
CURRENT_DIR := $(strip $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST)))))
EX_TEST := $(CURRENT_DIR)

FILES := $(shell find $(EX_TEST) -type f \( -iname "*.c" -o -iname "*.s" \))

ifeq ($(strip $(FILES)),)
  $(error Could not find source files in $(EX_TEST))
endif

# Detect main file (one that contains 'int main')
MAIN_FILE := $(shell grep -l -E 'int[ \t\n]+main' $(FILES))
REAL_FILES := $(filter-out $(MAIN_FILE),$(FILES))

ifeq ($(strip $(REAL_FILES)),)
  $(error Could not find non-main source files in $(EX_TEST))
endif

REAL_OBJS := $(patsubst %.c,%.o,$(patsubst %.s,%.o,$(notdir $(REAL_FILES))))

# --------------------------------------------------
#  Build Targets
# --------------------------------------------------

prog.elf: main.o callfunc.o ../../unity.o $(REAL_OBJS)
	$(CC) $(CFLAGS) $(ARCHFLAGS) $^ -o $@

main.o: main.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -I ../.. -c $< -o $@

callfunc.o: callfunc.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -I ../.. -c $< -o $@

../../unity.o: ../../unity.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -I ../.. -c $< -o $@

$(REAL_OBJS): $(REAL_FILES)
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c $(REAL_FILES)

# --------------------------------------------------
#  Run and Cleanup
# --------------------------------------------------

run: prog.elf
	qemu-riscv32 -L $(LIBRV32) ./prog.elf

debug: prog.elf
	@echo "Starting QEMU in debug mode and attaching GDB..."
	qemu-riscv32 -L $(LIBRV32) -g 1234 ./prog.elf & \
	sleep 1; \
		riscv32-linux-gdb prog.elf \
		-ex "target remote :1234" \
		-ex "layout src" \
		-ex "set disassemble-next-line on" \

clean:
	rm -f prog.elf *.o

full-clean: clean
	rm -f ../../unity.o
