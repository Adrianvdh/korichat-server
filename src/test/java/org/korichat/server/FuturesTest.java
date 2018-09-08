package org.korichat.server;

import org.junit.Test;

import java.util.concurrent.*;

public class FuturesTest {


    @Test
    public void testFuture() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "DONE";
        });

        System.out.println(future.get());
    }

    @Test
    public void testCompletableFuture() {

        CompletableFuture<String> result;
    }
}
