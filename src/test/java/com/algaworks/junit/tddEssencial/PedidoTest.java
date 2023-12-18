package com.algaworks.junit.tddEssencial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PedidoTest{

    private Pedido pedido;

    @BeforeEach
    public void init(){
        pedido = new Pedido();
    }

    @Test
    @DisplayName("Adicionar Pedido")
    public void addPedido(){
        pedido.adicionarItem("Sabonete", 3.0, 10);
    }

    @Test
    @DisplayName("Calcular valor total")
    public void calcularCalorTotal(){
        assertEquals(0.0, pedido.valorTotal());
    }
}
