package org.dei.Sprint1.US003;

import org.dei.Sprint1.Order.Order;
import org.dei._Facilities.Terminal.Warehouse.PickupPlan;
import org.dei.Sprint1.Repository.TerminalRepository;
import org.dei._Facilities.Terminal.Terminal;
import org.dei.Sprint1.US002.NotInitializedException;


/**
 * Controller class for the Picking Plan use case (USEI03).
 *
 * <p>This controller coordinates the creation of picking plans for warehouse orders,
 * allowing planners to select a terminal, retrieve pending orders, and generate a plan
 * according to one of the defined packing heuristics:
 * <ul>
 *     <li>First Fit (FF)</li>
 *     <li>First Fit Decreasing (FFD)</li>
 *     <li>Best Fit Decreasing (BFD)</li>
 * </ul>
 *
 * <p>The picking plan ensures that products are grouped into trolleys without exceeding
 * their weight capacity, based on the chosen heuristic.</p>
 *
 * <p>This controller interacts directly with the {@link TerminalRepository} and
 * {@link Terminal} classes to retrieve orders and create plans.</p>
 *
 */
public class OrderPlanPickController {

    private TerminalRepository terminalRepo;
    private Order order;
    private Terminal terminal;

    /**
     * Constructs a new controller and retrieves the singleton instance
     * of the {@link TerminalRepository}.
     */
    public OrderPlanPickController() {
        terminalRepo = TerminalRepository.getInstance();
    }

    /**
     * Lists all available terminals in the system.
     *
     * @return an array of terminal names available for selection
     */
    public String[] listAvailableTerminals() {
        return terminalRepo.listTerminals();
    }

    /**
     * Retrieves the order to plan from a specific terminal.
     *
     * @param terminalIndex the index of the terminal selected by the user
     * @throws NotInitializedException if no terminal or no order is available
     */
    public void getOrder(int terminalIndex) throws NotInitializedException {
        terminal = terminalRepo.getTerminal(terminalIndex);
        if (terminal == null)
            throw new NotInitializedException("No Terminal Initialized!");

        order = terminal.getOrderToPlan();
        if (order == null)
            throw new NotInitializedException("No Order Initialized in the Terminal!");
    }


    /**
     * Lists all available picking heuristics (packing modes).
     *
     * @return an array of strings representing the available heuristics,
     *         or {@code null} if no modes are available
     */
    public String[] listPickModes() {
        String []toReturn = Terminal.listPickModes().split("\n");
        if (toReturn.length == 0)
            return null;
        return toReturn;
    }


    /**
     * Creates a picking plan for the selected order using a chosen heuristic
     * and a given trolley capacity.
     *
     * <p>If successful, removes the order from the terminal’s queue.</p>
     *
     * @param mode     the selected heuristic mode index
     * @param capacity the maximum trolley capacity in kilograms
     * @return a message describing the plan creation result
     */
    public String createPlan(int mode,double capacity){
        PickupPlan newPickupPlan = terminal.getPlan(order,mode,capacity);
        StringBuilder string = new StringBuilder();
        if (newPickupPlan == null){
            string.append("Creation has failed");
        }else{
            string.append("Plan has been created!\n");
            string.append(newPickupPlan);
            terminal.removeOrder();
        }
        return string.toString();
    }


}
