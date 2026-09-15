package org.dei.Sprint1.ListWagons;

import org.dei._Facilities.FrightYard.FreightYard;

public class ListWagonsController {
    private FreightYard freightYard;

    public ListWagonsController() {
        //freightYard = FreightYard.getInstance();
        freightYard = null;
    }

    public String listWagons() {
        if (freightYard.size() == 0)
        {
            String s = "╔════════════════════════════════════╗\n║       No Wagons Available          ║\n╚════════════════════════════════════╝\n";
            return (s);
        }
        return freightYard.listPrintWagon();
    }
}
