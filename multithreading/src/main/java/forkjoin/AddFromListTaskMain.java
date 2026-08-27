package forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class AddFromListTaskMain {
    public static void main(String[] args) {
        ForkJoinPool fjp=new ForkJoinPool(6);
        List<Integer> numList=new ArrayList<>();
        for(int i=1;i<100;i++){
            numList.add(i);
        }

    int sum= fjp.invoke(new AddFromListTask(numList));
        System.out.println("The Sum = "+sum);
    }
}
