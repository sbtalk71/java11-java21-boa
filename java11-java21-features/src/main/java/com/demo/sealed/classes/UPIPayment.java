package com.demo.sealed.classes;

public sealed class UPIPayment extends Payment permits WalletPayment{
    @Override
    void pay(double amount) {
        System.out.format("Paid %f with UPI",amount);
    }
}
