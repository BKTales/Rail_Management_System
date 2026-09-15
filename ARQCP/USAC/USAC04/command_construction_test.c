#include <string.h>
#include <stdint.h>
#include "../../unity.h"

// Protótipo da função assembly
extern void comand_construction(char *a0, int a1, char *a2);

// Buffer global para capturar saída
char output_buffer[1024];

void setUp(void) {}
void tearDown(void) {}

// ----------------- Funções auxiliares -----------------

// Testa números válidos, espera que o buffer seja preenchido corretamente
void test_comand_valid(const char *input, int number, const char *expected_output) {
    memset(output_buffer, 0xAA, sizeof(output_buffer));

    comand_construction((char *)input, number, output_buffer);

    TEST_ASSERT_EQUAL_STRING_MESSAGE(expected_output, output_buffer, "Conteúdo do output_buffer incorreto");
}

// Testa números inválidos, espera que o buffer permaneça não modificado
void test_comand_invalid(const char *input, int number) {
    memset(output_buffer, 0xAA, sizeof(output_buffer));

    comand_construction((char *)input, number, output_buffer);

    if (input[0] == 'G' && input[1] == 'T' && input[2] == 'H') {
        // GTH ignora o número
        TEST_ASSERT_EQUAL_STRING("GTH", output_buffer);
    } else {
        // Comandos normais: só a primeira letra foi copiada antes da verificação
        TEST_ASSERT_EQUAL_CHAR(input[0], output_buffer[0]);
    }
}


// ----------------- TESTES -----------------

void test_copy_string_normal(void) {
    test_comand_valid("YE", 1, "YE,01");
}

void test_copy_string_upper_lower(void) {
    test_comand_valid("re", 2, "re,02");
}

void test_comand_gth(void) {
    test_comand_valid("GTH", 99, "GTH"); // Caso especial: GTH ignora número
}

void test_comand_gth_trim(void) {
    test_comand_valid("GTH", 0, "GTH"); // GTH não usa número
}

void test_comand_two_digits(void) {
    test_comand_valid("YE", 42, "YE,42");
}

void test_comand_one_digit(void) {
    test_comand_valid("RE", 7, "RE,07");
}

void test_comand_invalid_number_low(void) {
    test_comand_invalid("YE", -1);
}

void test_comand_invalid_number_high(void) {
    test_comand_invalid("RE", 123);
}

// ----------------- MAIN -----------------

int main(void) {
    UNITY_BEGIN();

    RUN_TEST(test_copy_string_normal);
    RUN_TEST(test_copy_string_upper_lower);
    RUN_TEST(test_comand_gth);
    RUN_TEST(test_comand_gth_trim);
    RUN_TEST(test_comand_two_digits);
    RUN_TEST(test_comand_one_digit);
    RUN_TEST(test_comand_invalid_number_low);
    RUN_TEST(test_comand_invalid_number_high);

    return UNITY_END();
}
