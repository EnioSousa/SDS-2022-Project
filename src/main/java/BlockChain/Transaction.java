package BlockChain;

/**
 * Class is responsible for
 */
public class Transaction {
    /**
     * Unique identifier of an entity
     */
    byte[] entityA;
    /**
     * Unique identifier of an entity
     */
    byte[] entityB;
    /**
     * Unique product Id
     */
    byte[] productId;
    /**
     * If bidTrans == 0x01 then A bids on product from B, otherwise A transfers product to B
     */
    byte bidTrans;

    public Transaction(byte[] entityA, byte[] entityB, byte[] productId, byte bidTrans) {
        this.entityA = entityA;
        this.entityB = entityB;
        this.productId = productId;
    }
}
