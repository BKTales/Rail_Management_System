package org.dei.Sprint3.JavaFX;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import org.dei.Repository.GraphRepository;
import org.dei.Repository.RouteRepository;
import org.dei.Sprint3.DTOs.LocomotiveWithStatus;
import org.dei.Sprint3.DTOs.WagonWithStatus;
import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
import org.dei.Sprint3.Schedule.CreateScheduleController;
import org.dei.Sprint3.Services.CalculateDistanceBetweenFacilities;
import org.dei.Sprint3.Services.CreateManualRoute.CreateRouteController;
import org.dei.Sprint3.Services.ResetDatabase;
import org.dei._Facilities.Facility;
import org.dei._Path.Route;
import org.dei._Train.Freight;
import org.dei._Train.Locomotive;
import org.dei._Train.Train;
import org.dei._Train.Wagon;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Interface gráfica para gestão de agendamentos.
 * Inclui criação de Freights, agendamento de Comboios e monitorização da rede.
 */
public class ScheduleView extends BorderPane {

    private final CreateScheduleController controller;
    private final CreateRouteController routeController;

    // Componentes de UI
    private ComboBox<Facility> originBox;
    private ListView<WagonWithStatus> wagonList;
    private ComboBox<Route> routeCombo;
    private ListView<Freight> freightList;
    private ListView<LocomotiveWithStatus> locoList;
    private ListView<Train> trainListView;
    private VBox detailsPane;

    public ScheduleView() {
        this.controller = new CreateScheduleController();
        this.routeController = new CreateRouteController();
        initializeUI();
    }

    private void initializeUI() {
        this.getStyleClass().add("root");
        this.setPadding(new Insets(20));

        // Cabeçalho
        Label title = new Label("Logistics Scheduler");
        title.getStyleClass().add("content-header");
        this.setTop(title);

        // Refresh routes when view is initialized
        controller.forceRefreshData();

        // Layout Principal: 3 Colunas
        HBox content = new HBox(15);
        content.setAlignment(Pos.TOP_LEFT);

        VBox col1 = createFreightCreator();
        VBox col2 = createTrainScheduler();
        VBox col3 = createNetworkMonitor();

        // Expansão Horizontal
        HBox.setHgrow(col1, Priority.ALWAYS);
        HBox.setHgrow(col2, Priority.ALWAYS);
        HBox.setHgrow(col3, Priority.ALWAYS);

        // Largura igual para as colunas
        col1.setMaxWidth(Double.MAX_VALUE); col1.setPrefWidth(0);
        col2.setMaxWidth(Double.MAX_VALUE); col2.setPrefWidth(0);
        col3.setMaxWidth(Double.MAX_VALUE); col3.setPrefWidth(0);

        content.getChildren().addAll(col1, col2, col3);
        this.setCenter(content);

        // Botão de Reset na parte inferior
        addResetButton(this);
    }

