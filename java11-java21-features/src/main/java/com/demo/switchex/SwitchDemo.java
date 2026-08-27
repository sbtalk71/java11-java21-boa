package com.demo.switchex;

import com.demo.records.Employee;

public class SwitchDemo {

  static  Day day = Day.WEDNESDAY;

    public static int traditionalSwitch() {
        int numOfLetters = 0;
        switch (day) {
            case MONDAY, FRIDAY, SUNDAY:
                numOfLetters = 6;
                break;
            case THURSDAY, SATURDAY:
                numOfLetters = 8;
                break;
            case WEDNESDAY:
                numOfLetters = 9;
                break;
            default:
                throw new IllegalArgumentException("Unexpected Value : " + day);
        }
        return numOfLetters;
    }

    public static int switchExpression() {

        return switch (day) {
            case MONDAY, FRIDAY, SUNDAY -> 6;
            case THURSDAY, SATURDAY -> 8;
            case WEDNESDAY -> 9;
            default -> throw new IllegalArgumentException("Unexpected Value : " + day);
        };

    }

    public static int switchExpressionWithYield() {

        return switch (day) {
            case MONDAY, FRIDAY, SUNDAY:
                System.out.println(6);
                yield 6;
            case THURSDAY, SATURDAY:
                System.out.println(8);
                yield 8;
            case WEDNESDAY:
                System.out.println(9);
                yield 9;
            default:
                throw new IllegalArgumentException("Unexpected Value : " + day);
        };

    }

    public static Object guardedSwitchExp() {
        Object x = "Test";

        return switch (x) {
            case Integer i when i > 20:
                System.out.println("Integer value " + i);
                yield i;
            case String s when s.length() > 5:
                System.out.println("String Value " + s);
                yield s;
            default:
                yield x;
        };
    }
        public static String guardedRecordPattern() {
            Employee emp=new Employee(100,"Shantanu","Hyderabad",95000);
            return switch (emp){
                case Employee(int id, String name,String city, double salary) when salary>85000->"executive";
                case Employee(int id, String name,String city, double salary) when salary<=80000 && salary>=75000->"Senior Employee";
                case Employee(int id, String name,String city, double salary) when salary<75000->"worker";
                default-> "";
            };

    }

    public static void main(String[] args) throws Exception{

        System.out.println(guardedRecordPattern());
        //Thread.sleep(Long.MAX_VALUE);




    }
}