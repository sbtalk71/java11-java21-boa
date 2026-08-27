package completable.future;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo01 {

    public static void main(String[] args) throws Exception {
        CompletableFuture<Void> asyncResult=CompletableFuture.runAsync(
                ()-> System.out.println(Thread.currentThread().getName())
        );

        asyncResult.get();
    }
}