    // ==========================================
    // COLUNA 1: COMPOR CARGA (FREIGHT)
    // ==========================================
    private VBox createFreightCreator() {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");

        Label lbl = new Label("1. Compose Freight");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #3E2723;");

        // Carregar dados iniciais
        List<Facility> stations = controller.getAllFacilities();
        Map<Integer, Integer> stockMap = controller.getWagonCounts();

        // Seletor de Origem (Pesquisável + Cinzento se vazio)
        originBox = new ComboBox<>();
        originBox.setPromptText("Type to search Origin...");
        originBox.setMaxWidth(Double.MAX_VALUE);
        setupSearchableComboBox(originBox, stations, stockMap, true);

        // Seletor de Destino
        ComboBox<Facility> destBox = new ComboBox<>();
        destBox.setPromptText("Type to search Destination...");
        destBox.setMaxWidth(Double.MAX_VALUE);
        setupSearchableComboBox(destBox, stations, null, false);

        // Lista de Vagões
        wagonList = new ListView<>();
        wagonList.setPrefHeight(250);
        wagonList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        wagonList.setPlaceholder(new Label("Select an Origin to see wagons."));

        // Ação ao selecionar Origem
        originBox.setOnAction(e -> {
            Facility selected = originBox.getSelectionModel().getSelectedItem();
            if (selected != null) {
                loadWagonsAt(selected);
            } else {
                wagonList.getItems().clear();
            }
        });

        // Botão Criar
        Button btnCreate = new Button("Create Freight");
        btnCreate.getStyleClass().add("login-btn");
        btnCreate.setMaxWidth(Double.MAX_VALUE);

        btnCreate.setOnAction(e -> {
            Facility s = originBox.getSelectionModel().getSelectedItem();
            Facility d = destBox.getSelectionModel().getSelectedItem();
            List<WagonWithStatus> sel = wagonList.getSelectionModel().getSelectedItems();

            if (s == null || d == null || sel.isEmpty()) {
                showAlert("Missing Info", "Please select origin, destination and wagons.");
                return;
            }

            if (s.equals(d)) {
                showAlert("Invalid Route", "Origin and Destination cannot be the same.");
                return;
            }

            // Converter DTOs para objetos Core
            List<Wagon> cores = sel.stream().map(WagonWithStatus::getWagon).collect(Collectors.toList());

            // Chamar Controller

            controller.createFreight(s, d, cores);


            // Atualizar UI
            loadWagonsAt(s); // Atualiza lista para mostrar wagons como unavailable (in local freight)
            wagonList.getSelectionModel().clearSelection(); // Clear selection
            refreshFreights(); // Atualiza a coluna 2

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Freight Created");
            success.setHeaderText(null);
            success.setContentText("Freight created successfully! It is now available in the Schedule list.");
            success.show();
        });

        box.getChildren().addAll(lbl, new Label("From (Grey = No Wagons):"), originBox, new Label("To:"), destBox, new Label("Available Wagons:"), wagonList, btnCreate);
        return box;
    }

