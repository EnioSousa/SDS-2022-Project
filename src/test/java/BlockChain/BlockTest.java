package BlockChain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    private static BlockHeader blockHeader;

    @BeforeAll
    public static void setup() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] prevHash = digest.digest("prevHash".getBytes(StandardCharsets.UTF_8));
        byte[] txRootHash = digest.digest("txRootHash".getBytes(StandardCharsets.UTF_8));

        BlockTest.blockHeader = new BlockHeader(1, 9999, 6, prevHash, txRootHash, 0);

        assertNotNull(BlockTest.blockHeader, "Block was not created");
    }

    @Test
    void testToString() {
        StringBuilder stringBuilder = new StringBuilder("Block{");

        stringBuilder.append("\nversion=1");
        stringBuilder.append("\nunixTimestamp=9999");
        stringBuilder.append("\ndifficulty=6");
        stringBuilder.append("\nprevHash=5D25DEC8523C8B3726E073A9946AA99EB8A509BDDD94C649B209C81B143A95D5");
        stringBuilder.append("\ntxRootHash=10EFBBACD7CC9FDD971133AF8458F28CE9C5CB6851AC70216F936494CE7B8DAF");
        stringBuilder.append("\nnonce=0");
        stringBuilder.append("\n}");

        assertEquals(stringBuilder.toString(), BlockTest.blockHeader.toString());
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
        byte[] hash = BlockTest.blockHeader.getHash();

        assertTrue(BlockTest.blockHeader.validHash(hash));

        System.out.println(BlockHeader.byteToHex(hash));
        System.out.println(BlockTest.blockHeader.getNonce());
    }

}