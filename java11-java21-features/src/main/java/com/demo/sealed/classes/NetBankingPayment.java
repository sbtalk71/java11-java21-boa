package com.demo.sealed.classes;

public non-sealed class NetBankingPayment extends Payment{
    @Override
    void pay(double amount) {
        System.out.format("Paid %f with NetBanking",amount);
    }
}
