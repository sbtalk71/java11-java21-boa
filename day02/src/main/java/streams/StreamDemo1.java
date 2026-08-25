package streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamDemo1 {
    public static void main(String[] args) {
        List<Integer> numList= Arrays.asList(1,2,4,4,5,3,6,5,7,3,8,9,12,13,12,15,34,56,67,89,98);
/*
        for(Integer x: numList){
            if(x%2==0){
                System.out.println(x);
            }
        }*/

        numList.stream().filter(n->n%2==0).forEach(n->System.out.println(n));
        //sum of all the even numbers
      long countDistinct=  numList.stream().filter(n->n%2==0).distinct().peek(n->System.out.println(n)).count();
        System.out.println("Distinct Even nums "+countDistinct);

        long sum=  numList.stream().filter(n->n%2==0).distinct().mapToInt(n->n.intValue()).sum();
        numList.stream().distinct().sorted(Comparator.comparing(Integer::intValue).reversed()).forEach(System.out::println);

        numList.stream().distinct().filter(n->n%2!=0).collect(Collectors.toList()).forEach(System.out::println);


    }
}
