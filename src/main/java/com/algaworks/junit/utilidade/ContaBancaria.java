package com.algaworks.junit.utilidade;

import java.math.BigDecimal;

public class ContaBancaria {

    private BigDecimal saldo;

    public ContaBancaria(BigDecimal saldo) {
        if (saldo == null) {
            throw new IllegalArgumentException("Saldo não pode ser nulo");
        } else {
            this.saldo = saldo;
        }
    }

    public void saque(BigDecimal valor) {
        if (valor == null || valor.intValue() <= 0) {
            throw new IllegalArgumentException("Não pode ser nulo, zero ou negativo");
        }
        if (this.saldo.compareTo(valor) < 0) {
            throw new RuntimeException("Saldo Insuficiente");
        } else {
            this.saldo = this.saldo.subtract(valor);
        }
    }

    public void deposito(BigDecimal valor) {
        if ((valor == null) || valor.intValue() <= 0) {
            throw new IllegalArgumentException("Não pode ser nulo, zero ou negativo");
        }
        this.saldo = this.saldo.add(valor);
    }

    public BigDecimal saldo() {
        return this.saldo;
    }
}
