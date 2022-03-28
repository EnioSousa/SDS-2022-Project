package BlockChain;

import java.security.NoSuchAlgorithmException;
import java.util.List;

// TODO: Alot.. 1) This neeeds tests. 2) Need a way proper data base. 3) A lot more..........

/**
 * This class holds all the information relative to a given block. It contains the block header, the
 * transaction list and the merkle tree
 */
public class Block {
    private final List<Transaction> transactionsList;
    private final MerkleTree merkleTree;
    private final BlockHeader blockHeader;

    public Block(int version, long unixTimestamp, int difficulty, byte[] prevHash,
                 List<Transaction> transactionsList) throws NoSuchAlgorithmException {
        this.transactionsList = transactionsList;

        this.merkleTree = new MerkleTree(this.transactionsList);

        this.blockHeader = new BlockHeader(version, unixTimestamp, difficulty,
                prevHash, merkleTree.getHash(), 0);

    }

    /**
     * Get the full list of transaction in this given block
     * @return The transaction list
     */
    public List<Transaction> getTransactionsList() {
        return transactionsList;
    }

    /**
     * Get the merkle tree with the transactions hashes
     * @return the merkle tree
     */
    public MerkleTree getMerkleTree() {
        return merkleTree;
    }

    /**
     * Get the block header for this block
     * @return the block header
     */
    public BlockHeader getBlockHeader() {
        return blockHeader;
    }
}
