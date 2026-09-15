package org.dei.Sprint1.Repository;

import org.dei.Sprint1._Item.Item;

import java.util.Collection;
import java.util.HashMap;

public class ItemRepository
{
    private static ItemRepository instance = null;
    private HashMap<String, Item> items;

    public ItemRepository() {
        items = new HashMap<>();
    }

    public static ItemRepository getInstance() {
        if (instance == null) {
            instance = new ItemRepository();
        }
        return instance;
    }

    public Item getItem(String itemSku) {
        return items.get(itemSku);
    }

    /**
     * Function will search for an SKU inside the loaded items in program
     * @param itemSku
     * @return (true - if it has the given product) (false - If it does not have it)
     */
    public boolean hasThisItem(String itemSku) {
        return items.containsKey(itemSku);
    }

    public Item getItemBySku(String itemSku) {
        return items.get(itemSku);
    }

    public void addItem(Item item) {
        items.put(item.getSku(), item);
    }

    public Collection<Item> getAllItems() {
        return items.values();
    }

    public void removeItem(String itemSku) {
        items.remove(itemSku);
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

}