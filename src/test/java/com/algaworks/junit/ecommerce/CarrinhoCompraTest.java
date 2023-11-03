package com.algaworks.junit.ecommerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("Shopping Cart")
class CarrinhoCompraTest {

    private CarrinhoCompra cart;
    private Cliente client;
    private Produto chord;
    private Produto tablet;
    private Produto notebook;
    private List<ItemCarrinhoCompra> list;

    @Nested
    @DisplayName("GIVEN a cart with itens")
    class CartWithItens {
        @BeforeEach
        void arrange() {
            client = new Cliente(1L, "Filipe");

            chord = new Produto(1L, "Corda", "resistente e confiável,10m", BigDecimal.TEN);
            tablet = new Produto(3L, "Tablet", "Tablet", BigDecimal.valueOf(30.50));
            notebook = new Produto(1L, "Notebook", "Notebook", BigDecimal.TEN);

            ItemCarrinhoCompra item1 = new ItemCarrinhoCompra(tablet, 1);
            ItemCarrinhoCompra item2 = new ItemCarrinhoCompra(chord, 2);
            list = new ArrayList<>();
            list.add(item1);
            list.add(item2);
            cart = new CarrinhoCompra(client, list);
        }

        @Nested
        @DisplayName("Check Information reliability")
        class Check {
            @Test
            @DisplayName("SHOULD List Itens")
            void checkGetItens() {
                assertEquals(cart.getItens(), list);
            }

            @Test
            @DisplayName("SHOULD Return client")
            void checkClient() {
                assertEquals(cart.getCliente(), client);
            }
        }

        @Nested
        @DisplayName("WHEN returning itens")
        class ReturningItens {
            @Test
            @DisplayName("SHOULD return two itens")
            void returnList() {
                assertEquals(2, cart.getItens().size());
            }

            @Test
            @DisplayName("SHOULD return a new instance of the list")
            void returnNewList() {
                cart.getItens().clear();
                assertEquals(2, cart.getItens().size());
            }
        }

        @Nested
        @DisplayName("WHEN adding an item")
        class AddingItens {

            @BeforeEach
            void addItem() {
                cart.adicionarProduto(notebook, 1);
            }

            @Test
            @DisplayName("SHOULD add a product to the cart")
            void checkIfAdded() {
                assertEquals(4, cart.getQuantidadeTotalDeProdutos());
            }

            @Test
            @DisplayName("AND CHECK if is the SAME product")
            void checkIfConsistent() {
                assertEquals(notebook, cart.getItens().get(2).getProduto());
            }

            @Test
            @DisplayName("IF product is null THEN throw exception")
            void additionNullProductException() {
                assertThrows(
                        NullPointerException.class,
                        () -> cart.adicionarProduto(null, 1));
            }

            @Test
            @DisplayName("IF amount is 0 THEN throw exception")
            void additionZeroAmountException() {
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cart.adicionarProduto(notebook, 0));
            }

