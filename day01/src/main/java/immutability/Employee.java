package immutability;

import java.util.List;
import java.util.Objects;

public final class Employee {
    private final int empId;
    private final String name;
    private final List<String> skills;

    public Employee(int empId, String name,List<String> skills) {
        this.empId = empId;
        this.name = name;
        this.skills=List.copyOf(skills);
            }

    public void profileInfo(){

        System.out.println(empId+" "+name+" ");
    }

    @Override
    public String toString() {
        return "["+empId+" "+name+"]";
    }

    public int getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public List<String> getSkills(){
        return this.skills;
    }
}
