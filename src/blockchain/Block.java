package blockchain;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static blockchain.StringUtil.applySha256;

public class Block implements Serializable {
    private long creationTime;
    private final int id;
    private final String minerId;
    private static final long serialVersionUID = 1L;
    private final String previousBlockHash;
    private final Block previousBlock;


    private final int zeros;
    private final String hash;
    private final String magic;
    private final long timestamp;

    public String getPreviousBlockHash() {
        return previousBlock.getHash();
    }

    public Block(String minerId, Block previousBlock) {

        if (previousBlock == null) {
            this.minerId = minerId;
            this.id = 1;
            this.timestamp = new Date().getTime();
            this.previousBlock = null;
            this.zeros = 4;         //todo constant
            this.previousBlockHash = "0";   //todo constant
            this.magic = findMagic(this.id + this.timestamp + "0", this.zeros);
            this.hash = StringUtil.applySha256(this.id + this.timestamp + "0" + this.magic);

        } else {
            this.minerId = minerId;
            this.previousBlock = previousBlock;
            this.id = previousBlock.getId() + 1;
            this.timestamp = new Date().getTime();
            this.previousBlockHash = previousBlock.getHash();
            this.zeros = previousBlock.getZeros() + numberOfZerosForNext(previousBlock.getCreationTime());
            this.magic = findMagic(this.id + this.timestamp + previousBlock.getHash(), this.zeros);
            this.hash = StringUtil.applySha256(this.id + this.timestamp + previousBlock.getHash() + this.magic);
        }
    }

    public long getCreationTime() {
        return creationTime;
    }

    public int getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public int getZeros() {
        return zeros;
    }

    private String findMagic(String s, int zeros) {
        Instant startTime = Instant.now();
        int i = 0;

        while (!applySha256(s + i).startsWith("0".repeat(zeros))) {
            i++;
        }
        this.creationTime = Duration.between(startTime, Instant.now()).toSeconds();

        return String.valueOf(i);
    }

    @Override
    public String toString() {
        return ("Block:\n" +
                "Created by miner: " + this.minerId + '\n' +
                "Id: " + this.id + '\n' +
                "Timestamp: " + this.timestamp + '\n' +
                "Magic number: " + this.magic + '\n' +
                "Hash of the previous block:" + '\n' +
                this.previousBlockHash + '\n' +
                "Hash of the block:" + '\n' +
                this.hash + '\n' +
                "Block was generating for " + this.creationTime + " seconds");
    }

    private int numberOfZerosForNext(long creationTime) {
        if (creationTime < 10) return 1;
        if (creationTime > 60 && this.id != 1) return -1;
        return 0;
    }

    public void printBlock() {
        System.out.println("Block:\n" +
                "Created by miner: " + this.minerId + '\n' +
                "Id: " + this.id + '\n' +
                "Timestamp: " + this.timestamp + '\n' +
                "Magic number: " + this.magic + '\n' +
                "Hash of the previous block:" + '\n' +
                this.previousBlockHash + '\n' +
                "Hash of the block:" + '\n' +
                this.hash + '\n' +
                "Block was generating for " + this.creationTime + " seconds");
    }

}
