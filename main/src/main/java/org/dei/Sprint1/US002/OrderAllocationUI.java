package org.dei.Sprint1.US002;

import org.dei.Utils.Utils;

public class OrderAllocationUI {
    private final int SELECTED_MIN = 0;

    private OrderAllocationController controller;
    private int terminalIndex;
    private boolean orderAllocationType;

    public OrderAllocationUI() {
        controller = new OrderAllocationController();
    }

    public void run() {
        try {
            getOrder();
            orderEligibility();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void listAvailableTerminals() throws NotInitializedException{
        String []availableTerminals = controller.listAvailableTerminals();
        if (availableTerminals == null)
            throw new NotInitializedException("Terminals not available!");

        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║        Available Terminals         ║");
        System.out.println("╚════════════════════════════════════╝");

        for (int i = 0; i < availableTerminals.length; i++)
            System.out.println(availableTerminals[i]);

        terminalIndex = Utils.readValue("Enter which terminal: ", availableTerminals.length, SELECTED_MIN);
    }

    public void getOrder() throws NotInitializedException{
        controller.getOrder(terminalIndex);
    }

    public void orderEligibility() {
        orderAllocationType = Utils.readValueYorN("Want to perform the allocation in strict mode(y/n):");
        System.out.println(controller.orderEligibility(orderAllocationType));
    }

}
