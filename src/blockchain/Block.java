package blockchain;

import blockchain.utils.StringUtil;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

import static blockchain.utils.StringUtil.applySha256;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final String minerId;
    private final String previousBlockHash;
    private final Block previousBlock;
    private Message message;
    private final int zeros;
    private final String hash;
    private final String magic;
    private final long timestamp;
    private long creationTime;

    public Block(String minerId, Block previousBlock) {

        if (previousBlock == null) {
            this.minerId = minerId;
            this.id = 1;
            this.timestamp = new Date().getTime();
            this.previousBlock = null;
            this.zeros = 6;
            this.previousBlockHash = getPreviousBlockHash();
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

    public String getPreviousBlockHash() {
        if (this.previousBlock == null) {
            return "0";
        } else {
            return this.previousBlock.getHash();
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

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getZeros() {
        return zeros;
    }

    public Message getMessage() {
        return message;
    }

    private String findMagic(String s, int zeros) {
        Random rand = new Random();
        Instant startTime = Instant.now();
        int r = 0;

        while (!applySha256(s + r).startsWith("0".repeat(zeros))) {
            r = rand.nextInt();
        }
        this.creationTime = Duration.between(startTime, Instant.now()).toSeconds();

        return String.valueOf(r);
    }

    //adjust difficulty of the puzzle
    private int numberOfZerosForNext(long creationTime) {
        int LOWER_BOUND_DURATION = 10;
        int HIGHER_BOUND_DURATION = 60;

        if (creationTime < LOWER_BOUND_DURATION) return 1;
        if (creationTime > HIGHER_BOUND_DURATION) return -1;
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
                "Block data: " + '\n' + this.message + '\n' +
                "Block was generating for " + this.creationTime + " seconds");
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
                "Block data: " + '\n' + this.message + '\n' +
                "Block was generating for " + this.creationTime + " seconds");
    }
}
