package BlockChain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    private static Block block;

    @BeforeAll
    public static void setup() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] prevHash = digest.digest("prevHash".getBytes(StandardCharsets.UTF_8));
        byte[] txRootHash = digest.digest("txRootHash".getBytes(StandardCharsets.UTF_8));

        BlockTest.block = new Block(1, 9999, 6, prevHash, txRootHash, 0);

        assertNotNull(BlockTest.block, "Block was not created");
    }

    @Test
    void testToString() {
        StringBuilder stringBuilder = new StringBuilder("Block{");

        stringBuilder.append("\nversion=1");
        stringBuilder.append("\nunixTimestamp=9999");
        stringBuilder.append("\ndifficulty=12");
        stringBuilder.append("\nprevHash=5D25DEC8523C8B3726E073A9946AA99EB8A509BDDD94C649B209C81B143A95D5");
        stringBuilder.append("\ntxRootHash=10EFBBACD7CC9FDD971133AF8458F28CE9C5CB6851AC70216F936494CE7B8DAF");
        stringBuilder.append("\nnonce=0");
        stringBuilder.append("\n}");

        assertEquals(stringBuilder.toString(), BlockTest.block.toString());
    }

    @Test
    void validHash() {
        assertAll(
                () -> assertTrue(Block.validHash(new byte[]{(byte) 0x01}, 7)),
                () -> assertTrue(Block.validHash(new byte[]{(byte) 0x02}, 6)),
                () -> assertFalse(Block.validHash(new byte[]{(byte) 0x02}, 7)),
                () -> assertTrue(Block.validHash(new byte[]{(byte) 0x75}, 1)),
                () -> assertFalse(Block.validHash(new byte[]{(byte) 0x02}, 7)),
                () -> assertTrue(Block.validHash(new byte[]{(byte) 0x00, (byte) 0x01}, 15))
        );
    }

    @Test
    void getHash() throws NoSuchAlgorithmException {
        byte[] hash = BlockTest.block.getHash();

        assertTrue(BlockTest.block.validHash(hash));

        System.out.println(Block.byteToHex(hash));
        System.out.println(BlockTest.block.getNonce());
    }

}