/*
 * Ficheiro: tests_concatenate.c
 * Objetivo: Testar unitariamente a função Assembly 'concatenate'
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>


typedef struct {
    char padding[4]; 
    char *name;      // Começa no byte 4
} User;

typedef struct {
    char padding[4]; 
    User *user;      // Começa no byte 4
} Program;

// Variável global para controlar o tempo retornado nos testes
char *MOCK_TIME = "00:00";

// O assembly chama 'get_time'. Nós interceptamos aqui.
char* get_time() {
    return MOCK_TIME;
}
// O assembly chama 'ft_strlen'. Implementamos uma simples aqui.
int ft_strlen(char *str) {
    int len = 0;
    while (str[len] != '\0') {
        len++;
    }
    return len;
}

extern char* concatenate(Program* p, char* msg);

void test_basic_concatenation() {
    printf("[TEST 1] Basic concat... ");
    
    MOCK_TIME = "10:30";
    
    User u; 
    u.name = "Joao";
    Program p; 
    p.user = &u;
    
    char *msg = "Bem-vindo";
    
    char *res = concatenate(&p, msg);
    
    char *expected = "Joao: Bem-vindo: 10:30\n";
    
    assert(strcmp(res, expected) == 0);
    free(res); 
    printf("OK\n");
}

void test_empty_strings() {
    printf("[TEST 2] Empty String... ");
    
    MOCK_TIME = "00:00";
    
    User u; 
    u.name = "";
    Program p; 
    p.user = &u;
    
    char *msg = "";
    
    char *res = concatenate(&p, msg);
    
    char *expected = ": : 00:00\n";
    
    assert(strcmp(res, expected) == 0);
    free(res);
    printf("OK\n");
}

void test_long_strings() {
    printf("[TEST 3] Long String... ");
    
    MOCK_TIME = "12:00";
    
    char *long_name = "UmNomeMuitoExtensoParaTestarALimiteDeMemoria";
    char *long_msg = "Uma mensagem igualmente longa para garantir que nao ha buffer overflow";
    
    User u; 
    u.name = long_name;
    Program p; 
    p.user = &u;
    
    char *res = concatenate(&p, long_msg);
    
    char expected[512];
    sprintf(expected, "%s: %s: %s\n", long_name, long_msg, MOCK_TIME);
    
    assert(strcmp(res, expected) == 0);
    free(res);
    printf("OK\n");
}

void test_special_chars() {
    printf("[TEST 4] Special chars... ");
    
    MOCK_TIME = "23:59";
    
    User u; 
    u.name = "Ana Maria"; // Espaço no nome
    Program p; 
    p.user = &u;
    
    char *msg = "Erro #404: File_Not_Found!"; // Simbolos
    
    char *res = concatenate(&p, msg);
    
    char *expected = "Ana Maria: Erro #404: File_Not_Found!: 23:59\n";
    
    assert(strcmp(res, expected) == 0);
    free(res);
    printf("OK\n");
}


int main() {
    printf("--- Testing (Concatenate) ---\n");
    
    test_basic_concatenation();
    test_empty_strings();
    test_long_strings();
    test_special_chars();
    test_time_updates();
    
    return 0;
}