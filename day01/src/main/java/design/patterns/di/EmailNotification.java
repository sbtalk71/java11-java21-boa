package design.patterns.di;

public class EmailNotification implements Notification{
    @Override
    public void send(String message) {
        System.out.println(message);
    }
}
