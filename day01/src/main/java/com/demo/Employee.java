package com.demo;

import java.util.Objects;

public class Employee implements Comparable<Employee>{
    private int empId;
    private String name;
    private String city;
    private double salary;

    public Employee(int empId, String name, String city, double salary) {
        this.empId = empId;
        this.name = name;
        this.city = city;
        this.salary = salary;
    }

    public void profileInfo(){

        System.out.println(empId+" "+name+" "+city);
    }

    @Override
    public String toString() {
        return "["+empId+" "+name+" "+city+"]";
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("equals called...");
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return empId == employee.empId;
    }

    @Override
    public int hashCode() {
        System.out.println("hashcode called...");
        return Objects.hashCode(empId);
    }

    @Override
    public int compareTo(Employee o) {
        return name.compareTo(o.name);
    }
}
