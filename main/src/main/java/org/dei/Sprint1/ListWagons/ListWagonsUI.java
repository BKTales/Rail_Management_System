package org.dei.Sprint1.ListWagons;

public class ListWagonsUI {
    private ListWagonsController controller;

    public ListWagonsUI() {
        controller = new ListWagonsController();
    }

    public void run(){
        System.out.println(controller.listWagons());
    }
}
