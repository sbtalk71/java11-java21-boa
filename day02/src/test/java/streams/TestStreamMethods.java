package streams;

import dataset.Data;
import org.junit.jupiter.api.Test;

public class TestStreamMethods {

    @Test
    void testMapOnEmployeeStream(){
        Data.employees.stream().filter(e->e.city().equalsIgnoreCase("hyderabad")).forEach(System.out::println);
    }

    @Test
    void testfirstEmpFromHyderabad(){
        Data.employees.stream()
                .filter(e->e.city().equalsIgnoreCase("hyderabad"))
                .findFirst()
                .ifPresent(System.out::println);
    }

    @Test
    void testLastEmpFromHyderabad(){
        Data.employees.stream()
                .filter(e->e.city().equalsIgnoreCase("hyderabad"))
                .reduce((e1,e2)->e2)
                .ifPresent(System.out::println);
    }

    @Test
    void testTotalSalaryOfITdept(){
        Data.employees.stream()
                .filter(e->e.department().equalsIgnoreCase("IT"))
                .mapToDouble(e->e.salary())
                .reduce((s1,s2)->s1+s2)
                .ifPresent(System.out::println);
    }

    @Test
    void testEmployeeWithSalaryMoreThan85k(){
       if(Data.employees.stream().anyMatch(e->e.salary()>85000)){
           Data.employees.stream().filter(e->e.salary()>85000).forEach(System.out::println);
       }

    }
    @Test
    void testLimit(){
        Data.employees.stream().limit(5).forEach(System.out::println);

    }

    @Test
    void testSkip(){
        Data.employees.stream().skip(10).forEach(System.out::println);

    }
}
