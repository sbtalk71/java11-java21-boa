package abstraction;

public class OverloadDemo {

    public int add(int a, int b){
        System.out.println("calling int, int");
        return a+b;
    }

    public int add(int a, int b,int c){
        System.out.println("calling int, int, int");
        return a+b+b;
    }

    public float add(int a, float b){
        System.out.println("calling int, float");
        return a+b;
    }

    public double add(double a, double b){
        System.out.println("calling double, double");
        return a+b;
    }

    public static void main(String[] args) {
        OverloadDemo od=new OverloadDemo();

        od.add(2,3);
        od.add(1,2,3);
        od.add(2,3f);
        od.add(2.0,4.0);
    }
}
