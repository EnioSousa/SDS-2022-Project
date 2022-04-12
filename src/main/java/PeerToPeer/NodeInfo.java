package PeerToPeer;

public class NodeInfo {
    private byte[] id;
    private String ip;
    private int port;

    public NodeInfo(byte[] id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public byte[] getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
