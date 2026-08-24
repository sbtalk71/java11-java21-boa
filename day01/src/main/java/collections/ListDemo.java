package collections;

import com.demo.Employee;

import java.util.*;

public class ListDemo {
    public static void main(String[] args) {
       //List<Employee> empList=new ArrayList<>();

       SortedSet<Employee> empList=new TreeSet<>();
        empList.add(new Employee(100,"Shantanu","Hyderabad",45000));
        empList.add(new Employee(101,"Pavan","Hyderabad",45000));
        empList.add(new Employee(102,"Shiva","Hyderabad",45000));
        empList.add(new Employee(103,"Durga","Hyderabad",45000));
        empList.add(new Employee(100,"Hanuman","Hyderabad",45000));

        System.out.println(empList);

       // System.out.println(empList.get(3));

        Iterator<Employee> itr=empList.iterator();
        while (itr.hasNext()){
            Employee emp= itr.next();
            emp.profileInfo();
        }

        for(Employee e:empList){
            e.profileInfo();
        }

    }
}
