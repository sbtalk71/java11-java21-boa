package com.demo.sealed.classes;

public final class WalletPayment extends UPIPayment{
    @Override
    void pay(double amount) {
        System.out.format("Paid %f with Wallet",amount);
    }
}
