package org.dei.Utils;

import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;
import org.dei.Sprint1._Item.Box;

import java.util.Comparator;

public class ComparatorsUtils {

    public static final Comparator<Box> boxComparatorByDates = (o1, o2) -> {
        if (o1.getExpireDate() != null && o2.getExpireDate() != null) {
            if(o1.getExpireDate().equals(o2.getExpireDate())){
                if(o1.getReceivedAt().equals(o2.getReceivedAt())){
                    return o1.getBoxID().compareTo(o2.getBoxID());
                } else {
                    return o1.getReceivedAt().compareTo(o2.getReceivedAt());
                }
            } else {
                return o1.getExpireDate().compareTo(o2.getExpireDate());
            }
        } else if (o1.getExpireDate() != null) {
            return -1;
        } else if (o2.getExpireDate() != null) {
            return 1;
        } else {
            if(o1.getReceivedAt().equals(o2.getReceivedAt())){
                return o1.getBoxID().compareTo(o2.getBoxID());
            } else {
                return o1.getReceivedAt().compareTo(o2.getReceivedAt());
            }
        }
    };

    public static final Comparator<WarehousePosition> ascendingComparator = (o1, o2) -> {
        if (o1.getAisleIndex() < o2.getAisleIndex()) {
            return -1;
        } else if (o1.getAisleIndex() > o2.getAisleIndex()) {
            return 1;
        } else {
            if (o1.getBayIndex() < o2.getBayIndex()) {
                return -1;
            } else {
                return 1;
            }
        }
    };
}
