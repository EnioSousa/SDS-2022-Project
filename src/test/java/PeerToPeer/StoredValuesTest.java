package PeerToPeer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StoredValuesTest {

    public static StoredValues storedValues;

    @BeforeAll
    public static void setUp() {
        storedValues = new StoredValues(8, null);
    }

    @Test
    void storeValue() {
        assertTrue(storedValues.storeValue(new byte[]{0x04},
                new byte[]{0x0F}));
    }

    @Test
    void getStoredValue() {
        storedValues.storeValue(new byte[]{(byte) 0x87}, new byte[]{0x0F});

        assertTrue(Arrays.equals(storedValues.getStoredValue(new byte[]{(byte) 0x87}),
                new byte[]{0x0F}));

    }
}