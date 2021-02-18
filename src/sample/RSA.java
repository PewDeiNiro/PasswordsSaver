package sample;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class RSA implements Serializable{

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSA(){
        generateKeys();
    }

    public void generateKeys(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();

            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("GenerateKey error");
        }

    }

    public String encrypt(String text){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = cipher.doFinal(textBytes);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            System.out.println("Encrypt Error");
            return "";
        }

    }

    public String decrypt(String text){
        byte[] encrypted = Base64.getDecoder().decode(text);

        try {
            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.out.println("Decrypt Error");
            return "";
        }

    }


}
