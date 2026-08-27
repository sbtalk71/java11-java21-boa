package concurrent.collections;

import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListMapDemo {

    public static void main(String[] args) throws InterruptedException {

        ConcurrentSkipListMap<Integer, String> employees =
                new ConcurrentSkipListMap<>();

        employees.put(103, "Raj");
        employees.put(101, "Amit");
        employees.put(105, "John");

        Thread reader = new Thread(() -> {

            for (int i = 0; i < 5; i++) {

                System.out.println(
                    "Reader: " + employees
                );

                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread writer = new Thread(() -> {

            try {
                Thread.sleep(500);

                employees.put(102, "Priya");
                System.out.println("Added 102");

                Thread.sleep(500);

                employees.put(104, "David");
                System.out.println("Added 104");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();

        System.out.println("Final map: " + employees);
    }
}