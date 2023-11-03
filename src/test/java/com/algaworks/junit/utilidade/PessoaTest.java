package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PessoaTest {

    @Test
    void groupAssertion() {
        Pessoa pessoa = new Pessoa("Alex", "Silva");

        assertAll("Assertions of Pessoa",
                () -> assertEquals("Alex", pessoa.getNome()),
                () -> assertEquals("Silva", pessoa.getSobrenome()));
    }

}