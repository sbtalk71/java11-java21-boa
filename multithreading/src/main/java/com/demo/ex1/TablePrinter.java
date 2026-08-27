package com.demo.ex1;

public class TablePrinter {

    public void  printTable(int num){
        System.out.println(Thread.currentThread().getName()+" starts");
        System.out.println("Is it a virtual Thread "+Thread.currentThread().isVirtual());
        try {
            for(int i=1;i<11;i++){
                System.out.println(num+" X "+i+" = "+(i*num));
               // System.out.println(Thread.currentThread().getState());
                Thread.sleep(1500);
               // System.out.println(Thread.currentThread().getState());
            }

        }catch (InterruptedException ex){
            ex.printStackTrace();

        }
        System.out.println(Thread.currentThread().getName()+" exits");
    }
}
