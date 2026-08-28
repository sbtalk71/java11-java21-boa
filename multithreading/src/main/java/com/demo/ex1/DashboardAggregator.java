package com.demo.ex1;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DashboardAggregator {

    // A standard, thread-safe HttpClient shared across tasks
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // 1. Create a Virtual Thread Per Task Executor
        // This does NOT pool threads. It seamlessly spins up a lightweight virtual thread for every submission.
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            // 2. Define the blocking I/O tasks
            Callable<String> fetchProfile = () -> fetchMockData("https://httpbin.org");
            Callable<String> fetchOrders = () -> fetchMockData("https://httpbin.org");
            Callable<String> fetchRecommendations = () -> fetchMockData("https://httpbin.org");

            System.out.println("Submitting tasks to Virtual Threads...");

            // 3. Submit tasks concurrently
            List<Future<String>> futures = executor.invokeAll(List.of(fetchProfile, fetchOrders, fetchRecommendations));

            // 4. Gather results (blocking operations on the virtual thread layer)
            String profile = futures.get(0).get();
            String orders = futures.get(1).get();
            String recommendations = futures.get(2).get();

            System.out.println("\n--- Dashboard Data Compiled ---");
            System.out.println("Profile Status: Loaded");
            System.out.println("Orders Status: Loaded");
            System.out.println("Recommendations Status: Loaded");

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("Total execution time: %d ms\n", (endTime - startTime));
    }

    // Simulates a blocking HTTP network call
    private static String fetchMockData(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        // The virtual thread blocks right here, yielding its OS carrier thread to someone else
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
