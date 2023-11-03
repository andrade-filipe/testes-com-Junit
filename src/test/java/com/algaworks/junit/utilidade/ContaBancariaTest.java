package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Nested
@DisplayName("GIVEN a bank account with $1000")
class ContaBancariaTest {

    private ContaBancaria contaBancaria;
    private BigDecimal saldo;

    @BeforeEach
    void arrange() {
        saldo = new BigDecimal("1000");
        contaBancaria = new ContaBancaria(saldo);
    }

    @Nested
    @DisplayName("WHEN Creating account")
    class Saldo {

        @Test
        @DisplayName("SHOULD display the money")
        void saldo() {
            assertEquals(contaBancaria.saldo(), saldo);
        }

        @Test
        @DisplayName("IF the amount is null THEN throw exception")
        void constructorException() {
            IllegalArgumentException illegalArgumentException =
                    assertThrows(IllegalArgumentException.class,
                            () -> new ContaBancaria(null));

            assertEquals(
                    "Saldo não pode ser nulo",
                    illegalArgumentException.getMessage());
        }
    }

    @Nested
    @DisplayName("WHEN withdrawing")
    class Saque {
        @Test
        @DisplayName("SHOULD successfully do the operation")
        void saque() {
            //Arrange
            BigDecimal valor = new BigDecimal("500");

            //Act
            contaBancaria.saque(valor);

            //Assert
            assertEquals(
                    contaBancaria.saldo(),
                    saldo.subtract(valor));
        }

        @Test
        @DisplayName("IF value is NEGATIVE, THEN throw exception")
        void saqueIllegalException() {
            //Arrange
            BigDecimal valorInvalido = new BigDecimal("-1");

            //Act
            Executable executable = () -> contaBancaria.saque(valorInvalido);

            //Assert
            IllegalArgumentException illegalArgumentException =
                    assertThrows(
                            IllegalArgumentException.class, executable);
            assertEquals(
                    "Não pode ser nulo, zero ou negativo",
                    illegalArgumentException.getMessage());
        }

        @Test
        @DisplayName("IF value is 0 THEN throw exception")
        void saqueNegativeIllegalException() {
            //Arrange
            BigDecimal valorInvalido = new BigDecimal("0");

            //Act
            Executable executable = () -> contaBancaria.saque(valorInvalido);

            //Assert
            IllegalArgumentException illegalArgumentException =
                    assertThrows(
                            IllegalArgumentException.class, executable);
            assertEquals(
                    "Não pode ser nulo, zero ou negativo",
                    illegalArgumentException.getMessage());
        }

        @Test
        @DisplayName("IF value is superior to the balance THEN throw exception")
        void saqueRuntimeException() {
            BigDecimal valorInvalido = new BigDecimal("1001");

            Executable executable = () -> contaBancaria.saque(valorInvalido);

            RuntimeException runtimeException =
                    assertThrows(RuntimeException.class, executable);
            assertEquals(
                    "Saldo Insuficiente",
                    runtimeException.getMessage());
        }
    }

    @Nested
    @DisplayName("WHEN depositing")
    class Deposito {
        @Test
        @DisplayName("SHOULD successfully deposit the amount to the balance")
        void deposito() {
            BigDecimal valor = new BigDecimal("1000");

            contaBancaria.deposito(valor);

            assertEquals(
                    contaBancaria.saldo(),
                    saldo.add(valor),
                    "Problema no método de depósito");
        }

        @Test
        @DisplayName("IF value is 0 THEN throw exception")
        void depositoException() {
            BigDecimal valorInvalido = new BigDecimal("0");

            Executable executable = () -> contaBancaria.deposito(valorInvalido);

            IllegalArgumentException illegalArgumentException =
                    assertThrows(IllegalArgumentException.class, executable);
            assertEquals(
                    "Não pode ser nulo, zero ou negativo",
                    illegalArgumentException.getMessage());
        }

        @Test
        @DisplayName("IF value is NEGATIVE, THEN throw exception")
        void depositoNegativeException() {
            BigDecimal valorInvalido = new BigDecimal("-1");

            Executable executable = () -> contaBancaria.deposito(valorInvalido);

            IllegalArgumentException illegalArgumentException =
                    assertThrows(IllegalArgumentException.class, executable);
            assertEquals(
                    "Não pode ser nulo, zero ou negativo",
                    illegalArgumentException.getMessage());
        }
    }


}