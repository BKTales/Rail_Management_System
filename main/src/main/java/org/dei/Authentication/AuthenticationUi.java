package org.dei.Authentication;

import org.dei.Authentication.User.UserRoleDTO;
import org.dei.Authentication.User.UsersUIs.FreightManagerUI;
import org.dei.Authentication.User.UsersUIs.StationMasterUI;
import org.dei.Authentication.User.UsersUIs.StationStorageMangerUI;

import java.util.List;
import java.util.Scanner;

public class AuthenticationUi implements Runnable {

    private final AuthenticationController controller;

    // ANSI colors
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String ORANGE = "\u001B[38;5;208m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public AuthenticationUi() {
        controller = new AuthenticationController();
    }

    public void displayColoredBanner() {
        String tremEsquerda =
                "                         ______\n" +
                        "                      .-\"\"\"\".._'.       _,##\n" +
                        "               _..__ |.-\"\"\"\"-.|  |   _,##'`-._\n" +
                        "              (_____)||_____||  |_,##'`-._,##'`\n" +
                        "              _|   |.;-\"\"\"-.  |  |#'`-._,##'`\n" +
                        "           _.;_ `--' `\\    \\ |.'`\\._,##'`\n" +
                        "          /.-.\\ `\\     |.-\";.`_, |##'`\n" +
                        "          |\\__/   | _..;__  |'-' /\n" +
                        "          '.____.'_.-`)\\--' /'-'`\n" +
                        "           //||\\\\(_.-'_,\'-'\n" +
                        "         (`-...-')_,##'`\n" +
                        "  jgs _,##`-..,-;##`\n" +
                        "   _,##'`-._,##'`\n" +
                        "_,##'`-._,##'`\n" +
                        "  `-._,##";

        String textoCentral =
                " \n \n \n" + // linhas vazias no topo para centralizar
                        " █████                          ███  ███████████                              █████     \n" +
                        "▒▒███                          ▒▒▒  ▒█▒▒▒███▒▒▒█                             ▒▒███      \n" +
                        " ▒███         ██████   ███████ ████ ▒   ▒███  ▒  ████████   ██████    ██████  ▒███ █████\n" +
                        " ▒███        ███▒▒███ ███▒▒███▒▒███     ▒███    ▒▒███▒▒███ ▒▒▒▒▒███  ███▒▒███ ▒███▒▒███ \n" +
                        " ▒███       ▒███ ▒███▒███ ▒███ ▒███     ▒███     ▒███ ▒▒▒   ███████ ▒███ ▒▒▒  ▒██████▒  \n" +
                        " ▒███      █▒███ ▒███▒███ ▒███ ▒███     ▒███     ▒███      ███▒▒███ ▒███  ███ ▒███▒▒███ \n" +
                        " ███████████▒▒██████ ▒▒███████ █████    █████    █████    ▒▒████████▒▒██████  ████ █████\n" +
                        "▒▒▒▒▒▒▒▒▒▒▒  ▒▒▒▒▒▒   ▒▒▒▒▒███▒▒▒▒▒    ▒▒▒▒▒    ▒▒▒▒▒      ▒▒▒▒▒▒▒▒  ▒▒▒▒▒▒  ▒▒▒▒ ▒▒▒▒▒ \n" +
                        "                      ███ ▒███                                                          \n" +
                        "                     ▒▒██████                                                           \n" +
                        "                      ▒▒▒▒▒▒";

        String tremDireita = tremEsquerda;

        String[] leftLines = tremEsquerda.split("\n");
        String[] centerLines = textoCentral.split("\n");
        String[] rightLines = tremDireita.split("\n");

        int maxHeight = Math.max(leftLines.length, Math.max(centerLines.length, rightLines.length));

        int widthLeft = 0, widthCenter = 0, widthRight = 0;
        for (String line : leftLines) widthLeft = Math.max(widthLeft, line.length());
        for (String line : centerLines) widthCenter = Math.max(widthCenter, line.length());
        for (String line : rightLines) widthRight = Math.max(widthRight, line.length());

        for (int i = 0; i < maxHeight; i++) {
            String left = i < leftLines.length ? leftLines[i] : "";
            left = String.format("%-" + widthLeft + "s", left);

            String center = i < centerLines.length ? centerLines[i] : "";
            center = String.format("%-" + widthCenter + "s", center);

            String right = i < rightLines.length ? rightLines[i] : "";
            right = String.format("%-" + widthRight + "s", right);

            System.out.println(BLUE + left + RESET + " " +
                    PURPLE + center + RESET + " " +
                    RED + right + RESET);
        }
    }



    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        boolean mainLoop = true;
        displayColoredBanner();
        int i = 0;
        do{
            if (i == 1){
                break;
            }
            System.out.print("Do you want to log in? [Y/N]: ");
            String loginResponse = sc.nextLine().trim().toUpperCase();

            if (!loginResponse.equals("Y")) {
                System.out.println(BLUE + "Exiting application." + RESET);
                break;
            }

            boolean success = doLogin();
            if (!success) {
                System.out.println(RED + "Failed to login after multiple attempts." + RESET);
                i++;
                continue;
            }

            UserRoleDTO currentRole = controller.getCurrentUserRole();
            if (currentRole == null) {
                System.out.println(RED + "No role assigned to user." + RESET);
                logout();
                continue;
            }

            boolean sessionActive = true;
            while (sessionActive) {
                try {
                    redirectToRoleUI(currentRole);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.print("\nDo you want to perform another operation? [Y/N]: ");
                String continueResponse = sc.nextLine().trim().toUpperCase();
                if (!continueResponse.equals("Y")) {
                    sessionActive = false;
                }
            }

            logout();

            System.out.print("\nDo you want to login with another user? [Y/N]: ");
            String anotherUser = sc.nextLine().trim().toUpperCase();
            if (!anotherUser.equals("Y")) {
                mainLoop = false;
                System.out.println(BLUE + "Goodbye!" + RESET);
            }
        }while (mainLoop);
    }

    private boolean doLogin() {
        Scanner sc = new Scanner(System.in);

        System.out.println("╔══════════════════════════════╗");
        System.out.println("║           LOGIN UI           ║");
        System.out.println("╚══════════════════════════════╝");

        int maxAttempts = 3;
        boolean success = false;

        do {
            maxAttempts--;
            String email = readEmail();
            String pwd = readPassword();

            success = controller.doLogin(email, pwd);
            if (!success) {
                System.out.println(RED + "╔══════════════════════════════════════╗" + RESET);
                System.out.println(RED + "║     You have " + maxAttempts + " attempt(s) left.      ║" + RESET);
                System.out.println(RED + "╚══════════════════════════════════════╝" + RESET);
            }
        } while (!success && maxAttempts > 0);

        if (success) {
            System.out.println(GREEN + "Login successful!" + RESET);
        }

        return success;
    }

    private void logout() {
        controller.doLogout();
        System.out.println(ORANGE + "You have been logged out." + RESET);
    }

    private String readEmail() {
        System.out.print("Enter UserId/Email: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    private String readPassword() {
        System.out.print("Enter Password: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    private void addUser() {
        Scanner sc = new Scanner(System.in);
        try {
            List<UserRoleDTO> roles = controller.getUserRoles();
            if ((roles == null) || (roles.isEmpty())) {
                System.out.println(RED + "No role assigned to user." + RESET);
            } else {
                UserRoleDTO role = selectsRole(roles);

                System.out.print("Enter email: ");
                String email = sc.nextLine().trim();

                System.out.print("Enter password (7 characters, 2 digits, 3 uppercase): ");
                String pwd = sc.nextLine().trim();

                controller.addUser(email, pwd, role.getId(), role.getDescription());
                System.out.println(GREEN + "User created successfully." + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + "Error creating user: " + e.getMessage() + RESET);
        }
    }

    private UserRoleDTO selectsRole(List<UserRoleDTO> roles) {
        if (roles.size() == 1) return roles.get(0);
        return readRole(roles);
    }

    private UserRoleDTO readRole(List<UserRoleDTO> roles) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select User Role by choosing an index:");
        for (int i = 0; i < roles.size(); i++) {
            System.out.println("[" + i + "] " + roles.get(i).getDescription());
        }

        int index = -1;
        boolean selecting = true;
        while (selecting) {
            System.out.print("Enter index (0-" + (roles.size() - 1) + "): ");
            String input = sc.nextLine().trim();
            try {
                index = Integer.parseInt(input);
                if (index >= 0 && index < roles.size()) selecting = false;
                else System.out.println(RED + "Invalid index." + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input. Please enter a valid integer." + RESET);
            }
        }
        return roles.get(index);
    }

    private void redirectToRoleUI(UserRoleDTO selectedRole) throws InterruptedException {
        if (selectedRole == null || selectedRole.getId() == null || selectedRole.getDescription() == null) {
            System.out.println(RED + "Invalid role information." + RESET);
            return;
        }

        switch (selectedRole.getId()) {
            case "TRAIN_DRIVER" -> System.out.println("Not implemented yet!");
            case "STATION_MASTER" -> new StationMasterUI().run();
            case "STATION_STOR_MANAGER" -> new StationStorageMangerUI().run();
            case "FREIGHT_MANAGER" -> new FreightManagerUI().run();
            case "PLANNER" -> System.out.println("Not implemented yet!");
            //case "TRAFFIC_DISPATCHER" -> new TrafficDispatcherUI().run();
            default -> System.out.println(RED + "Unknown role!" + RESET);
        }
    }

}
