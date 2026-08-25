package com.demo.lambda;

public class MessagePrinter {

    public static  void print(String message){
        System.out.println(message);
    }

    public void printDecoratedMessage(String s){
        System.out.println("Hello there "+s);
    }
}