    // ==========================================
    // COLUNA 2: AGENDAR COMBOIO
    // ==========================================
    private VBox createTrainScheduler() {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");

        Label lbl = new Label("2. Schedule Train");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #3E2723;");

        // Seletor de Rota
        routeCombo = new ComboBox<>();
        routeCombo.setMaxWidth(Double.MAX_VALUE);
        routeCombo.setPromptText("Select Route...");
        refreshRoutes();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Conversor para mostrar "Rota X: A -> B -> C"
        routeCombo.setConverter(new StringConverter<Route>() {
            @Override public String toString(Route r) {
                if (r == null) return "";

                // Formatação da data
                String dateStr = (r.getDepartureDay() != null)
                        ? " [" + r.getDepartureDay().format(formatter) + "]"
                        : "";

                if (r.getPath() == null) return "Route " + r.getRouteId() + dateStr;

                // Construção do caminho A -> B -> C
                String pathStr = r.getPath().getRailFacilities().stream()
                        .map(Facility::getName)
                        .collect(Collectors.joining(" ➝ "));

                return "Route " + r.getRouteId() + dateStr + ": " + pathStr;
            }

            @Override
            public Route fromString(String s) {
                return null;
            }
        });

        // Carregar rotas (protegido contra erros) - refresh routes from DB
        refreshRoutes();

        // Listas de Seleção
        freightList = new ListView<>();
        freightList.setPrefHeight(200);
        freightList.setPlaceholder(new Label("Select Route first."));
        freightList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        locoList = new ListView<>();
        locoList.setPrefHeight(300);
        locoList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        locoList.setPlaceholder(new Label("Select Route first."));

        // Listener da Rota (Carrega as listas dependentes)
        routeCombo.setOnAction(e -> {
            Route r = routeCombo.getValue();
            if(r != null) {
                refreshFreights();
                Facility startNode = r.getPath().getStartFacility();
                if (startNode != null) {
                    try {
                        Connection con = DatabaseConnection.getInstance();
                        List<LocomotiveWithStatus> allLocos = controller.getLocomotivesAt(startNode,r.getDepartureDay());

                        // Mapas para pré-carregamento (Performance & Estabilidade)
                        Map<Integer, String> locoScheduleStatus = new HashMap<>();
                        Map<Integer, Facility> locoFacilities = new HashMap<>();
                        Map<Integer, Boolean> hasConnectionToStart = new HashMap<>();
                        Map<Integer, Double> locoDistances = new HashMap<>();
                        Map<Integer, Facility> locoDestination = new HashMap<>();

                        for (LocomotiveWithStatus lws : allLocos) {
                            String numberStr = lws.getLocomotive().getNumber();
                            if (numberStr == null) continue;
                            int locoNum = Integer.parseInt(numberStr);

                            Facility currentLoc = controller.getFacilityByLocomotive(con, locoNum);
                            locoScheduleStatus.put(locoNum, controller.getLocomotiveScheduleStatus(numberStr));
                            locoFacilities.put(locoNum, currentLoc);

                            for (Train train : controller.getValidScheduledTrains(r.getDepartureDay())) {
                                for (Locomotive locomotive : train.getLocomotives()){
                                    if (locomotive.getNumber().equals(numberStr)){
                                        if (train.getDepartureTime().isBefore(r.getDepartureDay())){
                                            locoDestination.put(locoNum,train.getRoute().getPath().getEndFacility());
                                        }
                                    }
                                }
                            }
                            if (currentLoc != null) {
                                boolean exists = controller.existsPath(currentLoc, startNode);
                                hasConnectionToStart.put(locoNum, exists);

                                if (exists) {
                                    double dist = controller.shortestPath(GraphRepository.getInstance().getGraph(), routeController.getVertexByName(currentLoc.getName()), routeController.getVertexByName(startNode.getName()));
                                    locoDistances.put(locoNum, dist);
                                } else {
                                    locoDistances.put(locoNum, Double.MAX_VALUE);
                                }
                            } else {
                                hasConnectionToStart.put(locoNum, false);
                                locoDistances.put(locoNum, Double.MAX_VALUE);
                            }
                        }

                        allLocos.sort((l1, l2) -> {
                            int num1 = Integer.parseInt(l1.getLocomotive().getNumber());
                            int num2 = Integer.parseInt(l2.getLocomotive().getNumber());

                            // Critério 1: Disponibilidade física e de rede
                            boolean available1 = !l1.isInTransit() && hasConnectionToStart.get(num1)
                                    && !"IN TRANSIT".equals(locoScheduleStatus.get(num1));
                            boolean available2 = !l2.isInTransit() && hasConnectionToStart.get(num2)
                                    && !"IN TRANSIT".equals(locoScheduleStatus.get(num2));

                            if (available1 && !available2) return -1;
                            if (!available1 && available2) return 1;


                            return Double.compare(locoDistances.get(num1), locoDistances.get(num2));
                        });

                        LocalDateTime now = LocalDateTime.now();

                        // 2. Configurar a CellFactory UMA VEZ usando os dados pré-carregados
                        locoList.setCellFactory(lv -> new ListCell<LocomotiveWithStatus>() {
                            @Override protected void updateItem(LocomotiveWithStatus item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(null); setStyle(""); setDisable(false);
                                } else {
                                    int num = Integer.parseInt(item.getLocomotive().getNumber());
                                    String transitStatus = item.getTransitStatus();
                                    Facility currentLoc = locoFacilities.get(num);
                                    boolean canReach = hasConnectionToStart.getOrDefault(num, false);

                                    String statusText;
                                    String emoji = "○";
                                    String color = "#388E3C";
                                    boolean disable = false;

                                    if ("IN_TRANSIT".equals(transitStatus) || item.isInTransit()) {
                                        Facility dest = locoDestination.get(num);

                                        if (dest != null) {
                                            statusText = "Running → " + dest.getName();
                                        } else {
                                            statusText = "Running (In Transit)";
                                        }

                                        emoji = "▶";
                                        color = "#FF9800";
                                        disable = true;
                                    }
                                    else if (!canReach) {
                                        statusText = "No Rail Connection";
                                        emoji = "✖"; color = "#D32F2F"; disable = true;
                                    }
                                    else {
                                        boolean isAtStart = currentLoc != null && currentLoc.equals(startNode);
                                        LocalDateTime departureDay = r.getDepartureDay();

                                        LocalDateTime arrivalLocomotiveTime = controller.calculateLocomotiveTrip(now,item.getLocomotive(), locoDistances.get(num));
                                        if (!isAtStart) {

                                            if (arrivalLocomotiveTime != null && arrivalLocomotiveTime.isBefore(departureDay)) {

                                                statusText = "Available at " + (currentLoc != null ? currentLoc.getName() : "Other Station") +
                                                        " (Arrival: " + arrivalLocomotiveTime.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) + ")";
                                                color = "#1976D2";
                                            } else {
                                                statusText = "Too late! Arrives at " +
                                                        (arrivalLocomotiveTime != null ? arrivalLocomotiveTime.format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A");
                                                color = "#D32F2F";
                                            }
                                        } else {
                                            statusText = "Available Here";
                                            color = "#388E3C";
                                        }
                                    }

                                    setText("Loco " + num + " " + emoji + " " + statusText);
                                    setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                                    setDisable(disable);
                                }
                            }
                        });

                        // 3. Atualizar a lista com os itens (isto dispara a CellFactory)
                        locoList.getItems().setAll(allLocos);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        // Botão Agendar
        Button btnSched = new Button("Confirm Schedule");
        btnSched.getStyleClass().add("login-btn");
        btnSched.setMaxWidth(Double.MAX_VALUE);

        btnSched.setOnAction(e -> {
            Route r = routeCombo.getValue();
            List<LocomotiveWithStatus> ls = locoList.getSelectionModel().getSelectedItems();
            List<Freight> fs = freightList.getSelectionModel().getSelectedItems();

            if(r != null && !ls.isEmpty()) {
                try {
                    controller.createSchedule(r, ls, fs);

                    // Refresh Geral da UI
                    if(originBox.getValue() != null) loadWagonsAt(originBox.getValue());
                    wagonList.getSelectionModel().clearSelection();
                    routeCombo.getSelectionModel().clearSelection();
                    freightList.getItems().clear();
                    freightList.getSelectionModel().clearSelection();
                    locoList.getItems().clear();
                    locoList.getSelectionModel().clearSelection();
                    refreshNetwork();

                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setContentText("Train Scheduled Successfully!");
                    success.show();

                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            } else {
                showAlert("Incomplete", "Select Route and Locomotive.");
            }
        });

        box.getChildren().addAll(lbl, new Label("Route:"), routeCombo, new Label("Load Freights:"), freightList, new Label("Assign Power:"), locoList, btnSched);
        return box;
    }

    // ==========================================
    // COLUNA 3: MONITORIZAÇÃO DA REDE
    // ==========================================
    private VBox createNetworkMonitor() {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");

        Label lbl = new Label("3. Live Network");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #3E2723;");

        trainListView = new ListView<>();
        trainListView.setPrefHeight(200);

        // Set cell factory for train list with color coding
        trainListView.setCellFactory(lv -> new ListCell<Train>() {
            @Override protected void updateItem(Train item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    setDisable(false);
                } else {
                    setText("Train #" + item.getTrainId());

                    // Determine train status based on current time
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime departure = item.getDepartureTime();
                    LocalDateTime arrival = null;

                    if (item.getArrival() != null && item.getArrival().getArrivalTime() != null) {
                        arrival = item.getArrival().getArrivalTime();
                    } else if (departure != null && item.getRoute() != null) {
                        try {
                            arrival = item.calculateArrivalTime(departure, false);
                        } catch (Exception e) {
                            arrival = departure != null ? departure.plusHours(2) : null;
                        }
                    }

                    // Color coding: Available (Green), Running (Yellow), Finished (Red)
                    // Available = departure time hasn't arrived yet (departure > now)
                    // Running = train has departed but hasn't arrived (departure <= now < arrival)
                    // Finished = train has arrived (arrival <= now)
                    String statusText;
                    String emoji;
                    String color;

                    if (departure != null && arrival != null) {
                        if (now.isAfter(arrival) || now.isEqual(arrival)) {
                            // Finished - Red
                            statusText = "Finished";
                            emoji = "✓";
                            color = "#D32F2F";
                        } else if (now.isAfter(departure) || now.isEqual(departure)) {
                            // Running - Yellow (departure time has passed or is now, but hasn't arrived)
                            statusText = "Running";
                            emoji = "▶";
                            color = "#FF9800";
                        } else {
                            // Available - Green (departure time is in the future)
                            statusText = "Available";
                            emoji = "○";
                            color = "#388E3C";
                        }
                    } else {
                        // Unknown status - default
                        statusText = "Unknown";
                        emoji = "?";
                        color = "#3E2723";
                    }

                    setText("Train #" + item.getTrainId() + " " + emoji + " " + statusText);
                    setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                }
            }
        });

        detailsPane = new VBox(5);
        detailsPane.setStyle("-fx-background-color: #ECEFF1; -fx-padding: 10; -fx-background-radius: 5;");
        detailsPane.getChildren().add(new Label("Select a train for details..."));

        // Wrap detailsPane in a ScrollPane for scrollable content
        ScrollPane scrollPane = new ScrollPane(detailsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setPrefHeight(400);
        scrollPane.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        trainListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if(newV != null) showTrainDetails(newV);
        });

        refreshNetwork();

        box.getChildren().addAll(lbl, trainListView, new Separator(), new Label("Details:"), scrollPane);
        return box;
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    private void addResetButton(BorderPane root) {
        Button btnReset = new Button("⚠ RESET DB");
        btnReset.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10px;");

        btnReset.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Reset Database");
            alert.setHeaderText("Delete All Logistics Data?");
            alert.setContentText("This will delete Freights, Trains, and Routes. Stations/Wagons remain.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    if (detailsPane != null) {
                        detailsPane.getChildren().clear();
                        detailsPane.getChildren().add(new Label("Select a train for details..."));
                    }
                    // Chama o serviço de Reset (classe auxiliar)
                    ResetDatabase.cleanLogisticsData();
                    ResetDatabase.cleanRepositories();


                    // Refresh routes from database
                    controller.forceRefreshData();

                    // Refresh all UI components immediately
                    refreshRoutes();
                    refreshNetwork();

                    // Limpa UI
                    routeCombo.getSelectionModel().clearSelection();
                    freightList.getItems().clear();
                    locoList.getItems().clear();
                    wagonList.getItems().clear();

                    // Clear local freights in controller
                    controller.clearLocalFreights();

                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setContentText("Database Cleaned.");
                    info.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox bottomBox = (VBox) root.getBottom();
        if (bottomBox == null) {
            bottomBox = new VBox(10);
            root.setBottom(bottomBox);
        }
        bottomBox.getChildren().add(0, new HBox(10, new Label(""), btnReset));
    }

    private void refreshRoutes() {
        try {
            controller.forceRefreshData();
            routeCombo.getItems().setAll(controller.getAvailableRoutes());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void refreshFreights() {
        Route r = routeCombo.getValue();
        if (r != null) {
            Map<Freight,Boolean> avMap = controller.getAvailableFreights(r);

            freightList.getItems().setAll(avMap.keySet());

            freightList.setCellFactory(lv -> new ListCell<Freight>() {
                @Override
                protected void updateItem(Freight item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                        setDisable(false);
                    } else {
                        boolean isAvailable = avMap.getOrDefault(item, false);
                        String emoji;
                        String color;
                        String statusText;

                        if(isAvailable){
                            emoji = "○";
                            color = "#388E3C";
                            statusText = "Available";
                            String route = item.getStartFacility().getName() + " → " + item.getEndFacility().getName();
                            setText("Freight " + item.getId() + " " + emoji + " " + statusText + " (" + route + ")");
                        } else {
                            emoji = "▶";
                            color = "#D32F2F";
                            statusText = "1 or More Wagons In Transit → " + item.getEndFacility().getName();
                            setText("Freight " + item.getId() + " " + emoji + " " + statusText);
                        }

                        setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                        setDisable(!isAvailable);
                    }
                }
            });
        }
    }


    private void refreshNetwork() {
        try {
            trainListView.getItems().setAll(controller.getScheduledTrains());
        } catch(Exception e){}
    }

    private void loadWagonsAt(Facility f) {
        List<WagonWithStatus> wagons = controller.getWagonsAt(f);

        try {
            Connection con = DatabaseConnection.getInstance();

            // PRÉ-CARREGAR todos os dados ANTES de configurar a célula
            Map<String, Boolean> localFreightStatus = new HashMap<>();
            Map<String, Boolean> scheduledFreightStatus = new HashMap<>();
            Map<String, Facility> wagonFacilities = new HashMap<>();
            Map<String, Double> wagonDistances = new HashMap<>();

            for (WagonWithStatus wagon : wagons) {
                String wagonId = wagon.getWagon().getWagonId();
                boolean isInLocalFreight = controller.isWagonInLocalFreight(wagon.getWagon());
                localFreightStatus.put(wagonId, isInLocalFreight);
                scheduledFreightStatus.put(wagonId, controller.isWagonInScheduledFreight(wagon.getWagon()));

                Facility parkedFacility = controller.getFacilityByWagon(con, Integer.parseInt(wagonId));
                wagonFacilities.put(wagonId, parkedFacility);


                // Calcular e guardar a distância
                if (parkedFacility != null) {
                    double distance = CalculateDistanceBetweenFacilities.haversineDistance(
                            f.getLocation().getLatitude(),
                            f.getLocation().getLongitude(),
                            parkedFacility.getLocation().getLatitude(),
                            parkedFacility.getLocation().getLongitude()
                    );
                    wagonDistances.put(wagonId, distance);
                } else {
                    wagonDistances.put(wagonId, Double.MAX_VALUE);
                }
            }

            // ORDENAR a lista
            wagons.sort((w1, w2) -> {
                String id1 = w1.getWagon().getWagonId();
                String id2 = w2.getWagon().getWagonId();

                boolean available1 = !localFreightStatus.get(id1) &&
                        !scheduledFreightStatus.get(id1) &&
                        !w1.isInTransit() &&
                        wagonDistances.get(id1) == 0;

                boolean available2 = !localFreightStatus.get(id2) &&
                        !scheduledFreightStatus.get(id2) &&
                        !w2.isInTransit() &&
                        wagonDistances.get(id2) == 0;

                if (available1 && !available2) return -1;
                if (!available1 && available2) return 1;

                return Double.compare(wagonDistances.get(id1), wagonDistances.get(id2));
            });

            wagonList.getItems().setAll(wagons);

            final Map<String, Boolean> finalLocalFreight = localFreightStatus;
            final Map<String, Boolean> finalScheduledFreight = scheduledFreightStatus;
            final Map<String, Double> finalDistances = wagonDistances;

            wagonList.setCellFactory(lv -> new ListCell<WagonWithStatus>() {
                @Override
                protected void updateItem(WagonWithStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                        setDisable(false);
                    } else {
                        String wagonId = item.getWagon().getWagonId();
                        String status = item.getTransitStatus();

                        // Usar os Maps finais
                        boolean isInLocalFreight = finalLocalFreight.getOrDefault(wagonId, false);
                        boolean isInScheduledFreight = finalScheduledFreight.getOrDefault(wagonId, false);

                        double distance = finalDistances.getOrDefault(wagonId, Double.MAX_VALUE);

                        String statusText;
                        String emoji;
                        String color;
                        boolean disable;

                        if ("IN_TRANSIT".equals(status) || item.isInTransit()) {
                            statusText = "Running";
                            emoji = "▶";
                            color = "#FF9800";
                            disable = true;
                        } else if (isInLocalFreight) {
                            statusText = "Assigned to other Freight";
                            emoji = "○";
                            color = "#FF9800";
                            disable = false;
                        } else if (distance == 0) {
                            statusText = "Available";
                            emoji = "○";
                            color = "#388E3C";
                            disable = false;
                        } else {
                            statusText = "Parked in another Facility";
                            emoji = "✖";
                            color = "#D32F2F";
                            disable = true;
                        }

                        setText("Wagon " + wagonId + " " + emoji + " " + statusText);
                        setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                        setDisable(disable);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load wagon data");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Configura uma ComboBox para ser pesquisável e aplicar estilo aos items (cinzento se 0 vagões).
     */
    private void setupSearchableComboBox(ComboBox<Facility> box, List<Facility> allItems, Map<Integer, Integer> stockMap, boolean checkStock) {
        box.setEditable(true);
        ObservableList<Facility> masterList = FXCollections.observableArrayList(allItems);
        box.setItems(masterList);

        // Conversor String <-> Objeto
        box.setConverter(new StringConverter<Facility>() {
            @Override public String toString(Facility f) { return (f == null) ? "" : f.getName(); }
            @Override public Facility fromString(String s) {
                return box.getItems().stream().filter(f -> f.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
            }
        });

        // Estilização das Células
        box.setCellFactory(lv -> new ListCell<Facility>() {
            @Override protected void updateItem(Facility item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    int count = (stockMap != null && stockMap.containsKey(item.getId())) ? stockMap.get(item.getId()) : 0;
                    if (checkStock) {
                        setText(item.getName() + " (" + count + ")");
                        if (count > 0) setStyle("-fx-text-fill: #3E2723; -fx-font-weight: bold;");
                        else setStyle("-fx-text-fill: #B0BEC5;"); // Cinzento
                    } else {
                        setText(item.getName());
                        setStyle("-fx-text-fill: #3E2723;");
                    }
                }
            }
        });

        // Filtro de Pesquisa (Listener de Texto)
        box.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            // Ignora se o texto for igual à seleção atual (evita loops)
            if (box.getSelectionModel().getSelectedItem() != null &&
                    box.getConverter().toString(box.getSelectionModel().getSelectedItem()).equals(newText)) {
                return;
            }

            Platform.runLater(() -> {
                if (newText == null || newText.isEmpty()) {
                    box.setItems(masterList);
                } else {
                    List<Facility> filtered = allItems.stream()
                            .filter(f -> f.getName().toLowerCase().contains(newText.toLowerCase()))
                            .collect(Collectors.toList());
                    box.setItems(FXCollections.observableArrayList(filtered));
                }
                // Mostra a lista se tiver foco
                if (!box.isShowing() && box.isFocused()) {
                    box.show();
                }
            });
        });
    }

    private void showTrainDetails(Train t) {
        detailsPane.getChildren().clear();
        detailsPane.setSpacing(10);
        detailsPane.setPadding(new Insets(10)); // Add breathing room

        // --- 1. HEADER SECTION ---
        Label title = new Label("Train #" + t.getTrainId());
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#3E2723"));

        // Logic for times (Preserved from your original code)
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");

        String depString = "N/A";
        LocalDateTime departure = t.getDepartureTime();
        if (departure != null) {
            depString = departure.format(dateFmt);
        }

        String arrString = "N/A";
        LocalDateTime arrival = null;
        if (t.getArrival() != null && t.getArrival().getArrivalTime() != null) {
            arrival = t.getArrival().getArrivalTime();
            arrString = arrival.format(dateFmt);
//        } else if (departure != null) {
//            try {
//                // Try to calculate, fallback to estimate
//                if (t.getRoute() != null) {
//                    arrival = t.calculateArrivalTime(departure, true);
//                    arrString = arrival.format(dateFmt);
//                } else {
//                    throw new Exception("No route");
//                }
//            } catch (Exception e) {
//                arrival = departure.plusHours(2);
//                arrString = arrival.format(dateFmt) + " (est)";
//            }
        }

        // Use a Grid for nice alignment of times
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(15);
        infoGrid.setVgap(5);
        infoGrid.addRow(0, new Label("Departure:"), new Label(depString));
        infoGrid.addRow(1, new Label("Arrival:"), new Label(arrString));

        // Style the labels in the grid slightly
        infoGrid.getChildren().forEach(n -> {
            if(n instanceof Label) ((Label)n).setStyle("-fx-font-size: 13px;");
        });

        detailsPane.getChildren().addAll(title, infoGrid, new Separator());

        // --- 2. LOCOMOTIVES ---
        if (t.getLocomotives() != null && !t.getLocomotives().isEmpty()) {
            Label locoHeader = new Label("Locomotives");
            locoHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");

            FlowPane locoPane = new FlowPane(10, 10); // Horizontal flow
            for (Locomotive loco : t.getLocomotives()) {
                Label tag = new Label(loco.getNumber());
                tag.setStyle("-fx-background-color: #eee; -fx-padding: 3 8; -fx-background-radius: 4; -fx-border-color: #ccc; -fx-border-radius: 4;");
                locoPane.getChildren().add(tag);
            }
            detailsPane.getChildren().addAll(locoHeader, locoPane, new Separator());
        }

        // --- 3. FREIGHTS ---
        List<Freight> freights = t.getRoute().getFreights();
        if (freights != null && !freights.isEmpty()) {
            Label freightHeader = new Label("Freight Details");
            freightHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
            detailsPane.getChildren().add(freightHeader);

            for (Freight freight : freights) {
                VBox freightCard = new VBox(5);
                freightCard.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 8; -fx-background-color: #fafafa;");

                String routeStr = freight.getStartFacility().getName() + " → " + freight.getEndFacility().getName();
                Label fTitle = new Label("Freight " + freight.getId() + " (" + routeStr + ")");
                fTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");

                freightCard.getChildren().add(fTitle);

                if (freight.getWagons() != null && !freight.getWagons().isEmpty()) {
                    String wagonsStr = "Wagons: " + freight.getWagons().stream()
                            .map(w -> String.valueOf(w.getWagonId()))
                            .reduce((a, b) -> a + ", " + b).orElse("");
                    Label wLabel = new Label(wagonsStr);
                    wLabel.setWrapText(true);
                    wLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
                    freightCard.getChildren().add(wLabel);
                }
                detailsPane.getChildren().add(freightCard);
            }
            detailsPane.getChildren().add(new Separator());
        }

        // --- 4. ROUTE TABLE (THE CHART) ---
        Label routeHeader = new Label("Route Schedule");
        routeHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");

        // Resolve Route
        Route fullRoute = null;
        if (t.getRoute() != null) {
            // Keeping your repository logic exactly as is
            fullRoute = RouteRepository.getInstance().getAllRoutes().stream()
                    .filter(r -> r.getRouteId() == t.getRoute().getRouteId())
                    .findFirst()
                    .orElse(t.getRoute());
        }

        if(fullRoute != null && fullRoute.getPath() != null) {
            TableView<RouteStop> table = new TableView<>();
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            table.setPrefHeight(250); // Fixed height, scrollable

            TableColumn<RouteStop, String> stationCol = new TableColumn<>("Station");
            stationCol.setCellValueFactory(new PropertyValueFactory<>("station"));

            TableColumn<RouteStop, String> arrCol = new TableColumn<>("Arrival");
            arrCol.setCellValueFactory(new PropertyValueFactory<>("arrival"));
            arrCol.setStyle("-fx-alignment: CENTER;");

            TableColumn<RouteStop, String> depCol = new TableColumn<>("Departure");
            depCol.setCellValueFactory(new PropertyValueFactory<>("departure"));
            depCol.setStyle("-fx-alignment: CENTER;");

            table.getColumns().addAll(stationCol, arrCol, depCol);

            DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("dd/MM HH:mm:ss");

            for(Facility f : fullRoute.getPath().getRailFacilities()) {
                var key = f.findKey(t);
                // Safety check: ensure key is not null before formatting
                String sTime = (key != null) ? key.getStartTime().format(timeFmt) : "--";
                String eTime = (key != null) ? key.getEndTime().format(timeFmt) : "--";

                table.getItems().add(new RouteStop(f.getName(), sTime, eTime));
            }

            detailsPane.getChildren().addAll(routeHeader, table);
        } else {
            String rId = (t.getRoute() != null ? String.valueOf(t.getRoute().getRouteId()) : "Unknown");
            Label noInfo = new Label("No route path data for ID: " + rId);
            noInfo.setStyle("-fx-text-fill: red;");
            detailsPane.getChildren().addAll(routeHeader, noInfo);
        }
    }

    public static class RouteStop {
        private final String station;
        private final String arrival;
        private final String departure;

        public RouteStop(String station, String arrival, String departure) {
            this.station = station;
            this.arrival = arrival;
            this.departure = departure;
        }

        public String getStation() { return station; }
        public String getArrival() { return arrival; }
        public String getDeparture() { return departure; }
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setContentText(content);
        a.show();
    }
}
