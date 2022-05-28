package Utils;

import java.security.*;
import java.lang.*;

public class PublicPrivateKey {


    //Creating KeyPair generator object
    KeyPairGenerator keyPairGen;

    {
        try {
            keyPairGen = KeyPairGenerator.getInstance("DSA");
            keyPairGen.initialize(2048);

            //Generating the pair of keys
            KeyPair pair = keyPairGen.generateKeyPair();

            //Getting the private key from the key pair
            PrivateKey privKey = pair.getPrivate();

            //Getting the public key from the key pair
            PublicKey publicKey = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {

        }
    }
}
