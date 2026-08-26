package com.demo.collections;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FactoryMethodsInCollections {
    public static void main(String[] args) {

        List<String> fruits=List.of("apple","mango","orange");

        System.out.println(fruits);
        //fruits.add("guava");

        Set<String> cities=Set.of("hyderabad","chennai","bengaluru");

       // cities.add("bhopal"); //not allowed

        Map<Integer, String> fruitsMap=Map.of(1,"apple",2,"mango");
        System.out.println(fruitsMap);
    }
}
