package executors;

import java.util.concurrent.Callable;

public class UppercaseConverter implements Callable<String> {
    private String input;

    public UppercaseConverter(String input) {
        this.input = input;
    }


    @Override
    public String call() throws Exception {
        Thread.sleep(20000);
        return input.toUpperCase();
    }
}
