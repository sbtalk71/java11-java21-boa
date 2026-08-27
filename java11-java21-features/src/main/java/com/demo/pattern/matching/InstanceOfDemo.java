package com.demo.pattern.matching;

import com.demo.records.Employee;

public class InstanceOfDemo {
    public static void main(String[] args) {
        Object str="Hello there";
        //traditional
        if(str instanceof String){
            String data=(String)str;
            System.out.println(data);
        }

        if(str instanceof String s){
            System.out.println(s);
        }

        Employee emp=new Employee(100,"Shantanu","Hyderabad",80000);

        if(emp instanceof Employee(int empId, String name,String city,double salary) && salary>70000){
            System.out.println(empId+" "+city+" "+salary);
        }
    }
}
