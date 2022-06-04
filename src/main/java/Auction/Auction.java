package Auction;

import PeerToPeer.Node;

import java.util.Random;

public class Auction {

    private int auctionId;
    private byte[] auctioneerNodeId;
    private String auctionName;
    private float minimumBids;

    public Auction(byte[] auctioneerNodeId, String auctionName, float minimumBids){

        Random rand = new Random();

        this.auctioneerNodeId = auctioneerNodeId;
        this.auctionName = auctionName;
        this.auctionId = rand.nextInt(Integer.MAX_VALUE);
        this.minimumBids = minimumBids;
    }

    public static Auction initializeAuction(Node node, String auctionName, float minimumBids){
        Auction auction = new Auction(node.getNodeInfo().getId(),auctionName,minimumBids);

        return auction;
    }

}
