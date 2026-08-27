package executors;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledThredPoolDemo {
    public static void main(String[] args) throws Exception {
        ScheduledExecutorService schedular = Executors.newScheduledThreadPool(4);

        schedular.schedule(() -> {
            System.out.println("One Time Task " + LocalTime.now() + " | " + Thread.currentThread());
        }, 5, TimeUnit.SECONDS);

        ScheduledFuture<?> future = schedular.scheduleAtFixedRate(() -> {
            System.out.println("Periodic Task " + LocalTime.now() + " | " + Thread.currentThread());}, 2, 2, TimeUnit.SECONDS);

        Thread.sleep(10000);
        future.cancel(false);
        schedular.shutdown();

    }
}
