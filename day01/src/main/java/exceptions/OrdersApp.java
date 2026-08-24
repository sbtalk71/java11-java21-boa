package exceptions;

public class OrdersApp {
    void chargeCard(double amt) throws InsufficientFundException{
        if (amt <= 0) {
            throw new InsufficientFundException("Invalid amount");
        }
    }

    void validatePayment(double amt)  {
        try {
            chargeCard(amt);       // no try/catch — propagates
        } catch (InsufficientFundException e) {
            throw new RuntimeException(e);
        }
    }

    void processOrder(double amt) {
        validatePayment(amt);  // no try/catch — propagates
    }

    public static void main(String[] args) {
        try {
            new OrdersApp().processOrder(-50);
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Caught in main: " + e.getMessage());
        }

        System.out.printf("The Order Completed");
    }
    }
