package sample;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Keys implements Serializable {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Keys(PublicKey publicKey, PrivateKey privateKey){
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
