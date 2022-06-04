package BlockChain;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HexFormat;

public class HashAlgorithm {
    /**
     * Function hashes a given string with SHA-256 algorithm
     *
     * @param string String to hash
     * @return A byte array containing the hash
     * @throws NoSuchAlgorithmException Exception if the hsh algorithm is not found
     */
    public static byte[] generateHash(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        return digest.digest(string.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Given a byte array, this method generates a hash
     *
     * @param byteArray The byte array containing the data
     * @return the hash in byte array
     * @throws NoSuchAlgorithmException Exception if the hash algorithm is
     *                                  not found
     */
    public static byte[] generateHash(byte[] byteArray) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        return digest.digest(byteArray);
    }

    /**
     * Hash a given byte array to a specific size
     *
     * @param byteArray The byte array to hash
     * @param size      The size in bits
     * @return The hash
     * @throws NoSuchAlgorithmException Exception if the hash algorithm is
     *                                  not found
     */
    public static byte[] generateHash(byte[] byteArray, int size) throws NoSuchAlgorithmException {
        byte[] hash = generateHash(byteArray);

        return Arrays.copyOfRange(hash, 0, size / 8);
    }

    /**
     * This function combines two hash in to one and passes the hash algorithm again
     *
     * @param byteArray0 The first hash
     * @param byteArray1 The second hash
     * @return The combined hash
     * @throws NoSuchAlgorithmException Exception if hash algorithm is not found
     */
    public static byte[] generateHash(byte[] byteArray0, byte[] byteArray1) throws NoSuchAlgorithmException {
        byte[] byteArray = new byte[byteArray0.length + byteArray1.length];

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        byteBuffer.put(byteArray0);
        byteBuffer.put(byteArray1);

        return generateHash(byteBuffer.array());
    }

    /**
     * Function will translate a byte array into a Hex format
     *
     * @param byteArray byte array
     * @return String representation in hex of the byte array
     */
    public static String byteToHex(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : byteArray) {
            stringBuilder.append(String.format("%02X", b));
        }

        return stringBuilder.toString();
    }

    public static byte[] hexToByte(String string) {
        return HexFormat.of().parseHex(string);
    }

    public static int byteToInt(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);

    }

    /**
     * Converts a int into to a byte array
     *
     * @param value the int value
     * @return the byte array
     */
    public static byte[] intToByte(int value) {
        return new byte[]{
                (byte) ((value >> 24) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 0) & 0xff)
        };
    }

    /**
     * converts a long into a byte array
     *
     * @param value the long value
     * @return the corresponding byte array
     */
    public static byte[] longToByte(long value) {
        return new byte[]{
                (byte) ((value >> 56) & 0xff),
                (byte) ((value >> 48) & 0xff),
                (byte) ((value >> 40) & 0xff),
                (byte) ((value >> 32) & 0xff),
                (byte) ((value >> 24) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 0) & 0xff)};
    }

    public static boolean validHash(byte[] hash, int difficulty) {
        int numZero = difficulty;
        int numByteZero = numZero / 8;

        int i;

        // Here we check if the byte is 0
        for (i = 0; i < numByteZero && i < hash.length; i++) {
            if (hash[i] != 0x0) {
                return false;
            }
        }

        // Check if we still have to check more of the byte array, aka hash
        if (i >= hash.length)
            return true;

        // Here we check the number of zero bits in a byte
        byte value = switch (numZero % 8) {
            case 7 -> (byte) 0xFE;
            case 6 -> (byte) 0xFC;
            case 5 -> (byte) 0xF8;
            case 4 -> (byte) 0xF0;
            case 3 -> (byte) 0xE0;
            case 2 -> (byte) 0xC0;
            case 1 -> (byte) 0x80;
            default -> (byte) 0xFF;
        };

        return (hash[i] & value) == 0x00;
    }

    /**
     * Generates a random bte array of length size
     *
     * @param size The size of the byte array
     * @return The random byte array
     */
    public static byte[] generateRandomByteArray(int size) {
        SecureRandom random = new SecureRandom();

        byte[] byteArray = new byte[size];

        random.nextBytes(byteArray);

        return byteArray;
    }

    public static void copyNBits(byte[] from, byte[] to, int nBits) {
        // Copy bytes
        for (int i = 0; i < nBits / 8; i++) {
            to[i] = from[i];
        }

        // Copy bits
        if (nBits / 8 < from.length && nBits % 8 != 0) {
            byte bytePrefix = from[nBits / 8];
            byte byteSuffix = to[nBits / 8];

            switch (nBits % 8) {
                case 1 -> {
                    bytePrefix = (byte) (bytePrefix & (byte) 0x80);
                    byteSuffix = (byte) (byteSuffix & (byte) 0x01);
                }
                case 2 -> {
                    bytePrefix = (byte) (bytePrefix & (byte) 0xC0);
                    byteSuffix = (byte) (byteSuffix & (byte) 0x03);
                }
                case 3 -> {
                    bytePrefix = (byte) (bytePrefix & (byte) 0xE0);
                    byteSuffix = (byte) (byteSuffix & (byte) 0x05);
                }
                case 4 -> {
                    bytePrefix = (byte) (bytePrefix & (byte) 0xF0);
                    byteSuffix = (byte) (byteSuffix & (byte) 0x07);
                }
                case 5 -> {
                    bytePrefix = (byte) (bytePrefix & (byte) 0xF8);
                    byteSuffix = (byte) (byteSuffix & (byte) 0x17);
                }
                case 6 -> {
                    bytePrefix = (byte) (bytePrefix & (byte) 0xFC);
                    byteSuffix = (byte) (byteSuffix & (byte) 0x37);
                }
                case 7 -> {
                    bytePrefix = (byte) (bytePrefix & (byte) 0xFE);
                    byteSuffix = (byte) (byteSuffix & (byte) 0x57);
                }
                default -> {
                }
            }

            to[nBits / 8] = (byte) (bytePrefix | byteSuffix);
        }
    }
}
