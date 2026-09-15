package org.dei.Sprint3.JavaFX;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.dei.Authentication.AuthenticationController;

public class LoginView extends StackPane {

    private final AuthenticationController authController;
    private final LogisticsApplication mainApp;

    public LoginView(LogisticsApplication mainApp) {
        this.mainApp = mainApp;
        this.authController = new AuthenticationController();

        this.setStyle("-fx-background-color: #FDFBF7;");

        VBox loginBox = new VBox(15);
        loginBox.setStyle("-fx-background-color: white; -fx-padding: 40; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
        loginBox.setMaxWidth(350);
        loginBox.setMaxHeight(450);
        loginBox.setAlignment(Pos.CENTER);

        Label title = new Label("LOGITRACK");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        Label subtitle = new Label("Rail Management System");
        subtitle.setStyle("-fx-text-fill: #6D4C41; -fx-padding: 0 0 20 0;");

        TextField userField = new TextField();
        userField.setPromptText("Email (ex: freightManager@gmail.com)");
        userField.setStyle("-fx-padding: 10;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle("-fx-padding: 10;");

        Label errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: #D32F2F; -fx-font-size: 12px;");
        errorLbl.setVisible(false);

        Button loginBtn = new Button("LOGIN");
        loginBtn.setStyle("-fx-background-color: #3E2723; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20; -fx-background-radius: 5;");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        loginBtn.setOnAction(e -> handleLogin(userField.getText(), passField.getText(), errorLbl));

        passField.setOnAction(e -> loginBtn.fire());

        Label hint = new Label("Hint: freightManager@gmail.com / ADMin12");
        hint.setStyle("-fx-text-fill: gray; -fx-font-size: 10px;");

        loginBox.getChildren().addAll(title, subtitle, new Label("Email:"), userField, new Label("Password:"), passField, errorLbl, new Region(), loginBtn, hint);
        this.getChildren().add(loginBox);
    }

    private void handleLogin(String id, String pwd, Label errorLbl) {
        if (id.isEmpty() || pwd.isEmpty()) {
            errorLbl.setText("Please enter email and password.");
            errorLbl.setVisible(true);
            return;
        }

        boolean success = authController.doLogin(id, pwd);

        if (success) {
            // Check if user role should be redirected to terminal
            // Only FREIGHT_MANAGER can use JavaFX, all other roles go to terminal
            var role = authController.getCurrentUserRole();
            if (role != null) {
                String roleId = role.getId();
                // Redirect all roles except FREIGHT_MANAGER to terminal UI
                if (!"FREIGHT_MANAGER".equalsIgnoreCase(roleId)) {
                    // Redirect to terminal interface
                    redirectToTerminal();
                    return;
                }
            }
            mainApp.showMainScreen(authController);
        } else {
            errorLbl.setText("Invalid credentials. Try again.");
            errorLbl.setVisible(true);
        }
    }
    
    private void redirectToTerminal() {
        // Close JavaFX window and start terminal UI
        javafx.application.Platform.exit();
        // Start terminal UI in a new thread
        new Thread(() -> {
            try {
                org.dei.Authentication.AuthenticationUi terminalUI = new org.dei.Authentication.AuthenticationUi();
                terminalUI.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}