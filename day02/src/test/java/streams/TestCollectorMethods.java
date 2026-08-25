package streams;

import dataset.Data;
import dataset.Employee;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestCollectorMethods {

    @Test
    void testGroupingBy(){
        Data.employees.stream()
                .collect(Collectors.groupingBy(emp->emp.department(),Collectors.toList())).forEach((k,v)->System.out.println(k+" "+v));
    }

    @Test
    void testGroupingByAndMapping(){
        Data.employees.stream()
                .collect(Collectors.groupingBy(emp->emp.department(),Collectors.mapping(emp->emp.name(),Collectors.toList())))
                .forEach((dept,list)->System.out.println(dept+" -> "+list));
    }

    @Test
    void testCounting(){
        System.out.println(Data.employees.stream()
                .collect(Collectors.groupingBy(emp->emp.department(),Collectors.counting())));
    }

    @Test
    void testSummerizing(){
        DoubleSummaryStatistics stats=Data.employees.stream().collect(Collectors.summarizingDouble(Employee::salary));
        System.out.println("Total No of Employees : "+stats.getCount());
        System.out.println("Total salary : "+stats.getSum());
        System.out.println("Average Salary : "+stats.getAverage());
        System.out.println("Min salary : "+stats.getMin());
        System.out.println("Max Salary : "+stats.getMax());
    }

    @Test
    void testJoining(){
       System.out.println(Data.employees.stream().map(e->e.name()).collect(Collectors.joining(", ")));
    }

    @Test
    void testPartionBy(){
       Map<Boolean, List<Employee>> partitionedData=Data.employees.stream().collect(Collectors.partitioningBy(e->e.salary()>75000));
        System.out.println("TRUE:->"+partitionedData.get(true).stream().map(e->e.name()).collect(Collectors.toList()));
        System.out.println("FALSE:->"+partitionedData.get(false).stream().map(e->e.name()).collect(Collectors.toList()));
    }

    @Test
    void testParallelStream(){

        List<Integer> numList= Arrays.asList(1,2,4,4,5,3,6,5,7,3,8,9,12,13,12,15,34,56,67,89,98);

        numList.parallelStream().distinct().filter(n->n%2==0).peek(n->System.out.println(Thread.currentThread().getName()+" processed "+n)).forEach(System.out::println);

    }
}
