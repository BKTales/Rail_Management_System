package org.dei.Sprint1._Item;

import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class representing a Box in a warehouse.
 * Each box contains a quantity of a specific item and may have an expiration date.
 */
public class Box implements Comparable<Box> {
    private int               qntY;
    private final String         boxID;
    private final Item        product;
    private final LocalDate        expireDate;
	private final LocalDateTime receivedAt;

    private WarehousePosition pos;

    /**
     * Constructor to create a Box with the given information.
     *
     * @param boxID      Unique identifier for the box
     * @param qntY       Quantity of items in the box
     * @param product    Item stored in the box
     * @param expireDate Expiration date (nullable)
     * @param receivedAt Timestamp when the box was received
     */
    public Box(String boxID, int qntY, Item product, LocalDate expireDate, LocalDateTime receivedAt) {
        this.boxID = boxID;
        this.qntY = qntY;
        this.product = product;
        this.expireDate = expireDate;
        this.receivedAt = receivedAt;

        // they are started at -1, and only receive index once they arrive at the warehouse!
        pos = new WarehousePosition(-1, -1, -1);
    }

    // ===================== Getters ===================== //

    /** @return the unique ID of the box */
    public String getBoxID() {
        return boxID;
    }

    /** @return the current quantity of items in the box */
    public int getQntY() {
        return qntY;
    }

    /**
     * Calculate the weight of all the products inside the box
     * @return total weight in box
     */
    public double getWeight() {
        return qntY * product.getUnitWeight() ;
    }

    /**
     * Decreases the quantity by the given amount.
     *
     * @param many number of items to take from the box
     * @return remaining quantity in the box
     */
    public int takeQntY(int many) {
        this.qntY -= many;
        return this.qntY;
    }

    /** @return the product stored in the box */
    public Item getProduct() {
        return product;
    }

    /** @return the expiration date of the box, may be null */
    public LocalDate getExpireDate() {
        return expireDate;
    }

    /** @return timestamp when the box was received */
    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    /** @return the SKU of the item inside the box */
    public String getSku() {
        return product.getSku();
    }

    /** @return the aisle index of the box in the warehouse */
    public int getAisleIndex() {
        return pos.getAisleIndex();
    }

    /** @return the bay index of the box in the warehouse */
    public int getBayIndex() {
        return pos.getBayIndex();
    }

    /** @return the warehouse index where the box is stored */
    public int getWarehouseIndex() {
        return pos.getWarehouseIndex();
    }
    // ===================== Setters ===================== //
    /** @param pos sets the current position of the box in the warehouse */
    public void setPosition(WarehousePosition pos) {
        this.pos = pos;
    }

    /** @param warehouseIndex sets the warehouse index for the box */
    public void setWarehouseIndex(int warehouseIndex) {
        pos.setWarehouseIndex(warehouseIndex);
    }

    /** @param bayIndex sets the bay index for the box */
    public void setBayIndex(int bayIndex) {
        pos.setBayIndex(bayIndex);
    }

    /** @param aisleIndex sets the aisle index for the box */
    public void setAisleIndex(int aisleIndex) {
        pos.setAisleIndex(aisleIndex);
    }
    // ===================== Other ===================== //

    /**
     * Function will take a given quantity from the box,
     * if quantity exceed the quantity inside the box, it will take
     * only what it has to offer.
     * @param remainingQty requested quantity to take
     * @return quantity actually taken
     */
    public int takeQuantity(int remainingQty) {
        int allocated = Math.min(remainingQty, qntY);
        qntY -= allocated;
        return allocated;
    }

    @Override
    public int compareTo(Box other) {
        if (this.getExpireDate() != null && other.getExpireDate() != null) {
            if(this.getExpireDate().equals(other.getExpireDate())){
                if(this.getReceivedAt().equals(other.getReceivedAt())){
                    return this.getBoxID().compareTo(other.getBoxID());
                } else {
                    return this.getReceivedAt().compareTo(other.getReceivedAt());
                }
            } else {
                return this.getExpireDate().compareTo(other.getExpireDate());
            }
        } else if (this.getExpireDate() != null) {
            return -1;
        } else if (other.getExpireDate() != null) {
            return 1;
        } else {
            if(this.getReceivedAt().equals(other.getReceivedAt())){
                return this.getBoxID().compareTo(other.getBoxID());
            } else {
                return this.getReceivedAt().compareTo(other.getReceivedAt());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return Objects.equals(boxID, box.boxID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxID);
    }

    @Override
    public String toString() {
        return "BoxID=" + boxID +
                ", qntY=" + qntY +
                ", product=" + product.toString() +
                '}';
    }
}
