package org.dei._Facilities.Terminal.WarehouseServices;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.QuarantineItem;
import org.dei.Sprint1._Item.Reason;
import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;
import org.dei._Facilities.Terminal.Warehouse.Quarantine;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * The {@code RestockService} class is responsible for processing and managing
 * the restocking of items from the quarantine area of a {@link Warehouse}.
 *
 * This service retrieves quarantined items, determines whether they should be
 * discarded or restocked, and generates a formatted report of all processed
 * operations.
 *
 * <p><b>Processing logic:</b></p>
 * <ul>
 *     <li>Items with reason {@link Reason#DAMAGED} or {@link Reason#EXPIRED} are discarded.</li>
 *     <li>All other items are restocked into the warehouse.</li>
 *     <li>Each successfully processed item is recorded in a tabular report format.</li>
 * </ul>
 *
 * <p><b>Output format:</b></p>
 * The report is returned as a string formatted in a table, with the following columns:
 * <pre>
 * ┌───────────────────────┬──────────────┬──────────────┬────────────┬────────┐
 * │ Timestamp             │ Return ID    │ SKU          │ Action     │ Qty    │
 * ├───────────────────────┼──────────────┼──────────────┼────────────┼────────┤
 * │ 2025-10-22T15:00:12   │ RET12345     │ SKU0001      │ RESTOCKED  │ 10     │
 * │ 2025-10-22T15:05:09   │ RET12346     │ SKU0002      │ DISCARDED  │ N/A    │
 * └───────────────────────┴──────────────┴──────────────┴────────────┴────────┘
 * </pre>
 *
 */
public class RestockService {

    /**
     * Processes all items currently in the quarantine of the given {@link Warehouse},
     * deciding whether to restock or discard them based on their {@link Reason}.
     * <p>
     * When restocking:
     * <ul>
     *     <li>A new {@link Box} is created with ID format {@code "RET-" + returnId}.</li>
     *     <li>The item is stored in the warehouse at position (0, 0).</li>
     * </ul>
     * If storage fails at any point, processing stops immediately.
     *
     * @param warehouse the {@link Warehouse} whose quarantine items will be processed
     * @return a formatted string report listing all processed quarantine items
     */
    public static String restock(Warehouse warehouse, int warehouseIndex, List<Box> addedBoxes) {
        StringBuilder s = new StringBuilder();
        Quarantine quarantine = warehouse.getQuarantine();
        QuarantineItem quI;

        s.append("┌───────────────────────┬──────────────┬──────────────┬────────────┬────────┐\n");
        s.append("│ Timestamp             │ Return ID    │ SKU          │ Action     │ Qty    │\n");
        s.append("├───────────────────────┼──────────────┼──────────────┼────────────┼────────┤\n");

        while ((quI = quarantine.poll()) != null) {
            String timestamp = quI.getTimestamp().toString();
            String returnId = quI.getId();
            String sku = quI.getItem().getSku();

            if (quI.getReason() == Reason.DAMAGED || quI.getReason() == Reason.EXPIRED) {
                s.append(String.format("│ %-19s │ %-12s │ %-12s │ %-10s │ %-6s │\n",
                        timestamp,
                        returnId,
                        sku,
                        "DISCARDED",
                        "N/A"));
            } else {
                int qty = quI.getQty();
                Item item = quI.getItem();
                LocalDate expireDate = quI.getExpireDate() == null ? null : quI.getExpireDate();
                LocalDateTime receivedAt = LocalDateTime.now();
                String BoxID = "RET-" + quI.getId(); //AC "boxId=”RET-”+returnId"
                Box newBox = new Box(BoxID, qty, item, expireDate, receivedAt);

                WarehousePosition p = new WarehousePosition(0, 0, warehouseIndex);
                if (warehouse.storeBox(newBox, p)) {
                    s.append(String.format("│ %-19s │ %-12s │ %-12s │ %-10s │ %-6d │\n",
                            timestamp,
                            returnId,
                            sku,
                            "RESTOCKED",
                            qty));
                    addedBoxes.add(newBox);
                } else {
                    break;
                }
            }
        }


        s.append("└───────────────────────┴──────────────┴──────────────┴────────────┴────────┘\n");

        return s.toString();
    }
}