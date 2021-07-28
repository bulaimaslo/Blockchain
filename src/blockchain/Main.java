package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

        final int BLOCKS_TO_GENERATE = 20;
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

/*
        Blockchain blockchain = new Blockchain(3);
        String filename = "blockchain.data";

        Blockchain blockchain = (Blockchain) SerializationUtils.deserialize(filename);

        SerializationUtils.serialize(blockchain, filename);
        Blockchain b1 = (Blockchain) SerializationUtils.deserialize(filename);
        assert b1 != null;

        try {
        FileWriter myWriter = new FileWriter(filename);
        myWriter.write(b1.toString());
        myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }   */