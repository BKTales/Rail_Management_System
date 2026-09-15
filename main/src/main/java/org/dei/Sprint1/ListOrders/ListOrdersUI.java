package org.dei.Sprint1.ListOrders;


public class ListOrdersUI {
    private ListOrdersController controller;

    public ListOrdersUI() {
        controller = new ListOrdersController();
    }

    public void run(){
        System.out.println(controller.listOrders());
    }
}
