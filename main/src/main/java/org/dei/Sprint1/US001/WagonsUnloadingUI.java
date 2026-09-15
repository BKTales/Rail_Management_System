package org.dei.Sprint1.US001;

import org.dei.Sprint1.US002.NotInitializedException;
import org.dei.Sprint1.US002.NotValidOrderException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WagonsUnloadingUI {
    private final int SELECTED_MIN = 0;
    private final String GREEN_COLOR = "\u001B[32m";
    private final String GRAY_COLOR = "\u001B[90m";
    private final String RESET_COLOR = "\u001B[0m";
    private final String HIGHLIGHT_COLOR = "\u001B[34m";

    private WagonsUnloadingController controller;
    private int terminalIndex;
    private List<Boolean> selectedWagons;
    private List<String> availableWagonsList;
    private int currentSelection;

    public WagonsUnloadingUI() {
        controller = new WagonsUnloadingController();
        terminalIndex = 0;
        selectedWagons = new ArrayList<>();
        currentSelection = 0;
    }

    public void run() {
        try {
            interactiveWagonSelection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void interactiveWagonSelection() {
        if (controller.listWagonsSize() == 0) {
            String s = "╔════════════════════════════════════╗\n║       No Wagons Available          ║\n╚════════════════════════════════════╝\n";
            throw new NotValidOrderException(s);
        }

        availableWagonsList = controller.getWagonsList();
        selectedWagons.clear();
        for (int i = 0; i < availableWagonsList.size(); i++) {
            selectedWagons.add(false);
        }

        boolean confirmed = false;
        Scanner scanner = new Scanner(System.in);

        while (!confirmed) {
            displayWagonMenu();
            System.out.println("\nInstructions:");
            System.out.println("• Use numbers 0-" + (availableWagonsList.size()-1) + " to select/deselect wagons");
            System.out.println("• Press 'A' to select/deselect all wagons");
            System.out.println("• Press 'X' to confirm selection");
            System.out.println("• Press 'C' to cancel");
            System.out.print("\nEnter your choice: ");

            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("X")) {
                confirmed = true;
                processSelectedWagons();
            } else if (input.equals("C")) {
                System.out.println("Selection cancelled.");
                return;
            } else if (input.equals("A")) {
                boolean allSelected = areAllWagonsSelected();
                for (int i = 0; i < selectedWagons.size(); i++) {
                    selectedWagons.set(i, !allSelected);
                }
                if (!allSelected) {
                    System.out.println(GREEN_COLOR + "All wagons selected!" + RESET_COLOR);
                } else {
                    System.out.println(GRAY_COLOR + "All wagons deselected!" + RESET_COLOR);
                }
            } else {
                try {
                    int wagonIndex = Integer.parseInt(input);
                    if (wagonIndex >= 0 && wagonIndex < availableWagonsList.size()) {
                        selectedWagons.set(wagonIndex, !selectedWagons.get(wagonIndex));
                        currentSelection = wagonIndex;
                    } else {
                        System.out.println("\u001B[31mInvalid wagon number! Please try again.\u001B[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input! Please enter a number or 'X' to confirm.\u001B[0m");
                }
            }
        }
    }

    private void displayWagonMenu() {
        System.out.println("\n\n╔═══════════════════════════════════════════════╗");
        System.out.println("║            AVAILABLE WAGONS MENU              ║");
        System.out.println("╠═══════════════════════════════════════════════╣");

        int boxWidth = 48;
        int contentWidth = boxWidth - 3;

        for (int i = 0; i < availableWagonsList.size(); i++) {
            String wagonInfo = availableWagonsList.get(i);
            String displayNumber = String.format("%d", i);

            String lineContent;
            if (selectedWagons.get(i)) {
                lineContent = "[" + displayNumber + "] " + wagonInfo + " ✓ SELECTED";
                lineContent = GREEN_COLOR + lineContent + RESET_COLOR;
            }else {
                lineContent = "[" + displayNumber + "] " + wagonInfo;
                lineContent = GRAY_COLOR + lineContent + RESET_COLOR;
            }

            String cleanContent = lineContent.replaceAll("\u001B\\[[;\\d]*m", "");
            int paddingNeeded = Math.max(0, contentWidth - cleanContent.length());

            System.out.println("║ " + lineContent + " ".repeat(paddingNeeded) + " ║");
        }

        System.out.println("╠═══════════════════════════════════════════════╣");

        String countLine = "Selected wagons: " + getSelectedCount();
        int countPadding = Math.max(0, contentWidth - countLine.length());
        System.out.println("║ " + countLine + " ".repeat(countPadding) + " ║");

        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    private int getSelectedCount() {
        int count = 0;
        for (boolean selected : selectedWagons) {
            if (selected) count++;
        }
        return count;
    }
    
    private boolean areAllWagonsSelected() {
        for (boolean selected : selectedWagons) {
            if (!selected) {
                return false;
            }
        }
        return true;
    }

    private void processSelectedWagons() throws NotInitializedException {
        List<Integer> wagonsToUnload = new ArrayList<>();

        for (int i = 0; i < selectedWagons.size(); i++) {
            if (selectedWagons.get(i)) {
                wagonsToUnload.add(i);
            }
        }

        if (wagonsToUnload.isEmpty()) {
            System.out.println("\u001B[33mNo wagons selected for unloading.\u001B[0m");
            return;
        }

        System.out.println("\n\u001B[34mUnloading selected wagons...\u001B[0m");

        List<String> results = controller.unloadSelectedWagons(terminalIndex, selectedWagons);

        boolean allSuccess = true;
        for (String result : results) {
            System.out.println(result);
            if (result.startsWith("✗")) {
                allSuccess = false;
            }
        }

        if (allSuccess) {
            System.out.println("\n\u001B[34mAll selected wagons were unloaded successfully!\u001B[0m");
        } else {
            System.out.println("\n\u001B[33mSome wagons could not be unloaded. Please check warehouse capacity.\u001B[0m");
        }
    }
}