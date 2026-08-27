package com.demo.sealed.classes;

public abstract sealed class Payment permits CreditCardPayment, NetBankingPayment, UPIPayment {
    abstract void pay(double amount);
}