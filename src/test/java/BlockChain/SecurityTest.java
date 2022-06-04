package BlockChain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {
    public static Security security00;
    public static Security security01;

    @BeforeAll
    public static void setUp() {
        security00 = new Security();
        security01 = new Security();

        security00.generateKeys("public","private");
        security01.generateKeys("public","private");
    }

    @Test
    void signData() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[] message = "Ola".getBytes(StandardCharsets.UTF_8);

        byte[] test = security00.signData(message);

        System.out.println(test);
    }

    @Test
    void verifyData() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        byte[] message = "Ola".getBytes(StandardCharsets.UTF_8);
        byte[] message01 = "Todos".getBytes(StandardCharsets.UTF_8);
        byte[] sign = security00.signData(message);


        assertAll(
                () -> assertTrue(security01.verifyData(message,sign,security00.getPublicKey())),
                () -> assertFalse(security01.verifyData(message01,sign,security00.getPublicKey())),
                () -> assertTrue(security00.verifyData(message,sign,security00.getPublicKey())),
                () -> assertFalse(security00.verifyData(message01,sign,security00.getPublicKey()))
        );
    }
}