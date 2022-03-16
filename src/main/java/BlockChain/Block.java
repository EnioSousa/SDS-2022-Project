package BlockChain;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * TODO: Improve performance on hash validation
 * TODO: Change the difficulty method
 */

/**
 *
 */
public class Block {
    /**
     * Version of the block generation protocol
     */
    private final int version;
    /**
     * Unix time frame for the current block generation
     */
    private final long unixTimestamp;
    /**
     * Difficulty for the hash
     */
    private final int difficulty;
    /**
     * The previous block hash
     */
    private final byte[] prevHash;
    /**
     * The transaction group hash
     */
    private final byte[] txRootHash;
    /**
     * nonce used in to compute the hash with certain characteristics
     */
    private int nonce;

    public Block(int version, long unixTimestamp, int difficulty, byte[] prevHash, byte[] txRootHash, int nonce) {
        this.version = version;
        this.prevHash = prevHash;
        this.difficulty = difficulty;
        this.txRootHash = txRootHash;
        this.unixTimestamp = unixTimestamp;
        this.nonce = nonce;
    }

    /**
     * Method will generate a nonce so that the hash can have certain characteristics
     *
     * @return Returns the hash found
     * @throws NoSuchAlgorithmException Throws exception is hash algorithm is not usable
     */
    public byte[] getHash() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash;

        // Byte array to use for the hashing function
        byte[] byteArray = new byte[4 + 4 + 4 + 32 + 32 + 4];
        // Buffer so we combine add the different bytes array
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        // Add different bytes arrays
        byteBuffer.put((byte) this.version);
        byteBuffer.put((byte) this.unixTimestamp);
        byteBuffer.put((byte) this.difficulty);
        byteBuffer.put(this.prevHash);
        byteBuffer.put(this.txRootHash);
        byteBuffer.put((byte) this.nonce);

        hash = digest.digest(byteBuffer.array());

        // Cycle will iterate until a hash with the predefined characteristic is found
        // i.e. we will increment the nonce until we find a hash that we want
        while (!validHash(hash)) {
            this.nonce++;
            byteBuffer.put(4 + 4 + 4 + 32 + 32, (byte) this.nonce);
            hash = digest.digest(byteBuffer.array());
        }

        return hash;
    }

    @Override
    public String toString() {
        return "Block{" +
                "\nversion=" + version +
                "\nunixTimestamp=" + unixTimestamp +
                "\ndifficulty=" + difficulty +
                "\nprevHash=" + byteToHex(prevHash) +
                "\ntxRootHash=" + byteToHex(txRootHash) +
                "\nnonce=" + nonce +
                "\n}";
    }

    public static String byteToHex(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : byteArray) {
            stringBuilder.append(String.format("%02X", b));
        }

        return stringBuilder.toString();
    }

    /**
     * Given a certain dificulty, this function checks if a given hash code has a certain
     * number of zeros at the beginning
     * @param hash hash to check
     * @param difficulty number of zero's in the prefix
     * @return True if the hash e valid, otherwise false
     */
    public static boolean validHash(byte[] hash, int difficulty) {
        int numZero = difficulty;
        int numByteZero = numZero / 8;

        int i;

        // Here we check if the byte is 0
        for( i = 0; i<numByteZero && i<hash.length; i++ ) {
            if ( hash[i] != 0x0) {
                return false;
            }
        }

        // Check if we still have to check more of the byte array, aka hash
        if ( i >= hash.length )
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
     * Checks if the hash is valid, i.e. has a certain number of zeros at the beginning
     *
     * @param hash the byte array containing the hash
     * @return True if the hash is valid, otherwise false
     */
    public boolean validHash(byte[] hash) {
        return validHash(hash, whichDifficulty());
    }

    /**
     * Returns how many bits in the prefix of the hash, we need to have
     *
     * @return Return the number of bits at zero
     */
    private int whichDifficulty() {
        return Math.min(this.difficulty, 256);
    }

    /**
     * Get the block version
     *
     * @return the block version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Get the block unix time
     *
     * @return block unix time
     */
    public long getUnixTimestamp() {
        return unixTimestamp;
    }

    /**
     * Get difficulty for the block
     *
     * @return difficulty for the block
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Get the previous blockchain hash
     *
     * @return the previous block hash
     */
    public byte[] getPrevHash() {
        return prevHash;
    }

    /**
     * Get the transactions group hash
     *
     * @return The transactions group hash
     */
    public byte[] getTxRootHash() {
        return txRootHash;
    }

    /**
     * Get the block nonce
     *
     * @return Get the block nonce
     */
    public int getNonce() {
        return nonce;
    }
}
