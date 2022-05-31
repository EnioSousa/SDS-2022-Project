package PeerToPeer;

import Utils.Bootstrap;

import java.security.NoSuchAlgorithmException;

public class runMiner {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {

        Node miner = new Node(new NodeInfo(new byte[]{0x02},"localhost", Integer.parseInt(args[0]),true));

        miner.doJoin(miner.getNodeInfo());
    }
}
