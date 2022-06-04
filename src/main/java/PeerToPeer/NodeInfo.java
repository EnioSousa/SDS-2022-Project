package PeerToPeer;

import BlockChain.HashAlgorithm;

import java.util.Arrays;

public class NodeInfo {
    /**
     * id of the node
     */
    private byte[] id;
    /**
     * ip of the node
     */
    private String ip;
    /**
     * port of the node
     */
    private int port;


    private Boolean bootstrap = false;

    private Boolean miner = false;

    /**
     * Given a set a parameters this constructor creates a {@link NodeInfo}
     * object
     *
     * @param id   id of the node, in byte[] format
     * @param ip   ip of the node is listening
     * @param port port the node is listening
     */
    public NodeInfo(byte[] id, String ip, int port, boolean miner) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.miner = miner;
    }

    /**
     * Given a set a parameters this constructor creates a {@link NodeInfo}
     * object
     *
     * @param id   id of the node, in byte[] format
     * @param ip   ip of the node is listening
     * @param port port the node is listening
     */
    public NodeInfo(byte[] id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    /**
     * Given a set of parameters this constructor creates a {@link NodeInfo}
     * object
     *
     * @param hexId id of the node, in string format
     * @param ip    ip of the node is listening
     * @param port  port the node is listening
     */
    public NodeInfo(String hexId, String ip, int port) {
        this.id = HashAlgorithm.hexToByte(hexId);
        this.ip = ip;
        this.port = port;
    }

    /**
     * Get the id of the node
     *
     * @return id of node
     */
    public byte[] getId() {
        return id;
    }

    public String getIdString() {
        return HashAlgorithm.byteToHex(id);
    }

    /**
     * Get the ip of the node
     *
     * @return ip of node
     */
    public String getIp() {
        return ip;
    }

    /**
     * Get the port the node is listening
     *
     * @return port of the node
     */
    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return " id = " + getIdString() +
                " ip = " + getIp() +
                " port = " + getPort();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other == null || !(other instanceof NodeInfo))
            return false;

        if (!Arrays.equals(this.getId(), ((NodeInfo) other).getId())) {
            return false;
        }

        if (!this.getIp().equals(((NodeInfo) other).getIp())) {
            return false;
        }

        if (this.getPort() != ((NodeInfo) other).getPort()) {
            return false;
        }

        return true;
    }

    /**
     * Set bootstrap value
     */
    public void setBootstrap() {
        this.bootstrap = true;
    }

    /**
     * Check if its a bootstrap
     *
     * @return
     */
    public Boolean isBootstrap() {
        return this.bootstrap;
    }

    public void setMiner() {
        this.miner = true;
    }

    public Boolean isMiner() {
        return this.miner;
    }

    /**
     * Set if of the node
     *
     * @param id
     */
    public void setId(byte[] id) {
        this.id = id;
    }
}
