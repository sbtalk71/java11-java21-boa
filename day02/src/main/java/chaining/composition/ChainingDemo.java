package chaining.composition;

import java.util.function.Function;
import java.util.function.Predicate;

public class ChainingDemo {
    public static void main(String[] args) {

        Function<Integer, Integer> add10=n->n+10;
        Function<Integer, Integer> multiply20=n->n*20;

        System.out.println(add10.andThen(multiply20).apply(2));

        System.out.println(add10.compose(multiply20).apply(2));

        Predicate<Integer> even=n->n%2==0;
        Predicate<Integer> greaterThan20=n->n>20;
        System.out.println(even.and(greaterThan20).test(21));
    }
}
