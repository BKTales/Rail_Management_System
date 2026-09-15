package org.dei.Sprint1.DisplayTerminal;

import org.dei.Sprint1.Repository.TerminalRepository;
import org.dei._Facilities.Terminal.Terminal;

public class DisplayTerminalController {
    private Terminal terminal;

    public DisplayTerminalController() {
        terminal = TerminalRepository.getInstance().getTerminal(0);
    }

    public String displayTerminal() {
        return terminal.toString() ;
    }
}
