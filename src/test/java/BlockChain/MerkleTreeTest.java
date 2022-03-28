package BlockChain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MerkleTreeTest {

    // Hash: 7F3A229D5D5EB9CE138A30B3C89CCB0DCBECB06DFD636F5DDE667B56B7CB8246
    private static Transaction t1;
    // Hash: F7361444C6537E4BC21799E373CE022172A8D3E84867DCDC9592001E3D2405DA
    private static Transaction t2;
    // Hash: B70D9578A95327BBE2268B0A1B3F4384FE1892A4911EF28DD105EA49E7901E36
    private static Transaction t3;

    private static MerkleTree merkleTree;

    @BeforeAll
    public static void setUp() throws NoSuchAlgorithmException {
        t1 = new Transaction("enio".getBytes(StandardCharsets.UTF_8),
                "joao".getBytes(StandardCharsets.UTF_8),
                "car".getBytes(StandardCharsets.UTF_8),
                12000);
        t2 = new Transaction("andreia".getBytes(StandardCharsets.UTF_8),
                "joao".getBytes(StandardCharsets.UTF_8),
                "car".getBytes(StandardCharsets.UTF_8),
                11000);
        t3 = new Transaction("enio".getBytes(StandardCharsets.UTF_8),
                "joao".getBytes(StandardCharsets.UTF_8),
                "car".getBytes(StandardCharsets.UTF_8),
                -1);

        List<Transaction> list = new ArrayList<>();

        list.add(t1);
        list.add(t2);
        list.add(t3);

        merkleTree = new MerkleTree(list);

        System.out.println(merkleTree);
    }

    @Test
    void testGetHash() throws NoSuchAlgorithmException {
        byte[] hexT1 = t1.getHash();
        byte[] hexT2 = t2.getHash();
        byte[] hexT3 = t3.getHash();

        byte[] hexT1T2 = HashAlgorithm.generateHash(hexT1, hexT2);
        byte[] hexT3T3 = HashAlgorithm.generateHash(hexT3, hexT3);
        byte[] hexT1T2T3T3 = HashAlgorithm.generateHash(hexT1T2, hexT3T3);

        assertTrue(Arrays.equals(hexT1T2T3T3, merkleTree.getRoot().getHash()));
    }

    @Test
    void testGetRoot() {
        assertNotNull(merkleTree.getRoot());
    }

    @Test
    void testGetSize() {
        assertEquals(6, merkleTree.getSize());
    }

    @Test
    void testIterator() throws NoSuchAlgorithmException {
        byte[] hexT1 = t1.getHash();
        byte[] hexT2 = t2.getHash();
        byte[] hexT3 = t3.getHash();

        byte[] hexT1T2 = HashAlgorithm.generateHash(hexT1, hexT2);
        byte[] hexT3T3 = HashAlgorithm.generateHash(hexT3, hexT3);
        byte[] hexT1T2T3T3 = HashAlgorithm.generateHash(hexT1T2, hexT3T3);

        byte[][] byteArray = new byte[][]{hexT1T2T3T3, hexT1T2,
                hexT3T3, hexT1, hexT2, hexT3};

        int i=0;
        for(MerkleTree.MerkleNode node: merkleTree) {
            assertTrue(Arrays.equals(byteArray[i], node.getHash()));
            i++;
        }
    }
}