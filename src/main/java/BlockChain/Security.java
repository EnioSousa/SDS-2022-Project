package BlockChain;

import java.security.*;

public class Security {

    private KeyPair keyPair;

    Security() {
        // TODO: Change to proper file names
        generateKeys(null, null);
    }

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

    public boolean verifyData(byte[] data, byte[] signature, PublicKey key) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(key);

        sig.update(data);

        return sig.verify(signature);

    }

    public boolean verifyTransaction(Transaction transaction) {
        return true;
    }
}