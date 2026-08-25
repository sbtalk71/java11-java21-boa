package immutability;

import java.util.ArrayList;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        List<String> skills= new ArrayList<>();
        skills.add("Python");
        Employee emp1= new Employee(100,"Shantanu", skills);

        skills.add("Java");

        for(String skill: emp1.getSkills()){
            System.out.println(skill);
        }



    }
}
