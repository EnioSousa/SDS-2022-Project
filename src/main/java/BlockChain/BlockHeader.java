package BlockChain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/*
 * TODO: Improve performance on hash validation
 * TODO: Change the difficulty method
 */

/**
 * class is responsible for generating a block. The calculations for the validation
 * of the transactions and other stuff, should be done outside this class. This class
 * only generates the nonce that will generate a hash with certain characteristics
 */
public class BlockHeader {
    /**
     * Logger
     */
    public static Logger LOGGER = LogManager.getLogger(BlockHeader.class);
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
    private final byte[] merkleTreeHash;
    /**
     * nonce used in to compute the hash with certain characteristics
     */
    private int nonce;

    public BlockHeader(int version, long unixTimestamp, int difficulty, byte[] prevHash, byte[] merkleTreeHash, int nonce) {
        this.version = version;
        this.prevHash = prevHash;
        this.difficulty = difficulty;
        this.merkleTreeHash = merkleTreeHash;
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

        // Size of all the fields except the nonce
        int baseSize = 4 + 8 + 4 + 32 + 32;

        // Byte array to use for the hashing function
        byte[] byteArray = new byte[baseSize + 4];
        // Buffer so we combine add the different bytes array
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        // Add different bytes arrays
        byteBuffer.put(HashAlgorithm.intToByte(this.version));
        byteBuffer.put(HashAlgorithm.longToByte(this.unixTimestamp));
        byteBuffer.put(HashAlgorithm.intToByte(this.difficulty));
        byteBuffer.put(this.prevHash);
        byteBuffer.put(this.merkleTreeHash);
        byteBuffer.put(HashAlgorithm.intToByte(this.nonce));

        hash = digest.digest(byteBuffer.array());

        // Cycle will iterate until a hash with the predefined characteristic is found
        // i.e. we will increment the nonce until we find a hash that we want
        while (!validHash(hash)) {
            this.nonce++;
            byteBuffer.put(baseSize, HashAlgorithm.intToByte(nonce));
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
                "\nprevHash=" + HashAlgorithm.byteToHex(prevHash) +
                "\ntxRootHash=" + HashAlgorithm.byteToHex(merkleTreeHash) +
                "\nnonce=" + nonce +
                "\n}";
    }

    /**
     * Given a certain difficulty, this function checks if a given hash code has a certain
     * number of zeros at the beginning
     *
     * @param hash       hash to check
     * @param difficulty number of zero's in the prefix
     * @return True if the hash e valid, otherwise false
     */
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
    public byte[] getMerkleTreeHash() {
        return merkleTreeHash;
    }

    /**
     * Get the block nonce
     *
     * @return Get the block nonce
     */
    public int getNonce() {
        return nonce;
    }

    /**
     * Verify that a block header is valid
     *
     * @param blockHeader The block header to verify
     */
    public static boolean verifyBlockHeader(BlockHeader blockHeader) {
        try {
            if (!HashAlgorithm.validHash(blockHeader.getHash(),
                    blockHeader.getDifficulty())) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Failed verify: " + e);
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {

        if (other == this)
            return true;

        if (other == null || !(other instanceof BlockHeader))
            return false;

        try {
            return Arrays.compare(getHash(), ((BlockHeader) other).getHash()) == 0;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}
