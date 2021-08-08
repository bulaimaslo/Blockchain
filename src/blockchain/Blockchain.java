package blockchain;

import blockchain.utils.CryptographyUtils;

import java.io.Serializable;
import java.util.LinkedList;

public class Blockchain implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean isCompleted = false;
    private int size = 0;
    private volatile int zeros;
    private final int toGenerate;
    private final LinkedList<Block> blockchain = new LinkedList<>();
    private volatile Block lastBlock = null;


    public Blockchain(int toGenerate) {
        this.toGenerate = this.size + toGenerate;
    }


    public synchronized void acceptBlock(Block block) {

        if (blockIsValid(block)) {
            blockchain.add(block);
            this.lastBlock = block;
            block.printBlock();
            adjustDifficulity(block);
            this.size++;

            if (this.size == this.toGenerate) {
                isCompleted = true;
            }
        }
    }

    private synchronized boolean blockIsValid(Block block) {
        return zerosMatch(block);
    }

    private boolean zerosMatch(Block block) {
        return block.getHash().startsWith("0".repeat(this.zeros));
    }

    private boolean validSignature(Block block) {
       return CryptographyUtils.verifySignature(block.getMessage());
    }

    private synchronized void adjustDifficulity(Block block) {
        int LOWER_BOUND = 5;
        int HIGHER_BOUND = 30;
        long creationTime = block.getCreationTime();   //in seconds
        if (creationTime < LOWER_BOUND) {
            this.zeros += 1;
            System.out.println("Puzzle difficulity increased" + '\n');
        } else if (creationTime > HIGHER_BOUND) {
            this.zeros--;
            System.out.println("Puzzle difficulity decreased" + '\n');
        } else {
            System.out.println("Puzzle difficulity stays the same" + '\n');
        }
    }

    //check validity of every block including message
    public boolean isValid() {
        if (blockchain.isEmpty()) {
            return true;
        }

        Block lastBlock = blockchain.get(blockchain.size() - 1);

        for (int i = blockchain.size() - 2; i >= 0; i--) {
            Block block = blockchain.get(i);
            if (blockIsValid(block) && validSignature(block)) {
                lastBlock = blockchain.get(i);
            } else {
                return false;
            }
        }

        return lastBlock.getPreviousBlockHash().equals("0");
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public synchronized Block getLastBlock() {
        return lastBlock;
    }

    @Override
    public String toString() {
        return "Blockchain{" +
                "size=" + size +
                ", zeros=" + zeros +
                ", blockchain=" + blockchain +
                '}';
    }
}
