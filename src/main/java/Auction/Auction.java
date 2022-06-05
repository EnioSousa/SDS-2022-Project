package Auction;

import PeerToPeer.Node;

import java.util.Random;

public class Auction {

    private int auctionId;
    private byte[] auctioneerNodeId;
    private String auctionName;
    private byte[] productID;
    private float minimumBids;

    public Auction(byte[] auctioneerNodeId, String auctionName, float minimumBids, byte[] productID){

        Random rand = new Random();

        this.auctioneerNodeId = auctioneerNodeId;
        this.auctionName = auctionName;
        this.auctionId = rand.nextInt(Integer.MAX_VALUE);
        this.minimumBids = minimumBids;
        this.productID = productID;
    }

    public static Auction initializeAuction(Node node, String auctionName, float minimumBids, byte[] productID){
        Auction auction = new Auction(node.getNodeInfo().getId(),auctionName,minimumBids, productID);

        return auction;
    }

}
