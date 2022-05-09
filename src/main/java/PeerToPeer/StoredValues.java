package PeerToPeer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class StoredValues {
    /**
     * Logger object to log stuff
     */
    public static final Logger LOGGER = LogManager.getLogger(StoredValues.class);
    /**
     * Map holds the stored values
     */
    private HashMap<byte[], byte[]> storedValues = new HashMap<>();
    /**
     * Size of the key. Note the key size needs to be the same as the id
     * address size.
     */
    private final int keySize;
    /**
     * Node that is running this stored values table
     */
    private final Node runningNode;

    public StoredValues(int keySize, Node runningNode) {
        this.keySize = keySize;
        this.runningNode = runningNode;
    }

    public boolean storeValue(byte[] key, byte[] value) {
        if (key.length != keySize / 8) {
            LOGGER.error("Key wrong size: " + key.length);
            return false;
        }

        return storedValues.put(key, value) != null;
    }

    public byte[] getStoredValue(byte[] key) {
        return storedValues.get(key);
    }

    public int getKeySize() {
        return keySize;
    }

}
