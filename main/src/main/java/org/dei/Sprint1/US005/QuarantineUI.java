package org.dei.Sprint1.US005;

import org.dei.Sprint1.US002.NotInitializedException;
import org.dei.Utils.Utils;

/**
 * User Interface for the Returns & Quarantine use case (USEI05).
 *
 * <p>This class provides a console-based interface for quality operators to
 * perform the quarantine process on returned goods. It allows the user to:</p>
 * <ul>
 *   <li>List available terminals in the warehouse</li>
 *   <li>Select a terminal to operate</li>
 *   <li>Trigger the quarantine inspection and restock/discard process</li>
 * </ul>
 *
 * <p>After running the quarantine operation, a summary is printed on the console,
 * and a detailed audit log is automatically generated in the <code>logs/</code> directory.</p>
 *
 */
public class QuarantineUI {

    private final int SELECTED_MIN = 0;
    private QuarantineController controller;
    private int terminalIndex;

    /**
     * Constructs the UI and initializes the {@link QuarantineController}.
     */
    public QuarantineUI(){
         controller  = new QuarantineController();
    }


    /**
     * Runs the quarantine user workflow.
     *
     * <p>This includes:
     * <ul>
     *   <li>Listing available terminals</li>
     *   <li>Selecting a terminal</li>
     *   <li>Executing the quarantine process</li>
     * </ul>
     * Any exception encountered during the process is printed to the console.</p>
     */
    public void run(){
        try {
            listAvailableTerminals();
            doQuarantine();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all available terminals and allows the operator to select one.
     *
     * @throws NotInitializedException if no terminals are available in the repository
     */
    public void listAvailableTerminals() throws NotInitializedException {
        String []availableTerminals = controller.listAvailableTerminals();
        if (availableTerminals == null)
            throw new NotInitializedException("Terminals not available!");

        System.out.println(" ==== Available Terminals: ====");
        for (int i = 0; i < availableTerminals.length; i++)
            System.out.println(availableTerminals[i]);

        terminalIndex = Utils.readValue("Enter which terminal: ", availableTerminals.length, SELECTED_MIN);
        controller.getTerminal(terminalIndex);
    }


    /**
     * Executes the quarantine process for the selected terminal.
     *
     * <p>If the terminal does not support quarantine operations, a warning message
     * is displayed to the user. Otherwise, the inspection summary is printed.</p>
     */
    public void doQuarantine(){
        String info = controller.doQuarantine();
        if (info == null){
            System.out.println("Quarantine not available!");
            return;
        }
        System.out.println(info);
    }
}
