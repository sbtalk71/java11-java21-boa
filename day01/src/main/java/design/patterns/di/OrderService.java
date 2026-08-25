package design.patterns.di;

public class OrderService {

    private Notification notification;

    public OrderService(Notification notification) {
        this.notification = notification;
    }

    public void placeOrder(){
        notification.send("Order Placed");
    }
}
