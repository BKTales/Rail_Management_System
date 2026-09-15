package org.dei._Facilities.Terminal.WarehouseServices.OrderAllocation;

import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1.Order.OrderLine;
import org.dei._Facilities.Terminal.Warehouse.Aisle;
import org.dei._Facilities.Terminal.Warehouse.Bay;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;

import java.util.List;

public class PartialMode {
    /**
     * Processes item allocation for a given {@link Item} in partial mode.
     * <p>
     * This method initiates a sequential traversal through the aisles and bays
     * of the specified {@link Warehouse}, attempting to fulfill the given
     * {@link OrderLine}. The process stops as soon as the requested quantity
     * is fully allocated or when no more stock is available.
     * </p>
     *
     * @param warehouse    the warehouse where allocation is performed
     * @param item         the item to allocate
     * @param remainingQty the remaining quantity to allocate
     * @param orderLine    the order line to be fulfilled
     * @return the remaining quantity that could not be allocated
     */
    static int processModePartial(Warehouse warehouse, Item item, int remainingQty, OrderLine orderLine) {
        int indexBay = 0;
        int indexAisle = 0;
        remainingQty = iterateAislesPartial(warehouse, item, remainingQty, orderLine, indexAisle, indexBay);

        return remainingQty;
    }

    /**
     * Iterates through the aisles of the given warehouse in partial mode.
     * <p>
     * For each aisle, the method attempts to allocate the item quantity
     * by calling {@link #iterateBaysPartial(Aisle, Item, Integer, OrderLine, String, int, int)}.
     * The iteration stops once the requested quantity is fulfilled.
     * </p>
     *
     * @param warehouse    the warehouse being processed
     * @param item         the item to allocate
     * @param qty          the remaining quantity to allocate
     * @param orderLine    the order line being fulfilled
     * @param indexAisle   the starting aisle index
     * @param indexBay     the starting bay index in the first aisle
     * @return the remaining quantity that could not be allocated
     */
    static private int iterateAislesPartial(Warehouse warehouse, Item item, Integer qty, OrderLine orderLine, int indexAisle, int indexBay){
        List<Aisle> aisles = warehouse.getAisles();
        while (indexAisle < aisles.size() && qty > 0)
        {
            Aisle aisle = aisles.get(indexAisle);
            qty = iterateBaysPartial(aisle, item, qty, orderLine, warehouse.getId(), indexAisle,indexBay);
            indexAisle++;
        }
        return qty;
    }

    /**
     * Iterates through the bays of a given aisle in partial mode.
     * <p>
     * For each bay, the method directly invokes iterateBoxesDirect()
     * to allocate items from the boxes inside that bay. If a bay is not full,
     * it is assumed to be the last one with available boxes in the aisle,
     * and the iteration stops.
     * </p>
     *
     * @param aisle        the aisle being processed
     * @param item         the item to allocate
     * @param qty          the remaining quantity to allocate
     * @param orderLine    the order line being fulfilled
     * @param warehouseId  the ID of the warehouse containing the aisle
     * @param aisleId      the index of the current aisle
     * @param indexBay     the starting bay index in the aisle
     * @return the remaining quantity that could not be allocated
     */
    static private int iterateBaysPartial(Aisle aisle, Item item, Integer qty, OrderLine orderLine, String warehouseId, int aisleId, int indexBay)  {
        List<Bay> bays = aisle.getBays();

        while (indexBay < bays.size() && qty > 0) {
            Bay bay = bays.get(indexBay);
//            if (bay.isFull())
//                qty = OrderAllocation.iterateBoxesDirect(bay, item, qty,orderLine,warehouseId, new Position(aisleId + 1,indexBay + 1, 0));
//            else
//                return (OrderAllocation.iterateBoxesDirect(bay, item,qty,orderLine,warehouseId, new Position(aisleId + 1,indexBay + 1, 0)));
//            indexBay++;
        }
        return qty;
    }
}
