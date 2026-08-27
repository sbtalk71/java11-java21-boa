package forkjoin;

import java.util.concurrent.RecursiveAction;

public class NumberSplitterAction extends RecursiveAction {
    private double num;

    public NumberSplitterAction(double num) {
        this.num = num;
    }

    @Override
    protected void compute() {
        if(num>10){
            NumberSplitterAction task1=new NumberSplitterAction(num/2);
            NumberSplitterAction task2=new NumberSplitterAction(num/2);
            task1.fork();
            task2.fork();
            task1.join();
            task2.join();
        }else{
            System.out.println("split data : "+num);
        }
    }
}
