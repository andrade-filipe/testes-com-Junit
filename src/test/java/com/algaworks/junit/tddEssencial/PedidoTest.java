package com.algaworks.junit.tddEssencial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.algaworks.junit.tddEssencial.desconto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PedidoTest{

    private Pedido pedido;

    @BeforeEach
    public void setup() {
        CalculadoraFaixaDesconto calculadoraFaixaDesconto =
            new CalculadoraDescontoTerceiraFaixa(
                new CalculadoraDescontoSegundaFaixa(
                    new CalculadoraDescontoPrimeiraFaixa(
                        new SemDesconto(null))));

        pedido = new Pedido(calculadoraFaixaDesconto);
    }

    private void assertResumoPedido(double valorTotal, double desconto) {
        ResumoPedido resumoPedido = pedido.resumo();
        assertEquals(valorTotal, resumoPedido.getValorTotal(), 0.0001);
        assertEquals(desconto, resumoPedido.getDesconto(), 0.0001);
    }

    @Test
    public void devePermitirAdicionarUmItemNoPedido() throws Exception {
        pedido.adicionarItem(new ItemPedido("Sabonete", 3.0, 10));
    }

    @Test
    public void deveCalcularValorTotalEDescontoParaPedidoVazio() throws Exception {
        assertResumoPedido(0.0, 0.0);
    }

    @Test
    public void deveCalcularResumoParaUmItemSemDesconto() throws Exception {
        pedido.adicionarItem(new ItemPedido("Sabonete", 5.0, 5));
        assertResumoPedido(25.0, 0.0);
    }

    @Test
    public void deveCalcularResumoParaDoisItensSemDesconto() throws Exception {
        pedido.adicionarItem(new ItemPedido("Sabonete", 3.0, 3));
        pedido.adicionarItem(new ItemPedido("Pasta dental", 7.0, 3));

        assertResumoPedido(30.0, 0.0);
    }

    @Test
    public void deveAplicarDescontoNa1aFaixa() throws Exception {
        pedido.adicionarItem(new ItemPedido("Creme", 20.0, 20));

        assertResumoPedido(400.0, 16.0);
    }

    @Test
    public void deveAplicarDescontoNa2aFaixa() throws Exception {
        pedido.adicionarItem(new ItemPedido("Shampoo", 15.0, 30));
        pedido.adicionarItem(new ItemPedido("Óleo", 15.0, 30));

        assertResumoPedido(900.0, 54.0);
    }

    @Test
    public void deveAplicarDescontoNa3aFaixa() throws Exception {
        pedido.adicionarItem(new ItemPedido("Creme", 15.0, 30));
        pedido.adicionarItem(new ItemPedido("Óleo", 15.0, 30));
        pedido.adicionarItem(new ItemPedido("Shampoo", 10.0, 30));

        assertResumoPedido(1200.0, 96.0);
    }
}
