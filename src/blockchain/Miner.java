package blockchain;

import blockchain.utils.CryptographyUtils;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Miner implements Runnable {

    private final Blockchain blockchain;
    private final String minerID;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Miner(Blockchain blockchain, String minerID) {
        this.blockchain = blockchain;
        this.minerID = minerID;
        initializeKeys();
    }

    @Override
    public void run() {

        while (!blockchain.isCompleted()) {
            Block lastBlock = blockchain.getLastBlock();

            Block block = new Block(minerID, lastBlock);
            addMessage(block);

            blockchain.acceptBlock(block);
        }
    }

    void initializeKeys() {
        final KeyPairGen keys;
        try {
            keys = new KeyPairGen();
            this.privateKey = keys.getPrivateKey();
            this.publicKey = keys.getPublicKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    void addMessage(Block block) {
        String message = "some message";

        //for now sign just a message
        byte[] signature = CryptographyUtils.generateSignature(message, privateKey);
        block.setMessage(new Message(this.minerID, message, signature, publicKey));
    }
}
