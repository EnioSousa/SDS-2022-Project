package PeerToPeer;

import BlockChain.HashAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class K_Buckets {
    /**
     * Logger
     */
    public static Logger LOGGER = LogManager.getLogger(K_Buckets.class);
    /**
     * Node that is running this k bucket
     */
    private final Node node;
    /**
     * Info of the node that's running this k-bucket class
     */
    private final NodeInfo runningNodeInfo;
    /**
     * size, in number of bits, of our id. Need to be multiple of 8
     */
    private final int spaceSize;
    /**
     * K size for out k-bucket
     */
    private final int k;
    /**
     * Alpha value
     */
    private final int alpha;
    /**
     * An array containing buckets from each required subtree. For more info
     * check kadamlia k-buckets
     */
    private final ArrayList<Bucket> buckets;

    /**
     * This constructor creates an appropriate k-bucket to use for kadamlia
     * protocol
     *
     * @param node      The node that is running this k bucket
     *                  k-bucket
     * @param spaceSize The number of bits used for the node's id
     * @param k         The number of node's info to keep in each k-bucket
     */
    public K_Buckets(Node node, int spaceSize, int k) {
        this.runningNodeInfo = node.getNodeInfo();
        this.spaceSize = spaceSize;
        this.k = k;
        this.node = node;

        // + 1 is because we are saving our own node info
        this.buckets = new ArrayList<>(spaceSize + 1);

        this.alpha = 3;

        for (int i = 0; i < spaceSize; i++) {
            buckets.add(i, new Bucket(i));
        }
    }

    public LinkedList<NodeInfo> getNodesFromBucket(int nBucket) {
        if (nBucket >= spaceSize) {
            return null;
        } else {
            return buckets.get(nBucket).getBucket();
        }
    }

    public Node getNode() {
        return node;
    }

    public int getAlpha() {
        return alpha;
    }

    /**
     * Get the node info of the node running this k-bucket
     *
     * @return Node info of the node running the k-bucket
     */
    public NodeInfo getRunningNodeInfo() {
        return runningNodeInfo;
    }

    /**
     * Get the id of the node running this k bucket
     *
     * @return the id of the node
     */
    public byte[] getId() {
        return getRunningNodeInfo().getId();
    }

    /**
     * Get the space size of the ids', i.e. get the number of bits used for
     * the id of each node
     *
     * @return the number of bits in the node id
     */
    public int getSpaceSize() {
        return spaceSize;
    }

    /**
     * Get the k used for the k-buckets
     *
     * @return The k
     */
    public int getK() {
        return k;
    }

    /**
     * Add node info to our k bucket
     *
     * @param nodeInfo the node info to add
     */
    public void addNodeInfo(NodeInfo nodeInfo) {
        if (!nodeInfo.equals(getRunningNodeInfo())) {
            buckets.get(closestBucket(nodeInfo.getId())).add(nodeInfo);
        }
    }

    /**
     * Get the k the closest nodes from the wanted id
     *
     * @param wantedId The wanted id
     * @return a list containing the k the closest nodes
     */
    public LinkedList<NodeInfo> getKClosest(byte[] wantedId) {
        LinkedList<NodeInfo> list = new LinkedList<>();

        int closestBucket = closestBucket(wantedId);

        if (closestBucket == spaceSize) {
            list.add(getRunningNodeInfo());
        } else {
            for (int i = closestBucket; i < spaceSize && list.size() != getK(); i++) {
                getClosestFromBucket(list, i, wantedId);
            }

            for (int i = closestBucket - 1; i >= 0 && list.size() != getK(); i--) {
                getClosestFromBucket(list, i, wantedId);
            }

        }
        return list;
    }

    /**
     * This method will try to replace a node info with another
     *
     * @param newInfo The info to replace with
     * @param oldInfo The one being replaced
     */
    public void replace(NodeInfo newInfo, NodeInfo oldInfo) {
        int nb = closestBucket(newInfo.getId());
        int ob = closestBucket(newInfo.getId());

        if (nb != ob) {
            LOGGER.error("Replace error: Cant replace nodes of different " +
                    "buckets: old: " + oldInfo + ": new: " + newInfo);
        } else {
            buckets.get(nb).replace(newInfo, oldInfo);
        }
    }

    /**
     * Given a bucket index, we will try to get all the nodes from that
     * bucket, and select the nodes with the smaller distance. Distance is
     * done with XOR with the current id. For more info see kadamlia k-buckets
     *
     * @param kClosest The list containing the current nodes' info we have.
     *                 This list will then be updated if there are any nodes
     *                 in this bucket that are smaller than we currently have
     * @param nBucket  The index of the bucket, i.e. the size of the prefix
     *                 that is equal to our id.
     * @param wantedId The id we want to get the k closest
     */
    private void getClosestFromBucket(LinkedList<NodeInfo> kClosest,
                                      int nBucket, byte[] wantedId) {
        if (buckets.get(nBucket).getBucket().size() != 0) {
            LinkedList<NodeInfo> temp = buckets.get(nBucket).getBucket();

            kClosest.addAll(temp);

            kClosest.sort((o1, o2) -> {
                byte[] r0 = doXOR(wantedId, o1.getId());
                byte[] r1 = doXOR(wantedId, o2.getId());

                return Arrays.compareUnsigned(r0, r1);
            });

            int remCount = kClosest.size() - getK();

            for (int j = 0; j < remCount; j++) {
                kClosest.removeLast();
            }
        }
    }

    /**
     * This method does XOR operation between two byte arrays. Attention both
     * arrays need to have the same size, otherwise return null
     *
     * @param arr0 The first byte array
     * @param arr1 The second byte array
     * @return The XOR operation between two arrays
     */
    public byte[] doXOR(byte[] arr0, byte[] arr1) {
        if (arr0.length != arr1.length || arr0.length * 8 != spaceSize) {
            return null;
        } else {
            byte[] result = new byte[arr0.length];

            for (int i = 0; i < arr0.length; i++) {
                result[i] = (byte) (arr0[i] ^ arr1[i]);
            }

            return result;
        }
    }

    /**
     * Given an id, this function will give the closest bucket. The buckets
     * are ordered by the distance to our current node id. A return value of
     * zero, means the bucket has a prefix of size zero, meaning that given
     * tree is bigger.
     *
     * @param wantedId The byte array representing the wanted id
     * @return The index of the closest bucket
     */
    public int closestBucket(byte[] wantedId) {
        byte[] xorResult = doXOR(getId(), wantedId);

        int prefixBitCount = 0;

        for (int i = 0; i < spaceSize / 8; i++) {
            byte byteValue = xorResult[i];

            if (Byte.compareUnsigned((byte) 0x80, byteValue) <= 0) {
                return prefixBitCount;
            } else if (Byte.compareUnsigned((byte) 0x40, byteValue) <= 0) {
                return prefixBitCount + 1;
            } else if (Byte.compareUnsigned((byte) 0x20, byteValue) <= 0) {
                return prefixBitCount + 2;
            } else if (Byte.compareUnsigned((byte) 0x10, byteValue) <= 0) {
                return prefixBitCount + 3;
            } else if (Byte.compareUnsigned((byte) 0x08, byteValue) <= 0) {
                return prefixBitCount + 4;
            } else if (Byte.compareUnsigned((byte) 0x04, byteValue) <= 0) {
                return prefixBitCount + 5;
            } else if (Byte.compareUnsigned((byte) 0x02, byteValue) <= 0) {
                return prefixBitCount + 6;
            } else if (Byte.compareUnsigned((byte) 0x01, byteValue) >= 0) {
                return prefixBitCount + 7;
            } else {
                prefixBitCount += 8;
            }
        }

        return prefixBitCount;
    }


    private class Bucket {
        private final LinkedList<NodeInfo> bucket;
        private final int prefixSize;

        /**
         * Creates a bucket, to save nodes info
         *
         * @param prefixSize The prefix size of this bucket
         */
        Bucket(int prefixSize) {
            this.prefixSize = prefixSize;
            bucket = new LinkedList<>();
        }

        /**
         * Number of nodes in our bucket
         *
         * @return number of nodes in out bucket
         */
        private int numElem() {
            return bucket.size();
        }

        /**
         * Remove the first node from our bucket
         *
         * @return The removed node
         */
        private NodeInfo remove() {
            if (bucket.size() == 0) {
                return null;
            } else {
                NodeInfo nodeInfo = bucket.remove();

                LOGGER.info("Removed from k bucket: " + prefixSize + ": " +
                        "node:" + nodeInfo);

                return nodeInfo;
            }

        }

        /**
         * Add a given node to our bucket
         *
         * @param nodeInfo The node info to add
         */
        private void add(NodeInfo nodeInfo) {
            if (bucket.contains(nodeInfo)) {
                LOGGER.info("K bucket: Moving node to the end of the list: " +
                        nodeInfo);

                getBucket().remove(nodeInfo);
                getBucket().add(nodeInfo);

            } else if (numElem() >= getK()) {
                LOGGER.info("K bucket: " + prefixSize + ": is full: id: " +
                        HashAlgorithm.byteToHex(nodeInfo.getId()));

                getNode().kBucketFullDoPing(nodeInfo, getBucket().get(0));
            } else {
                boolean success = bucket.add(nodeInfo);

                if (success) {
                    LOGGER.info("K bucket: Added node: " + nodeInfo + ": to k" +
                            " bucket: " + prefixSize);
                } else {
                    LOGGER.error("K bucket: Failed to add node: " + nodeInfo +
                            ": " +
                            "to k bucket: " + prefixSize);
                }
            }
        }

        public void replace(NodeInfo newInfo, NodeInfo oldInfo) {
            if (getBucket().get(0).equals(oldInfo)) {
                getBucket().remove(oldInfo);
                getBucket().add(newInfo);

                LOGGER.info("Replaced: " + oldInfo + ": with: " + newInfo);
            }
        }

        /**
         * Get the list of k buckets saved in this node
         *
         * @return Number of nodes saved
         */
        private LinkedList<NodeInfo> getBucket() {
            return bucket;
        }

        /**
         * The size of the prefix that is equal to the id of node running
         * this k-bucket
         *
         * @return size of the prefix
         */
        private int getPrefixSize() {
            return prefixSize;
        }

        @Override
        public String toString() {
            if (numElem() == 0) {
                return "None";
            } else {
                StringBuilder builder = new StringBuilder();

                for (NodeInfo nodeInfo : bucket) {
                    builder.append(nodeInfo).append(": ");
                }

                return builder.toString();
            }
        }
    }
}
