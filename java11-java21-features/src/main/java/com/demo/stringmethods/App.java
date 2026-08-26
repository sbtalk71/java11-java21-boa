package com.demo.stringmethods;

public class App {
    public static void main(String[] args) {
        String input="{\n" +
                "\"userId\": 1,\n" +
                "\"id\": 1,\n" +
                "\"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
                "\"body\": \"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"\n" +
                "}";


    int userId=101;
    int id=101;
    String requestData= """
            {
            "userId": %d,
            "id": %d,
            "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
            "body": "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
            }
            """.formatted(userId,id);

        System.out.println(requestData);
    }
}
