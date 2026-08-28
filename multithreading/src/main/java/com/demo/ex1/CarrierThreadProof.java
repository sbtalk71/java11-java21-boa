package com.demo.ex1;

import java.util.concurrent.Executors;

public class CarrierThreadProof {
    public static void main(String[] args) throws InterruptedException {
        
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Runnable target=() -> {
                // 1. Check thread state before blocking
                System.out.printf("Before I/O block: %s\n", Thread.currentThread());

                try {
                    // 2. Perform a blocking action (forces unmounting)
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 3. Check thread state after waking up
                System.out.printf("After I/O block:  %s\n", Thread.currentThread());
            };
            for(int i=0;i<5;i++){
                executor.submit(target);
            }

            
        }
    }
}
