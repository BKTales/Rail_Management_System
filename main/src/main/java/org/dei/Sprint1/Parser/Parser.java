package org.dei.Sprint1.Parser;

import org.dei.Sprint1.Order.Order;
import org.dei.Sprint1.Repository.ItemRepository;
import org.dei.Sprint1._Item.*;
import org.dei._Facilities.FrightYard.FreightYard;
import org.dei._Facilities.Terminal.Terminal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.dei._Facilities.Terminal.Warehouse.Quarantine;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;
import org.dei._Train.Wagon;

public class Parser {

    private final static String itemsFile = "src/main/resources/train_station_dataset/items.csv";
    private static final String orderLinesFile = "src/main/resources/train_station_dataset/order_lines.csv";
    private static final String ordersFile = "src/main/resources/train_station_dataset/orders.csv";
    private static final String baysFile = "src/main/resources/train_station_dataset/bays.csv";
    private static final String returnsFile = "src/main/resources/train_station_dataset/returns.csv";
    private static final String wagonsFile = "src/main/resources/train_station_dataset/wagons.csv";
    
    public static void parseItems(ItemRepository itemRepository) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(itemsFile))){
            String line = bufferedReader.readLine();

            while((line = bufferedReader.readLine()) != null) {
                String[] atributes = line.split(",");

                if ( atributes.length < 6){
                    System.out.println("Line with missing atributes: " + line);
                    continue;//Missing atributes
                }
                String sku = atributes[0];
                String name = atributes[1];
                ItemType itemType = ItemType.fromString(atributes[2]);

                if (itemType == null)
                    System.out.println("Line with invalid item type: " + line);

                Unit unit = Unit.fromString(atributes[3]);

                if (unit == null)
                    System.out.println("Line with invalid unit: " + line);


                double volume = Double.parseDouble(atributes[4]);
                double  unitWeight = Double.parseDouble(atributes[5]);

                if(itemRepository.hasThisItem(sku)){
                    System.out.println("Item with repeated SKU: " + sku);
                    continue;//Repeated SKU
                }

                Item item = new Item(unit, sku, volume, itemType, unitWeight);
                itemRepository.addItem(item);

            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + itemsFile);
        } catch (Exception e) {
            System.err.println("Error parsing file: " + itemsFile);
        }
    }

    public static void parseReturns(Terminal terminal) {
        ItemRepository itemRepository = ItemRepository.getInstance();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(returnsFile))){
            String header = bufferedReader.readLine();
            String line;
            int currentWarehouse = 0;

            while ((line = bufferedReader.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] attr = line.split(",", -1);
                if (attr.length < 6) {
                    System.err.println("missing atributes: " + line);
                    continue;
                }

                String returnId = attr[0].trim();
                String sku = attr[1].trim();
                Item item = itemRepository.getItem(sku);
                int qty = Integer.parseInt(attr[2].trim());

                String reasonStr = attr[3].trim().replace("-", "_").toUpperCase();
                Reason reason = Reason.fromString(reasonStr);
                if (reason == null) {
                    System.err.println("Invalid reason: " + reasonStr + " returnID " + returnId);
                    continue;
                }

                LocalDateTime timestamp = LocalDateTime.parse(attr[4].trim());
                LocalDate expireDate = attr[5].isEmpty() ? null : LocalDate.parse(attr[5].trim());

                QuarantineItem quarantineItem = new QuarantineItem(
                        returnId, qty, item, reason, expireDate, Timestamp.valueOf(timestamp)
                );

                boolean stored = storeInQuarantine(terminal, quarantineItem, currentWarehouse);

                if (!stored) {
                    System.err.println("[WARNING] No Space in Terminal for " + returnId + " (" + sku + ")");
                }
            }

        } catch (IOException e){
            System.err.println("Error reading Return file: " + returnsFile);
        } catch (Exception e){
            System.err.println("Error processing return line " + ": " + e.getMessage());
        }
    }

    private static boolean storeInQuarantine(Terminal terminal, QuarantineItem quarantineItem, int startWarehouse) {
        int currentWarehouse = startWarehouse;

        while (currentWarehouse < terminal.getWarehouses().size()) {
            Warehouse warehouse = terminal.getWarehouses().get(currentWarehouse);
            Quarantine quarantine = warehouse.getQuarantine();

            int spaceLeft = quarantine.getMAX_ITEMS() - quarantine.getCurrentItems();
            if (spaceLeft > 0) {
                quarantine.addItem(quarantineItem);
                return true;
            } else{
                currentWarehouse++;
            }
        }
        return false;
    }

    public static void parseBays(Terminal terminal) {
        int         i = 0;
        String      line;
        String[]    attr;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(baysFile))) {
            int attrQnt = bufferedReader.readLine().split(";").length; // first line

            while ((line = bufferedReader.readLine()) != null) {
                attr = line.split(";");
                int bay = Integer.parseInt(attr[2]);
                int aisle = Integer.parseInt(attr[1]);
                int capacityBoxes = Integer.parseInt(attr[3]);

                if (!hasSameItemQuantity(attrQnt, attr))
                    System.out.println("Line " + i + ": missing attributes: \"" + line + "\"");
                else if (bay <= 0 || capacityBoxes <= 0 || aisle <= 0)
                    System.out.println("Line " + i + ": bad definition of values: \"" + line + "\"");
                else
                {
                    try {
                        terminal.addBayToWarehouse(attr[0], aisle -1 , bay-1, capacityBoxes);
                    } catch (DoubleDefinitionException e) {
                        System.out.println("Line " + i + ": " + e + "\"" + line + "\"");
                    }
                }
                i++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + ordersFile);
        }
    }

    public static void parseWagons(FreightYard freightYard) {
        ItemRepository itemRepository = ItemRepository.getInstance();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(wagonsFile))) {
            String header = bufferedReader.readLine();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] attributes = line.split(",", -1);

                if (attributes.length < 6){
                    System.out.println("Line with missing attributes: " + line);
                    continue;
                }

                // wagonId, boxId, sku, qty, expiryDate, receivedAt
                String wagonId = attributes[0].trim();
                String boxIdStr = attributes[1].trim();
                String sku = attributes[2].trim();
                String qtyStr = attributes[3].trim();
                String expiryDateStr = attributes[4].trim();
                String receivedAtStr = attributes[5].trim();

                if (!itemRepository.hasThisItem(sku)) {
                    System.out.println("Unknown SKU: " + sku);
                    continue;
                }

                int qty;
                try {
                    qty = Integer.parseInt(qtyStr);
                    if (qty <= 0) {
                        System.out.println("Invalid quantity (" + qty + ") for line: " + line);
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity format: " + qtyStr);
                    continue;
                }

                LocalDateTime receivedAtDate;
                try {
                    receivedAtDate = LocalDateTime.parse(receivedAtStr);
                } catch (Exception e) {
                    System.out.println("Invalid receivedAt format: " + receivedAtStr);
                    continue;
                }

                //(nullable)
                LocalDate expiryDate = null;
                if (!expiryDateStr.isEmpty()){
                    try {
                        expiryDate = LocalDate.parse(expiryDateStr);

                        if (expiryDate.isBefore(receivedAtDate.toLocalDate())){
                            System.out.println("Warning: expiryDate before receivedAt for " + boxIdStr);
                        }

                    } catch (Exception e){
                        System.out.println("Invalid expiryDate format: " + expiryDateStr);
                        continue;
                    }
                }
                Wagon wagon = freightYard.getOrCreateWagon(wagonId);

                Box box = new Box(boxIdStr, qty, itemRepository.getItemBySku(sku), expiryDate, receivedAtDate);
                wagon.addBox(box);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + wagonsFile);
        } catch (Exception e) {
            System.err.println("Unexpected error while parsing wagons: " + e.getMessage());
        }
    }

    public static void parseOrder(ItemRepository itemRepo, Terminal terminal) {
        int         i = 0;
        String      line;
        String[]    attr;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ordersFile))) {
            int attrQnt = bufferedReader.readLine().split(",").length; // first line

            while ((line = bufferedReader.readLine()) != null)
            {
                attr = line.split(",");

                if (!hasSameItemQuantity(attrQnt, attr))
                    System.out.println("Line " + i + " with missing attributes: \"" + line + "\"");
                else
                    terminal.addOrder(new Order(attr[0], LocalDateTime.parse(attr[1]), Integer.parseInt(attr[2])));
                i++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + baysFile);
        }
        parseOrderLines(itemRepo, terminal);
    }

    private static void parseOrderLines(ItemRepository itemRepo, Terminal terminal) {
        int         i = 0;
        String      line;
        String[]    attr;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(orderLinesFile))) {
            int attrQnt = bufferedReader.readLine().split(",").length; // first line

            while ((line = bufferedReader.readLine()) != null)
            {
                attr = line.split(",");

                if (!hasSameItemQuantity(attrQnt, attr))
                    System.out.println("Line " + i + ": missing attributes: \"" + line + "\"");
                else if (!terminal.hasOrder(attr[0]))
                    System.out.println("Line " + i + ": missing attributes: \"" + line + "\"");
                else if (!itemRepo.hasThisItem(attr[2]))
                    System.out.println("Line " + i + ": unregistered inputted Item SKU: \"" + line + "\"");
                else if (Integer.parseInt(attr[3]) <= 0)
                    System.out.println("Line " + i + ": item with no quantity: \"" + line + "\"");
                else
                    terminal.addToOrder(attr[0], itemRepo.getItem(attr[2]), Integer.parseInt(attr[3]));
                i++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + orderLinesFile);
        }
    }

    /**
     * Function will check if the
     * @param declarationArgs
     * @param lineSplit
     * @return (true - if it has) (false - if it does not have)
     */
    private static boolean hasSameItemQuantity(int declarationArgs, String []lineSplit){
        return (declarationArgs == lineSplit.length);
    }

    /**
     * Function will verify in the line if one of the argument is empty
     * @param lineSplit
     * @return (true - if at least one is) (false - if there isn't empty args)
     */
    private static boolean hasEmptyArgs(String []lineSplit) {
        for (int i = 0; i < lineSplit.length; i++) {
            if (lineSplit[i].isBlank()) {
                return true;
            }
        }
        return false;
    }
}
