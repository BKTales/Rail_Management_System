CC = riscv32-linux-gcc
CFLAGS = -Wall -Wextra -fanalyzer -g
ARCHFLAGS = -march=rv32imd -mabi=ilp32d

INCLUDES = -I. \
           -Iget_next_line \
           -I../../USAC/USAC01 \
           -I../../USAC/USAC02 \
           -I../../USAC/USAC03 \
           -I../../USAC/USAC04 \
           -I../../USAC/USAC05 \
           -I../../USAC/USAC07 \
           -I../../USAC/USAC08 \
           -I../../USAC/USAC09 \
           -I../../USAC/USARQCP

LIBRV32 = ${HOME}/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot


SRCS_C = main.c \
         writer.c \
         init.c \
         utils.c \
         arduino_func.c \
         arduino_comm.c \
         get_next_line/get_next_line.c \
         get_next_line/get_next_line_utils.c

SRCS_S := $(shell find ../../USAC -type f \( -name "*.s" -o -name "*.S" \) | grep -v -E "/USAC08/")

SRCS_S += ./concatenate.s

OBJS = $(SRCS_C:.c=.o) $(SRCS_S:.s=.o)

TARGET = train_control

TEST_SRC = tests_concatenate.c
TEST_TARGET = run_tests_concat


.PHONY: all clean run debug test_concat

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) $(ARCHFLAGS) $(OBJS) -o $@
	@echo "$(TARGET) - Sucess"

%.o: %.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) $(INCLUDES) -c $< -o $@

# Compilar ficheiros Assembly (.s)
%.o: %.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) $(INCLUDES) -c $< -o $@

# ---------------------------------------------------------
# ### TESTES UNITARIOS ###
# ---------------------------------------------------------

test_concat: $(TEST_SRC) concatenate.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) $(INCLUDES) $(TEST_SRC) concatenate.s -o $(TEST_TARGET)
	@echo "--- A Executar Testes no QEMU ---"
	qemu-riscv32 -L $(LIBRV32) ./$(TEST_TARGET)
	@echo "---------------------------------"

# ---------------------------------------------------------

clean:
	rm -f $(TARGET) $(TEST_TARGET) *.o get_next_line/*.o
	rm -f *.o

run: $(TARGET)
	qemu-riscv32 -L $(LIBRV32) ./$(TARGET) start_files/file_sample.txt