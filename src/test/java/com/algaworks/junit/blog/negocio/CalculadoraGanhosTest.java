package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.modelo.Editor;
import com.algaworks.junit.blog.modelo.Ganhos;
import com.algaworks.junit.blog.modelo.Post;
import com.algaworks.junit.blog.utilidade.ProcessadorTextoSimples;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CalculadoraGanhosTest {
    static CalculadoraGanhos calculadoraGanhos;
    Editor autor;
    Post post;

    @BeforeAll // antes de todos os testes uma só vez
    static void initAll() {
        calculadoraGanhos = new CalculadoraGanhos(
                new ProcessadorTextoSimples(),
                BigDecimal.TEN);
    }

    @BeforeEach
        // antes de cada um
    void beforeEach() {
        autor = new Editor(1L,
                "Alex",
                "alex@gmail.com",
                new BigDecimal(5),
                true);
        post = new Post(1L,
                "Ecossistema Java",
                "O Ecossitema do java é muito maduro",
                autor,
                "ecossitema-java-abc123",
                null, false, false);
    }

    @Test
    void should_Calculate_Profit_For_Premium() {
        Ganhos ganhos = calculadoraGanhos.calcular(post);
        assertEquals(new BigDecimal("45"), ganhos.getTotalGanho());
    }

    @Test
    void should_Calculate_Profit_Without_Premium() {
        autor.setPremium(false);
        Ganhos ganhos = calculadoraGanhos.calcular(post);
        assertEquals(new BigDecimal("35"), ganhos.getTotalGanho());
    }

    @Test
    void check_amount_payed_per_word() {
        Ganhos ganhos = calculadoraGanhos.calcular(post);
        assertEquals(autor.getValorPagoPorPalavra(), ganhos.getValorPagoPorPalavra());
    }

    @Test
    void check_word_count() {
        Ganhos ganhos = calculadoraGanhos.calcular(post);
        assertEquals(7, ganhos.getQuantidadePalavras());
    }
}