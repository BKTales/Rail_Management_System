package org.dei.Sprint1.Repository;

import org.dei._Facilities.Terminal.Terminal;

import java.util.ArrayList;

public class TerminalRepository {
    private static TerminalRepository instance = null;
    private ArrayList<Terminal> terminals;

    public static TerminalRepository getInstance() {
        if (instance == null) {
            instance = new TerminalRepository();
        }
        return instance;
    }

    private TerminalRepository() {
        terminals = new ArrayList<>();
    }

    public void add(Terminal terminal){
        terminals.add(terminal);
    }

    public void remove(Terminal terminal){
        terminals.remove(terminal);
    }

    public Terminal getTerminal(int index){
        return terminals.get(index);
    }

    public String[] listTerminals(){
        StringBuilder s = new StringBuilder();
        int i = 0;

        if (terminals.isEmpty())
            return (null);
        for (Terminal terminal : terminals) {
            s.append("[" + i + "] - terminal" + i + "\n");
            i++;
        }
        return (s.toString().split("\n"));
    }

    public int size(){
        return terminals.size();
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for (Terminal terminal : terminals) {
            s.append(terminal.toString() + "\n");
        }
        return (s.toString());
    }
}
