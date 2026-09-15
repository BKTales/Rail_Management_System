#include <string.h>
#include "../../unity.h"

// Protótipo da função assembly
extern int string_compare(const char *a0, const char *a1);

void setUp(void) {}
void tearDown(void) {}

// ----------------- TESTES -----------------

void test_strings_equal(void) {
    TEST_ASSERT_EQUAL_INT(1, string_compare("HELLO", "HELLO"));
}

void test_strings_equal_empty(void) {
    TEST_ASSERT_EQUAL_INT(1, string_compare("", ""));
}

void test_strings_not_equal(void) {
    TEST_ASSERT_EQUAL_INT(0, string_compare("HELLO", "WORLD"));
}

void test_strings_prefix(void) {
    TEST_ASSERT_EQUAL_INT(0, string_compare("HELLO", "HELL"));
}

void test_strings_case_difference(void) {
    TEST_ASSERT_EQUAL_INT(0, string_compare("HELLO", "hello"));
}

// ----------------- MAIN -----------------

int main(void) {
    UNITY_BEGIN();

    RUN_TEST(test_strings_equal);
    RUN_TEST(test_strings_equal_empty);
    RUN_TEST(test_strings_not_equal);
    RUN_TEST(test_strings_prefix);
    RUN_TEST(test_strings_case_difference);

    return UNITY_END();
}
