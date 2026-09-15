package org.dei._Train;

import org.dei.Utils.ComparatorsUtils;
import org.dei.Sprint1._Item.Box;
import java.util.PriorityQueue;

public class Wagon {
    private String wagonId;
    private WagonModel wagonModel;
    private PriorityQueue<Box> boxes;

    public  Wagon(String wagonId) {
        this.wagonId = wagonId;
        boxes = new PriorityQueue<>(ComparatorsUtils.boxComparatorByDates);
    }

    /**
     * Sum the weight of all boxes with the wagon
     * @return total weight
     */
    public double getTotalWeight() {
        double totalWeight = 0;
        if(wagonModel != null) totalWeight += wagonModel.getWeight() * 1000;
        for (Box box : boxes) totalWeight += box.getWeight();
        return  totalWeight; //kg
    }

    public int getSize() { return (boxes.size()); }
    public Box getUnloadBox() { return (boxes.poll()); }
    public String getWagonId() { return wagonId; }
    public WagonModel getWagonModel() { return wagonModel; }
    public void setWagonModel(WagonModel model) { this.wagonModel = model; }
    public PriorityQueue<Box> getBoxes() { return (boxes); }
    public void addBox(Box box) { boxes.add(box); }

    @Override
    public String toString() {
        return "Wagon " + wagonId + " (" + (wagonModel!=null ? wagonModel.getId() : "NoModel") + ")";
    }
}