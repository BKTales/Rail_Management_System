package org.dei.Sprint1._Item;

import java.sql.Timestamp;
import java.time.LocalDate;

public class QuarantineItem {
    private String id;
    private int qty;
    private Item item;
    private Reason reason;
    private LocalDate expireDate;
    private Timestamp timestamp;

    public QuarantineItem(String id, int qty, Item item, Reason reason, LocalDate expireDate, Timestamp timestamp) {
        this.id = id;
        this.qty = qty;
        this.item = item;
        this.reason = reason;
        this.expireDate = expireDate;
        this.timestamp = timestamp;
    }
    public String getId() {
        return id;
    }

    public int getQty() {
        return qty;
    }

    public Item getItem(){
        return item;
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }

    public Reason getReason(){
        return reason;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public String toString() {
        return reason.inString + " " + item.toString();
    }
}
