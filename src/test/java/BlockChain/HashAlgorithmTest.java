package BlockChain;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HashAlgorithmTest {
    @Test
    void testByteToHex() {
        String string = "D32C10";

        assertEquals(string, HashAlgorithm.byteToHex(new byte[]{(byte) 0xD3, (byte) 0x2C, (byte) 0x10}));
    }

    @Test
    void testGenerateHash() throws NoSuchAlgorithmException {
        byte[] byteArray0 = HashAlgorithm.generateHash("Enio");
        byte[] byteArray1 = HashAlgorithm.generateHash(new byte[]{0x01, (byte) 0xFD});
        byte[] byteArray2 = HashAlgorithm.generateHash(new byte[]{0x01}, new byte[]{(byte) 0xFD});

        String hex0 = HashAlgorithm.byteToHex(byteArray0);
        String hex1 = HashAlgorithm.byteToHex(byteArray1);
        String hex2 = HashAlgorithm.byteToHex(byteArray2);

        assertAll(
                () -> assertEquals("87FD7989768AAD4FFC574593C8F6BEF8BA9023F2068306EE26F5A9E519E7D724", hex0),
                () -> assertEquals("61B712BAB581A8F4F7478DA0E44A2AE559BFC5A36C1FAC0FD1F092EF25CAC573", hex1),
                () -> assertEquals("61B712BAB581A8F4F7478DA0E44A2AE559BFC5A36C1FAC0FD1F092EF25CAC573", hex2)
        );

        byte[] hash0 =
                HashAlgorithm.generateHash("Enio".getBytes(StandardCharsets.UTF_8), 1 * 8);
        byte[] hash1 =
                HashAlgorithm.generateHash("Enio".getBytes(StandardCharsets.UTF_8), 2 * 8);
        byte[] hash2 =
                HashAlgorithm.generateHash("Enio".getBytes(StandardCharsets.UTF_8), 30 * 8);

        assertAll(
                () -> assertEquals(1, hash0.length),
                () -> assertEquals(2, hash1.length),
                () -> assertEquals(30, hash2.length)
        );
    }

    @Test
    void testIntToByte() {
        int[] intArray = {53223681, 312355, 123958, 3212332};
        String[] stringArray = {HashAlgorithm.byteToHex(HashAlgorithm.intToByte(intArray[0])),
                HashAlgorithm.byteToHex(HashAlgorithm.intToByte(intArray[1])),
                HashAlgorithm.byteToHex(HashAlgorithm.intToByte(intArray[2])),
                HashAlgorithm.byteToHex(HashAlgorithm.intToByte(intArray[3]))};

        assertAll(() -> assertEquals("032C2101", stringArray[0]),
                () -> assertEquals("0004C423", stringArray[1]),
                () -> assertEquals("0001E436", stringArray[2]),
                () -> assertEquals("0031042C", stringArray[3]));
    }

    @Test
    void testLongToByte() {
        long[] longArray = {53223681L, 312355L, 123958L, 32123321232323L};
        String[] stringArray = {HashAlgorithm.byteToHex(HashAlgorithm.longToByte(longArray[0])),
                HashAlgorithm.byteToHex(HashAlgorithm.longToByte(longArray[1])),
                HashAlgorithm.byteToHex(HashAlgorithm.longToByte(longArray[2])),
                HashAlgorithm.byteToHex(HashAlgorithm.longToByte(longArray[3]))};

        assertAll(() -> assertEquals("00000000032C2101", stringArray[0]),
                () -> assertEquals("000000000004C423", stringArray[1]),
                () -> assertEquals("000000000001E436", stringArray[2]),
                () -> assertEquals("00001D374B26ABC3", stringArray[3]));
    }

    @Test
    void hexToByte() {
        byte[] byteArray = {(byte) 0x01, (byte) 0x11, (byte) 0xAF};

        assertTrue(Arrays.equals(byteArray, HashAlgorithm.hexToByte("0111AF")));
    }
}