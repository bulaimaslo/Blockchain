package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
        final int BLOCKS_TO_GENERATE = 10;

        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
        Blockchain blockchain = new Blockchain(BLOCKS_TO_GENERATE);

        for (int i = 0; i < POOL_SIZE; i++) {
            executor.submit(new Miner(blockchain, String.valueOf(i)));
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(120, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println(blockchain.isValid());
    }
}
