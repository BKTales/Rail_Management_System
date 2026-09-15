#include <string.h>
#include "../../unity.h"

// Protótipo da função assembly
extern void initialize_auxiliar_variables(char *str);

void setUp(void) {}
void tearDown(void) {}

// ----------------- TESTES -----------------

void test_lowercase_to_uppercase(void) {
    char str[] = "hello";
    initialize_auxiliar_variables(str);
    TEST_ASSERT_EQUAL_STRING("HELLO", str);
}

void test_mixed_case(void) {
    char str[] = "HeLLo WoRLD";
    initialize_auxiliar_variables(str);
    TEST_ASSERT_EQUAL_STRING("HELLO WORLD", str);
}

void test_already_uppercase(void) {
    char str[] = "WORLD";
    initialize_auxiliar_variables(str);
    TEST_ASSERT_EQUAL_STRING("WORLD", str);
}

void test_numbers_and_symbols(void) {
    char str[] = "abc123!@#";
    initialize_auxiliar_variables(str);
    TEST_ASSERT_EQUAL_STRING("ABC123!@#", str);
}

void test_empty_string(void) {
    char str[] = "";
    initialize_auxiliar_variables(str);
    TEST_ASSERT_EQUAL_STRING("", str);
}

// ----------------- MAIN -----------------

int main(void) {
    UNITY_BEGIN();

    RUN_TEST(test_lowercase_to_uppercase);
    RUN_TEST(test_mixed_case);
    RUN_TEST(test_already_uppercase);
    RUN_TEST(test_numbers_and_symbols);
    RUN_TEST(test_empty_string);

    return UNITY_END();
}
