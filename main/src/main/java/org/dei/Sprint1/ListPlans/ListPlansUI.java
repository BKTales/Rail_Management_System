package org.dei.Sprint1.ListPlans;

public class ListPlansUI {
    private ListPlansController controller;

    public ListPlansUI() {
        controller = new ListPlansController();
    }

    public void run(){
        System.out.println(controller.ListPlans());
    }
}
