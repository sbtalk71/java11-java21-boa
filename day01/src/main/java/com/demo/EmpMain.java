package com.demo;

public class EmpMain {
    public static void main(String[] args) {

        Employee emp1=new Employee(1,"Shantanu","Hyderabad",10000);
        Employee emp2=new Employee(1,"Shantanu","Hyderabad",10000);

        System.out.println(emp1.toString());

        System.out.println(emp1==emp2);

        System.out.println(emp1.equals(emp2));
    }
}
