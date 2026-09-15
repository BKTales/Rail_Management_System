package org.dei.Sprint1.Order;

import org.dei.Sprint1._Item.Item;
import org.dei._Facilities.Terminal.Warehouse.Allocation;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a customer order containing multiple {@link OrderLine} items.
 * <p>
 * Each order has a unique identifier, a due date, a priority level, and a current {@link OrderStatus}.
 * The class provides methods to add items, check the overall status, and format the order details
 * in a structured, human-readable table format.
 * </p>
 *
 * <p>
 * Orders can contain multiple order lines for the same {@link Item}. If an item is added again,
 * its quantity is automatically increased instead of creating a duplicate line.
 * </p>
 *
 * <p>
 * The {@code Order} class implements {@link Comparable}, allowing comparison based on priority
 * and due date (higher priority and earlier due dates come first).
 * </p>
 *
 * @see OrderLine
 * @see OrderStatus
 * @see Item
 */
public class Order implements Comparable<Order> {
    private ArrayList<OrderLine> orderLines;
    private int                     priority;
    private LocalDateTime           dueDate;
    private String                  id ;
    private OrderStatus             status;

    /**
     * Constructs a new {@code Order} instance with a given ID, due date, and priority.
     *
     * @param id        unique identifier of the order
     * @param dueDate   date and time by which the order should be completed
     * @param priority  order priority (lower numbers usually indicate higher priority)
     */
    public Order(String id, LocalDateTime dueDate, int priority) {
        this.id = id;
        this.dueDate = dueDate;
        this.priority = priority;
        orderLines = new ArrayList<>();
        this.status = OrderStatus.UNDISPATCHABLE;
    }

    /**
     * Adds an item to the order.
     * <p>
     * If the item already exists in the order, the requested quantity is increased instead of creating
     * a new order line. Otherwise, a new {@link OrderLine} is added.
     * </p>
     *
     * @param item the {@link Item} to add to the order
     * @param qnt  the quantity requested for this item
     */
    public void addItem(Item item, int qnt) {
        for (OrderLine orderLine : orderLines)
        {
            if (orderLine.getItem().equals(item))
            {
                orderLine.addRequestedQty(qnt);
                return ;
            }
        }
        orderLines.add(new OrderLine(item, qnt));
    }

    /**
     * Checks whether this order contains any items.
     *
     * @return {@code true} if there are no order lines; {@code false} otherwise
     */
    public boolean isEmpty() {
        return orderLines.isEmpty();
    }

    /**
     * Returns the list of {@link OrderLine} objects in this order.
     *
     * @return a list of order lines
     */
    public ArrayList<OrderLine> getItems() {
        return orderLines;
    }

    /**
     * Returns the current status of the order, recalculated based on its order lines.
     *
     * @return the updated {@link OrderStatus} of the order
     */
    public OrderStatus getStatus() {
        this.status = calculateStatus();
        return status;
    }

    /**
     * Calculates the overall status of the order based on its order lines.
     * <ul>
     *     <li>If all lines are {@code ELIGIBLE}, the order is {@code ELIGIBLE}.</li>
     *     <li>If at least one line is {@code PARTIAL}, the order is {@code PARTIAL}.</li>
     *     <li>If any line is {@code UNDISPATCHABLE}, the order is {@code UNDISPATCHABLE}.</li>
     * </ul>
     *
     * @return the calculated {@link OrderStatus}
     */
    private OrderStatus calculateStatus() {
        if (orderLines == null || orderLines.isEmpty()) {
            return OrderStatus.UNDISPATCHABLE; // default se não há linhas
        }

        if (orderLines.stream().allMatch(line -> line.getStatus() == OrderStatus.ELIGIBLE)) {
            return OrderStatus.ELIGIBLE;
        }

        if (orderLines.stream().anyMatch(line -> line.getStatus() == OrderStatus.PARTIAL)) {
            return OrderStatus.PARTIAL;
        }

        if (orderLines.stream().anyMatch(line -> line.getStatus() == OrderStatus.UNDISPATCHABLE)) {
            return OrderStatus.UNDISPATCHABLE;
        }

        return OrderStatus.UNDISPATCHABLE;
    }

    /**
     * Returns the quantity of a specific {@link Item} requested in this order.
     *
     * @param item the item to look for
     * @return the requested quantity of the item, or {@code -1} if the item is not in the order
     */
     public int getItemQnt(Item item) {
         for (OrderLine orderLine : orderLines)
         {
             if (orderLine.getItem().equals(item))
                 return (orderLine.getRequestedQty());
         }
         return (-1);
     }


    /**
     * Returns the unique identifier of this order.
     *
     * @return the order ID
     */
    public String getId() {
        return id;
    }


