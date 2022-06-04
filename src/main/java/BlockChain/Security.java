package BlockChain;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class Security {

    private KeyPair keyPair;

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public PublicKey getPublicKey() {
        return keyPair == null ? null : keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair == null ? null : keyPair.getPrivate();
    }

    public void generateKeys(String publicFileName,
                             String privateFileName) {

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);

            setKeyPair(keyGen.generateKeyPair());

            // TODO: Save keys

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decrypt(byte[] in, PrivateKey key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {

        Cipher decryptCypher = Cipher.getInstance("RSA");
        decryptCypher.init(Cipher.DECRYPT_MODE, key);

        return decryptCypher.doFinal(in);

    }

    public byte[] decrypt(byte[] in) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {

        Cipher decryptCypher = Cipher.getInstance("RSA");
        decryptCypher.init(Cipher.DECRYPT_MODE, getPrivateKey());

        return decryptCypher.doFinal(in);
    }

    public byte[] encrypt(byte[] in, PublicKey key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        Cipher encryptCypher = Cipher.getInstance("RSA");
        encryptCypher.init(Cipher.ENCRYPT_MODE, key);

        return encryptCypher.doFinal(in);
    }

    public byte[] encrypt(byte[] in) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        Cipher encryptCypher = Cipher.getInstance("RSA");
        encryptCypher.init(Cipher.ENCRYPT_MODE, getPublicKey());

        return encryptCypher.doFinal(in);
    }

    public byte[] signData(byte[] in) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // TODO: Do sign stuff
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(getPrivateKey());

        //Update the privateSignature with the message we want to send
        privateSignature.update(in);

        //Sign the message we want to send
        byte[] signature = privateSignature.sign();

        return signature;
    }

    public boolean verifyData(byte[] in) {
        return true;
    }

    public boolean verifyTransaction(Transaction transaction) {
        return true;
    }
}
