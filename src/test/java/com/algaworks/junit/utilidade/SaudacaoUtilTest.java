package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes em Saudacao")
class SaudacaoUtilTest {

    @Test
    @DisplayName("Deve saudar Bom dia")
    public void saudarDia() {
        String saudacao = SaudacaoUtil.saudar(9);
        assertEquals("Bom dia", saudacao, "Saudação Incorreta");
    }

    @Test
    @DisplayName("Deve saudar Boa Tarde")
    public void saudarTarde() {
        String saudacao = SaudacaoUtil.saudar(14);
        assertEquals("Boa tarde", saudacao, "Saudação Incorreta");
    }

    @Test
    void saudarNoite() {
        String saudacao = SaudacaoUtil.saudar(19);
        assertEquals("Boa noite", saudacao, "Saudação Incorreta");
    }

    @Test
    public void exceptionCase() {
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class,
                        () -> SaudacaoUtil.saudar(-10));
        assertEquals("Hora inválida", illegalArgumentException.getMessage());
    }

    @Test
    public void shouldNotThrowException() {
        assertDoesNotThrow(() -> SaudacaoUtil.saudar(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8, 9, 10, 11})
    @DisplayName("Teste Parametizado de Bom dia")
    void testeParametizado(int hora) {
        String saudacao = SaudacaoUtil.saudar(9);
        assertEquals("Bom dia", saudacao, "Saudação Incorreta");
    }
}