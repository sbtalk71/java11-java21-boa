package com.demo.vardemo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpPOSTClient {
    public static void main(String[] args) throws Exception{
        int userId=113;
        int id=113;
        String requestData= """
            {
            "userId": %d,
            "id": %d,
            "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
            "body": "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
            }
            """.formatted(userId,id);

        var httpClient=HttpClient.newHttpClient();

        var request= HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .POST(HttpRequest.BodyPublishers.ofString(requestData))
                .build();
        var response=httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status Code : "+response.statusCode());
        System.out.println(response.body());


    }
}
