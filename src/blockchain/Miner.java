package blockchain;

public class Miner implements Runnable {

    private final Blockchain blockchain;
    private final String minerID;

    public Miner(Blockchain blockchain, String minerID) {
        this.blockchain = blockchain;
        this.minerID = minerID;
    }

    @Override
    public void run() {
        while (!blockchain.isCompleted()) {
            Block lastBlock = blockchain.getLastBlock();

            Block block = new Block(minerID, lastBlock);
            blockchain.acceptBlock(block);
        }
    }
}
