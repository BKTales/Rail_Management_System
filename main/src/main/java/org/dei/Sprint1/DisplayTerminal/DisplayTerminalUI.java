package org.dei.Sprint1.DisplayTerminal;


public class DisplayTerminalUI {
    private DisplayTerminalController controller;

    public DisplayTerminalUI() {
        controller = new DisplayTerminalController();
    }

    public void run(){
        System.out.println(controller.displayTerminal());
    }
}
