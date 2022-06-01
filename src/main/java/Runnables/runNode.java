package Runnables;

import PeerToPeer.Node;
import PeerToPeer.NodeInfo;
import Utils.Bootstrap;

import java.security.NoSuchAlgorithmException;

public class runNode {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {

        Node node = new Node(new NodeInfo(new byte[]{0x02}, args[1],
                Integer.parseInt(args[0])));

        node.doJoin(new NodeInfo(Bootstrap.bootstrapId, Bootstrap.bootstrapIp, Bootstrap.bootstrapPort));
    }
}
