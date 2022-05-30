package PeerToPeer;

import Utils.Bootstrap;

import java.security.NoSuchAlgorithmException;

public class runNode {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {

        Node node = new Node(new NodeInfo(new byte[]{0x02},Bootstrap.bootstrapIp, Integer.parseInt(args[0])));

        node.doJoin(node.getNodeInfo());
    }
}
