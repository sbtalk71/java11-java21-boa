package concurrent.collections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerPattern {
    public static void main(String[] args) {
        // 1. Create a shared bounded BlockingQueue with a capacity of 5
        BlockingQueue<Integer> sharedQueue = new ArrayBlockingQueue<>(5);

        // 2. Create the Producer and Consumer instances
        Thread producerThread = new Thread(new Producer(sharedQueue), "Producer-Thread");
        Thread consumerThread = new Thread(new Consumer(sharedQueue), "Consumer-Thread");

        // 3. Start both threads
       producerThread.start();
        consumerThread.start();
    }
}

// --- PRODUCER CLASS ---
class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + " producing: " + i);
                
                // The put() method will block if the queue is full
                queue.put(i); 
                
                // Simulate variable production time
                Thread.sleep(500); 
            }
            
            // Send a Poison Pill (sentinel value) to signal the consumer to stop
            queue.put(-1); 
            System.out.println(Thread.currentThread().getName() + " finished production.");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// --- CONSUMER CLASS ---
class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // The take() method will block if the queue is empty
                Integer item = queue.take(); 
                
                // If poison pill is detected, exit the loop
                if (item == -1) { 
                    System.out.println(Thread.currentThread().getName() + " detected stop signal. Exiting.");
                    break;
                }
                
                System.out.println(Thread.currentThread().getName() + " consumed: " + item);
                
                // Simulate variable processing time (Consumer is slower than Producer)
                Thread.sleep(1000); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
