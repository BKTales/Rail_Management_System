#include <string.h>
#include "../../unity.h"

// Protótipo da função assembly
extern void trim(char *input);
extern char buffer[];

void setUp(void) {}
void tearDown(void) {}

// ----------------- TESTES -----------------

void test_trim_no_spaces(void) {
    trim("HELLO");
    TEST_ASSERT_EQUAL_STRING("HELLO", buffer);
}

void test_trim_left_spaces(void) {
    trim("   HELLO");
    TEST_ASSERT_EQUAL_STRING("HELLO", buffer);
}

void test_trim_right_spaces(void) {
    trim("WORLD   ");
    TEST_ASSERT_EQUAL_STRING("WORLD", buffer);
}

void test_trim_both_sides(void) {
    trim("   HELLO WORLD   ");
    TEST_ASSERT_EQUAL_STRING("HELLO", buffer); // copia até o primeiro espaço
}

void test_trim_only_spaces(void) {
    trim("     ");
    TEST_ASSERT_EQUAL_STRING("", buffer);
}

void test_trim_empty_string(void) {
    trim("");
    TEST_ASSERT_EQUAL_STRING("", buffer);
}

// ----------------- MAIN -----------------

int main(void) {
    UNITY_BEGIN();

    RUN_TEST(test_trim_no_spaces);
    RUN_TEST(test_trim_left_spaces);
    RUN_TEST(test_trim_right_spaces);
    RUN_TEST(test_trim_both_sides);
    RUN_TEST(test_trim_only_spaces);
    RUN_TEST(test_trim_empty_string);

    return UNITY_END();
}
