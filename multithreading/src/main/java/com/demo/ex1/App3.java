package com.demo.ex1;

import java.time.LocalTime;

public class App3 {
    public static void main(String[] args)  {

        Thread t1=new Thread(()->{
            for(int i=0;i<5;i++){
                System.out.println(LocalTime.now()+" | "+Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
    }
}
