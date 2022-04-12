package PeerToPeer;

import java.nio.charset.StandardCharsets;

public class PeerToPeer {
    public static Node node;

    public static void main(String[] args) {
        mainNode("Enio", "localhost", 5000);
    }

    private static void mainNode(NodeInfo nodeInfo) {
        node = new Node(nodeInfo);
    }

    private static void mainNode(byte[] id, String ip, int port) {
        mainNode(new NodeInfo(id, ip, port));
    }

    private static void mainNode(String id, String ip, int port) {
        mainNode(id.getBytes(StandardCharsets.UTF_8), ip, port);
    }

}
