package com.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpGETClient {
    public static void main(String[] args) throws Exception{

        var httpClient=HttpClient.newHttpClient();

        var request= HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .GET()
                .build();
        var response=httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status Code : "+response.statusCode());
        System.out.println(response.body());

    }
}
