package com.demo.stringmethods;

public class StringNewMethods {
    public static void main(String[] args) {
        //isBlank
        //strip()
        //stripTrailing()
        //stripLeading()
        //repeat(int x)

        String s1="";
        String s2="   ";
        String s3="     Java     ";

        System.out.println(s1.isEmpty());
        System.out.println(s2.isEmpty());
        System.out.println(s3.isEmpty());
        System.out.println("---".repeat(20));

        System.out.println(s1.isBlank());
        System.out.println(s2.isBlank());
        System.out.println(s3.isBlank());

        System.out.println(s3.strip()+" |");
        System.out.println(s3.stripLeading()+" |");
        System.out.println(s3.stripTrailing()+" |");

    }
}
