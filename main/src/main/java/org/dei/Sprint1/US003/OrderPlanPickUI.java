package org.dei.Sprint1.US003;

import org.dei.Sprint1.US002.NotInitializedException;
import org.dei.Utils.Utils;


/**
 * User Interface class for the Picking Plan use case (USEI03).
 *
 * <p>This class provides a console-based interface for planners to:
 * <ul>
 *     <li>List available warehouse terminals</li>
 *     <li>Select an order to plan</li>
 *     <li>Choose a picking heuristic (FF, FFD, BFD)</li>
 *     <li>Enter the trolley capacity</li>
 *     <li>Create and display the picking plan</li>
 * </ul>
 *
 * <p>It interacts with the {@link OrderPlanPickController} to perform
 * business operations and handles user input/output formatting.</p>
 *
 * <p>In case of capacity errors (e.g., a trolley too small to fit any item),
 * the system prompts the user to retry with a new capacity value.</p>
 *
 */
public class OrderPlanPickUI {

    private final int SELECTED_MIN = 0;
    private final double DOUBLE_SELECTED_MIN = 0.1;
    private OrderPlanPickController controller;
    private int terminalIndex;
    private int orderMode;
    private double capacity;

    /**
     * Constructs the UI and initializes the associated controller.
     */ 
    public OrderPlanPickUI() {
        controller = new OrderPlanPickController();
    }

    /**
     * Runs the full picking plan creation workflow.
     *
     * <p>It lists available terminals, prompts for capacity and heuristic selection,
     * retrieves the corresponding order, and creates the plan.</p>
     *
     * @throws NotInitializedException if no terminals or orders are available
     */
    public void run() throws NotInitializedException {
        try {
            listAvailableTerminals();
            readTrolleyCapacity();
            getOrder();
            listPickModes();
            createPlan();

        } catch (NotInitializedException e) {
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║        There are no orders available to be processed         ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * Lists all available terminals and allows the user to select one.
     *
     * @throws NotInitializedException if no terminals are available
     */ 
    public void listAvailableTerminals() throws NotInitializedException {
        String []availableTerminals = controller.listAvailableTerminals();
        if (availableTerminals == null)
            System.out.println("Terminals not available!");

        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║        Available Terminals         ║");
        System.out.println("╚════════════════════════════════════╝");

        for (int i = 0; i < availableTerminals.length; i++)
            System.out.println(availableTerminals[i]);

        terminalIndex = Utils.readValue("Enter which terminal: ", availableTerminals.length, SELECTED_MIN);
    }

    /**
     * Lists all available picking heuristics and allows the user to select one.
     *
     * @throws NotInitializedException if no pick modes are available
     */
    public void listPickModes() throws NotInitializedException {
        String []pickModes = controller.listPickModes();
        if (pickModes == null)
            System.out.println("No modes available!");

        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║        Available PickUp Modes         ║");
        System.out.println("╚═══════════════════════════════════════╝");
        for (int i = 0; i < pickModes.length; i++)
            System.out.println(pickModes[i]);

        orderMode = Utils.readValue("Enter which mode: ", pickModes.length, SELECTED_MIN);
    }

    /**
     * Prompts the user to enter the trolley’s maximum weight capacity.
     */
    public void readTrolleyCapacity(){
        System.out.println("┌────────────────────────────────────┐");
        System.out.println("│          Trolley Capacity          │");
        System.out.println("└────────────────────────────────────┘");
        capacity = Utils.readDoubleValue("Enter trolley capacity (kg): ", Double.MAX_VALUE,DOUBLE_SELECTED_MIN);
    }

    /**
     * Attempts to create a picking plan using the selected parameters.
     *
     * <p>If the capacity is too low to fit any item, the process will prompt
     * the user to retry.</p>
     *
     * @throws InterruptedException if the thread is interrupted during retries
     */
    public void createPlan() throws InterruptedException {
        try{
            System.out.println(controller.createPlan(orderMode, capacity));
        }catch (ExceptionInInitializerError e) {

            System.out.println("╔════════════════════════════════════════════════════╗");
            System.out.println("║  Your trolley can't hold any items, low capacity.  ║");
            System.out.println("╚════════════════════════════════════════════════════╝");

            System.out.println("Retrying...");
            Thread.sleep(1000);
            System.out.println("Retrying..");
            Thread.sleep(1000);
            System.out.println("Retrying.");
            Thread.sleep(1000);
            System.out.println("Retrying");

            run();
        }
    }

    /**
     * Retrieves the next order from the selected terminal to be planned.
     *
     * @throws NotInitializedException if no order is initialized
     */
    public void getOrder() throws NotInitializedException {
        controller.getOrder(terminalIndex);
    }

}
