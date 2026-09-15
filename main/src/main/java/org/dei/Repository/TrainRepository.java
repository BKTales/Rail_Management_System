package org.dei.Repository;

import org.dei._Train.*;

import java.time.LocalDateTime;
import java.util.*;

public class TrainRepository {
    private static TrainRepository instance;
    private final Map<String, Locomotive> locomotives;
    private final Map<String, Wagon> wagons;
    private final Map<String, WagonModel> wagonModels;
    private final Map<String, LocomotiveModel> locomotiveModels;
    private final Map<String, Train> trainMap;

    private TrainRepository() {
        locomotives = new HashMap<>();
        wagons = new HashMap<>();
        wagonModels = new HashMap<>();
        locomotiveModels = new HashMap<>();
        trainMap = new HashMap<>();
    }

    public static TrainRepository getInstance() {
        if (instance == null) {
            instance = new TrainRepository();
        }
        return instance;
    }

    public void addLocomotive(Locomotive locomotive) {
        locomotives.put(locomotive.getNumber(), locomotive);
    }

    public void addWagon(Wagon wagon) {
        wagons.put(wagon.getWagonId(), wagon);
    }

    public void addWagonModel(WagonModel wagonModel) {
        wagonModels.put(wagonModel.getId(), wagonModel);
    }

    public void addTrain(Train train) {
        trainMap.put(train.getTrainId(), train);
    }

    public Train getTrain(String trainId) {
        return trainMap.get(trainId);
    }

    public Locomotive getLocomotive(String number) {
        return locomotives.get(number);
    }

    public Wagon getWagon(String wagonId) {
        return wagons.get(wagonId);
    }

    public WagonModel getWagonModel(String modelName) {
        return wagonModels.get(modelName);
    }

    public LocomotiveModel getLocomotiveModel(String modelKey) {
        return locomotiveModels.get(modelKey);
    }

    public boolean containsLocomotive(String number) {
        return locomotives.containsKey(number);
    }

    public boolean containsWagon(String wagonId) {
        return wagons.containsKey(wagonId);
    }

    public boolean containsWagonModel(String modelName) {
        return wagonModels.containsKey(modelName);
    }

    public Collection<Locomotive> getAllLocomotives() {
        return locomotives.values();
    }

    public Collection<Wagon> getAllWagons() {
        return wagons.values();
    }

    public Collection<WagonModel> getAllWagonModels() {
        return wagonModels.values();
    }

    public Collection<Train> getAllTrains() {
        return trainMap.values();
    }

    public Collection<Train> getAllValidTrains(LocalDateTime departureTime) {
        List<Train> validTrains = new ArrayList<>();
        for (Train train : trainMap.values()) {
            if (train.getArrival().getArrivalTime().isAfter(departureTime) ) {
                validTrains.add(train);
            }
        }
        return validTrains;
    }

    // Utility methods
    public int getLocomotiveCount() {
        return locomotives.size();
    }

    public int getWagonCount() {
        return wagons.size();
    }

    public int getWagonModelCount() {
        return wagonModels.size();
    }

    public void clear() {
        locomotives.clear();
        wagons.clear();
        wagonModels.clear();
        locomotiveModels.clear();
    }