            @Test
            @DisplayName("IF amount is NEGATIVE THEN throw exception")
            void additionNegativeAmountException() {
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cart.adicionarProduto(notebook, -1));
            }
        }

        @Nested
        @DisplayName("WHEN removing an item")
        class RemovingItens {

            @BeforeEach
            void removeItem() {
                cart.removerProduto(tablet);
            }

            @Test
            @DisplayName("SHOULD remove an item from the cart")
            void checkIfRemoved() {
                assertEquals(2, cart.getQuantidadeTotalDeProdutos());
            }

            @Test
            @DisplayName("AND CHECK if was the right item")
            void checkRemoval() {
                for (ItemCarrinhoCompra itemCarrinhoCompra : cart.getItens().stream().toList()) {
                    assertNotSame(itemCarrinhoCompra.getProduto(), tablet);
                }
            }

            @Test
            @DisplayName("IF product is null THEN throw exception")
            void additionNullProductException() {
                assertThrows(
                        NullPointerException.class,
                        () -> cart.removerProduto(null));
            }
        }

        @Nested
        @DisplayName("WHEN adding the amount of a product")
        class AddAmount {

            @BeforeEach
            void addAmountOfProduct() {
                cart.aumentarQuantidadeProduto(chord);
            }

            @Test
            @DisplayName("SHOULD sum the amount inside the cart")
            void sumAmount() {
                assertEquals(3, cart.getItens().get(1).getQuantidade());
                assertEquals(1, cart.getItens().get(0).getQuantidade());
            }

            @Test
            @DisplayName("SHOULD return four as total of itens")
            void allItens() {
                assertEquals(4, cart.getQuantidadeTotalDeProdutos());
            }

            @Test
            @DisplayName("SHOULD return the right price for all itens")
            void totalPrice() {
                assertEquals(new BigDecimal("60.5"), cart.getValorTotal());
            }

            @Test
            @DisplayName("IF product null THEN throw Exception")
            void exception() {
                assertThrows(
                        NullPointerException.class,
                        () -> cart.aumentarQuantidadeProduto(null));
            }
        }

        @Nested
        @DisplayName("WHEN decreasing the amount of a product")
        class DecreaseAmount {

            @BeforeEach
            void decreaseAmountOfProduct() {
                cart.diminuirQuantidadeProduto(chord);
            }

            @Test
            @DisplayName("SHOULD decrease inside the cart")
            void decreaseInsideCart() {
                assertEquals(1, cart.getItens().get(0).getQuantidade());
                assertEquals(1, cart.getItens().get(1).getQuantidade());
            }

            @Test
            @DisplayName("SHOULD return two as total itens in the cart")
            void decreaseCartTotal() {
                assertEquals(2, cart.getQuantidadeTotalDeProdutos());
            }

            @Test
            @DisplayName("SHOULD return the correct total price")
            void totalPrice() {
                assertEquals(new BigDecimal("40.5"), cart.getValorTotal());
            }

            @Test
            @DisplayName("IF product null THEN throw Exception")
            void exception() {
                assertThrows(
                        NullPointerException.class,
                        () -> cart.diminuirQuantidadeProduto(null));
            }
        }

        @Nested
        @DisplayName("WHEN decreasing a product that has quantity 1")
        class DecreaseUniqueProduct {
            @BeforeEach
            void decreaseUnique() {
                cart.diminuirQuantidadeProduto(tablet);
            }

            @Test
            @DisplayName("SHOULD remove item")
            void removeItem() {
                assertNotEquals(cart.getItens().get(0).getProduto(), tablet);
            }
        }

        @Nested
        @DisplayName("WHEN empty cart")
        class EmptyCart {
            @BeforeEach
            void emptyCart() {
                cart.esvaziar();
            }

            @Test
            @DisplayName("SHOULD set the itens quantity to zero")
            void zeroItens() {
                assertEquals(0, cart.getItens().size());
            }

            @Test
            @DisplayName("SHOULD have the size zero")
            void sizeZero() {
                assertEquals(0, cart.getQuantidadeTotalDeProdutos());
            }

            @Test
            @DisplayName("SHOULD return price zero")
            void priceZero() {
                assertEquals(BigDecimal.ZERO, cart.getValorTotal());
            }
        }

    }

    @Nested
    @DisplayName("GIVEN an empty cart")
    class CartWithNoItens {
        @BeforeEach
        void arrange() {
            client = new Cliente(1L, "Filipe");

            chord = new Produto(1L, "Corda", "resistente e confiável,10m", BigDecimal.TEN);
            tablet = new Produto(3L, "Tablet", "Tablet", BigDecimal.valueOf(30.50));
            notebook = new Produto(1L, "Notebook", "Notebook", BigDecimal.TEN);

            list = new ArrayList<>();

            cart = new CarrinhoCompra(client, list);
        }

        @Nested
        @DisplayName("WHEN adding to itens that are the same")
        class AddEqualItens {

            @BeforeEach
            void beforeEach() {
                cart.adicionarProduto(notebook, 1);
                cart.adicionarProduto(notebook, 1);
                cart.adicionarProduto(tablet, 1);
            }

            @Test
            @DisplayName("SHOULD sum into the same item in the cart")
            void sumOnQuantity() {
                assertEquals(2, cart.getItens().get(0).getQuantidade());
                assertEquals(1, cart.getItens().get(1).getQuantidade());
            }

            @Test
            @DisplayName("AND return three as total of itens")
            void threeAsTotal() {
                assertEquals(3, cart.getQuantidadeTotalDeProdutos());
            }

            @Test
            @DisplayName("AND return the total price correctly")
            void price() {
                assertEquals(new BigDecimal("50.5"), cart.getValorTotal());
            }
        }
    }
}