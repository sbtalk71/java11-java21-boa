package com.demo.records;

public class RecordsMain {
    public static void main(String[] args) {
        Employee emp=new Employee(100,"Shantanu","Hyderabad",89000);
        Employee emp1=new Employee(100,"Shantanu","Hyderabad",89000);

        String data= """
                emp Id: %d 
                name : %s 
                city: %s 
                salary: %f
                """.formatted(emp.id(),emp.name(),emp.city(),emp.salary());
        System.out.println(data);

        System.out.println(emp.equals(emp1));

        System.out.println(emp);


        Employee emp3=new Employee(100,"Rajesh");
        System.out.println(emp3.salary());
    }
}
