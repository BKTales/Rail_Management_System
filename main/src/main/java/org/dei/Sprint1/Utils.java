package org.dei.Sprint1;

import java.util.Scanner;

public class Utils {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    /**
     * Function will display the message and read the user's input until a
     * value between max and min is inputed
     * @param msg message
     * @param max maxValue
     * @param min minValue
     * @return a number between max and min
     */
    public static int readValue(String msg, int max, int min) {
        Scanner s = new Scanner(System.in);
        boolean frst = true;
        int i = -1;

        do {
            System.out.println(msg);
            String input = s.nextLine();

            if (!input.isEmpty()) {
                try {
                    i = Integer.parseInt(input);
                } catch (NumberFormatException e) { }
            }
            if (frst)
                msg = ANSI_YELLOW + "Bad answer, try again!" + ANSI_RESET + "\n" + msg; frst = false;
        } while (!(i < max && i >= min));
        return i;
    }

    /**
     * Function will display the message and read the user's input until a
     * yes(y) or no(n) is inputted.
     * @param msg message
     * @return (true - if y/yes) (false - if n/no)
     */
    public static boolean readValueYorN( String msg ) {
        Scanner s = new Scanner(System.in);
        boolean frst = true;
        String  answ;

        do {
            System.out.println(msg);
            answ = s.nextLine();

            if (frst)
                msg = ANSI_YELLOW + "Bad answer, try again!" + ANSI_RESET + "\n" + msg; frst = false;
        } while (!(answ.equals("y") || answ.equals("yes") || answ.equals("n") || answ.equals("no")));

        if (answ.equals("y") || answ.equals("yes"))
            return true;
        return false;
    }
}
