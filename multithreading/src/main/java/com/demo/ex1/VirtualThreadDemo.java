package com.demo.ex1;

public class VirtualThreadDemo {
    public static void main(String[] args) throws Exception {
        TablePrinter tp = new TablePrinter();
        Thread t1=Thread.ofVirtual().unstarted(new Worker(tp,4));
        Thread t2=Thread.ofVirtual().unstarted(new Worker(tp,5));
        Thread t3=Thread.ofVirtual().unstarted(new Worker(tp,6));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }
}
