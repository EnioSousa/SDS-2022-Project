package Utils;

import PeerToPeer.Node;

public class Bootstrap {

    /**
     * Id, IP and Port od the bootstrap node
     */
    public static byte[] bootstrapId = new byte[Node.getIdSize()];
    public static String bootstrapIp = "localhost";
    public static int bootstrapPort = 50000;
}
