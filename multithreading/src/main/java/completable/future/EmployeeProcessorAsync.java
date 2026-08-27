package completable.future;

import dataset.Data;

import java.util.concurrent.CompletableFuture;

public class EmployeeProcessorAsync {
    public static void main(String[] args) throws Exception{
        CompletableFuture<Void> asyncResult= CompletableFuture
                .supplyAsync(()-> Data.employees)
                .thenApplyAsync(employees -> employees.stream().filter(emp->emp.salary()>85000))
                .thenAcceptAsync(employeeStream->employeeStream.forEach(System.out::println));

        asyncResult.get();
    }
}
