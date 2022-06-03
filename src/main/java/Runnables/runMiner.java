package Runnables;

import PeerToPeer.Node;
import PeerToPeer.NodeInfo;
import Utils.Bootstrap;

import java.security.NoSuchAlgorithmException;

public class runMiner {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {

        Node miner = new Node(new NodeInfo(new byte[]{0x02}, args[1], Integer.parseInt(args[0]), true));

        miner.doJoin(new NodeInfo(Bootstrap.bootstrapId, Bootstrap.bootstrapIp, Bootstrap.bootstrapPort));
    }
}
