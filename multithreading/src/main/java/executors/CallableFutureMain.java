package executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableFutureMain {
    public static void main(String[] args) throws Exception{
        ExecutorService es= Executors.newCachedThreadPool();

      Future<String> future= es.submit(new UppercaseConverter("tony"));

      System.out.println("Thread submitted...");

      /*while(!future.isDone()){
          System.out.println("Processing data..");
      }*/
        System.out.println(future.get());

        es.shutdown();

        System.out.println("Exiting...");
    }
}
