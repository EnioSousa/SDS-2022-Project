package PeerToPeer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class K_BucketsTest {

    public static Node node;
    public static K_Buckets kBuckets;

    public static int spaceSize = 8;
    public static int k = 4;

    @BeforeAll
    public static void setUp() {
        byte[] id = {0x69};
        String ip = "localhost";
        int port = 5000;

        NodeInfo nodeInfo = new NodeInfo(id, ip, port);

        node = new Node(nodeInfo);

        kBuckets = node.getKBuckets();
    }


    @Test
    void getRunningNodeInfo() {
        assertTrue(node.getNodeInfo().equals(kBuckets.getRunningNodeInfo()));
    }

    @Test
    void getId() {
        assertTrue(Arrays.equals(node.getNodeInfo().getId(), kBuckets.getId()));
    }

    @Test
    void getSpaceSize() {
        assertEquals(kBuckets.getSpaceSize(), spaceSize);
    }

    @Test
    void getK() {
        assertEquals(kBuckets.getK(), k);
    }

    @Test
    void addNodeInfo() {
        NodeInfo nodeInfo0 = new NodeInfo(new byte[]{(byte) 0xDA}, "localhost",
                5000);

        kBuckets.addNodeInfo(nodeInfo0);

        assertTrue(kBuckets.getNodesFromBucket(0).get(0).equals(nodeInfo0));
    }

    @Test
    void doXOR() {
        assertTrue(Arrays.equals(kBuckets.doXOR(new byte[]{0x6D},
                new byte[]{0x2A}), new byte[]{71}));
    }

    @Test
    void closestBucket() {
        assertAll(
                () -> assertEquals(0,
                        kBuckets.closestBucket(new byte[]{(byte) 0x80})),
                () -> assertEquals(1,
                        kBuckets.closestBucket(new byte[]{(byte) 0x00})),
                () -> assertEquals(2,
                        kBuckets.closestBucket(new byte[]{(byte) 0x40})),
                () -> assertEquals(3,
                        kBuckets.closestBucket(new byte[]{(byte) 0x70})),
                () -> assertEquals(4,
                        kBuckets.closestBucket(new byte[]{(byte) 0x60})),
                () -> assertEquals(5,
                        kBuckets.closestBucket(new byte[]{(byte) 0x6C})),
                () -> assertEquals(6,
                        kBuckets.closestBucket(new byte[]{(byte) 0x6A})),
                () -> assertEquals(7,
                        kBuckets.closestBucket(new byte[]{(byte) 0x68})));
    }

    @Test
    void getKClosest() {
        LinkedList<NodeInfo> list;

        list = kBuckets.getKClosest(new byte[]{(byte) 0x00});

        assertEquals(0, list.size());


        kBuckets.addNodeInfo(new NodeInfo(new byte[]{(byte) 0xF3}, "localhost"
                , 5000));

        list = kBuckets.getKClosest(new byte[]{(byte) 0x00});

        assertEquals(1, list.size());


        kBuckets.addNodeInfo(new NodeInfo(new byte[]{(byte) 0x53}, "localhost"
                , 5000));

        list = kBuckets.getKClosest(new byte[]{(byte) 0x00});

        assertEquals(2, list.size());


        kBuckets.addNodeInfo(new NodeInfo(new byte[]{(byte) 0x01}, "localhost"
                , 5000));

        list = kBuckets.getKClosest(new byte[]{(byte) 0x00});

        assertEquals(3, list.size());


        kBuckets.addNodeInfo(new NodeInfo(new byte[]{(byte) 0x6F}, "localhost"
                , 5000));

        list = kBuckets.getKClosest(new byte[]{(byte) 0x00});

        assertEquals(4, list.size());


        kBuckets.addNodeInfo(new NodeInfo(new byte[]{(byte) 0x0F}, "localhost"
                , 5000));

        list = kBuckets.getKClosest(new byte[]{(byte) 0x00});

        assertEquals(4, list.size());

        kBuckets.addNodeInfo(new NodeInfo(new byte[]{(byte) 0x68}, "localhost"
                , 5000));
        kBuckets.addNodeInfo(new NodeInfo(new byte[]{(byte) 0x6A}, "localhost"
                , 5000));


        LinkedList<NodeInfo> kClosest0 =
                kBuckets.getKClosest(new byte[]{(byte) 0x00});
        assertEquals(4, list.size());

        assertAll(() -> assertEquals(new NodeInfo(new byte[]{(byte) 0x01},
                        "localhost"
                        , 5000), kClosest0.get(0)),
                () -> assertEquals(new NodeInfo(new byte[]{(byte) 0x0F},
                        "localhost"
                        , 5000), kClosest0.get(1)),
                () -> assertEquals(new NodeInfo(new byte[]{(byte) 0x53},
                        "localhost"
                        , 5000), kClosest0.get(2)),
                () -> assertEquals(new NodeInfo(new byte[]{(byte) 0x6F},
                        "localhost"
                        , 5000), kClosest0.get(3)));


        LinkedList<NodeInfo> kClosest1 =
                kBuckets.getKClosest(new byte[]{(byte) 0x5F});
        assertEquals(4, list.size());

        assertAll(() -> assertEquals(new NodeInfo(new byte[]{(byte) 0x53},
                        "localhost"
                        , 5000), kClosest1.get(0)),
                () -> assertEquals(new NodeInfo(new byte[]{(byte) 0x6F},
                        "localhost"
                        , 5000), kClosest1.get(1)),
                () -> assertEquals(new NodeInfo(new byte[]{(byte) 0x6A},
                        "localhost"
                        , 5000), kClosest1.get(2)),
                () -> assertEquals(new NodeInfo(new byte[]{(byte) 0x68},
                        "localhost"
                        , 5000), kClosest1.get(3)));


        LinkedList<NodeInfo> kClosest2 =
                kBuckets.getKClosest(new byte[]{(byte) 0x6A});
        assertEquals(4, list.size());

        assertAll(() -> assertEquals(new NodeInfo(new byte[]{(byte) 0x6A},
                        "localhost"
                        , 5000), kClosest2.get(0)),
                () -> assertEquals(new NodeInfo(new byte[]{(byte) 0x68},
                        "localhost"
                        , 5000), kClosest2.get(1)),
                () -> assertEquals(new NodeInfo(new byte[]{(byte) 0x6F},
                        "localhost"
                        , 5000), kClosest2.get(2)),
                () -> assertEquals(new NodeInfo(new byte[]{(byte) 0x53},
                        "localhost"
                        , 5000), kClosest2.get(3)));
    }
}