package org.dei.Sprint3.Schedule;

import org.dei.Repository.FreightRepository;
import org.dei.Repository.TrainRepository;
import org.dei.Sprint3.DTOs.LocomotiveWithStatus;
import org.dei.Sprint3.DTOs.WagonWithStatus;
import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
import org.dei.Sprint3.Graph.Algorithms;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeightWithLine;
import org.dei.Sprint3.Graph.map.MapGraph;
import org.dei.Sprint3.Services.DataBaseAccessService;
import org.dei.Sprint3.Services.Inserts.*;
import org.dei.Repository.GraphRepository;
import org.dei.Repository.FacilityRepository;
import org.dei.Repository.RouteRepository;
import org.dei.Sprint3.Graph.RailNetworkGraphs.FacilityVertex;
import org.dei._Facilities.Facility;
import org.dei._Path.Route;
import org.dei._RailLineNetwork.RailLine;
import org.dei._Train.Freight;
import org.dei._Train.Locomotive;
import org.dei._Train.Train;
import org.dei._Train.Wagon;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class CreateScheduleController {

    private final DataBaseAccessService dbService;
    private final List<Freight> localFreights;

    public CreateScheduleController() {
        this.dbService = new DataBaseAccessService();
        this.localFreights = new ArrayList<>(FreightRepository.getInstance().getAllFreights());

        if (RouteRepository.getInstance().getAllRoutes().isEmpty()) {
            RouteRepository.getInstance().loadRoutesFromDB();
        }
    }

    public void forceRefreshData() {
        RouteRepository.getInstance().loadRoutesFromDB();
    }
    
    public void clearLocalFreights() {
        localFreights.clear();
    }
    
    public boolean isWagonInLocalFreight(Wagon wagon) {
        if (wagon == null) return false;
        return localFreights.stream()
                .anyMatch(f -> f.getWagons() != null && 
                        f.getWagons().stream()
                                .anyMatch(w -> w != null && w.getWagonId().equals(wagon.getWagonId())));
    }
    
    /**
     * Checks if a wagon is in a freight that has been scheduled on a train
     * @param wagon The wagon to check
     * @return true if the wagon is in a scheduled freight, false otherwise
     */
    public boolean isWagonInScheduledFreight(Wagon wagon) {
        if (wagon == null) return false;
        try {
            List<Train> trains = dbService.getAllTrains(DatabaseConnection.getInstance());
            for (Train train : trains) {
                if (train.getFreightsOnTrain() != null) {
                    for (Freight freight : train.getFreightsOnTrain()) {
                        if (freight.getWagons() != null) {
                            for (Wagon w : freight.getWagons()) {
                                if (w != null && w.getWagonId().equals(wagon.getWagonId())) {
                                    // Found wagon in a scheduled freight
                                    return true;
                                }
                            }
                        }
                    }
                }
                // Also check route freights if they exist
                if (train.getRoute() != null && train.getRoute().getFreights() != null) {
                    for (Freight freight : train.getRoute().getFreights()) {
                        if (freight.getWagons() != null) {
                            for (Wagon w : freight.getWagons()) {
                                if (w != null && w.getWagonId().equals(wagon.getWagonId())) {
                                    // Found wagon in a scheduled freight
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Checks if a locomotive is scheduled in a train and if that train has already started
     * @param locomotiveId The locomotive ID to check
     * @return "RUNNING" if train has started, "PLANNED" if scheduled but not started, null if not scheduled
     */
    public String getLocomotiveScheduleStatus(String locomotiveId) {
        if (locomotiveId == null) return null;
        try {
            List<Train> trains = dbService.getAllTrains(DatabaseConnection.getInstance());
            LocalDateTime now = LocalDateTime.now();
            
            for (Train train : trains) {
                if (train.getLocomotives() != null &&
                        train.getLocomotives().stream()
                                .anyMatch(l -> l != null && l.getNumber().equals(locomotiveId))) {
                    // Locomotive is in this train
                    LocalDateTime departure = train.getDepartureTime();
                    if (departure != null && (now.isAfter(departure) || now.isEqual(departure))) {
                        return "RUNNING"; // Train has started
                    } else {
                        return "PLANNED"; // Scheduled but not started yet
                    }
                }
            }
            return null; // Not scheduled
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean isLocomotiveInScheduledTrain(String locomotiveId) {
        String status = getLocomotiveScheduleStatus(locomotiveId);
        return status != null; // Returns true if scheduled (either PLANNED or RUNNING)
    }

    // --- MÉTODOS DE UI E DADOS ---

    public List<Facility> getNetworkStations() {
        List<Facility> stations = new ArrayList<>();
        if (GraphRepository.getInstance().getGraph() != null) {
            for (FacilityVertex v : GraphRepository.getInstance().getGraph().vertices()) {
                stations.add(v.getStation());
            }
        } else {
            stations.addAll(FacilityRepository.getInstance().getFacilities());
        }
        stations.sort((f1, f2) -> f1.getName().compareTo(f2.getName()));
        return stations;
    }

    public List<Facility> getAllFacilities() {
        return getNetworkStations();
    }

    public Map<Integer, Integer> getWagonCounts() {
        try {
            return dbService.getStationWagonCounts(DatabaseConnection.getInstance());
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    // --- MÉTODOS OPERACIONAIS ---

    public List<Route> getAllRoutes() {
        return RouteRepository.getInstance().getAllRoutes();
    }

    public List<Route> getAvailableRoutes() {
        return RouteRepository.getInstance().getValidRoutes();
    }

    public List<Train> getScheduledTrains() {
        try {
            return TrainRepository.getInstance().getAllTrains().stream().toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Train> getValidScheduledTrains(LocalDateTime departureTime) {
        try {
            return TrainRepository.getInstance().getAllValidTrains(departureTime).stream().toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Facility getFacilityByWagon(Connection con, int wagonId){
        if(wagonId <= 0) return null;
        try {
            int facilityId = dbService.getFacilityByWagon(con,wagonId);
            if (facilityId <= 0) return null;
            return FacilityRepository.getInstance().getFacility(facilityId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Facility getFacilityByLocomotive(Connection con, int locomotiveId){
        if(locomotiveId <= 0) return null;
        try {
            int facilityId = dbService.getFacilityByLocomotive(con,locomotiveId);
            if (facilityId <= 0) return null;
            return FacilityRepository.getInstance().getFacility(facilityId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<WagonWithStatus> getWagonsAt(Facility facility) {
        if(facility == null) return new ArrayList<>();
        try {
            // Use facilityId to filter wagons at the specific facility - PL/SQL should handle this
            List<WagonWithStatus> wagons = dbService.getWagonsAtFacility(DatabaseConnection.getInstance(), facility.getId());
            // Additional filter: ensure wagons are not in transit and are at this specific facility
            return wagons;
//                    .filter(w -> !w.isInTransit()) // Exclude wagons in transit
//                    .filter(w -> w.getLocationInfo() != null &&
//                            w.getLocationInfo().equalsIgnoreCase(facility.getName())) // Exact match on location name

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<LocomotiveWithStatus> getLocomotivesAt(Facility facility, LocalDateTime startTime) {
        if(facility == null) return new ArrayList<>();
        try {
            // Use facilityId to filter locomotives at the specific facility
            List<LocomotiveWithStatus> locos = dbService.getLocomotivesAtFacility(DatabaseConnection.getInstance(), facility.getId(),startTime);
            // Filter out locomotives that are in transit AND ensure location matches the facility
            return locos;
//                    .filter(l -> !l.isInTransit()) // Exclude locomotives in transit
//                    .filter(l -> l.getLocationInfo() != null &&
//                            l.getLocationInfo().equalsIgnoreCase(facility.getName())) // Exact match on location name
//                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void createFreight(Facility start, Facility end, List<Wagon> wagons)  {
        String id = String.valueOf(System.currentTimeMillis() % 1000000000);
        Freight f = new Freight(id, start, end, wagons);
        localFreights.add(f);

        System.out.println("[LOG] Freight " + id + " criado: " + start.getName() + " → " + end.getName() + 
                         " com " + wagons.size() + " wagon(s)");
        for (Wagon w : wagons) {
            System.out.println("[LOG]   - Wagon " + w.getWagonId() + " adicionado ao Freight " + id);
        }

        try {
            InsertNewFreight.run(DatabaseConnection.getInstance(), f);
            for(Wagon w : wagons) {
                InsertWagonInFreight.run(DatabaseConnection.getInstance(), f, w);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createSchedule(Route route, List<LocomotiveWithStatus> locos, List<Freight> freights) {
        int trainIdInt = 0;
        try {
            Connection con = DatabaseConnection.getInstance();
            LocalDateTime departureTime = route.getDepartureDay();
            String trainId = String.valueOf(System.currentTimeMillis() % 1000000000);
            trainIdInt = Integer.parseInt(trainId);
            for(Freight f : freights)
                route.addFreight(f);

            Train t = new Train(trainId, route, departureTime);
            dbService.saveTrainSchedule(con,t);

            // Adicionar locomotivas antes de gravar
            for(LocomotiveWithStatus l : locos) {
                t.addLocomotive(l.getLocomotive());
            }

            // Calcular tempo de chegada
            LocalDateTime arrivalTime = t.calculateArrivalTime(t.getDepartureTime(), true);


            // Gravar associações locomotiva-train
            for(LocomotiveWithStatus l : locos) {
                dbService.addLocomotiveTrain(con,Integer.parseInt(trainId),Integer.parseInt(l.getLocomotive().getNumber()));
            }

            // Atualizar endDate na BD
            if(arrivalTime != null){
                dbService.addTrainEndTime(con, Integer.parseInt(trainId), arrivalTime);
            }

            for (Freight f : freights) {
                dbService.setRouteInFreight(con, route.getRouteId(), Integer.parseInt(f.getId()));
            }

            TrainRepository.getInstance().addTrain(t);
//            localFreights.removeAll(freights);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            try {
                dbService.cleanTrain(DatabaseConnection.getInstance(),trainIdInt);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public Map<Freight, Boolean> getAvailableFreights(Route r) {
        Map<Freight, Boolean> results = new HashMap<>();

        if (r == null ||  r.getPath() == null || r.getDepartureDay() == null) {
            return results;
        }

        List<Facility> routeFacilities = r.getPath().getRailFacilities();
        if (routeFacilities.isEmpty()) return results;
        try (Connection con = DatabaseConnection.getInstance()) {
            for (Freight f : localFreights) {
                int startIndex = -1;
                int endIndex = -1;

                // 2. Lógica Geográfica
                for (int i = 0; i < routeFacilities.size(); i++) {
                    if (routeFacilities.get(i).getId() == f.getStartFacility().getId()) {
                        startIndex = i;
                    }
                    if (routeFacilities.get(i).getId() == f.getEndFacility().getId()) {
                        endIndex = i;
                    }
                }

                // 3. Se o frete cabe na rota, verificamos a disponibilidade temporal
                if (startIndex >= 0 && endIndex >= 0 && startIndex < endIndex) {
                    boolean isAvailable = dbService.isFreightAvailableInDB(con, Integer.parseInt(f.getId()), r.getDepartureDay());
                    System.out.println("[LOG] Freight " + f.getId() + " (" + f.getStartFacility().getName() +
                            " → " + f.getEndFacility().getName() + ") availability: " +
                            (isAvailable ? "AVAILABLE" : "IN TRANSIT / WAGONS BUSY"));
                    results.put(f, isAvailable);
                }
            }
        } catch (SQLException e) {
            System.err.println("[Error] Database validation failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return results;
    }

    public boolean existsPath(Facility start, Facility target) {
        if (start == null || target == null) return false;
        if (start.getId() == target.getId()) return true;

        Queue<Facility> queue = new LinkedList<>();
        Set<Integer> visitedIds = new HashSet<>();

        queue.add(start);
        visitedIds.add(start.getId());

        while (!queue.isEmpty()) {
            Facility current = queue.poll();
            Map<Facility, RailLine> connections = current.getConnections();

            if (connections != null) {
                for (Facility neighbor : connections.keySet()) {
                    int neighborId = neighbor.getId();

                    if (!visitedIds.contains(neighborId)) {
                        if (neighborId == target.getId()) return true;

                        visitedIds.add(neighborId);
                        queue.add(neighbor);
                    }
                }
            }
        }
        return false;
    }


    public Freight getLocalFreightByWagon(Wagon wagon) {
        if (wagon == null) return null;

        for (Freight f : localFreights) {
            for (Wagon w : f.getWagons()) {
                if (w.getWagonId().equals(wagon.getWagonId())){
                    return f;
                }
            }
        }

        return null;
    }

    public double shortestPath(MapGraph<FacilityVertex, TrackWeightWithLine> graph, FacilityVertex vFrom, FacilityVertex vTo) {
        LinkedList<FacilityVertex> facilityVertices = new LinkedList<>();
        List<Facility> facility = new LinkedList<>();
        double dist = 0;

        Algorithms.<FacilityVertex, TrackWeightWithLine> shortestPath(graph, vFrom, vTo, TrackWeightWithLine::compareTo,
                TrackWeightWithLine::apply, new TrackWeightWithLine(0, 0, 0), facilityVertices);

        for (FacilityVertex v : facilityVertices)
            facility.add(v.getStation());

        for (int i = 0; i < facility.size() - 1; i++) {
            Facility f1 = facility.get(i);
            Facility f2 = facility.get(i + 1);
            dist += f1.getConnections().get(f2).getTotalDistance();
        }
        return (dist);
    }

    public LocalDateTime calculateLocomotiveTrip(LocalDateTime now, Locomotive locomotive, double distance) {
        double fullLengthKm = distance + (locomotive.getLength() / 1000.0);
        double speedKmh = locomotive.getModel().getMaxSpeed();
        double travelTimeHours = fullLengthKm / speedKmh;
        long travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);
        return now.plusNanos(travelTimeNanos);
    }
}