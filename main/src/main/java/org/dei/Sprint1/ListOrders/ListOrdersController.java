package org.dei.Sprint1.ListOrders;

import org.dei.Sprint1.Repository.TerminalRepository;
import org.dei._Facilities.Terminal.Terminal;

public class ListOrdersController {
    private Terminal terminal;

    public ListOrdersController() {
        terminal = TerminalRepository.getInstance().getTerminal(0);
    }

    public String listOrders() {
        return terminal.listOrders() ;
    }
}
