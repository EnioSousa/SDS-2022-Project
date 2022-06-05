package BlockChain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;

public class Chain {
    /**
     * Logger
     */
    public static Logger LOGGER = LogManager.getLogger(Chain.class);
    /**
     * List holds all the blocks
     */
    private LinkedList<Block> blockChain;

    public Chain() {
        blockChain = new LinkedList<>();
    }

    public LinkedList<Block> getBlockChain() {
        return blockChain;
    }

    public void setBlockChain(LinkedList<Block> list) {
        this.blockChain = list;
    }

    public int blockChainSize() {
        return blockChain.size();
    }

    /**
     * @param block
     * @return
     * @throws NoSuchAlgorithmException
     */
    public boolean addBlock(Block block) throws NoSuchAlgorithmException {
        if (Block.verifyBlock(block) && Arrays.compare(getLastHash(),
                block.getBlockHeader().getPrevHash()) == 0) {
            if (blockChain.add(block)) {
                LOGGER.info("Block added to blockChain: " + block);
                return true;
            }

        } else {
            LOGGER.info("Block invalid: " + block);
        }

        return false;
    }

    /**
     * Gets the hash of the list
     *
     * @return The hash of the last block in the current blockchain
     * @throws NoSuchAlgorithmException Hash algorithm not found
     */
    public byte[] getLastHash() throws NoSuchAlgorithmException {
        return blockChain.isEmpty() ? null : blockChain.getLast().getBlockHeader().getHash();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Block block : blockChain) {
            stringBuilder.append(block);
        }

        return stringBuilder.toString();
    }

    public static boolean verifyBlockChain(Chain blockChain) {
        return verifyBlockChain(blockChain.getBlockChain());
    }

    public static boolean verifyBlockChain(LinkedList<Block> list) {
        for (Block block : list) {
            if (!Block.verifyBlock(block)) {
                return false;
            }
        }

        return true;
    }

    public void replaceBlockChain(LinkedList<Block> list) {
        if (Chain.verifyBlockChain(list)) {
            setBlockChain(list);

            LOGGER.info("Block chain replaced: size: " + blockChain.size());
        }
    }
}
