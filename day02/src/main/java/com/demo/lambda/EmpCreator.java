package com.demo.lambda;
@FunctionalInterface
public interface EmpCreator {
    Employee create(int empId, String name);
}
