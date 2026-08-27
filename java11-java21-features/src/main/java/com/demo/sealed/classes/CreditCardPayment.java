package com.demo.sealed.classes;

public final class CreditCardPayment extends Payment{
    @Override
    void pay(double amount) {
        System.out.format("Paid %f with Credit Card",amount);
    }
}
