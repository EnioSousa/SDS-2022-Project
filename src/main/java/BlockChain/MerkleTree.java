package BlockChain;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO: Improve iterator. To much memory is being spent

public class MerkleTree implements Iterable<MerkleTree.MerkleNode>{

    /**
     * This Merkle node root
     */
    private final MerkleNode root;
    private int size;

    public MerkleTree(List<Transaction> transactions) throws NoSuchAlgorithmException {
        ArrayList<MerkleNode> merkleNodes = new ArrayList<>();

        for( Transaction transaction: transactions) {
            merkleNodes.add(new MerkleNode(null, null, transaction.getHash()));
        }

        size = merkleNodes.size();
        root = buildTree(merkleNodes);
    }

    /**
     * Given a lists of leaf nodes, i.e. nodes where the hash represent the transaction, this method
     * will build a merkle tree, based on these child nodes
     * @param merkleNodes An array list containing all the leaf nodes
     * @return The merkle root node
     * @throws NoSuchAlgorithmException Exception is raised if the hash algorithm is not found
     */
    private MerkleNode buildTree(ArrayList<MerkleNode> merkleNodes) throws NoSuchAlgorithmException {
        if ( merkleNodes.size() == 1 ) {
            return merkleNodes.remove(0);
        }
        else {
            int limIndex = merkleNodes.size() - 1;
            int curIndex = 0;

            while ( curIndex <= limIndex) {
                MerkleNode left = null;
                MerkleNode right = null;

                left = merkleNodes.remove(0);
                curIndex++;

                // If there are no more nodes, then we duplicate the left node to be able to combine hashes
                if ( curIndex <= limIndex) {
                    right = merkleNodes.remove(0);
                    curIndex++;
                }

                merkleNodes.add(new MerkleNode(left, right, combineHash(left, right)));
                this.size++;
            }

            return buildTree(merkleNodes);
        }
    }

    /**
     * Get the root hash of the merkle tree
     * @return The root hash of the merkle tree
     */
    public byte[] getHash() {
        return root == null ? null: root.getHash();
    }

    /**
     * Given two merkle nodes, this method will combine both hashes to generate the parent hash
     * @param left The left merkle node
     * @param right The right merkle node
     * @return Return the combined hash, passed though SHA-256, of both nodes
     * @throws NoSuchAlgorithmException Exception if the algorithm chosen is not valid
     */
    private byte[] combineHash(MerkleNode left, MerkleNode right) throws NoSuchAlgorithmException {
        // The left son is always present, the right one may be missing if we have
        // an odd number od transactions
        byte[] byteArray = new byte[left.getHash().length +
                (right == null ? left.getHash().length: right.getHash().length)];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        byteBuffer.put(left.getHash());
        byteBuffer.put(right == null ? left.getHash() : right.getHash());

        return HashAlgorithm.generateHash(byteBuffer.array());
    }

    /**
     * Get the Merkle root
     * @return Returns the MerkleNode root
     */
    public MerkleNode getRoot() { return this.root; }

    /**
     * Get the size of the Merkle Tree
     * @return size of tree
     */
    public int getSize() { return this.size; }

    @Override
    public Iterator<MerkleNode> iterator() {
        List<MerkleNode> list = new ArrayList<>();

        if ( getRoot() != null)
            list.add(getRoot());

        for( int i=0; i<getSize(); i++) {
            MerkleNode merkleNode = list.get(i);

            if ( merkleNode.getLeft() != null) {
                list.add(merkleNode.getLeft());
            }

            if ( merkleNode.getRight() != null) {
                list.add(merkleNode.getRight());
            }
        }

        return list.iterator();
    }

    @Override
    public String toString() {
        int level = 0;
        int curNode = 0;
        StringBuilder stringBuilder = new StringBuilder("MerkleTree{\nLevel: 0\n");

        for ( MerkleNode merkleNode: this) {
            if ( curNode >= Math.pow(2, level + 1) - 1 ) {
                level++;
              stringBuilder.append("\nLEVEL: ");
              stringBuilder.append(level);
              stringBuilder.append("\n");
            }

            stringBuilder.append(merkleNode);
            stringBuilder.append("\n");
            curNode++;
        }

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }

    /**
     * This class represents a merkle node. It contains its sons, left and right, and the respective hash.
     * This hash can be the transaction hash, if the node is a leaf node, otherwise is a combination of the
     * hash of its sons
     */
    public class MerkleNode {
        private MerkleNode left;
        private MerkleNode right;
        private byte[] hash;

        private MerkleNode(MerkleNode left, MerkleNode right, byte[] hash) {
            setLeft(left);
            setRight(right);
            setHash(hash);
        }

        public MerkleNode getLeft() {
            return left;
        }

        public MerkleNode getRight() {
            return right;
        }

        public byte[] getHash() {
            return hash;
        }

        private void setLeft(MerkleNode left) {
            this.left = left;
        }

        private void setRight(MerkleNode right) {
            this.right = right;
        }

        private void setHash(byte[] hash) {
            this.hash = hash;
        }

        @Override
        public String toString() {
            return "MerkleNode{" +
                    "\nhash=" + HashAlgorithm.byteToHex(hash) +
                    "\n}";
        }
    }


}
