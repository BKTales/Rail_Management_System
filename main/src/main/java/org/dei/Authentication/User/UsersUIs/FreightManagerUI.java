package org.dei.Authentication.User.UsersUIs;

import org.dei.Sprint3.Services.CreateManualRoute.CreateRouteUI;
import org.dei.Sprint1.ListWagons.ListWagonsUI;
import org.dei.Sprint1.US001.WagonsUnloadingUI;
import org.dei.Utils.Utils;

import java.util.Scanner;

public class FreightManagerUI {
    private final int BEG_OF_OPTIONS = 1;
    private final int FINAL_OF_OPTIONS = 10;

    private final int LIST_WAGON = 1;
    private final int UNLOAD_WAGON = 2;
    private final int CREATE_ROUTE = 3;
    private final int EXIT = 4;

    public void displayMenu(){
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║              Freight Manager Menu              ║");
        System.out.println("╚════════════════════════════════════════════════╝");

    }
    public void displayFunctions(){
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║  1 - List Wagons                               ║");
        System.out.println("║  2 - Unload Wagon                              ║");
        System.out.println("║  3 - Create Route                              ║");
        System.out.println("║  3 - Exit                                      ║");
        System.out.println("╚════════════════════════════════════════════════╝");

    }

    public FreightManagerUI() {
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
                case LIST_WAGON:
                    new ListWagonsUI().run();
                    break;
                case UNLOAD_WAGON:
                    new WagonsUnloadingUI().run();
                    break;
                case CREATE_ROUTE:
                    new CreateRouteUI().start();
                case EXIT:
                    return ;
            }

        }
    }



}
