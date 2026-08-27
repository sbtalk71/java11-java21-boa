package concurrent.collections;

import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteDemo {

    public static void main(String[] args) throws InterruptedException {

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        list.add("Java");
        list.add("Spring");

        // Thread continuously reading
        Thread reader = new Thread(() -> {
            for (int i = 0; i < 5; i++) {

                System.out.println("Reader: " + list);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Thread modifying the list
        Thread writer = new Thread(() -> {

            try {
                Thread.sleep(700);

                list.add("Kafka");
                System.out.println("Writer added Kafka");

                Thread.sleep(700);
                list.add("Docker");
                System.out.println("Writer added Docker");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();

        System.out.println("Final list: " + list);
    }
}