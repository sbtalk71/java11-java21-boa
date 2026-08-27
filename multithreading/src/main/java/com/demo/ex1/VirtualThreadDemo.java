package com.demo.ex1;

public class VirtualThreadDemo {
    public static void main(String[] args) throws Exception {
        TablePrinter tp = new TablePrinter();
        Thread t1=Thread.ofVirtual().unstarted(new Worker(tp,4));
        t1.start();
        t1.join();
    }
}
