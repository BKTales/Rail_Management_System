package org.dei.Sprint3.Services.CreateManualRoute;

import org.dei._Facilities.Facility;
import org.dei._Path.Path;
import org.dei._RailLineNetwork.RailLine;
import org.dei._Path.Route;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CreateRouteUI {
    private final CreateRouteController controller;
    private final Scanner scanner;

    // --- COLOR PALETTE ---
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE_BOLD = "\u001B[1;37m";
    private static final String BOLD = "\u001B[1m";

    private static final int PAGE_SIZE = 10;

    public CreateRouteUI() {
        this.controller = new CreateRouteController();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        displayWelcome();
        mainMenu();
    }

    private void displayWelcome() {
        clearScreen();
        System.out.println(CYAN + "╔══════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║            " + PURPLE + BOLD + "ROUTE PLANNER SYSTEM" + CYAN + "                  ║" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private void mainMenu() {
        while (true) {
            System.out.println(WHITE_BOLD + "================ MAIN MENU ================" + RESET);
            System.out.println("1. View All Stations");
            System.out.println("2. View Station Connections");
            System.out.println(YELLOW + "3. Route & Path Management" + RESET);
            System.out.println("4. View Created Routes");
            System.out.println("5. Exit");
            System.out.println(WHITE_BOLD + "============================================" + RESET);
            System.out.print(BOLD + "Select option: " + RESET);

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1": showAllStationsPaginated(); break;
                    case "2": showStationConnections(); break;
                    case "3": routeManagementMenu(); break;
                    case "4": viewAllRoutesPaginated(); break;
                    case "5": System.out.println(GREEN + "Goodbye!" + RESET); return;
                    default: System.out.println(RED + "[INVALID] Invalid selection." + RESET); waitForEnter();
                }
            } catch (Exception e) {
                System.out.println(RED + "\n[SYSTEM ERROR] An unexpected error occurred: " + e.getMessage() + RESET);
                waitForEnter();
            }
        }
    }

    // ==========================================
    //   ROUTE MANAGEMENT MENU
    // ==========================================
    private void routeManagementMenu() {
        while (true) {
            clearScreen();
            System.out.println(PURPLE + BOLD + "╔════════════════════════════════════════════╗" + RESET);
            System.out.println(PURPLE + BOLD + "║         ROUTE & PATH MANAGEMENT            ║" + RESET);
            System.out.println(PURPLE + BOLD + "╚════════════════════════════════════════════╝" + RESET);
            System.out.println();

            System.out.println(CYAN + "  [ Manual Creation ]" + RESET);
            System.out.println(WHITE_BOLD + "  1." + RESET + " Create Manual Path (Point-to-Point)");
            System.out.println(WHITE_BOLD + "  2." + RESET + " Create Route with New Manual Path");
            System.out.println();

            System.out.println(YELLOW + "  [ Automatic Generation ]" + RESET);
            System.out.println(WHITE_BOLD + "  3." + RESET + GREEN + " * Create Automatic Path (Shortest Route) *" + RESET);
            System.out.println();

            System.out.println(BLUE + "  [ Existing Data ]" + RESET);
            System.out.println(WHITE_BOLD + "  4." + RESET + " Create Route from Saved Path");
            System.out.println();

            System.out.println(RED + "  0. Back to Main Menu" + RESET);
            System.out.println();
            System.out.print(BOLD + "Choose option: " + RESET);

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1": handleCreateIsolatedPath(); waitForEnter(); break;
                    case "2": handleCreateRouteWithNewPath(); waitForEnter(); break;
                    case "3": handleCreateAutomaticPath(); waitForEnter(); break;
                    case "4": handleCreateRouteFromExisting(); waitForEnter(); break;
                    case "0": return;
                    default: System.out.println(RED + "[INVALID] Invalid selection." + RESET); waitForEnter();
                }
            } catch (Exception e) {
                System.out.println(RED + "\n[SYSTEM ERROR] " + e.getMessage() + RESET);
                waitForEnter();
            }
        }
    }

    // ==========================================
    //   AUTOMATIC PATH UI
    // ==========================================
    private void handleCreateAutomaticPath() {
        clearScreen();
        System.out.println(GREEN + BOLD + "=== AUTOMATIC PATH GENERATOR ===" + RESET);
        System.out.println(YELLOW + "This tool calculates the shortest path between two stations." + RESET);
        System.out.println();

        try {
            // 1. Select Start
            System.out.println(CYAN + "Step 1: Select Origin Station" + RESET);
            List<String> allStations = controller.getAllStationNames();
            String startStation = selectStationWithPagination("SELECT ORIGIN", allStations);
            if (startStation == null) return;

            // 2. Select End (Reachability Filter)
            System.out.println(CYAN + "\nStep 2: Select Destination Station" + RESET);
            System.out.print(YELLOW + "Scanning reachable stations... " + RESET);

            List<String> reachableStations = controller.getReachableStations(startStation);
            System.out.println(GREEN + "Found " + reachableStations.size() + "." + RESET);

            if (reachableStations.isEmpty()) {
                System.out.println(RED + "[INFO] This station is isolated! No destinations available." + RESET);
                waitForEnter();
                return;
            }

            String endStation = selectStationWithPagination("SELECT DESTINATION (Reachable Only)", reachableStations);
            if (endStation == null) return;

            // 3. Calculation & Preview
            System.out.print(YELLOW + "\nCalculating optimal path..." + RESET);

            // O Controller cria o Path e guarda-o automaticamente no HashMap 'savedPaths'
            Path generatedPath = controller.createAutomaticPath(startStation, endStation);

            System.out.println(GREEN + " [DONE]" + RESET);
            displayPathPreview(generatedPath);

            // 4. Confirmation (Create Route OR Keep Path)
            System.out.println();
            System.out.println(PURPLE + "The Path has been generated and cached in memory." + RESET);
            System.out.print(WHITE_BOLD + "Do you want to instantiate a ROUTE (add schedule) now? (y/n): " + RESET);

            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("y") || confirm.equals("yes")) {
                LocalDateTime departure = getDepartureTime();
                if (departure != null) {
                    controller.createAndSaveRoute(generatedPath, new ArrayList<>(), departure);
                    waitForEnter();
                }
            } else {
                System.out.println(GREEN + "[INFO] Path kept in memory." + RESET);
                System.out.println(YELLOW + "You can assign this Path to a new Route later using Option 4 (Existing Data)." + RESET);
                waitForEnter();
            }

        } catch (IllegalArgumentException e) {
            System.out.println(RED + "\n[LOGIC ERROR] " + e.getMessage() + RESET);
            waitForEnter();
        } catch (Exception e) {
            System.out.println(RED + "\n[SYSTEM ERROR] " + e.getMessage() + RESET);
            waitForEnter();
        }
    }

    private void displayPathPreview(Path path) {
        System.out.println();
        System.out.println(BLUE + "╔════════════ PATH PREVIEW ════════════╗" + RESET);
        List<Facility> stations = path.getRailFacilities();

        System.out.println(CYAN + "  Origin:      " + WHITE_BOLD + stations.get(0).getName() + RESET);
        System.out.println(CYAN + "  Destination: " + WHITE_BOLD + stations.get(stations.size()-1).getName() + RESET);
        System.out.println(CYAN + "  Stops:       " + WHITE_BOLD + stations.size() + RESET);

        System.out.println(BLUE + "╟──────────────────────────────────────╢" + RESET);
        System.out.println(YELLOW + "  Sequence:" + RESET);

        for (int i = 0; i < stations.size(); i++) {
            String arrow = (i == stations.size() - 1) ? "" : " |";
            System.out.printf("    %2d. %-20s %s%n", i + 1, stations.get(i).getName(), arrow);
        }
        System.out.println(BLUE + "╚══════════════════════════════════════╝" + RESET);
    }

    // ==========================================
    //   MANUAL HANDLERS
    // ==========================================

    private void handleCreateIsolatedPath() {
        System.out.println(CYAN + "\n--- Creating Manual Path ---" + RESET);
        try {
            List<String> sequence = collectStationSequence();
            if (sequence != null) {
                controller.createIsolatedPath(sequence);
                System.out.println(GREEN + "[SUCCESS] Path created and saved locally." + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + "[ERROR] " + e.getMessage() + RESET);
        }
    }

    private void handleCreateRouteWithNewPath() {
        System.out.println(CYAN + "\n--- Create Route with New Path ---" + RESET);
        try {
            List<String> sequence = collectStationSequence();
            if (sequence != null) {
                LocalDateTime time = getDepartureTime();
                if (time != null) {
                    controller.createRouteWithNewPath(sequence, time);
                    System.out.println(GREEN + "[SUCCESS] Route and Path created successfully." + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(RED + "[ERROR] " + e.getMessage() + RESET);
        }
    }

    private void handleCreateRouteFromExisting() {
        System.out.println(CYAN + "\n--- Create Route from Existing Path ---" + RESET);
        try {
            Map<String, Path> savedPaths = controller.getSavedPaths();

            if (savedPaths.isEmpty()) {
                System.out.println(RED + "[INFO] No saved paths available." + RESET);
                return;
            }

            List<String> pathIds = new ArrayList<>(savedPaths.keySet());
            System.out.println(BLUE + "--------------------------------------------------------" + RESET);
            for (int i = 0; i < pathIds.size(); i++) {
                String id = pathIds.get(i);
                Path p = savedPaths.get(id);
                System.out.printf("%d. %-15s -> %-15s (ID: %s)%n", (i + 1), p.getStartFacility().getName(), p.getEndFacility().getName(), id);
            }
            System.out.println(BLUE + "--------------------------------------------------------" + RESET);

            System.out.print(BOLD + "Enter Path Number: " + RESET);
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (idx >= 0 && idx < pathIds.size()) {
                LocalDateTime time = getDepartureTime();
                if (time != null) {
                    controller.createRouteFromExistingPath(pathIds.get(idx), time);
                    System.out.println(GREEN + "[SUCCESS] Route created." + RESET);
                }
            } else {
                System.out.println(RED + "[ERROR] Invalid number." + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "[ERROR] Invalid input format." + RESET);
        } catch (Exception e) {
            System.out.println(RED + "[ERROR] " + e.getMessage() + RESET);
        }
    }

    // ==========================================
    //   PAGINATED VIEWS & UTILS
    // ==========================================

    private void viewAllRoutesPaginated() {
        try {
            List<Route> routes = controller.getAllRoutes();

            if (routes.isEmpty()) {
                System.out.println(RED + "[INFO] No routes have been created yet." + RESET);
                waitForEnter();
                return;
            }

            int currentPage = 0;
            int totalPages = (int) Math.ceil((double) routes.size() / PAGE_SIZE);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            while (true) {
                clearScreen();
                System.out.println(CYAN + BOLD + "============== CREATED ROUTES ==============" + RESET);
                System.out.println(YELLOW + BOLD + String.format("Page %d of %d (Total: %,d)",
                        currentPage + 1, totalPages, routes.size()) + RESET);

                System.out.println(BLUE + String.format("%-20s | %-40s | %-20s", "Route ID", "Start -> End", "Departure") + RESET);
                System.out.println(BLUE + "─────────────────────┼──────────────────────────────────────────┼──────────────────────" + RESET);

                int start = currentPage * PAGE_SIZE;
                int end = Math.min(start + PAGE_SIZE, routes.size());

                for (int i = start; i < end; i++) {
                    Route r = routes.get(i);
                    String routeStr = r.getPath().getStartFacility().getName() + " -> " + r.getPath().getEndFacility().getName();
                    if (routeStr.length() > 38) routeStr = routeStr.substring(0, 35) + "...";
                    String timeStr = r.getDepartureDay().format(fmt);

                    System.out.printf("%-20s | %-40s | %-20s%n", r.getRouteId(), routeStr, timeStr);
                }

                System.out.println(BLUE + "───────────────────────────────────────────────────────────────────────────────────" + RESET);
                displayPaginationFooter(null);
                System.out.print(BOLD + "Option: " + RESET);

                String input = scanner.nextLine().trim().toLowerCase();
                PaginationResult result = handlePaginationInput(input, currentPage, totalPages, routes.size());

                if (result != null) {
                    if (result.isNavigation()) {
                        currentPage = result.getPage();
                    } else if (result.isExit()) {
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(RED + "[ERROR] Failed to load routes: " + e.getMessage() + RESET);
            waitForEnter();
        }
    }

    private void showStationConnections() {
        try {
            List<String> allStations = controller.getAllStationNames();
            List<String> connectedOnly = new ArrayList<>();
            for (String s : allStations) if (controller.hasConnections(s)) connectedOnly.add(s);

            String selected = selectStationWithPagination("VIEW CONNECTIONS", connectedOnly);
            if (selected != null) {
                Map<Facility, RailLine> conns = controller.getConnections(selected);
                clearScreen();
                System.out.println(CYAN + "=== CONNECTIONS FROM " + selected.toUpperCase() + " ===" + RESET);
                for(Map.Entry<Facility, RailLine> e : conns.entrySet())
                    System.out.printf(" -> %-20s [Line: %s]%n", e.getKey().getName(), e.getValue().getRailLineId());
                waitForEnter();
            }
        } catch (Exception e) {
            System.out.println(RED + "[ERROR] " + e.getMessage() + RESET);
            waitForEnter();
        }
    }

    private void showAllStationsPaginated() {
        try {
            List<String> names = controller.getAllStationNames();
            if(names.isEmpty()) System.out.println(RED + "[INFO] No stations found in database." + RESET);
            else runPaginationLoop("ALL STATIONS", names, false);
        } catch (Exception e) {
            System.out.println(RED + "[ERROR] " + e.getMessage() + RESET);
            waitForEnter();
        }
    }

    private String selectStationWithPagination(String title, List<String> stations) {
        if (stations.isEmpty()) {
            System.out.println(RED + "[INFO] No stations available." + RESET);
            return null;
        }
        return runPaginationLoop(title, stations, true);
    }

    private String runPaginationLoop(String title, List<String> stations, boolean selectionMode) {
        int currentPage = 0;
        int totalPages = (int) Math.ceil((double) stations.size() / PAGE_SIZE);

        while (true) {
            clearScreen();
            System.out.println(CYAN + BOLD + "============== " + title.toUpperCase() + " ==============" + RESET);
            System.out.println(YELLOW + BOLD + String.format("Page %d of %d (Total: %,d stations)",
                    currentPage + 1, totalPages, stations.size()) + RESET);
            System.out.println(GREEN + "* Connected" + YELLOW + " o Isolated" + RESET);
            System.out.println();

            int start = currentPage * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, stations.size());

            for (int i = start; i < end; i++) {
                String stationName = stations.get(i);
                boolean hasConnections = controller.hasConnections(stationName);
                String color = hasConnections ? GREEN : YELLOW;
                String indicator = hasConnections ? " *" : " o";
                String number = String.format("%2d.", i + 1);

                System.out.printf("%s%s%s %-40s%s%n", color, number, RESET, stationName, indicator);
            }

            System.out.println();
            displayPaginationFooter(selectionMode ? "Select station by number" : null);
            System.out.print(BOLD + "Choose option: " + RESET);

            String choice = scanner.nextLine().trim().toLowerCase();
            PaginationResult result = handlePaginationInput(choice, currentPage, totalPages, stations.size());

            if (result != null) {
                if (result.isSelection() && selectionMode) return stations.get(result.getIndex());
                else if (result.isNavigation()) currentPage = result.getPage();
                else if (result.isExit()) return null;
            }
        }
    }

    private PaginationResult handlePaginationInput(String input, int currentPage, int totalPages, int listSize) {
        switch (input) {
            case "n": case "next":
                if (currentPage < totalPages - 1) return PaginationResult.navigation(currentPage + 1);
                else { System.out.println(RED + "Last page." + RESET); waitForEnter(); return null; }
            case "p": case "prev":
                if (currentPage > 0) return PaginationResult.navigation(currentPage - 1);
                else { System.out.println(RED + "First page." + RESET); waitForEnter(); return null; }
            case "e": case "exit": return PaginationResult.exit();
        }
        try {
            int index = Integer.parseInt(input);
            if (index >= 1 && index <= listSize) return PaginationResult.selection(index - 1);
        } catch (NumberFormatException ignored) {}
        return null;
    }

    private void displayPaginationFooter(String extra) {
        System.out.println(GREEN + "Nav: (N)ext, (P)rev, (E)xit" + RESET);
        if (extra != null) System.out.println(YELLOW + extra + RESET);
    }

    private LocalDateTime getDepartureTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        while (true) {
            System.out.print(YELLOW + "Enter departure (dd/MM/yyyy HH:mm): " + RESET);
            String in = scanner.nextLine().trim();
            try { return LocalDateTime.parse(in, fmt); }
            catch (DateTimeParseException e) { System.out.println(RED + "[ERROR] Invalid format." + RESET); }
        }
    }

    private List<String> collectStationSequence() {
        List<String> allStations = controller.getAllStationNames();
        List<String> validStarts = new ArrayList<>();
        for(String s : allStations) if(controller.hasConnections(s)) validStarts.add(s);

        String startName = selectStationWithPagination("SELECT START", validStarts);
        if (startName == null) return null;

        List<String> sequence = new ArrayList<>();
        sequence.add(startName);
        String current = startName;

        while (true) {
            clearScreen();
            System.out.println(PURPLE + BOLD + "=== BUILDING PATH ===" + RESET);
            System.out.println(YELLOW + "Path: " + String.join(" -> ", sequence) + RESET);

            List<String> connections = controller.getConnectedStationNames(current);
            connections.removeIf(sequence::contains);

            if(connections.isEmpty()) {
                System.out.println(RED + "[INFO] Dead end." + RESET);
                waitForEnter();
                return sequence.size() >= 2 ? sequence : null;
            }

            System.out.println(CYAN + "Next stops:" + RESET);
            for(int i=0; i<connections.size(); i++)
                System.out.println(GREEN + " " + (i+1) + ". " + connections.get(i) + RESET);

            System.out.println(" 0. Finish Here");
            System.out.print(BOLD + "> " + RESET);

            try {
                int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if(idx == -1) return sequence.size() >= 2 ? sequence : null;
                if(idx >= 0 && idx < connections.size()) {
                    String next = connections.get(idx);
                    sequence.add(next);
                    current = next;
                }
            } catch(Exception ignored){}
        }
    }

    private void waitForEnter() { System.out.print(YELLOW + "Press Enter..." + RESET); scanner.nextLine(); }
    private void clearScreen() { System.out.print("\033[H\033[2J"); System.out.flush(); }

    private static class PaginationResult {
        private final int page; private final int index; private final boolean exit;
        private PaginationResult(int p, int i, boolean e) { page=p; index=i; exit=e; }
        static PaginationResult navigation(int p) { return new PaginationResult(p,-1,false); }
        static PaginationResult selection(int i) { return new PaginationResult(-1,i,false); }
        static PaginationResult exit() { return new PaginationResult(-1,-1,true); }
        boolean isNavigation() { return page>=0; }
        boolean isSelection() { return index>=0; }
        boolean isExit() { return exit; }
        int getPage() { return page; }
        int getIndex() { return index; }
    }
}