//package org.dei.Authentication.User.UsersUIs;
//
//import org.dei.Sprint1.LAPRUS03.StationDistanceController;
//import org.dei.Utils.Utils;
//
//public class TrafficDispatcherUI {
//
//    private final int ASSEMBLE_TRAIN = 1;
//    private final int EXIT_CODE = 0;
//
//    private StationDistanceController controller;
//
//    public TrafficDispatcherUI() {
//        this.controller = new StationDistanceController();
//    }
//
//    public void run() {
//        boolean exit = false;
//        while (!exit) {
//            System.out.println("\n┌────────────────────────────────────────┐");
//            System.out.println("│           TRAFFIC DISPATCHER           │");
//            System.out.println("├────────────────────────────────────────┤");
//            System.out.println("│ 1. Assemble Train                      │");
//            System.out.println("│ 0. Exit                                │");
//            System.out.println("└────────────────────────────────────────┘");
//
//            int option = Utils.readValue("Select option: ", 1, 0);
//
//            switch (option) {
//                case ASSEMBLE_TRAIN:
//                    new CreateScheduleUI().start();
//                    break;
//                case EXIT_CODE:
//                    exit = true;
//                    System.out.println("┌────────────────────────────────────────┐");
//                    System.out.println("│             Goodbye! 👋                │");
//                    System.out.println("└────────────────────────────────────────┘");
//                    break;
//            }
//        }
//    }
//
//}
//
