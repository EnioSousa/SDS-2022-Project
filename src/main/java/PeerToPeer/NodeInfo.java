package PeerToPeer;

import BlockChain.HashAlgorithm;
import com.google.protobuf.ByteString;
import grpcCode.PeerToPeerOuterClass;

import java.util.Arrays;

public class NodeInfo {
    /**
     * id of the node
     */
    private final byte[] id;
    /**
     * ip of the node
     */
    private final String ip;
    /**
     * port of the node
     */
    private final int port;

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
     * Given a grpc nodeInfo type, this constructor creates the {@link NodeInfo}
     * object
     *
     * @param nodeInfo nodeInfo type object defined in the proto file
     */
    public NodeInfo(PeerToPeerOuterClass.NodeInfo nodeInfo) {
        this.id = nodeInfo.getNodeId().toByteArray();
        this.ip = nodeInfo.getNodeIp();
        this.port = nodeInfo.getNodePort();
    }

    /**
     * Given a NodeInfo, this method return the grpc type of the NodeInfo,
     * defined in the proto file
     *
     * @return {@link grpcCode.PeerToPeerOuterClass.NodeInfo} object
     */
    public PeerToPeerOuterClass.NodeInfo getServiceNodeInfo() {
        return PeerToPeerOuterClass.NodeInfo.newBuilder()
                .setNodeId(ByteString.copyFrom(getId()))
                .setNodeIp(getIp())
                .setNodePort(getPort())
                .build();
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

}
