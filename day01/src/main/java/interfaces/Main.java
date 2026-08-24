package interfaces;

interface PaymentService {

    void pay(double amount);

    default void process(double amount) {
        validate(amount);
        log(amount);
        pay(amount);
    }

    static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    private void validate(double amount) {
        if (!isValidAmount(amount)) {
            throw new IllegalArgumentException(
                "Amount must be greater than zero"
            );
        }
    }

    private void log(double amount) {
        System.out.println("Processing payment: " + amount);
    }
}

class CreditCardPayment implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println("Paid using credit card: " + amount);
    }
}

public class Main {

    public static void main(String[] args) {

        PaymentService payment = new CreditCardPayment();

        payment.process(500);

        System.out.println(
            PaymentService.isValidAmount(500)
        );
    }
}