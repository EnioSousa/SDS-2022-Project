package Runnables;

import PeerToPeer.Node;
import PeerToPeer.NodeInfo;
import Utils.Bootstrap;

import java.security.NoSuchAlgorithmException;

public class runBootstrap {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        Node node = new Node(new NodeInfo(Bootstrap.bootstrapId, Bootstrap.bootstrapIp, Bootstrap.bootstrapPort));
    }
}
