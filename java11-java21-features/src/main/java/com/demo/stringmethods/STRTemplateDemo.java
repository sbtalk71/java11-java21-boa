package com.demo.stringmethods;

//import static java.lang.StringTemplate.STR;

public class STRTemplateDemo {
    public static void main(String[] args) {
        String name="Shantanu";
        int age=55;

        String message="My Name is "+name+" and I am "+age+" years old";

        //String formattedMessage=STR."My Name is \{name} and I am \{age} years old";

        String formatedMessage="My Name is %s and I am %d years old".formatted(name,age);
        System.out.println(formatedMessage);
    }
}
