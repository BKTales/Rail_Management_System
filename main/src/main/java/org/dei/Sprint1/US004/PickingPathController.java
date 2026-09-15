package org.dei.Sprint1.US004;


import org.dei._Facilities.Terminal.Warehouse.PickupPlan;
import org.dei.Sprint1.Repository.TerminalRepository;
import org.dei._Facilities.Terminal.Warehouse.PickingPath;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Facilities.Terminal.Warehouse.Trolley;

import java.util.List;
import java.util.Map;

import org.dei._Facilities.Terminal.WarehouseServices.PickPathSequencing.PickPathSequencing;

public class PickingPathController {
    public Terminal terminal;

    public PickingPathController() {
        terminal = TerminalRepository.getInstance().getTerminal(0);
    }

    public String []listAvailablePlans() {
        return terminal.listPlans();
    }

    public String generatePickPath(int whichPlan){
        PickupPlan p = terminal.getPlanByIndex(whichPlan);
        Map<Trolley, List<PickingPath>> generatedPaths = PickPathSequencing.getPaths(p);

        if (generatedPaths != null)
            return PathPrinter.printPaths(generatedPaths);
        return ("Error: Path could not be created!");
    }
}
