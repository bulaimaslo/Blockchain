package blockchain;

import java.io.*;
import java.util.ArrayList;

public class Blockchain implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean isCompleted = false;
    private int size = 0;
    private volatile int zeros;
    private final int toGenerate;
    private final ArrayList<Block> blockchain = new ArrayList<>();
    private volatile Block lastBlock = null;


    public Blockchain(int toGenerate) {
        this.toGenerate = toGenerate;
    }

    @Override
    public String toString() {
        return "Blockchain{" +
                "size=" + size +
                ", zeros=" + zeros +
                ", blockchain=" + blockchain +
                '}';
    }

    public boolean isValid() {
        if (blockchain.isEmpty()) {
            return true;
        }

        Block lastBlock = blockchain.get(blockchain.size() - 1);
        String previousLastBlockHash = lastBlock.getPreviousBlockHash();
        for (int i = blockchain.size() - 2; i >= 0; i--) {
            if (blockchain.get(i).getHash().equals(previousLastBlockHash)) {
                previousLastBlockHash = blockchain.get(i).getPreviousBlockHash();
                lastBlock = blockchain.get(i);
            } else
            {
                return false;
            }
        }
        return lastBlock.getPreviousBlockHash().equals("0");
    }

//    private void writeObject(ObjectOutputStream oos) throws Exception {
//        oos.defaultWriteObject();
//    }
//
//    private void readObject(ObjectInputStream ois) throws Exception {
//        ois.defaultReadObject();
//    }


    public synchronized void acceptBlock(Block block) {

        if (blockIsValid(block)) {
            blockchain.add(block);
            this.lastBlock = block;
            block.printBlock();
            adjustZeros(block);
            this.size++;

            if (this.size == this.toGenerate) {
                isCompleted = true;
                System.out.println("done");
            }
        }
    }

    private synchronized boolean blockIsValid(Block block) {
        return zerosMatch(block);       //TODO add checking hashes
    }

    private boolean zerosMatch(Block block) {
        return block.getHash().startsWith("0".repeat(this.zeros));
    }

    private synchronized void adjustZeros(Block block) {

        long creationTime = block.getCreationTime();   //in seconds
        if (creationTime < 5) {
            this.zeros += 1;
            System.out.println("N was increased" + '\n');
        } else if (creationTime > 30) {
            this.zeros--;
            System.out.println("N was decreased" + '\n');
        } else {
            System.out.println("N stays the same" + '\n');
        }
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public synchronized Block getLastBlock() {
        return lastBlock;
    }

}