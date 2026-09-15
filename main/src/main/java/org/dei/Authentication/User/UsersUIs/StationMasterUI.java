package org.dei.Authentication.User.UsersUIs;

import org.dei.Sprint1.DisplayTerminal.DisplayTerminalUI;
import org.dei.Sprint1.ListOrders.ListOrdersUI;
import org.dei.Sprint1.ListPlans.ListPlansUI;
import org.dei.Sprint1.ListWagons.ListWagonsUI;
import org.dei.Sprint1.US001.WagonsUnloadingUI;
import org.dei.Sprint1.US002.OrderAllocationUI;
import org.dei.Sprint1.US003.OrderPlanPickUI;
import org.dei.Sprint1.US004.PickingPathUI;
import org.dei.Sprint1.US005.QuarantineUI;
import org.dei.Utils.Utils;

import java.util.Scanner;

public class StationMasterUI {
    private final int BEG_OF_OPTIONS = 1;
    private final int FINAL_OF_OPTIONS = 12;

    private final int LIST_PLANS = 1;
    private final int LIST_ORDERS = 2;
    private final int LIST_ORDERED_PLAN = 3;
    private final int DISPLAY_TERMINAL = 4;
    private final int ALLOCATE_FOR_ORDERS = 5;
    private final int GENERATE_PICKUP_PLAN = 6;
    private final int ORDER_PICKUP_PLAN = 7;
    private final int RETURN_QUARANTINE_ITEM = 8;
    private final int LIST_WAGON = 9;
    private final int UNLOAD_WAGON = 10;
    private final int EXIT = 11;

    public void displayMenu(){
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║              Station Master Menu               ║");
        System.out.println("╚════════════════════════════════════════════════╝");

    }
    public void displayFunctions(){
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║  1 - List Plans                                ║");
        System.out.println("║  2 - List Orders                               ║");
        System.out.println("║  3 - List Ordered Plans                        ║");
        System.out.println("║  4 - Display Terminal                          ║");
        System.out.println("║  5 - Allocate for Orders                       ║");
        System.out.println("║  6 - Generate Pickup Plans                     ║");
        System.out.println("║  7 - Order a Pickup Plans                      ║");
        System.out.println("║  8 - Return Quarantine Items                   ║");
        System.out.println("║  9 - List Wagons                               ║");
        System.out.println("║  10 - Unload Wagon                             ║");
        System.out.println("║  11 - Exit                                     ║");
        System.out.println("╚════════════════════════════════════════════════╝");

    }

    public StationMasterUI() {
    }

    public void run()
    {
        int selectedOption;
        Scanner scanner = new Scanner(System.in);
        displayMenu();
        while (true)
        {
            displayFunctions();
            selectedOption = Utils.readValue("Select an option: ", FINAL_OF_OPTIONS, BEG_OF_OPTIONS);
            switch (selectedOption){

                case LIST_PLANS:
                    new ListPlansUI().run();
                    break;
                case LIST_ORDERS:
                    new ListOrdersUI().run();
                    break;
                case LIST_ORDERED_PLAN:
                    System.out.println("\u001B[31mNot implemented yet!\n\u001B[0m");
                    break;
                case DISPLAY_TERMINAL:
                    new DisplayTerminalUI().run();
                    break;
                case ALLOCATE_FOR_ORDERS:
                    new OrderAllocationUI().run();
                    break;
                case GENERATE_PICKUP_PLAN:
                    new OrderPlanPickUI().run();
                    break;
                case ORDER_PICKUP_PLAN:
                    new PickingPathUI().run();
                    break;
                case RETURN_QUARANTINE_ITEM:
                    new QuarantineUI().run();
                    break;
                case LIST_WAGON:
                    new ListWagonsUI().run();
                    break;
                case UNLOAD_WAGON:
                    new WagonsUnloadingUI().run();
                    break;
                case EXIT:
                    return ;
            }

        }
    }

}
