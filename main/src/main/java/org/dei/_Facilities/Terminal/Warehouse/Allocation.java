package org.dei._Facilities.Terminal.Warehouse;

import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1.Repository.ItemRepository;
/**
 * Represents the allocation of a quantity of an {@link Item} from a specific warehouse and box
 * to an order line.
 * <p>
 * Each {@code Allocation} tracks the warehouse ID, box ID, quantity, item SKU, and the {@link WarehousePosition}
 * where the item was stored or picked from.
 * </p>
 */
public class Allocation {
    private String      warehouseId;
    private WarehousePosition warehousePosition;
    private String      boxId;
    private int         qty;
    private String      sku;

    /**
     * Constructs a new {@code Allocation}.
     *
     * @param warehousePosition    the {@link WarehousePosition} of the box in the warehouse
     * @param warehouseId the ID of the warehouse
     * @param boxId       the ID of the box containing the items
     * @param qty         the quantity allocated from the box
     * @param sku         the SKU of the item allocated
     */
    public Allocation(WarehousePosition warehousePosition, String warehouseId, String boxId, int qty, String sku) {
        this.warehouseId = warehouseId;
        this.boxId = boxId;
        this.qty = qty;
        this.warehousePosition = warehousePosition;
        this.sku = sku;
    }

    /**
     * Returns the SKU of the allocated item.
     *
     * @return the item SKU
     */
    public String getSku() {
        return sku;
    }

    /**
     * Returns the quantity allocated from this allocation.
     *
     * @return the allocated quantity
     */
    public int getQty() {
        return qty;
    }

    /**
     * Returns the total weight of the allocated quantity.
     *
     * @return the weight of the allocation (unit weight * quantity)
     */
    public double getWeighted() {
        Item item = ItemRepository.getInstance().getItemBySku(this.sku);
        return item.getUnitWeight() * this.qty;
    }

    /**
     * Returns the {@link Item} associated with this allocation.
     *
     * @return the allocated item
     */
    public Item getWeight(){
        Item item = ItemRepository.getInstance().getItemBySku(this.sku);

        return item;
    }

    /**
     * Returns the {@link WarehousePosition} of the allocation within the warehouse.
     *
     * @return the position
     */
    public WarehousePosition getPosition() {
        return warehousePosition;
    }

    /**
     * Returns the ID of the box used for this allocation.
     *
     * @return the box ID
     */
    public String getBoxId() {
        return boxId;
    }

    /**
     * Returns the ID of the warehouse where the allocation came from.
     *
     * @return the warehouse ID
     */
    public String getWarehouseId() {
        return warehouseId;
    }

    /**
     * Returns a string representation of the allocation.
     *
     * @return a formatted string showing warehouse, position, quantity, and box ID
     */
    @Override
    public String toString() {
        return "Allocation[WH:" + warehouseId +
                ", Pos:" + warehousePosition +
                ", Qty:" + qty +
                (boxId != null ? ", Box:" + boxId : "") + "]";
    }
}

