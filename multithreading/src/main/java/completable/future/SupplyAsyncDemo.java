package completable.future;

import java.util.concurrent.CompletableFuture;

public class SupplyAsyncDemo {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> messageAsync=CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName());
            return "Hello";
        }).thenApplyAsync(message->message+" from completable future").thenApplyAsync(m2->m2+" !!");

        System.out.println(messageAsync.get());

    }
}
