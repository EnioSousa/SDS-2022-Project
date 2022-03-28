package BlockChain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class BlockHeaderTest {

    private static BlockHeader blockHeader;

    @BeforeAll
    public static void setup() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] prevHash = digest.digest("prevHash".getBytes(StandardCharsets.UTF_8));
        byte[] txRootHash = digest.digest("txRootHash".getBytes(StandardCharsets.UTF_8));

        BlockHeaderTest.blockHeader = new BlockHeader(1, 9999L, 6, prevHash, txRootHash, 0);

        assertNotNull(BlockHeaderTest.blockHeader, "Block was not created");
    }

    @Test
    void testToString() {

        String stringBuilder = """
                Block{
                version=1
                unixTimestamp=9999
                difficulty=6
                prevHash=5D25DEC8523C8B3726E073A9946AA99EB8A509BDDD94C649B209C81B143A95D5
                txRootHash=10EFBBACD7CC9FDD971133AF8458F28CE9C5CB6851AC70216F936494CE7B8DAF
                nonce=0
                }""";

        assertEquals(stringBuilder, BlockHeaderTest.blockHeader.toString());
    }

    @Test
    void validHash() {
        assertAll(
                () -> assertTrue(BlockHeader.validHash(new byte[]{(byte) 0x01}, 7)),
                () -> assertTrue(BlockHeader.validHash(new byte[]{(byte) 0x02}, 6)),
                () -> assertFalse(BlockHeader.validHash(new byte[]{(byte) 0x02}, 7)),
                () -> assertTrue(BlockHeader.validHash(new byte[]{(byte) 0x75}, 1)),
                () -> assertFalse(BlockHeader.validHash(new byte[]{(byte) 0x02}, 7)),
                () -> assertTrue(BlockHeader.validHash(new byte[]{(byte) 0x00, (byte) 0x01}, 15))
        );
    }

    @Test
    void getHash() throws NoSuchAlgorithmException {
        byte[] hash = BlockHeaderTest.blockHeader.getHash();

        assertTrue(BlockHeaderTest.blockHeader.validHash(hash));

        System.out.println(HashAlgorithm.byteToHex(hash));
        System.out.println(BlockHeaderTest.blockHeader.getNonce());
    }

}