package exceptions;

public class ExceptionDemo1 {
    public static void main(String[] args) {
try {
    int a = 10;
    int b = 0;

    double d = a / b;
    System.out.println("No Exceptions..");
}catch (ArithmeticException ex){
            System.out.println(ex);
} catch(NullPointerException ex){
        System.out.println(ex);
    }
        System.out.println("Normal sequence resumed..");
       int result= add();
}

public static int add() {
        return 12;
}
}
