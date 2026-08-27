package com.demo.ex1;

public class App1 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName()+" starts");
        TablePrinter tp= new TablePrinter();

        MyThread t1= new MyThread(tp,7);
        MyThread t2= new MyThread(tp,5);

        t1.start();
        t2.start();
        System.out.println("My Thread is "+t1.getState());

        t1.join();
        t2.join();
        System.out.println(Thread.currentThread().getName()+" exits");
    }
}
