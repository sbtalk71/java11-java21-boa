package design.patterns;

public class BuilderDemoMain {
    public static void main(String[] args) {
        Employee emp = new Employee.Builder().name("Shantanu").age(56).city("Hyderabad").build();
            }
}
