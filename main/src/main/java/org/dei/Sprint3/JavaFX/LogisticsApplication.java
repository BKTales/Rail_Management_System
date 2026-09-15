package org.dei.Sprint3.JavaFX;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.dei.Authentication.AuthenticationController;
import org.dei.Authentication.User.UserRoleDTO;

public class LogisticsApplication extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private AuthenticationController authController;

    private Button btnRoutes;
    private Button btnSchedule;
    // btnTrainStatus REMOVIDO

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("LogiTrack - Rail Management System v2.0");
        showLoginScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void showLoginScreen() {
        try {
            LoginView loginView = new LoginView(this);
            Scene scene = new Scene(loginView, 1280, 800);
            try { scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); }
            catch (Exception e) {}
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMainScreen(AuthenticationController authController) {
        try {
            this.authController = authController;
            UserRoleDTO role = authController.getCurrentUserRole();

            rootLayout = new BorderPane();
            VBox sidebar = createSidebar(role);
            rootLayout.setLeft(sidebar);

            loadView(new RouteView(), btnRoutes);

            Scene scene = new Scene(rootLayout, 1280, 800);
            try { scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); }
            catch (Exception e) {}
            primaryStage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading main interface: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private VBox createSidebar(UserRoleDTO role) {
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(250);
        sidebar.setPadding(new Insets(20, 10, 20, 10));

        Label appTitle = new Label("LOGITRACK v2.0");
        appTitle.getStyleClass().add("app-title");

        String roleDesc = (role != null) ? role.getDescription() : "Unknown";
        Label userInfo = new Label("Role: " + roleDesc);
        userInfo.setStyle("-fx-text-fill: #A1887F; -fx-font-size: 11px; -fx-padding: 0 0 20 10;");

        btnRoutes = createMenuButton("⛗ Route Manager", e -> loadView(new RouteView(), btnRoutes));

        sidebar.getChildren().add(appTitle);
        sidebar.getChildren().add(userInfo);
        sidebar.getChildren().add(btnRoutes);

        // REMOVIDO O btnTrainStatus AQUI

        if (role != null && "FREIGHT_MANAGER".equalsIgnoreCase(role.getId())) {
            btnSchedule = createMenuButton("⏱ Schedules", e -> loadView(new ScheduleView(), btnSchedule));
            sidebar.getChildren().add(btnSchedule);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = createMenuButton("⬅ Logout", e -> {
            if (this.authController != null) this.authController.doLogout();
            showLoginScreen();
        });

        Button btnExit = createMenuButton("✖ Exit", e -> System.exit(0));

        sidebar.getChildren().addAll(spacer, btnLogout, btnExit);
        return sidebar;
    }

    private Button createMenuButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(action);
        return btn;
    }

    private void loadView(javafx.scene.Node view, Button activeBtn) {
        rootLayout.setCenter(view);
        if (btnRoutes != null) btnRoutes.getStyleClass().remove("menu-button-active");
        if (btnSchedule != null) btnSchedule.getStyleClass().remove("menu-button-active");
        if (activeBtn != null) activeBtn.getStyleClass().add("menu-button-active");
    }

    public static void main(String[] args) { launch(args); }
}