package com.demo.records;

//canonical constructor
public record Employee(int id, String name,String city,double salary) {

    /*public Employee(int id, String name,String city,double salary){
        if(id<1) throw new IllegalArgumentException("Id cannot be less than 1");
        this.id=id;
        this.name=name;
        this.city=city;
        this.salary=salary;
    }*/

    //short form constructor
    public Employee{
        if(id<1) throw new IllegalArgumentException("Id cannot be less than 1");
    }

    public Employee(int id,String name){
        this(id,name,"unknown",0.0);
    }
}
