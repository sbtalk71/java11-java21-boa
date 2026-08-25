package com.demo.lambda;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Demo1 {

    public static void main(String[] args) {
        Predicate<Integer> isEven=n->n%2==0;

        System.out.println(isEven.test(21));

        Consumer<String> greeter=(s)->System.out.println(s);

        greeter.accept("Hello There!");

        Function<Integer,Integer> doubleIt=n->n*2;

        System.out.println(doubleIt.apply(20));

        Function<Integer,Integer> add20=new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer+20;
            }
        };

        Supplier<String> greetMessage=()->"Hello World";
    }
}
