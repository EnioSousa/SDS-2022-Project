package BlockChain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.List;

// TODO: Alot.. 1) This needs tests. 2) Needs a  proper data base. 3) A lot more
//  ..........

/**
 * This class holds all the information relative to a given block. It contains the block header, the
 * transaction list and the merkle tree
 */
public class Block {
    /**
     * Logger
     */
    public static Logger LOGGER = LogManager.getLogger(Block.class);

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

    public Block(int version, long unixTimestamp, int difficulty, byte[] prevHash,
                 int nonce, List<Transaction> transactionsList) throws NoSuchAlgorithmException {
        this.transactionsList = transactionsList;

        this.merkleTree = new MerkleTree(this.transactionsList);

        this.blockHeader = new BlockHeader(version, unixTimestamp, difficulty,
                prevHash, merkleTree.getHash(), nonce);

    }

    public Block(BlockHeader blockHeader,
                 List<Transaction> transactionList) throws NoSuchAlgorithmException {
        this.transactionsList = transactionList;

        this.merkleTree = new MerkleTree(this.transactionsList);

        this.blockHeader = new BlockHeader(blockHeader.getVersion(),
                blockHeader.getUnixTimestamp(), blockHeader.getDifficulty(),
                blockHeader.getPrevHash(), blockHeader.getMerkleTreeHash(),
                blockHeader.getNonce());
    }

    /**
     * Get the full list of transaction in this given block
     *
     * @return The transaction list
     */
    public List<Transaction> getTransactionsList() {
        return transactionsList;
    }

    /**
     * Get the merkle tree with the transactions hashes
     *
     * @return the merkle tree
     */
    public MerkleTree getMerkleTree() {
        return merkleTree;
    }

    /**
     * Get the block header for this block
     *
     * @return the block header
     */
    public BlockHeader getBlockHeader() {
        return blockHeader;
    }

    public static boolean verifyBlock(Block block) {
        if (!BlockHeader.verifyBlockHeader(block.getBlockHeader())) {
            return false;
        }

        for (Transaction transaction : block.transactionsList) {
            if (!Transaction.verifyTransaction(transaction)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getBlockHeader());

        for (Transaction transaction : getTransactionsList()) {
            try {
                stringBuilder.append(HashAlgorithm.byteToHex(blockHeader.getHash()));
                stringBuilder.append(": " + transaction);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error("Hash error: " + e);
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other == null || !(other instanceof Block))
            return false;

        return blockHeader.equals(((Block) other).getBlockHeader());
    }
}
