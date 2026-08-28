package com.demo.ex1;

public class Worker implements Runnable{
    private TablePrinter tablePrinter;
    private int num;

    public Worker(TablePrinter tablePrinter, int num) {
        this.tablePrinter = tablePrinter;
        this.num = num;
    }

    @Override
    public void run() {
        //synchronized (tablePrinter) {
            tablePrinter.printTable(num);
       // }
    }
}
