package org.dei._Facilities.Terminal;


import org.dei.Utils.Exceptions.WarehousesFullException;
import org.dei._Facilities.Facility;
import org.dei._Facilities.Terminal.Warehouse.PickupPlan;
import org.dei._Facilities.Terminal.Warehouse.*;
import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint1.Order.Order;
import org.dei.Sprint1.Order.OrderLine;
import org.dei.Sprint1.Order.OrderStatus;
import org.dei._Facilities.Terminal.WarehouseServices.RestockService;
import org.dei._Train.Wagon;

import org.dei.Utils.Utils;
import org.dei.Sprint1.Parser.DoubleDefinitionException;

import java.util.*;

public class Terminal extends Facility {
    private PriorityQueue<Order>                orders;
    private List<Warehouse>                     warehouses;
    private List<PickupPlan>                    pickupPlans;
    private HashMap<String, PriorityQueue<Box>> itemHash;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";


    public Terminal(GeographicalLocation location, TimeZoneGroup timeZoneGroup, TimeZone timeZone,
                    Country country, String name, int id) {
        super(location, timeZoneGroup, timeZone, country, name, id);
        this.orders = new PriorityQueue<>();
        this.warehouses = new ArrayList<>();
        this.pickupPlans = new ArrayList<>();
        this.itemHash = new HashMap<>();
    }

    // don't erase this constructor otherwise the tests from sprint 1 don't compile because they are outdated and need an empty constructor :)
    public Terminal() {
        super();
        this.orders = new PriorityQueue<>();
        this.warehouses = new ArrayList<>();
        this.pickupPlans = new ArrayList<>();
        this.itemHash = new HashMap<>();
    }


    // ================ Getters ================ //
    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public PriorityQueue<Order> getOrders() {
        return orders;
    }

    /**
     * Function will get an order by its index
     *
     * @param orderIndex
     * @return (Order - if valid index) (null - indexOutOfBounds)
     */
    public Order getOrder(int orderIndex) {
        int index = 0;

        for (Order order : orders) {
            if (index == orderIndex)
                return order;
            index++;
        }
        return null;
    }


    /**
     * Function search through the orders and return the
     * @return (The first item of orders() that is not eligible) (null - no order to fulfill)
     */
    public Order getOrderToFulfill(){
        List<Order> orderListTemp = new ArrayList<>();
        Order toReturn = null;

        while (!orders.isEmpty() && toReturn == null)
        {
            Order order = orders.poll();
            orderListTemp.add(order); // saving all the compared orders

            if (order.getStatus() != OrderStatus.ELIGIBLE)
                toReturn =  order;
        }
        if (!orderListTemp.isEmpty())
            orders.addAll(orderListTemp); // adding the items back to the PQ

        return (toReturn); // no order to fulfill
    }

    /**
     * Function search through the orders and return the
     * @return (The first item of orders() that is eligible) (null - no order to plan)
     */
    public Order getOrderToPlan(){
        List<Order> orderListTemp = new ArrayList<>();
        Order toReturn = null;

        while (!orders.isEmpty() && toReturn == null)
        {
            Order order = orders.poll();
            orderListTemp.add(order); // saving all the compared orders

            if (order.getStatus() == OrderStatus.ELIGIBLE)
                toReturn =  order;
        }
        if (!orderListTemp.isEmpty())
            orders.addAll(orderListTemp); // adding the items back to the PQ

        return (toReturn);
    }

