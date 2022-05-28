package PeerToPeer;

import BlockChain.HashAlgorithm;
import Utils.Bootstrap;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class run2 {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        //Node node = new Node(new NodeInfo(new byte[]{0x01}, Bootstrap.bootstrapIp, 6000));
        Node node2 = new Node(new NodeInfo(new byte[]{0x02},Bootstrap.bootstrapIp, 7000));

        //node.doJoin(node.getNodeInfo());
        //System.out.println(HashAlgorithm.byteToHex(node.getNodeInfo().getId()));
        //Thread.sleep(5000);
        node2.doJoin(node2.getNodeInfo());
    }

    /*
    public static void test3(Node node) throws NoSuchAlgorithmException, InterruptedException {
        test2(node);

        if (node.getNodeInfo().getPort() == 5003) {
            Thread.sleep(5000);
            node.doFind("Enio".getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void test2(Node node) throws NoSuchAlgorithmException, InterruptedException {
        test1(node);


        if (node.getNodeInfo().getPort() == 5003) {
            node.doStore("Enio".getBytes(StandardCharsets.UTF_8),
                    "Fuck".getBytes(StandardCharsets.UTF_8));
        }

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
    }*/
}
