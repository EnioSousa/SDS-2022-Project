package BlockChain;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAlgorithm {
    /**
     * Function hashes a given string with SHA-256 algorithm
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
     * @param hash The byte array containing the data
     * @return the hash in byte array
     * @throws NoSuchAlgorithmException Exception if the hash algorithm is not foud
     */
    public static byte[] generateHash(byte[] hash) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        return digest.digest(hash);
    }

    /**
     * This function combines two hash in to one and passes the hash algorithm again
     * @param hash0 The first hash
     * @param hash1 The second hash
     * @return The combined hash
     * @throws NoSuchAlgorithmException Exception if hash algorithm is not found
     */
    public static byte[] generateHash(byte[] hash0, byte[] hash1) throws NoSuchAlgorithmException {
        byte[] byteArray = new byte[hash0.length + hash1.length];

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        byteBuffer.put(hash0);
        byteBuffer.put(hash1);

        return generateHash(byteBuffer.array());
    }

    /**
     * Function will translate a byte array into a Hex format
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

    /**
     * Converts a int into to a byte array
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
}
