package org.dei.Sprint1._Item;


public class Item {
    private String      sku;
    private Unit        unit;
    private double      volume;
    private ItemType    itemType;
    private double      unitWeight;


    public Item(Unit unit, String sku, double volume, ItemType itemType, double unitWeight) {
        this.sku = sku;
        this.unit = unit;
        this.volume = volume;
        this.itemType = itemType;
        this.unitWeight = unitWeight;
    }

    /* ======================= Getter ======================= */
    public double getUnitWeight() {
        return unitWeight;
    }

    public double getVolume() {
        return volume;
    }

    public String getSku() {
        return sku;
    }

    public Unit getUnit() {
        return unit;
    }

    public ItemType getItemType() {
        return itemType;
    }

    /* ======================= Getter ======================= */

    @Override
    public String toString() {
        return sku + " " + itemType.inString + " " + unit.inString + " " + volume;
    }
}
