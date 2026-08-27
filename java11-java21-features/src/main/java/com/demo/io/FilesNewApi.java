package com.demo.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class FilesNewApi {
    public static void main(String[] args) throws IOException {
        String myContent= """
                4,Ranga, hyderabad,45000
                5, Dinesh, Hyderabad, 67000
                6, Shantanu, Chennai, 89000
                """;
      //  Files.writeString(Path.of("employee.txt"),myContent, StandardOpenOption.WRITE,StandardOpenOption.APPEND);

       String data= Files.readString(Path.of("employee.txt"));
       // System.out.println(data);

        Files.lines(Path.of("employee.txt")).forEach(System.out::println);

    }
}