    public int getWarehouseIndexOf(String warehouseId){
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getId().equals(warehouseId)) {
                return (warehouses.indexOf(warehouse));
            }
        }
        return -1;
    }

    public HashMap<String, PriorityQueue<Box>> getItemHash() {
        return itemHash;
    }

    public void printItemInHash() {
        System.out.println("in here!");
        for (String key : itemHash.keySet()) {
            System.out.println("Key: " + key);

            PriorityQueue<Box> boxes = itemHash.get(key);

            if (boxes == null || boxes.isEmpty()) {
                System.out.println("  (no boxes)");
            } else {
                for (Box box : boxes) {
                    System.out.println("  " + box);
                    System.out.println(box.getAisleIndex() + " " + box.getBayIndex());
                }
            }
        }
    }


    // ================ Adds ================ //

    /**
     * Function add the order to the PriorityQueue of order
     * if the order isn't already in there!
     *
     * @param order Order
     */
    public void addOrder(Order order) {
        if (!orders.contains(order))
            orders.add(order);
    }

    public  void removeOrder() {
        orders.poll();
    }

    public void addWarehouse(Warehouse warehouse) {
        this.warehouses.add(warehouse);
    }

    /**
     * Add an item to an order with the given orderId
     *
     * @param orderId String
     * @param item    Item
     * @param qnt     int
     */
    public void addToOrder(String orderId, Item item, int qnt) {
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                order.addItem(item, qnt);
                return;
            }
        }
    }

    /**
     * Add to a given warehouseId the bay with the given capacity for boxes
     * if the wanted bay isn't already allocated(if it is already allocated
     * a exception will be thrown)
     *
     * @param warehouseId String
     * @param aisle       int
     * @param bay         int
     * @param boxCapacity int
     * @throws DoubleDefinitionException
     */
    public void addBayToWarehouse(String warehouseId, int aisle, int bay, int boxCapacity) throws DoubleDefinitionException {
        boolean hasWarehouse = false;

        for (Warehouse warehouse : warehouses) {
            if (warehouse.getId().equals(warehouseId)) {
                if (!warehouse.hasAisle(aisle))
                    warehouse.addAisle(new Aisle());
                if (!warehouse.getAisle(aisle).hasBay(bay))
                    warehouse.getAisle(aisle).addBay(new Bay(boxCapacity));
                else
                    throw new DoubleDefinitionException("bay was already previously defined!");
                hasWarehouse = true;
                break;
            }
        }

        if (!hasWarehouse) {
            Warehouse tmp = new Warehouse(warehouseId);
            addWarehouse(tmp);
            tmp.addAisle(new Aisle());
            tmp.getAisle(aisle).addBay(new Bay(boxCapacity));
        }
    }

    private void addToItemHash(Box box) {
        PriorityQueue<Box> pq = itemHash.get(box.getSku());

        if (pq != null) // already exist the sky in storage
            pq.add(box);
        else // creates the priority queue and adds the sku as key and its PriorityQueue
        {
            pq = new PriorityQueue<>();
            pq.add(box);
            itemHash.put(box.getSku(), pq);
        }
    }

    public  String restockWarehouse(){
        StringBuilder sb = new StringBuilder();
        List<Box> boxes = new ArrayList<>();

        int i = 0;
        for (Warehouse warehouse : warehouses)
        {
            sb.append(RestockService.restock(warehouse,i, boxes));
            i++;
        }
        for (Box box : boxes)
            addToItemHash(box);
        return sb.toString();
    }

    // ================ Has ================ //

    /**
     * Function will check if there is a given orderId inside the defined ones
     *
     * @param orderId String
     * @return (true - if it has) (false - if it does not have)
     */
    public boolean hasOrder(String orderId) {
        for (Order order : orders) {
            if (order.getId().equals(orderId))
                return true;
        }
        return false;
    }

    /**
     * Function will check if there is the given warehouseId inside the defined ones
     *
     * @param warehouseId String
     * @return (true - if it does) (false - if it doesn't)
     */
    public Warehouse getWarehouse(String warehouseId) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getId().equals(warehouseId))
                return warehouse;
        }
        return null;
    }

    // ================ Logic Functions ================ //

    public String []listPlans(){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (pickupPlans.isEmpty())
            return null;
        for (PickupPlan pickupPlan : pickupPlans) {
            sb.append("[" + i + "]\n\n" + pickupPlan.toString() + "\n");
            i++;
        }
        return sb.toString().split("\n");
    }

    public PickupPlan getPlanByIndex(int index){
        return pickupPlans.get(index);
    }

    public boolean unloadWagon(Wagon wagon) throws WarehousesFullException {
        if (firstAvailableWarehouse() == null){
            throw new WarehousesFullException(" IMPLEMENT IT !");
        }

        boolean stored;
        int currentWarehouse = 0;
        int wagonSize = wagon.getSize();
        WarehousePosition p = new WarehousePosition(0, 0, 0); // started to 0

        /// each box
        for ( int i = 0; i < wagonSize; i++ )
        {
            p.setWarehouseIndex(currentWarehouse); // add
            stored = false;
            Box box =  wagon.getUnloadBox();
            /// each warehouse
            while (currentWarehouse < warehouses.size() && !stored)
            {
                /// inside the warehouse(aisle and bays)
                if (warehouses.get(currentWarehouse).storeBox(box, p)){
                    stored = true;
                    addToItemHash(box);
                }
                else
                {
                    ///  reset the indexes and go to next warehouse!
                    p.setBayIndex(0);
                    p.setAisleIndex(0);
                    currentWarehouse++;
                }
               // System.out.println(stored + " " + p.toString());
            }
            if (!stored)
            {
               // System.out.println("All warehouses are full, cannot store more items!");
                break;
            }
        }
        return true;
    }

    public List<PickupPlan> getPlans () {
        return pickupPlans;
    }

    public void addPlan (PickupPlan pickupPlan){
        this.pickupPlans.add(pickupPlan);
    }

    public PickupPlan getPlan(Order ole, int indexMode, double capacity) {
        if(capacity > calculateHeaviestItem(ole)){
            return switch (indexMode) {
                case 0 -> firstFit(ole, capacity);
                case 1 -> firstFitDecreasing(ole, capacity);
                case 2 -> bestFitDecreasing(ole, capacity);
                default -> null;
            };
        }else{
            throw new ExceptionInInitializerError("The trolley must have capacity to hold at least one item.");
        }

    }

    private double calculateHeaviestItem(Order ole){

        double heaviestItemWeight = 0;

        for (int i = 0; i < ole.getItems().size(); i++) {
            if(ole.getItems().get(i).getItem().getUnitWeight() > heaviestItemWeight)
                heaviestItemWeight = ole.getItems().get(i).getItem().getUnitWeight() * ole.getItemQnt(ole.getItems().get(i).getItem());
        }

        System.out.println(heaviestItemWeight);

        return heaviestItemWeight;
    }

    public PickupPlan firstFit (Order ole, double capacity){
        PickupPlan newPickupPlan = new PickupPlan(Heuristic.FF);
        List<Allocation> allocations = new ArrayList<>();
        for (OrderLine ol : ole.getItems()) {
            allocations.addAll(ol.getAllocations());
        }
        firstFit(newPickupPlan, allocations, capacity);

        addPlan(newPickupPlan);

        return (newPickupPlan);
    }

    public void firstFit (PickupPlan pickupPlan, List<Allocation> allocations, double capacity){
        Trolley lastTrolley = pickupPlan.getLastTrolley();

        for (Allocation a : allocations) {
            boolean placed = false;


            if (lastTrolley != null && lastTrolley.addAllocation(a)) {
                placed = true;
            } else {
                for (Trolley t : pickupPlan.getTrolleys()) {
                    if (t.addAllocation(a)) {
                        placed = true;
                        lastTrolley = t;
                        break;
                    }
                }
            }

            if (!placed) {
                Trolley t = new Trolley(capacity);
                if(t.addAllocation(a)) {
                    pickupPlan.addTrolley(t);
                    lastTrolley = t;
                }
            }
        }
    }

    public PickupPlan firstFitDecreasing (Order ole, double capacity){
        PickupPlan newPickupPlan = new PickupPlan(Heuristic.FFD);
        List<Allocation> allocations = new ArrayList<>();
        for (OrderLine ol : ole.getItems()) {
            allocations.addAll(ol.getAllocations());
        }
        firstFitDecreasing(newPickupPlan, allocations, capacity);

        addPlan(newPickupPlan);

        return (newPickupPlan);
    }

    public void firstFitDecreasing (PickupPlan pickupPlan, List<Allocation> allocations, double capacity){
        allocations = Utils.mergeSortAllocationDESC(allocations);
        Trolley lastTrolley = pickupPlan.getLastTrolley();

        for (Allocation a : allocations) {
            boolean placed = false;

            if (lastTrolley != null && lastTrolley.addAllocation(a)) {
                placed = true;
            } else {
                for (Trolley t : pickupPlan.getTrolleys()) {
                    if (t.addAllocation(a)) {
                        placed = true;
                        lastTrolley = t;
                        break;
                    }
                }
            }

            if (!placed) {
                Trolley t = new Trolley(capacity);
                if(t.addAllocation(a)) {
                    pickupPlan.addTrolley(t);
                    lastTrolley = t;
                }
            }
        }
    }

    public PickupPlan bestFitDecreasing (Order ole, double capacity){
        PickupPlan newPickupPlan = new PickupPlan(Heuristic.BFD);
        List<Allocation> allocations = new ArrayList<>();

        for (OrderLine ol : ole.getItems()) {
            allocations.addAll(ol.getAllocations());
        }
        bestFitDecreasing(newPickupPlan, allocations, capacity);

        addPlan(newPickupPlan);
        return (newPickupPlan);
    }

    public void bestFitDecreasing (PickupPlan pickupPlan, List<Allocation> allocations, double capacity){
        allocations = Utils.mergeSortAllocationDESC(allocations);
        List<Trolley> trolleys = pickupPlan.getTrolleyList();
        List<Double> remainingCapacities = new ArrayList<>();

        for (Trolley trolley : trolleys) {
            remainingCapacities.add(capacity - trolley.getCurrentWeight());
        }


        for (Allocation alloc : allocations) {
            int bestTrolleyIndex = -1;
            double smallestRemaining = Double.MAX_VALUE;

            for (int i = 0; i < trolleys.size(); i++) {
                double remaining = remainingCapacities.get(i);
                double itemWeight = getItemWeight(alloc,alloc.getWeight());

                if (remaining >= itemWeight && remaining < smallestRemaining) {
                    smallestRemaining = remaining;
                    bestTrolleyIndex = i;
                }
            }

            if (bestTrolleyIndex != -1) {
                trolleys.get(bestTrolleyIndex).addAllocation(alloc);
                remainingCapacities.set(bestTrolleyIndex, remainingCapacities.get(bestTrolleyIndex) - getItemWeight(alloc, alloc.getWeight()));
            } else {
                Trolley newTrolley = new Trolley(capacity);
                if(newTrolley.addAllocation(alloc)) {
                    trolleys.add(newTrolley);
                    remainingCapacities.add(capacity - getItemWeight(alloc,alloc.getWeight()));
                }

            }
        }
    }

    private double getItemWeight (Allocation allocation,Item item){
            return item.getUnitWeight() * allocation.getQty();
        }

    public Warehouse firstAvailableWarehouse () {
        for (Warehouse w : getWarehouses()) {
            if (w.availableSpace() != 0) {
                return w;
            }
        }
        return null;
    }

    // ================ Display functions ================ //

    public String showOrders () {
        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            sb.append(order.toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * Function will list all the order of the terminal
     * @return String will all the Orders with their id's
     */
    public String listOrders () {
        StringBuilder sb = new StringBuilder();

        for (Order order : orders) {
            sb.append(order.toString() + "\n\n");
        }
        return sb.toString();
    }

    public static String listPickModes () {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Heuristic heuristic : Heuristic.values()) {
            sb.append("[" + i + "] " + heuristic.toString() + "\n");
            i++;
        }
        return sb.toString();
    }

    public String toString () {
        StringBuilder s = new StringBuilder();

        if (warehouses.isEmpty())
            return "Terminal is empty!";
        for (Warehouse warehouse : warehouses)
            s.append(warehouse.toString()).append("\n");

        // System.out.println("many sku in hash map: " + itemHash.size());
        return (s.toString());
    }
}
