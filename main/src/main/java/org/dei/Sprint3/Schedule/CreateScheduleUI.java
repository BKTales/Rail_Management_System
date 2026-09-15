//package org.dei.Sprint3.Schedule;
//
//import org.dei._Facilities.Facility;
//import org.dei._Path.Route;
//import org.dei._Train.Freight;
//import org.dei._Train.Locomotive;
//import org.dei._Train.Train;
//import org.dei._Train.Wagon;
//import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
//import org.dei.Sprint3.Services.DataBaseAccessService;
//
//import java.sql.Connection;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//public class CreateScheduleUI {
//
//    // --- PALETA DE CORES ---
//    private static final String RESET = "\u001B[0m";
//    private static final String RED = "\u001B[31m";
//    private static final String GREEN = "\u001B[32m";
//    private static final String YELLOW = "\u001B[33m";
//    private static final String BLUE = "\u001B[34m";
//    private static final String PURPLE = "\u001B[35m";
//    private static final String CYAN = "\u001B[36m";
//    private static final String WHITE_BOLD = "\u001B[1;37m";
//    private static final String BOLD = "\u001B[1m";
//
//    private final CreateScheduleController controller;
//    private final Scanner scanner;
//    private final DateTimeFormatter dateFormatter;
//
//    public CreateScheduleUI() {
//        this.controller = new CreateScheduleController();
//        this.scanner = new Scanner(System.in);
//        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//    }
//
//    public void start() {
//        while (true) {
//            clearScreen();
//            System.out.println(CYAN + "╔══════════════════════════════════════════════════╗" + RESET);
//            System.out.println(CYAN + "║             " + PURPLE + BOLD + "LOGISTICS & SCHEDULES" + CYAN + "                ║" + RESET);
//            System.out.println(CYAN + "╚══════════════════════════════════════════════════╝" + RESET);
//            System.out.println();
//
//            System.out.println(WHITE_BOLD + "  1." + RESET + " Create Independent Freight (Step 1)");
//            System.out.println(WHITE_BOLD + "  2." + RESET + " Create Train Schedule (Step 2 - Assign Freight)");
//            System.out.println(WHITE_BOLD + "  3." + RESET + " View Rolling Stock Status");
//            System.out.println(RED + "  4. Exit" + RESET);
//            System.out.println(BLUE + "====================================================" + RESET);
//            System.out.print(BOLD + "Select option: " + RESET);
//
//            String choice = scanner.nextLine().trim();
//
//            try {
//                switch (choice) {
//                    case "1": createIndependentFreight(); break;
//                    case "2": createNewTrainSchedule(); break;
//                    case "3": viewRollingStockStatus(); break;
//                    case "4": System.out.println(GREEN + "Goodbye!" + RESET); return;
//                    default: System.out.println(RED + "Invalid option." + RESET); waitForEnter();
//                }
//            } catch (Exception e) {
//                System.out.println(RED + "[ERROR] Unexpected: " + e.getMessage() + RESET);
//                e.printStackTrace();
//                waitForEnter();
//            }
//        }
//    }
//
//    // =========================================================================
//    // 1. CREATE FREIGHT
//    // =========================================================================
//
//    private void createIndependentFreight() {
//        controller.clearSelections();
//        System.out.println(PURPLE + BOLD + "\n=== CREATE NEW FREIGHT ===" + RESET);
//
//        // 1. Escolher Origem
//        Facility start = selectFacility("Origin");
//        if(start == null) return;
//
//        // 2. Escolher Destino
//        Facility end = selectFacility("Destination");
//        if(end == null) return;
//
//        if(start.getId() == end.getId()) {
//            System.out.println(RED + "Error: Origin and Destination cannot be the same." + RESET);
//            waitForEnter();
//            return;
//        }
//
//        // 3. Escolher Vagões
//        List<Wagon> wagons = selectWagonsForFreight();
//        if(wagons == null || wagons.isEmpty()) {
//            System.out.println(YELLOW + "Operation cancelled (no wagons selected)." + RESET);
//            return;
//        }
//
//        // 4. Gravar
//        try {
//            Freight f = controller.createIndependentFreight(start, end, wagons);
//            System.out.println(GREEN + "[SUCCESS] Freight " + f.getId() + " created with " + wagons.size() + " wagons." + RESET);
//            System.out.println("It is now waiting to be assigned to a Train.");
//        } catch (Exception e) {
//            System.out.println(RED + "Error creating freight: " + e.getMessage() + RESET);
//        }
//        waitForEnter();
//    }
//
//    // =========================================================================
//    // 2. CREATE TRAIN SCHEDULE (CONNECTING EVERYTHING)
//    // =========================================================================
//
//    private void createNewTrainSchedule() {
//        controller.clearSelections();
//        System.out.println(PURPLE + BOLD + "\n=== CREATE TRAIN SCHEDULE ===" + RESET);
//
//        // 1. Escolher Rota
//        Route route = selectRoute();
//        if (route == null) return;
//
//        System.out.println(CYAN + "\nRoute Selected: " + RESET + route.getPath().getStartFacility().getName() +
//                " -> " + route.getPath().getEndFacility().getName());
//
//        // 2. Escolher Cargas (Freights) Compatíveis com esta Rota
//        List<Freight> selectedFreights = selectFreightsForRoute(route);
//        if (selectedFreights == null) return; // Utilizador cancelou
//
//        // 3. Escolher Locomotivas
//        LocalDateTime start = route.getDepartureDay();
//        LocalDateTime endEstimate = start.plusHours(4); // Estimativa de duração
//
//        List<Locomotive> locos = selectLocomotives(start, endEstimate);
//        if (locos == null) return;
//
//        // 4. Gravar Comboio
//        try {
//            Train t = controller.createTrainSchedule(route, locos, selectedFreights);
//            System.out.println(GREEN + "[SUCCESS] Train " + t.getTrainId() + " saved to Database!" + RESET);
//            System.out.println("Route: " + route.getPath().getStartFacility().getName() + " -> " + route.getPath().getEndFacility().getName());
//
//            if(!selectedFreights.isEmpty()) {
//                System.out.println("Carrying " + selectedFreights.size() + " freight shipments.");
//            } else {
//                System.out.println(YELLOW + "Train is running empty (no freight assigned)." + RESET);
//            }
//        } catch (Exception e) {
//            System.out.println(RED + "[ERROR] " + e.getMessage() + RESET);
//        }
//        waitForEnter();
//    }
//
//    // =========================================================================
//    // 3. VIEW STATUS
//    // =========================================================================
//
//    private void viewRollingStockStatus() {
//        while (true) {
//            clearScreen();
//            System.out.println(PURPLE + BOLD + "=== SYSTEM STATUS ===" + RESET);
//            System.out.println("1. Locomotives Status (Transit/Parked)");
//            System.out.println("2. Wagon Status (Assigned to Freight)");
//            System.out.println("0. Back");
//            System.out.println();
//            System.out.print(BOLD + "Option: " + RESET);
//
//            String subChoice = scanner.nextLine().trim();
//            if (subChoice.equals("0")) return;
//
//            try {
//                Connection con = DatabaseConnection.getInstance();
//                DataBaseAccessService dbService = new DataBaseAccessService();
//
//                if (subChoice.equals("1")) {
//                    // Ver Locomotivas
//                    List<org.dei.Sprint3.DTOs.LocomotiveWithStatus> locos = dbService.getLocomotivesWithTransitStatus(con, null);
//                    System.out.println(CYAN + "\n--- LOCOMOTIVE STATUS ---" + RESET);
//                    if(locos.isEmpty()) System.out.println(YELLOW + "No locomotives found." + RESET);
//
//                    for (org.dei.Sprint3.DTOs.LocomotiveWithStatus l : locos) {
//                        String statusColor = l.isInTransit() ? BLUE : (l.isParked() ? GREEN : RED);
//                        System.out.println(statusColor + l.toString() + RESET);
//                    }
//
//                } else if (subChoice.equals("2")) {
//                    // Ver Vagões em Carga
//                    List<org.dei.Sprint3.DTOs.WagonWithStatus> wagons = dbService.getWagonsInFreightStatus(con);
//                    System.out.println(CYAN + "\n--- WAGONS IN FREIGHT ---" + RESET);
//                    if(wagons.isEmpty()) System.out.println(YELLOW + "No wagons are currently assigned to any freight." + RESET);
//
//                    for (org.dei.Sprint3.DTOs.WagonWithStatus ws : wagons) {
//                        String statusColor = ws.isInTransit() ? BLUE : WHITE_BOLD;
//                        System.out.println(statusColor + ws.toString() + RESET);
//                    }
//                } else {
//                    System.out.println(RED + "Invalid option." + RESET);
//                }
//            } catch (Exception e) {
//                System.out.println(RED + "Error: " + e.getMessage() + RESET);
//            }
//            waitForEnter();
//        }
//    }
//
//    // =========================================================================
//    // SELECTION MENUS & LOGIC
//    // =========================================================================
//
//    /**
//     * Menu especial para escolher cargas que caibam na rota selecionada
//     */
//    private List<Freight> selectFreightsForRoute(Route route) {
//        List<Freight> selected = new ArrayList<>();
//
//        while (true) {
//            clearScreen();
//            // Pede ao controller APENAS cargas que cabem nesta rota (A->B cabe em A->C)
//            List<Freight> compatible = controller.getCompatibleFreightsForRoute(route);
//
//            System.out.println(CYAN + "--- ADD FREIGHT TO TRAIN ---" + RESET);
//            System.out.println("Train Route: " + BOLD + route.getPath().getStartFacility().getName() +
//                    " -> " + route.getPath().getEndFacility().getName() + RESET);
//
//            System.out.println(BLUE + "\nSelected Freights:" + RESET);
//            if(selected.isEmpty()) System.out.println("  (none - train will run empty)");
//            else selected.forEach(f -> System.out.println(GREEN + "  + " + f.getId() + " [" + f.getStartFacility().getName() + "->" + f.getEndFacility().getName() + "]" + RESET));
//
//            System.out.println(BLUE + "\nAvailable Compatible Freights:" + RESET);
//            if (compatible.isEmpty()) {
//                System.out.println(YELLOW + "  No compatible freights found waiting." + RESET);
//            }
//
//            // Mostrar lista
//            for (int i = 0; i < compatible.size(); i++) {
//                Freight f = compatible.get(i);
//                if (!selected.contains(f)) {
//                    System.out.printf("%d. Freight %s [%s -> %s] (%d wagons)%n",
//                            i + 1, f.getId(), f.getStartFacility().getName(), f.getEndFacility().getName(), f.getWagons().size());
//                }
//            }
//
//            System.out.println("\nCommands: [Number] to add, 'done' to confirm, 'cancel'");
//            System.out.print(BOLD + "> " + RESET);
//            String input = scanner.nextLine().trim();
//
//            if (input.equalsIgnoreCase("done")) return selected;
//            if (input.equalsIgnoreCase("cancel")) return null;
//
//            try {
//                int idx = Integer.parseInt(input) - 1;
//                if (idx >= 0 && idx < compatible.size()) {
//                    Freight f = compatible.get(idx);
//                    if (!selected.contains(f)) {
//                        selected.add(f);
//                    } else {
//                        System.out.println(YELLOW + "Already selected." + RESET);
//                        waitForEnter();
//                    }
//                }
//            } catch (Exception e) {}
//        }
//    }
//
//    private List<Locomotive> selectLocomotives(LocalDateTime start, LocalDateTime end) {
//        List<Locomotive> selected = new ArrayList<>();
//        String lockedGauge = null; // Garante que todas têm a mesma bitola
//
//        while (true) {
//            clearScreen();
//            List<Locomotive> available = controller.getAvailableLocomotives(start, end);
//
//            System.out.println(CYAN + "--- SELECT LOCOMOTIVES ---" + RESET);
//            if(lockedGauge != null) System.out.println(PURPLE + "Required Gauge: " + lockedGauge + RESET);
//
//            System.out.println(BLUE + "\nSelected:" + RESET);
//            if(selected.isEmpty()) System.out.println("  (none)");
//            else selected.forEach(l -> System.out.println(GREEN + "  + " + l.getNumber() + RESET));
//
//            System.out.println(BLUE + "\nAvailable:" + RESET);
//            if(available.isEmpty()) System.out.println(RED + "  No locomotives available." + RESET);
//
//            for (int i = 0; i < available.size(); i++) {
//                Locomotive l = available.get(i);
//                boolean compat = (lockedGauge == null || l.getModel().getGauge().equals(lockedGauge));
//                String color = compat ? WHITE_BOLD : RED;
//
//                if(!selected.contains(l)) {
//                    System.out.printf("%s  %d. %s [Gauge: %s]%s%n", color, i + 1, l.getNumber(), l.getModel().getGauge(), RESET);
//                }
//            }
//
//            System.out.println("\nCommands: [Number] to add, 'done' to finish, 'cancel' to abort");
//            System.out.print(BOLD + "> " + RESET);
//            String input = scanner.nextLine().trim();
//
//            if (input.equalsIgnoreCase("done")) return selected.isEmpty() ? null : selected;
//            if (input.equalsIgnoreCase("cancel")) { selected.forEach(controller::deselectLocomotive); return null; }
//
//            try {
//                int idx = Integer.parseInt(input) - 1;
//                if (idx >= 0 && idx < available.size()) {
//                    Locomotive l = available.get(idx);
//                    if (selected.contains(l)) continue;
//
//                    if (lockedGauge == null || l.getModel().getGauge().equals(lockedGauge)) {
//                        if (lockedGauge == null) lockedGauge = l.getModel().getGauge();
//                        controller.selectLocomotive(l);
//                        selected.add(l);
//                    } else {
//                        System.out.println(RED + "Gauge mismatch! All locomotives must have same gauge." + RESET);
//                        waitForEnter();
//                    }
//                }
//            } catch (NumberFormatException ignored) {}
//        }
//    }
//
//    private List<Wagon> selectWagonsForFreight() {
//        List<Wagon> selected = new ArrayList<>();
//        List<Wagon> available = controller.getAvailableWagons(null, null, null);
//        String lockedGauge = null; // Garante mesma bitola nos vagões
//
//        while (true) {
//            clearScreen();
//            System.out.println(CYAN + "--- SELECT WAGONS FOR FREIGHT ---" + RESET);
//            if (lockedGauge != null) System.out.println(PURPLE + "Required Gauge: " + lockedGauge + RESET);
//
//            System.out.println(BLUE + "Selected: " + RESET + selected.size() + " wagons");
//
//            System.out.println(BLUE + "\nAvailable Wagons:" + RESET);
//            if(available.isEmpty()) System.out.println(RED + "  No wagons available." + RESET);
//
//            int limit = Math.min(available.size(), 20); // Paginação simples
//            for (int i = 0; i < limit; i++) {
//                Wagon w = available.get(i);
//                boolean compat = (lockedGauge == null || w.getWagonModel().getGauge().equals(lockedGauge));
//                String color = compat ? WHITE_BOLD : RED;
//
//                if(!selected.contains(w)) {
//                    System.out.printf("%s  %d. %s [Cap: %.1f t] [Gauge: %s]%s%n",
//                            color, i + 1, w.getWagonId(),
//                            (double) w.getWagonModel().getBoxCapacity(),
//                            w.getWagonModel().getGauge(), RESET);
//                }
//            }
//
//            System.out.println("\nCommands: [Number] to add, 'all' (adds compatible), 'done', 'cancel'");
//            System.out.print(BOLD + "> " + RESET);
//            String input = scanner.nextLine().trim();
//
//            if (input.equalsIgnoreCase("done")) return selected;
//            if (input.equalsIgnoreCase("cancel")) { selected.forEach(controller::deselectWagon); return null; }
//
//            if (input.equalsIgnoreCase("all")) {
//                for(Wagon w : available) {
//                    if(!selected.contains(w) && (lockedGauge == null || w.getWagonModel().getGauge().equals(lockedGauge))) {
//                        if(lockedGauge == null) lockedGauge = w.getWagonModel().getGauge();
//                        controller.selectWagon(w);
//                        selected.add(w);
//                    }
//                }
//                continue;
//            }
//
//            try {
//                int idx = Integer.parseInt(input) - 1;
//                if (idx >= 0 && idx < available.size()) {
//                    Wagon w = available.get(idx);
//                    if (selected.contains(w)) continue;
//
//                    if (lockedGauge == null || w.getWagonModel().getGauge().equals(lockedGauge)) {
//                        if (lockedGauge == null) lockedGauge = w.getWagonModel().getGauge();
//                        controller.selectWagon(w);
//                        selected.add(w);
//                    } else {
//                        System.out.println(RED + "Gauge mismatch!" + RESET);
//                        waitForEnter();
//                    }
//                }
//            } catch (NumberFormatException ignored) {}
//        }
//    }
//
//    private Facility selectFacility(String title) {
//        List<Facility> facilities = controller.getAllFacilities();
//        if(facilities.isEmpty()) {
//            System.out.println(RED + "No facilities found in DB." + RESET);
//            return null;
//        }
//
//        System.out.println(CYAN + "Select " + title + " (Type Number):" + RESET);
//        for(int i=0; i<facilities.size(); i++)
//            System.out.printf("%d. %s%n", i+1, facilities.get(i).getName());
//
//        System.out.print("> ");
//        try {
//            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
//            if(idx>=0 && idx<facilities.size()) return facilities.get(idx);
//        } catch(Exception e) {}
//        return null;
//    }
//
//    private Route selectRoute() {
//        List<Route> routes = controller.getAllRoutes();
//        if (routes.isEmpty()) {
//            System.out.println(RED + "[INFO] No routes available. Please create a route first in the Route Manager." + RESET);
//            waitForEnter();
//            return null;
//        }
//        System.out.println(CYAN + "Select Route:" + RESET);
//        for (int i = 0; i < routes.size(); i++) {
//            Route r = routes.get(i);
//            String complex = r.isComplex() ? " [Complex]" : "";
//            System.out.printf("%d. Route %d (%s -> %s)%s | Departs: %s%n",
//                    i + 1, r.getRouteId(),
//                    r.getPath().getStartFacility().getName(),
//                    r.getPath().getEndFacility().getName(),
//                    complex,
//                    r.getDepartureDay().format(dateFormatter));
//        }
//        System.out.print("> ");
//        try {
//            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
//            if(idx>=0 && idx<routes.size()) return routes.get(idx);
//        } catch(Exception e) {}
//        return null;
//    }
//
//    private void clearScreen() { System.out.print("\033[H\033[2J"); System.out.flush(); }
//    private void waitForEnter() { System.out.println(YELLOW + "Press Enter..." + RESET); scanner.nextLine(); }
//}