    public void clearTrains() {
        trainMap.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\u001B[36m"); // Cyan
        sb.append("  ████████╗██████╗  █████╗ ██╗███╗   ██╗\n");
        sb.append("  ╚══██╔══╝██╔══██╗██╔══██╗██║████╗  ██║\n");
        sb.append("     ██║   ██████╔╝███████║██║██╔██╗ ██║\n");
        sb.append("     ██║   ██╔══██╗██╔══██║██║██║╚██╗██║\n");
        sb.append("     ██║   ██║  ██║██║  ██║██║██║ ╚████║\n");
        sb.append("     ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝\n");
        sb.append("\u001B[0m"); // Reset

        sb.append("\u001B[33m"); // Yellow
        sb.append("╔══════════════════════════════════════════════╗\n");
        sb.append("║             REPOSITORY OVERVIEW             ║\n");
        sb.append("╚══════════════════════════════════════════════╝\n");
        sb.append("\u001B[0m"); // Reset

        sb.append("\u001B[32m"); // Green
        sb.append("┌───────────────── STATISTICS ─────────────────┐\n");
        sb.append("\u001B[0m"); // Reset
        sb.append(String.format("│ %sLocomotives:%s %-8d %sWagons:%s %-8d     │\n",
                "\u001B[34m", "\u001B[0m", getLocomotiveCount(), "\u001B[35m", "\u001B[0m", getWagonCount()));
        sb.append(String.format("│ %sWagon Models:%s %-6d %sTotal Items:%s %-6d   │\n",
                "\u001B[36m", "\u001B[0m", getWagonModelCount(), "\u001B[33m", "\u001B[0m",
                getLocomotiveCount() + getWagonCount() + getWagonModelCount()));
        sb.append("\u001B[32m"); // Green
        sb.append("└──────────────────────────────────────────────┘\n");
        sb.append("\u001B[0m"); // Reset
        sb.append("\n");

        // Locomotives Section
        if (!locomotives.isEmpty()) {
            sb.append("\u001B[34m"); // Blue
            sb.append("┌────────────── LOCOMOTIVES ────────────────┐\n");
            sb.append("\u001B[0m"); // Reset

            int count = 0;
            for (Locomotive loco : locomotives.values()) {
                if (count < 4) { // Show first 4 only
                    String typeSymbol = loco.isElectric() ? "[E]" : "[D]";
                    String typeColor = loco.isElectric() ? "\u001B[36m" : "\u001B[33m";
                    sb.append(String.format("│ %s%-3s%s %-12s %-20s │\n",
                            typeColor, typeSymbol, "\u001B[0m",
                            loco.getNumber(),
                            truncate(loco.getModel().getName(), 20)));
                }
                count++;
            }
            if (count > 4) {
                sb.append(String.format("│ ... and %-2d more %-23s │\n", count - 4, ""));
            }

            sb.append("\u001B[34m"); // Blue
            sb.append("└──────────────────────────────────────────────┘\n");
            sb.append("\u001B[0m"); // Reset
            sb.append("\n");
        }

        // Wagons Section
        if (!wagons.isEmpty()) {
            sb.append("\u001B[35m"); // Magenta
            sb.append("┌───────────────── WAGONS ──────────────────┐\n");
            sb.append("\u001B[0m"); // Reset

            int count = 0;
            for (Wagon wagon : wagons.values()) {
                if (count < 4) { // Show first 4 only
                    String status = wagon.getSize() > 0 ? "[LOADED]" : "[EMPTY]";
                    String statusColor = wagon.getSize() > 0 ? "\u001B[32m" : "\u001B[90m";
                    sb.append(String.format("│ %s%-8s%s %-10s Boxes:%-3d %-12s │\n",
                            statusColor, status, "\u001B[0m",
                            wagon.getWagonId(),
                            wagon.getSize(),
                            String.format("%.1ft", wagon.getTotalWeight())));
                }
                count++;
            }
            if (count > 4) {
                sb.append(String.format("│ ... and %-2d more %-23s │\n", count - 4, ""));
            }

            sb.append("\u001B[35m"); // Magenta
            sb.append("└──────────────────────────────────────────────┘\n");
            sb.append("\u001B[0m"); // Reset
        }

        // Summary
        sb.append("\n");
        sb.append("\u001B[37m"); // White
        sb.append("╔══════════════════════════════════════════════╗\n");
        sb.append("║                    SUMMARY                   ║\n");
        sb.append("╠══════════════════════════════════════════════╣\n");
        sb.append("\u001B[0m"); // Reset

        double avgBoxes = wagons.values().stream()
                .mapToInt(Wagon::getSize)
                .average()
                .orElse(0.0);

        sb.append(String.format("\u001B[37m║ %sAverage Load:%s %-6.1f boxes per wagon     ║\n",
                "\u001B[33m", "\u001B[37m", avgBoxes));
        sb.append(String.format("║ %sRepository Size:%s %-4d total entities    ║\n",
                "\u001B[36m", "\u001B[37m", getLocomotiveCount() + getWagonCount() + getWagonModelCount()));
        sb.append("╚══════════════════════════════════════════════╝\n");
        sb.append("\u001B[0m"); // Reset

        return sb.toString();
    }

    private String truncate(String text, int length) {
        if (text.length() <= length) return text;
        return text.substring(0, length - 3) + "...";
    }
}