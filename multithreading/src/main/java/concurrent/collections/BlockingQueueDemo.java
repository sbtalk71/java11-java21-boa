package concurrent.collections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Integer> queue =
                new ArrayBlockingQueue<>(5);

        // Producer 1
        Thread producer1 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    queue.put(i);
                    System.out.println(
                            "Producer-1 produced: " + i
                    );
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Producer 2
        Thread producer2 = new Thread(() -> {
            for (int i = 101; i <= 110; i++) {
                try {
                    queue.put(i);
                    System.out.println(
                            "Producer-2 produced: " + i
                    );
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Consumer 1
        Thread consumer1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer value = queue.take();

                    System.out.println(
                            "Consumer-1 consumed: " + value
                    );

                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Consumer 2
        Thread consumer2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer value = queue.take();

                    System.out.println(
                            "Consumer-2 consumed: " + value
                    );

                    Thread.sleep(700);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer1.start();
        producer2.start();

        consumer1.start();
        consumer2.start();

        producer1.join();
        producer2.join();

        consumer1.join();
        consumer2.join();

        System.out.println("All processing completed");
    }
}