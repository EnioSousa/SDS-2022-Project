package PeerToPeer;

import BlockChain.HashAlgorithm;

import java.security.NoSuchAlgorithmException;

public class run {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        Node node =
                new Node(new NodeInfo(HashAlgorithm.hexToByte(args[0]),
                        "localhost", Integer.parseInt(args[1])));

        test1(node);
    }

    public static void test2(Node node) throws NoSuchAlgorithmException, InterruptedException {
        test1(node);


    }

    public static void test1(Node node) throws InterruptedException, NoSuchAlgorithmException {
        if (node.getNodeInfo().getPort() != 5000) {
            node.doFindNode(node.getNodeInfo().getId());

            Thread.sleep(5000);
        }
    }

    public static void test0(Node node) throws InterruptedException {
        while (true) {
            // Tou a usar os port, mas isto é HADHOC, nao usar em mais lado nenhum
            if (node.getNodeInfo().getPort() == 5000) {
                node.doPingSync(new NodeInfo(new byte[]{0x01}, "localhost",
                        5001));
                node.doPingSync(new NodeInfo(new byte[]{0x02}, "localhost",
                        5002));
            } else if (node.getNodeInfo().getPort() == 5001) {
                node.doPingSync(new NodeInfo(new byte[]{0x00}, "localhost",
                        5000));
                node.doPingSync(new NodeInfo(new byte[]{0x02}, "localhost",
                        5002));
            } else {
                node.doPingSync(new NodeInfo(new byte[]{0x00}, "localhost",
                        5000));
                node.doPingSync(new NodeInfo(new byte[]{0x01}, "localhost",
                        5001));
            }

            Thread.sleep(1000);
        }
    }
}
