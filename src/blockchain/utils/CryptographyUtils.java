package blockchain.utils;

import blockchain.Message;

import java.security.*;

public class CryptographyUtils {

    public static boolean verifySignature(Message message) {
        Signature signature;
        try {
            signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(message.getPublicKey());
            String data = message.getSender() + message.getTextMessage() + message.getDateTime();
            signature.update(data.getBytes());
            return signature.verify(message.getSignature());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] generateSignature(String data, PrivateKey privateKey) {
        try {
            Signature generator = Signature.getInstance("SHA1withRSA");
            generator.initSign(privateKey);
            generator.update(data.getBytes());
            return generator.sign();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
