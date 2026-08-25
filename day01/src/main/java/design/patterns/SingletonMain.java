package design.patterns;

import java.util.concurrent.CopyOnWriteArrayList;

public class SingletonMain {
    public static void main(String[] args) {

        Configuration c1= Configuration.getInstance();
        Configuration c2= Configuration.getInstance();

        System.out.println(c1==c2);
    }
}
