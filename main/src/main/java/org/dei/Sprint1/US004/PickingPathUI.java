package org.dei.Sprint1.US004;

import org.dei.Utils.Exceptions.NoPlanDefinedException;
import org.dei.Utils.Utils;

public class PickingPathUI {
    private final int SELECTED_MIN = 0;
    private PickingPathController controller;
    private int whichPlan = -1;

    public PickingPathUI() {
        controller = new PickingPathController();
    }

    public void run() {
        try {
            listAvailablePlans();
            System.out.println(controller.generatePickPath(whichPlan));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void listAvailablePlans() throws NoPlanDefinedException {
        String []plans = controller.listAvailablePlans();
        if (plans == null)
            throw new NoPlanDefinedException(" Error: could not choose the plan, because there are no plans available.");

        for (String plan : plans)
            System.out.println(plan);
        whichPlan = Utils.readValue("Enter which plan: ", plans.length, SELECTED_MIN);
    }
}
