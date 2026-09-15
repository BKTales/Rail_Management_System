package org.dei._Facilities.Terminal.WarehouseServices;

import org.dei.Sprint1._Item.Box;
import org.dei._Facilities.Terminal.Warehouse.Aisle;
import org.dei._Facilities.Terminal.Warehouse.Bay;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;

public class WarehouseRelocation {
    /**
     * Function will see if there is empty spaces in bays inside the given warehouse
     * and if there are boxes allocated to other bays after it. If so, it will move
     * the needed boxes from one bay to the other, until have the requested allocation
     * @param warehouse Warehouse to Perform the relocation of boxes in
     */
    public static void reallocateItemInWarehouse(Warehouse warehouse) {
        boolean added = true;
        int aisleIndex  = 0;
        int bayIndex = 0;

        if (warehouse == null)
            return;

        for (Aisle aisle : warehouse.getAisles())
        {
            while (added)
            {
                added = false;
                for (int j = 1; j < aisle.getManyBays(); j++)
                {
                    bayIndex = j;
                    Bay currBay = aisle.getBay(j);
                    Bay prevBay = aisle.getBay(j - 1);

                    if (!prevBay.isFull() && !currBay.isEmpty())
                    {
                        added = true;
                        relocatingBoxInBays(currBay, prevBay, aisleIndex, bayIndex - 1);
                                                    // this is -1 because it is the prevBay index
                    }
                }
            }
            aisleIndex++;
        }
    }

    /**
     * Will move the possible boxes from goFrom to goTo.
     * @param goFrom Bay which boxes will move form
     * @param goTo Bay which boxes will go to
     */
    private static void relocatingBoxInBays(Bay goFrom, Bay goTo, int aisle, int bay) {
        int availableBoxesInGoTo = goTo.availableBoxes();
        int manyBoxesInGoFrom = goFrom.getManyBoxes();
        int i = 0;


        while (i < manyBoxesInGoFrom && i < availableBoxesInGoTo)
        {
            Box box = goFrom.getFirstBox();

            box.setBayIndex(bay);
            //System.out.println("Bay index is " + bay);
            box.setAisleIndex(aisle);

            goTo.addBox(box);
            i++;
        }
    }

}
