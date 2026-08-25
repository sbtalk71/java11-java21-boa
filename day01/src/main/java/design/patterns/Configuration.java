package design.patterns;

public class Configuration {

    private static Configuration instance;

    private Configuration() {
        // Prevent external instantiation
    }

    public static Configuration getInstance() {

        if (instance == null) {
            instance = new Configuration();
        }

        return instance;
    }
}