    /**
     * Returns the due date of this order.
     *
     * @return the {@link LocalDateTime} representing the due date
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }


    @Override
    public String toString() {
        // Definição das larguras das colunas
        int skuWidth = 10;
        int reqWidth = 12;
        int allocWidth = 12;
        int whWidth = 12;
        int boxWidth = 12;
        int statusWidth = 15;
        int totalWidth = skuWidth + reqWidth + allocWidth + whWidth + boxWidth + statusWidth + 7;

        StringBuilder sb = new StringBuilder();

        // Função auxiliar para centralizar texto
        java.util.function.BiFunction<String, Integer, String> center = (text, width) -> {
            if (text == null) text = "";
            int padding = width - text.length();
            if (padding <= 0) return text;
            int left = padding / 2;
            int right = padding - left;
            return " ".repeat(left) + text + " ".repeat(right);
        };

        // Cabeçalho com o ID centralizado
        sb.append("╔").append("═".repeat(totalWidth - 2)).append("╗\n");
        String orderTitle = "ORDER " + id;
        int innerWidth = totalWidth - 2;
        int leftPadding = (innerWidth - orderTitle.length()) / 2;
        int rightPadding = innerWidth - orderTitle.length() - leftPadding;
        sb.append("║")
                .append(" ".repeat(leftPadding))
                .append(orderTitle)
                .append(" ".repeat(rightPadding))
                .append("║\n");
        sb.append("╠").append("═".repeat(totalWidth - 2)).append("╣\n");

        // Status
        String statusLine = "Status: " + status.getName();
        sb.append("║").append(String.format("%-" + innerWidth + "s", statusLine)).append("║\n");

        // Linha separadora
        sb.append("╠")
                .append("═".repeat(skuWidth)).append("╦")
                .append("═".repeat(reqWidth)).append("╦")
                .append("═".repeat(allocWidth)).append("╦")
                .append("═".repeat(whWidth)).append("╦")
                .append("═".repeat(boxWidth)).append("╦")
                .append("═".repeat(statusWidth)).append("╣\n");

        // Cabeçalho da tabela (centralizado também)
        sb.append("║")
                .append(center.apply("SKU", skuWidth)).append("║")
                .append(center.apply("Requested", reqWidth)).append("║")
                .append(center.apply("Allocated", allocWidth)).append("║")
                .append(center.apply("Warehouse", whWidth)).append("║")
                .append(center.apply("BoxID", boxWidth)).append("║")
                .append(center.apply("Status", statusWidth)).append("║\n");

        // Linha de separação abaixo do cabeçalho
        sb.append("╠")
                .append("═".repeat(skuWidth)).append("╬")
                .append("═".repeat(reqWidth)).append("╬")
                .append("═".repeat(allocWidth)).append("╬")
                .append("═".repeat(whWidth)).append("╬")
                .append("═".repeat(boxWidth)).append("╬")
                .append("═".repeat(statusWidth)).append("╣\n");

        // Linhas da tabela
        for (OrderLine ol : orderLines) {
            if (ol.getAllocations().isEmpty()) {
                sb.append("║")
                        .append(center.apply(ol.getItem().getSku(), skuWidth)).append("║")
                        .append(center.apply(String.valueOf(ol.getRequestedQty()), reqWidth)).append("║")
                        .append(center.apply(String.valueOf(ol.getAllocatedQty()), allocWidth)).append("║")
                        .append(center.apply("-", whWidth)).append("║")
                        .append(center.apply("-", boxWidth)).append("║")
                        .append(center.apply(ol.getStatus().getName(), statusWidth)).append("║\n");
            } else {
                boolean first = true;
                for (Allocation a : ol.getAllocations()) {
                    if (first) {
                        sb.append("║")
                                .append(center.apply(ol.getItem().getSku(), skuWidth)).append("║")
                                .append(center.apply(String.valueOf(ol.getRequestedQty()), reqWidth)).append("║")
                                .append(center.apply(String.valueOf(ol.getAllocatedQty()), allocWidth)).append("║")
                                .append(center.apply(a.getWarehouseId(), whWidth)).append("║")
                                .append(center.apply(a.getBoxId(), boxWidth)).append("║")
                                .append(center.apply(ol.getStatus().getName(), statusWidth)).append("║\n");
                        first = false;
                    } else {
                        sb.append("║")
                                .append(center.apply("", skuWidth)).append("║")
                                .append(center.apply("", reqWidth)).append("║")
                                .append(center.apply("", allocWidth)).append("║")
                                .append(center.apply(a.getWarehouseId(), whWidth)).append("║")
                                .append(center.apply(a.getBoxId(), boxWidth)).append("║")
                                .append(center.apply("", statusWidth)).append("║\n");
                    }
                }
            }
        }

        // Rodapé
        sb.append("╚")
                .append("═".repeat(skuWidth)).append("╩")
                .append("═".repeat(reqWidth)).append("╩")
                .append("═".repeat(allocWidth)).append("╩")
                .append("═".repeat(whWidth)).append("╩")
                .append("═".repeat(boxWidth)).append("╩")
                .append("═".repeat(statusWidth)).append("╝\n");

        return sb.toString();
    }



    /**
     * Compares this order with another based on priority and due date.
     * <ul>
     *     <li>If priorities are equal, the one with the earlier due date comes first.</li>
     *     <li>Otherwise, the order with the lower priority value comes first.</li>
     * </ul>
     *
     * @param o the other order to compare against
     * @return {@code 0} if both have the same priority and due date;
     *         {@code -1} if this order has higher priority;
     *         {@code 1} if the other order has higher priority
     */
    @Override
    public int compareTo(Order o) {
        if (priority == o.priority) {
            if (dueDate.isBefore(o.getDueDate())) // this.date is closer that other.date
                return (-1);
            return (1);
        }
        if (priority < o.priority)

         if (priority < o.priority)
            return (-1);
        return (1);
    }
}
