package BlockChain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private static Transaction transaction;

    @BeforeAll
    public static void setUp() {
        transaction = new Transaction("enio".getBytes(StandardCharsets.UTF_8),
                "joao".getBytes(StandardCharsets.UTF_8),
                "car".getBytes(StandardCharsets.UTF_8),
                12000);
    }

    @Test
    void testToString() {
        String string = """
                Transaction{
                SourceEntity=656E696F
                DestEntity=6A6F616F
                ProductId=636172
                Type=bid
                }""";

        assertEquals(string, transaction.toString());
    }

    @Test
    void TestGetHash() throws NoSuchAlgorithmException {
        // Note: You can calculate the hash online, by using a converter with Hex values
        assertEquals("7F3A229D5D5EB9CE138A30B3C89CCB0DCBECB06DFD636F5DDE667B56B7CB8246",
                HashAlgorithm.byteToHex(transaction.getHash()));
    }
}