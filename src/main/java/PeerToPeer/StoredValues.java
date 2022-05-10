package PeerToPeer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;

public class StoredValues {
    /**
     * Logger object to log stuff
     */
    public static final Logger LOGGER = LogManager.getLogger(StoredValues.class);
    /**
     * Map holds the stored values
     */
    private final HashMap<KeyId, byte[]> storedValues = new HashMap<>();
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
            LOGGER.error("Key wrong size: " + key.length * 8);
            return false;
        }

        storedValues.put(new KeyId(key), value);

        return true;
    }

    public byte[] getStoredValue(byte[] key) {
        return storedValues.get(new KeyId(key));
    }

    public int getKeySize() {
        return keySize;
    }

    /**
     * For some reason in java, when we try to use the keyPair list using
     * keys as bytes, java uses its address to compute the hash. We don't
     * have this, we want the hash to be computed by the contents of the key
     */
    public class KeyId {
        byte[] key;

        KeyId(byte[] key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KeyId key1 = (KeyId) o;
            return Arrays.equals(key, key1.key);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(key);
        }
    }

}
