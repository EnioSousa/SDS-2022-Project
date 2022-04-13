package PeerToPeer;

import BlockChain.HashAlgorithm;

import java.util.concurrent.TimeUnit;

public class PeerToPeer {
    public static Node runNode;

    public static Node getRunningNode() {
        return runNode;
    }

    public static void main(String[] args) throws InterruptedException {
        byte[] id = HashAlgorithm.hexToByte(args[0]);
        String ip = args[1];
        int port = Integer.parseInt(args[2]);

        runNode = new Node(new NodeInfo(id, ip, port));

        while (!args[0].equals("00")) {
            runNode.doPing(new NodeInfo("00", "localhost", 5000));

            TimeUnit.MILLISECONDS.sleep(1000);
        }
    }

}
