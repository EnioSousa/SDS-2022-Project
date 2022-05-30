package PeerToPeer;

import Utils.Bootstrap;

import java.security.NoSuchAlgorithmException;

public class runBootstrap {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        Node node = new Node(new NodeInfo(Bootstrap.bootstrapId, Bootstrap.bootstrapIp, Bootstrap.bootstrapPort));
        //Node node2 = new Node(new NodeInfo(new byte[]{0x02},Bootstrap.bootstrapIp, 7000));

        //node.doJoin(node.getNodeInfo());
        //Thread.sleep(5000);
        //node2.doJoin(node2.getNodeInfo());
    }
}
