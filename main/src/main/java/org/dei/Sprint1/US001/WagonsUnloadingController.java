package org.dei.Sprint1.US001;

import org.dei.Utils.Exceptions.WarehousesFullException;
import org.dei.Sprint1.Repository.TerminalRepository;
import org.dei._Facilities.FrightYard.FreightYard;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Train.Wagon;

import java.util.ArrayList;
import java.util.List;

public class WagonsUnloadingController {
    private TerminalRepository terminalRepo;
    private FreightYard freightYard;

    public WagonsUnloadingController() {
        terminalRepo = TerminalRepository.getInstance();
        freightYard = null;
        //freightYard = FreightYard.getInstance();
    }

    public int listWagonsSize() {
        return freightYard.size();
    }

    public String listWagons() {
        return freightYard.listPrintWagon();
    }

    public boolean unloadWagon(int terminalIndex, int wagonIndex) throws WarehousesFullException {
        Terminal terminal = terminalRepo.getTerminal(terminalIndex);
        Wagon wagon = freightYard.removeWagonByIndex(wagonIndex);
        return terminal.unloadWagon(wagon);
    }

    public List<String> getWagonsList() {
        List<String> wagons = new ArrayList<>();
        int wagonCount = freightYard.size();

        for (int i = 0; i < wagonCount; i++) {
            wagons.add(String.format("Wagon %d - Ready for unloading", i));
        }
        return wagons;
    }

    public List<Boolean> unloadMultipleWagons(int terminalIndex, List<Integer> wagonIndices) {
        List<Boolean> results = new ArrayList<>();
        List<Integer> sortedIndices = new ArrayList<>(wagonIndices);

        sortedIndices.sort((a, b) -> b - a);

        for (int wagonIndex : sortedIndices) {
            try {
                if (wagonIndex >= 0 && wagonIndex < freightYard.size()) {
                    Terminal terminal = terminalRepo.getTerminal(terminalIndex);
                    Wagon wagon = freightYard.removeWagonByIndex(wagonIndex);
                    boolean success = terminal.unloadWagon(wagon);
                    results.add(success);
                } else {
                    results.add(false);
                }
            } catch (Exception e) {
                results.add(false);
            }
        }

        return results;
    }

    public List<String> unloadSelectedWagons(int terminalIndex, List<Boolean> selectedWagons) {
        List<String> results = new ArrayList<>();
        List<Integer> indicesToUnload = new ArrayList<>();

        for (int i = 0; i < selectedWagons.size(); i++) {
            if (selectedWagons.get(i)) {
                indicesToUnload.add(i);
            }
        }

        indicesToUnload.sort((a, b) -> b - a);

        for (int originalIndex : indicesToUnload) {
            try {
                if (originalIndex < freightYard.size()) {
                    Terminal terminal = terminalRepo.getTerminal(terminalIndex);
                    Wagon wagon = freightYard.removeWagonByIndex(originalIndex);
                    boolean success = terminal.unloadWagon(wagon);
                    if (success) {
                        results.add("✓ Wagon " + originalIndex + " unloaded successfully");
                    } else {
                        results.add("✗ Failed to unload wagon " + originalIndex + " - No space in warehouses");
                    }
                } else {
                    results.add("✗ Error unloading wagon " + originalIndex + " - Wagon no longer available");
                }
            } catch (Exception e) {
                results.add("✗ Error unloading wagon " + originalIndex + ": " + e.getMessage());
            }
        }

        return results;
    }
}