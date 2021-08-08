package blockchain;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

public class Message implements Serializable {

    private static final long serialVersionUID = 12L;

    private final String sender;
    private final String textMessage;
    private final long dateTime;
    private final byte[] signature;
    private final PublicKey publicKey;

    public Message(String sender, String textMessage,
                   byte[] signature, PublicKey publicKey) {
        this.sender = sender;
        this.textMessage = textMessage;
        this.dateTime = new Date().getTime();
        this.signature = signature;
        this.publicKey = publicKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public long getDateTime() {
        return dateTime;
    }

    public String getSender() {
        return sender;
    }


    @Override
    public String toString() {
        return String.format("%s: %s", sender, textMessage);
    }
}