package com.demo.ex1;

public class MyThread extends Thread{
private TablePrinter tablePrinter;
private int num;

    public MyThread(TablePrinter tablePrinter, int num) {
        this.tablePrinter = tablePrinter;
        this.num = num;
    }

    @Override
    public void run() {
       tablePrinter.printTable(num);
    }
}
