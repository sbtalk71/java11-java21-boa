package collections;

import com.demo.Employee;

import javax.swing.text.html.HTML;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapDemo {
    public static void main(String[] args) {
        Map<Integer, Employee> empMap=new HashMap<>();
        empMap.put(100,new Employee(100,"Shantanu","Hyderabad",60000));
        empMap.put(101,new Employee(101,"Shantanu","Hyderabad",60000));
        empMap.put(102,new Employee(102,"Shantanu","Hyderabad",60000));
        empMap.put(103,new Employee(104,"Shantanu","Hyderabad",60000));

        Set<Integer> keys=empMap.keySet();

        for(Integer key:keys){
            System.out.println(key+" -> "+empMap.get(key));
        }

      Set<Map.Entry<Integer, Employee>> entrySet=empMap.entrySet();
        for(Map.Entry<Integer,Employee> entry:entrySet){
            System.out.println(entry.getKey()+" - > "+entry.getValue());
        }
    }
}
