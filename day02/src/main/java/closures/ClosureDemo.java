package closures;

import java.util.function.Function;

public class ClosureDemo {

    public static Function<Integer,Integer> createMultiplier(){
        int multiplier=30;

        return n->n*multiplier;
    }
    public static void main(String[] args) {

        Function<Integer, Integer> myMultiplier=createMultiplier();

        System.out.println(myMultiplier.apply(10));

    }
}
