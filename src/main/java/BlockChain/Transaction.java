package BlockChain;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class is responsible for defining a transaction and suitable methods
 */
public class Transaction {
    /**
     * Unique identifier of an entity
     */
    byte[] sourceEntity;
    /**
     * Unique identifier of an entity
     */
    byte[] destEntity;
    /**
     * Unique product Id
     */
    byte[] productId;
    /**
     * If bidTrans >= 0 then it's a bid, otherwise it's a transfer
     */
    int bidTrans;

    public Transaction(byte[] sourceEntity, byte[] destEntity, byte[] productId, int bidTrans) {
        this.sourceEntity = sourceEntity;
        this.destEntity = destEntity;
        this.productId = productId;
        this.bidTrans = bidTrans;
    }

    /**
     * Gets the hash that represent the transaction
     * @return The SHA-256 hash with the important fields used
     * @throws NoSuchAlgorithmException If the hashing algorithm is not found
     */
    public byte[] getHash() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        int byteArraySize = sourceEntity.length + destEntity.length + productId.length + 4;

        byte[] byteArray = new byte[byteArraySize];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        byteBuffer.put(sourceEntity);
        byteBuffer.put(destEntity);
        byteBuffer.put(productId);
        byteBuffer.put(HashAlgorithm.intToByte(bidTrans));

        return digest.digest(byteBuffer.array());
    }

    /**
     * Translates an entity into something we can read
     * @param entity byte arrays containing an identifier of the entity
     * @return A string readable of the entity identifier
     */
    private String toStringEntity(byte[] entity) {
        return HashAlgorithm.byteToHex(entity);
    }

    @Override
    public String toString() {

        return "Transaction{" +
                "\nSourceEntity=" +
                toStringEntity(sourceEntity) +
                "\nDestEntity=" +
                toStringEntity(destEntity) +
                "\nProductId=" +
                toStringEntity(productId) +
                "\nType=" +
                (bidTrans >= 0 ? "bid" : "transfer") +
                "\n}";
    }
}
