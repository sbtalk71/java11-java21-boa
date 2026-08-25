package com.demo;

public class Main {
    public static void main(String[] args) {

        Greet greet= new Greeter();
        System.out.println(greet.greet());

        Greet greet2= new Greet() {
            @Override
            public String greet() {
                return "Good Evening";
            }
        };


        Greet greet3= ()->"Good Evening";


    }
}

class Greeter implements Greet{

    @Override
    public String greet() {
        return "Good morning";
    }
}
