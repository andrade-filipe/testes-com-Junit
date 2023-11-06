package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.algaworks.junit.utilidade.SaudacaoUtil.saudar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testes em Saudacao")
class SaudacaoUtilTest {

    @Test
    @DisplayName("Deve saudar Bom dia")
    public void saudarDia() {
        String saudacao = saudar(9);
        String saudacaoCorreta = "Bom dia";

        assertThat(saudacao)
                .as("Validando se a saudacao é %s", saudacaoCorreta)
                .withFailMessage("Erro: Saudação incorreta, Resultado: %s", saudacao)
                .isEqualTo(saudacaoCorreta);
    }

    @Test
    @DisplayName("Deve saudar Boa Tarde")
    public void saudarTarde() {
        String saudacao = saudar(14);
        assertEquals("Boa tarde", saudacao, "Saudação Incorreta");
    }

    @Test
    void saudarNoite() {
        String saudacao = saudar(19);
        assertEquals("Boa noite", saudacao, "Saudação Incorreta");
    }

    @Test
    public void exceptionCase() {
        assertThatThrownBy(() -> saudar(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hora inválida");
    }

    @Test
    public void shouldNotThrowException() {
        assertDoesNotThrow(() -> saudar(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8, 9, 10, 11})
    @DisplayName("Teste Parametizado de Bom dia")
    void testeParametizado(int hora) {
        String saudacao = saudar(9);
        assertEquals("Bom dia", saudacao, "Saudação Incorreta");
    }
}