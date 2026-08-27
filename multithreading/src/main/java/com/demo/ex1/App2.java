package com.demo.ex1;

public class App2 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName()+" starts");
        TablePrinter tp= new TablePrinter();

        Thread t1=new Thread(new Worker(tp,6));
        Thread t2=new Thread(new Worker(tp,8));

        System.out.println(Thread.currentThread());
        System.out.println(t1);
        System.out.println(t2);

        t1.start();
        t2.start();
        System.out.println("My Thread is "+t1.getState());

        t1.join();
        t2.join();
        System.out.println(Thread.currentThread().getName()+" exits");
        System.out.println("Thread state of t2 "+t2.getState());


    }
}
