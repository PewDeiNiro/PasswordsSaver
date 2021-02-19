package sample;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class RSA implements Serializable{

    private Keys keys;
    private final String PATH = "keys.ser";

    public RSA(){
        if (checkEmptyFile(PATH)) {
            generateKeys();
        }
        else{
            try{
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PATH));
                keys = (Keys)inputStream.readObject();
            }catch (IOException | ClassNotFoundException e){
                System.out.println("Serializable exception");
                generateKeys();
            }
        }
    }

    private Boolean checkEmptyFile(String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            boolean result = true;
            while (reader.ready()) {
                String string = reader.readLine();
                if (!string.trim().equals("")) {
                    result = false;
                    break;
                }
            }
            reader.close();
            return result;
        } catch (IOException e){
            return null;
        }
    }

    public void generateKeys(){

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();

            PublicKey publicKey = pair.getPublic();
            PrivateKey privateKey = pair.getPrivate();
            keys = new Keys(publicKey, privateKey);
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(PATH));
            outputStream.writeObject(keys);
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("GenerateKey error");
        }

    }

    public String encrypt(String text){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keys.getPublicKey());
            byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = cipher.doFinal(textBytes);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            System.out.println("Encrypt Error");
            return "";
        }

    }

    private long[] bytesToLongs(byte[] arr){
        return null;
    }

    public String decrypt(String text){
        byte[] encrypted = Base64.getDecoder().decode(text);
        try {
            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, keys.getPrivateKey());

            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.out.println("Decrypt Error");
            return "";
        }

    }


}
