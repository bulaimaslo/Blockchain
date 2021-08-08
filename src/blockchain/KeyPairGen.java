package blockchain;

import java.security.*;

public class KeyPairGen {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;


    public KeyPairGen() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        KeyPair keys = keyGen.generateKeyPair();

        this.privateKey = keys.getPrivate();
        this.publicKey = keys.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}