package com.demo.lambda;

import java.util.function.Consumer;
import java.util.function.Function;

public class MethodRefDemo {
    public static void main(String[] args) {
        Consumer<String> printer=System.out::println;

        Consumer<String> printer2=s->System.out.println(s);

        Consumer<String> printer3=MessagePrinter::print;
        printer3.accept("Hi there");

        MessagePrinter mp= new MessagePrinter();

        Consumer<String> printer4=mp::printDecoratedMessage;
        printer4.accept("good morning");

        Function<String, String> stringData=String::new;
        stringData.apply("Hello");

        EmpCreator empCreator=Employee::new;

        EmpCreator empCreator1=(x,y)->new Employee(x,y);

    }
}
