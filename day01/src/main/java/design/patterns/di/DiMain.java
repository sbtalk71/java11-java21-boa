package design.patterns.di;

public class DiMain {
    public static void main(String[] args) {
        Notification notification=new EmailNotification();

        OrderService orderService=new OrderService(notification);
        orderService.placeOrder();
    }
}
