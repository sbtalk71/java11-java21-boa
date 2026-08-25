package design.patterns;

public class Employee {

    private final String name;
    private final int age;
    private final String city;
    private final String department;
    private final double salary;

    private Employee(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.city = builder.city;
        this.department = builder.department;
        this.salary = builder.salary;
    }

    public static class Builder {

        private String name;
        private int age;
        private String city;
        private String department;
        private double salary;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder salary(double salary) {
            this.salary = salary;
            return this;
        }

        public Employee build() {

            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(
                        "Name is required");
            }

            return new Employee(this);
        }
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}