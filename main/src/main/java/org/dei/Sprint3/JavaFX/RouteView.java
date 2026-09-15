package org.dei.Sprint3.JavaFX;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.dei.Sprint3.Services.CreateManualRoute.CreateRouteController;
import org.dei._Path.Path;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RouteView extends BorderPane {

    private final CreateRouteController controller;
    private final TextArea logArea;
    private final VBox centerContainer;

    private ToggleButton btnAuto;
    private ToggleButton btnManual;

    public RouteView() {
        this.controller = new CreateRouteController();
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #FDFBF7;");

        Label title = new Label("Route Management");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        btnAuto = new ToggleButton("Automatic Generator");
        btnManual = new ToggleButton("Manual Builder");

        String toggleStyle = "-fx-background-color: #8D6E63; -fx-text-fill: white; -fx-cursor: hand;";
        btnAuto.setStyle(toggleStyle);
        btnManual.setStyle(toggleStyle);

        ToggleGroup group = new ToggleGroup();
        btnAuto.setToggleGroup(group);
        btnManual.setToggleGroup(group);
        btnAuto.setSelected(true);

        HBox controls = new HBox(10, btnAuto, btnManual);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(0, 0, 20, 0));

        VBox topBox = new VBox(10, title, controls);
        this.setTop(topBox);

        centerContainer = new VBox(20);
        this.setCenter(centerContainer);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(120);
        logArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-control-inner-background: #EFEBE9;");

        VBox bottomBox = new VBox(10, new Label("System Logs:"), logArea);
        bottomBox.setPadding(new Insets(20, 0, 0, 0));
        this.setBottom(bottomBox);

        renderAutoView();
        btnAuto.setOnAction(e -> { if(!btnAuto.isSelected()) btnAuto.setSelected(true); renderAutoView(); });
        btnManual.setOnAction(e -> { if(!btnManual.isSelected()) btnManual.setSelected(true); renderManualView(); });
    }

    private void renderAutoView() {
        centerContainer.getChildren().clear();
        log("Switched to Automatic Mode.");

        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 0);");
        card.setMaxWidth(800);

        Label lbl = new Label("Generate Shortest Path");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #3E2723;");

        ComboBox<String> startBox = new ComboBox<>();
        try { startBox.getItems().addAll(controller.getAllStationNames()); } catch (Exception e) {}
        makeSearchable(startBox);
        startBox.setPromptText("Select Origin...");
        startBox.setMaxWidth(300);
        applyIsolatedStationStyler(startBox);

        ComboBox<String> endBox = new ComboBox<>();
        makeSearchable(endBox);
        endBox.setPromptText("Select Destination (Reachable only)...");
        endBox.setMaxWidth(300);
        endBox.setDisable(true);

        startBox.setOnAction(e -> {
            String start = startBox.getValue();
            if (start != null) {
                boolean isIsolated = false;
                try { isIsolated = controller.isStationIsolated(start); } catch(Exception ex) {}

                if(isIsolated) {
                    log("[WARNING] Selected station '" + start + "' is ISOLATED (No tracks out).");
                    endBox.setDisable(true);
                    return;
                }

                List<String> reachable = controller.getReachableStations(start);
                updateSearchableItems(endBox, reachable);

                endBox.setDisable(reachable.isEmpty());
                endBox.setPromptText(reachable.isEmpty() ? "No reachable stations." : "Select Destination...");
            }
        });

        Button btnGen = new Button("Calculate Path");
        btnGen.setMaxWidth(300);
        btnGen.setStyle("-fx-background-color: #5D4037; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");

        btnGen.setOnAction(e -> {
            try {
                String s = startBox.getValue();
                String d = endBox.getValue();
                if(s == null || d == null) throw new IllegalArgumentException("Select both stations.");
                Path p = controller.createAutomaticPath(s, d);
                log("[SUCCESS] Path generated with " + p.getRailFacilities().size() + " stops.");
                askToSaveRoute(p);
            } catch (Exception ex) {
                log("[ERROR] " + ex.getMessage());
            }
        });

        card.getChildren().addAll(lbl, new Separator(),
                new Label("From (Origin):"), startBox,
                new Label("To (Destination):"), endBox,
                new Separator(), btnGen);

        centerContainer.getChildren().add(card);
    }

    private void renderManualView() {
        centerContainer.getChildren().clear();
        log("Switched to Manual Mode.");

        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 0);");
        card.setMaxWidth(800);

        Label lbl = new Label("Build Custom Path (Station by Station)");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #3E2723;");

        ListView<String> sequenceList = new ListView<>();
        sequenceList.setPrefHeight(150);
        sequenceList.setPlaceholder(new Label("Select Origin to start..."));

        ComboBox<String> nextStationBox = new ComboBox<>();
        try { nextStationBox.getItems().addAll(controller.getAllStationNames()); } catch (Exception e){}

        makeSearchable(nextStationBox);

        nextStationBox.setPromptText("Select Origin...");
        nextStationBox.setMaxWidth(300);
        applyIsolatedStationStyler(nextStationBox);

        Button btnAdd = new Button("Add Station");
        Button btnSaveManual = new Button("Save Manual Path");
        btnSaveManual.setDisable(true);
        Button btnReset = new Button("Reset");

        List<String> currentSequence = new ArrayList<>();

        btnAdd.setOnAction(e -> {
            String selected = nextStationBox.getValue();
            if (selected != null) {
                currentSequence.add(selected);
                sequenceList.getItems().add((currentSequence.size()) + ". " + selected);

                List<String> neighbors = controller.getDirectConnections(selected);
                if (currentSequence.size() >= 2) {
                    neighbors.remove(currentSequence.get(currentSequence.size() - 2));
                }

                if (neighbors.isEmpty()) {
                    log("[INFO] Dead end reached at " + selected);
                    nextStationBox.setDisable(true);
                    nextStationBox.setPromptText("End of Line.");
                } else {
                    updateSearchableItems(nextStationBox, neighbors);
                    nextStationBox.setValue(null);
                    nextStationBox.getEditor().clear();
                    nextStationBox.setPromptText("Select Next Stop (Connected to " + selected + ")...");
                    nextStationBox.show();
                    nextStationBox.setCellFactory(null);
                }

                if(currentSequence.size() >= 2) btnSaveManual.setDisable(false);
            }
        });

        btnReset.setOnAction(e -> {
            currentSequence.clear();
            sequenceList.getItems().clear();
            nextStationBox.setDisable(false);
            try {
                updateSearchableItems(nextStationBox, controller.getAllStationNames());
            } catch (Exception ex){}

            nextStationBox.setPromptText("Select Origin...");
            nextStationBox.setValue(null);
            nextStationBox.getEditor().clear();
            applyIsolatedStationStyler(nextStationBox);
            btnSaveManual.setDisable(true);
            log("[INFO] Sequence cleared.");
        });

        btnSaveManual.setOnAction(e -> {
            try {
                if(currentSequence.size() < 2) throw new IllegalArgumentException("Need > 2 stations.");
                Path p = controller.createIsolatedPath(currentSequence);
                log("[SUCCESS] Manual Path created!");
                askToSaveRoute(p);
                btnReset.fire();
            } catch (Exception ex) {
                log("[ERROR] " + ex.getMessage());
            }
        });

        HBox actions = new HBox(10, nextStationBox, btnAdd, btnReset);
        actions.setAlignment(Pos.CENTER_LEFT);
        card.getChildren().addAll(lbl, new Separator(), sequenceList, actions, new Separator(), btnSaveManual);
        centerContainer.getChildren().add(card);
    }

    private void log(String msg) { logArea.appendText("> " + msg + "\n"); }

    private void applyIsolatedStationStyler(ComboBox<String> box) {
        box.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); setGraphic(null);
                } else {
                    setText(item);
                    boolean iso = false;
                    try { iso = controller.isStationIsolated(item); } catch(Exception e){}
                    if (iso) {
                        setText(item + " (Isolated)");
                        setStyle("-fx-text-fill: #B0BEC5; -fx-font-style: italic;");
                    } else {
                        setStyle("-fx-text-fill: #3E2723;");
                    }
                }
            }
        });
    }

    private void askToSaveRoute(Path path) {
        centerContainer.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("savePanel"));
        VBox saveBox = new VBox(10);
        saveBox.setId("savePanel");
        saveBox.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2; -fx-background-color: #E8F5E9; -fx-padding: 10; -fx-background-radius: 5;");
        saveBox.setMaxWidth(800);
        Label l = new Label("Path created! Save to DB?");
        l.setStyle("-fx-font-weight: bold;");
        HBox form = new HBox(10);
        form.setAlignment(Pos.CENTER_LEFT);
        DatePicker dp = new DatePicker();
        TextField timeField = new TextField("12:00");
        timeField.setPrefWidth(80);
        Button btnConfirm = new Button("Confirm Save");
        btnConfirm.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConfirm.setOnAction(e -> {
            try {
                LocalTime time;
                try {
                    time = LocalTime.parse(timeField.getText());
                } catch (Exception ex) {
                    log("Invalid time format. Use HH:mm (e.g., 14:30).");
                    return;
                }
                LocalDateTime dt = dp.getValue().atTime(time);

                // 4. VERIFICAÇÃO DE TEMPO PASSADO
                if (dt.isBefore(LocalDateTime.now())) {
                    log("[VALIDATION ERROR] Departure time cannot be in the past!");
                    return;
                }
                controller.createAndSaveRoute(path, new ArrayList<>(), dt);
                log("[DB SUCCESS] Saved!");
                centerContainer.getChildren().remove(saveBox);
            } catch(Exception ex) { log("[SAVE ERROR] " + ex.getMessage()); }
        });
        form.getChildren().addAll(new Label("Departure:"), dp, timeField, btnConfirm);
        saveBox.getChildren().addAll(l, form);
        centerContainer.getChildren().add(1, saveBox);
    }

    private <T> void updateSearchableItems(ComboBox<T> box, List<T> newItems) {
        box.getItems().setAll(newItems);
        box.setUserData(FXCollections.observableArrayList(newItems));
    }

    private <T> void makeSearchable(ComboBox<T> box) {
        box.setEditable(true);
        if (box.getUserData() == null) {
            box.setUserData(FXCollections.observableArrayList(box.getItems()));
        }

        box.setOnShowing(e -> {
            ObservableList<T> masterList = (ObservableList<T>) box.getUserData();
            if (masterList != null) {
                T selected = box.getSelectionModel().getSelectedItem();
                box.setItems(masterList);
                if(selected != null) box.getSelectionModel().select(selected);
            }
        });

        box.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (box.getSelectionModel().getSelectedItem() != null) {
                String selectedStr = box.getConverter().toString(box.getSelectionModel().getSelectedItem());
                if (selectedStr != null && selectedStr.equals(newText)) return;
            }

            ObservableList<T> masterList = (ObservableList<T>) box.getUserData();
            if (masterList == null) return;

            if (newText == null || newText.isEmpty()) {
                box.setItems(masterList);
                return;
            }

            ObservableList<T> filteredList = FXCollections.observableArrayList();
            for (T item : masterList) {
                String itemText = (item == null) ? "" : box.getConverter().toString(item);
                if (itemText.toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(item);
                }
            }

            box.setItems(filteredList);
            box.getEditor().setText(newText);
            Platform.runLater(() -> box.getEditor().positionCaret(newText.length()));

            if(!box.isShowing()) box.show();
        });
    }
}