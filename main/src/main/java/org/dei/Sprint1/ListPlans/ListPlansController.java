package org.dei.Sprint1.ListPlans;

import org.dei.Sprint1.Repository.TerminalRepository;
import org.dei._Facilities.Terminal.Terminal;

public class ListPlansController {
    private Terminal terminal;

    public ListPlansController() {
        terminal = TerminalRepository.getInstance().getTerminal(0);
    }

    public String ListPlans() {
        StringBuilder sb = new StringBuilder();

        String[] a = terminal.listPlans();
        if (a == null)
            return ("No plans available!");
        for (String s : a)
            sb.append(s);

        return sb.toString() ;
    }
}
