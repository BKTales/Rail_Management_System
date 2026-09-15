package org.dei.Sprint1.US002;

import org.dei.Sprint1.Order.Order;
import org.dei.Sprint1.Order.OrderMode;
import org.dei.Sprint1.Repository.TerminalRepository;
import org.dei._Facilities.Terminal.WarehouseServices.OrderAllocation.OrderAllocation;
import org.dei._Facilities.Terminal.Terminal;

import java.util.PriorityQueue;

public class OrderAllocationController {
    private TerminalRepository terminalRepo;
    private PriorityQueue<Order> orders;
    private Terminal terminal;

    public OrderAllocationController() {
        terminalRepo = TerminalRepository.getInstance();

    }

    public String[] listAvailableTerminals() {
        return terminalRepo.listTerminals();
    }

    /**
     * Function will call the controller to get and save the
     * selected order
     */
    public void getOrder(int terminalIndex) throws NotInitializedException {
        terminal = terminalRepo.getTerminal(terminalIndex);
        if (terminal == null)
            throw new NotInitializedException("No Terminal Initialized!");

        orders = new PriorityQueue<>(terminal.getOrders());
        if (orders.isEmpty())
            throw new NotInitializedException("No Order Initialized in the Terminal!");
    }

    /**
     * Function will call the controller to execute the search for
     * the item inside the selected order
     *
     * @param eligible true - partial mode, false - strict mode
     * @return if the order can be concluded following the mode selected
     */
    public String orderEligibility(boolean eligible) {
        StringBuilder orderEligibility = new StringBuilder();
        while (!orders.isEmpty()) {
            Order order = orders.poll();
            if (eligible)
                orderEligibility.append(OrderAllocation.orderAllocation(terminal,order, OrderMode.STRICT));
            else
                orderEligibility.append(OrderAllocation.orderAllocation(terminal, order, OrderMode.PARTIAL));
            orderEligibility.append("\n\n");
        }

        return orderEligibility.toString(); // return to be added later, when asked teacher
    }
}
