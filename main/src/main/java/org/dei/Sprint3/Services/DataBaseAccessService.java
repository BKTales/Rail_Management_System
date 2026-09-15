package org.dei.Sprint3.Services;

import oracle.jdbc.OracleTypes;
import org.dei.Sprint3.DTOs.LocomotiveWithStatus;
import org.dei.Sprint3.DTOs.WagonWithStatus;
import org.dei.Repository.TrainRepository;
import org.dei.Repository.FacilityRepository;
import org.dei.Repository.RouteRepository;
import org.dei._Facilities.Facility;
import org.dei._Path.Route;
import org.dei._Train.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DataBaseAccessService {

    // --- MÉTODOS DE MONITORIZAÇÃO E CONTAGEM ---

    public Map<Integer, Integer> getStationWagonCounts(Connection con) throws SQLException {
        Map<Integer, Integer> counts = new HashMap<>();
        String call = "{ ? = call get_station_wagon_counts() }";

        try (CallableStatement cstmt = con.prepareCall(call)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    int id = rs.getInt("facilityId");
                    int count = rs.getInt("wagon_count");
                    counts.put(id, count);
                }
            }
        } catch (SQLException e) {
            System.err.println("Warning: Could not fetch wagon counts (PL/SQL missing?): " + e.getMessage());
        }
        return counts;
    }

    // --- MÉTODOS DE STATUS ---

    public List<WagonWithStatus> getWagonsWithStatus(Connection con, int startStationId) throws SQLException {
        List<WagonWithStatus> list = new ArrayList<>();
        String call = "{ ? = call get_available_wagonsTransitINF(?) }";

        try (CallableStatement cstmt = con.prepareCall(call)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.setInt(2, startStationId);
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    String wagonId = String.valueOf(rs.getInt("wagonId"));
                    String status = rs.getString("status");
                    String location = rs.getString("location_name");
                    double dist = rs.getDouble("distance_metric");

                    Wagon w = TrainRepository.getInstance().getWagon(wagonId);
                    if (w == null) w = new Wagon(wagonId);

                    list.add(new WagonWithStatus(w, status, location, dist));
                }
            }
        }
        return list;
    }

    public List<LocomotiveWithStatus> getLocomotivesWithStatus(Connection con, int startStationId,LocalDateTime starTime) throws SQLException {
        List<LocomotiveWithStatus> list = new ArrayList<>();
        String call = "{ ? = call get_available_locomotivesTransitINF(?,?) }";

        try (CallableStatement cstmt = con.prepareCall(call)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.setInt(2, startStationId);
            cstmt.setTimestamp(3, Timestamp.valueOf(starTime));
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    String locoId = String.valueOf(rs.getInt("locomotiveId"));
                    String status = rs.getString("status");
                    String location = rs.getString("location_name");
                    double dist = rs.getDouble("distance_metric");

                    double power = rs.getDouble("power");
                    double length = rs.getDouble("length");
                    double width = rs.getDouble("width");
                    double height = rs.getDouble("height");
                    double weight = rs.getDouble("weight");
                    double maxSpeed = rs.getDouble("max_speed");
                    String make = rs.getString("make_name");
                    String modelName = rs.getString("model_name");
                    String gauge = rs.getString("gauge_name");

                    String typeStr = rs.getString("engine_type");
                    LocomotiveType type = LocomotiveType.valueOf(typeStr);

                    LocomotiveModel model = new LocomotiveModel(
                            power, length, width, height, weight, maxSpeed, make, modelName, type, "Iberian gauge"
                    );

                    Locomotive l = TrainRepository.getInstance().getLocomotive(locoId);

                    if (l == null) {
                        l = new Locomotive(locoId);
                        l.setModel(model);
                        TrainRepository.getInstance().addLocomotive(l);
                    } else if (l.getModel() == null) {
                        l.setModel(model);
                    }

                    list.add(new LocomotiveWithStatus(l, status, location, dist));
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao converter Tipo de Locomotiva: " + e.getMessage());
        }
        return list;
    }

    // --- MÉTODOS DE INFRAESTRUTURA ---

    public List<Train> getAllTrains(Connection con) throws SQLException {
        List<Train> trains = new ArrayList<>();
        String call = "{ ? = call getAllTrainsCursor() }";

        try (CallableStatement cstmt = con.prepareCall(call)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    int trainIdInt = rs.getInt("trainId");
                    Train t = new Train(String.valueOf(trainIdInt));

                    // 1. Tempos de Partida
                    Timestamp start = rs.getTimestamp("startDate");
                    if (start != null) t.setDepartureTime(start.toLocalDateTime());

                    // 2. Carregar Rota
                    int routeId = rs.getInt("routeId");
                    Route route = RouteRepository.getInstance().getRouteByIdentifier(routeId);
                    route.setDepartureDay(t.getDepartureTime());
                    if (route != null) {
                        t.setRoute(route);
                    }

                    // 3. Carregar Componentes (Locomotivas e Fretes)
                    loadLocomotivesForTrain(con, t, trainIdInt);
                    loadFreightsForTrain(con, t, trainIdInt);

                    trains.add(t);
                }
            }
        }
        return trains;
    }

    /**
     * Carrega as locomotivas associadas ao comboio (Tabela: Locomotive_Train)
     */
    private void loadLocomotivesForTrain(Connection con, Train train, int trainId) throws SQLException {
        String sql = "SELECT locomotiveId FROM Locomotive_Train WHERE trainId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, trainId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String locoId = String.valueOf(rs.getInt("locomotiveId"));

                    Locomotive loco = TrainRepository.getInstance().getLocomotive(locoId);
                    if (loco != null) {
                        train.addLocomotive(loco);
                    }
                }
            }
        }
    }

    /**
     * Carrega os fretes associados à rota do comboio (Tabela: Freight)
     */
    private void loadFreightsForTrain(Connection con, Train train, int trainId) throws SQLException {

        String sql = "SELECT freightId FROM Freight WHERE routeId = (SELECT routeId FROM Train WHERE trainId = ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, trainId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String freightId = String.valueOf(rs.getInt("freightId"));
                    Freight freight = loadFreightById(con, freightId);

                    if (freight != null) {
                        // Sincronização com o objeto Route
                        if (train.getRoute() != null) {
                            train.getRoute().addFreight(freight);
                        }
                    }
                }
            }
        }
    }

    /**
     * Carrega um Frete específico e os seus vagões (Tabelas: Freight e Wagon_Freight)
     */
    private Freight loadFreightById(Connection con, String freightId) throws SQLException {
        String sql = "SELECT startPoint, endPoint FROM Freight WHERE freightId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(freightId));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Facility start = FacilityRepository.getInstance().getFacility(rs.getInt("startPoint"));
                    Facility end = FacilityRepository.getInstance().getFacility(rs.getInt("endPoint"));

                    Freight freight = new Freight(freightId, start, end);
                    loadWagonsForFreight(con, freight);
                    return freight;
                }
            }
        }
        return null;
    }

    /**
     * Carrega os vagões de um frete (Tabela: Wagon_Freight)
     */
    private void loadWagonsForFreight(Connection con, Freight freight) throws SQLException {
        String sql = "SELECT wagonId FROM Wagon_Freight WHERE freightId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(freight.getId()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String wagonId = String.valueOf(rs.getInt("wagonId"));
                    Wagon wagon = TrainRepository.getInstance().getWagon(wagonId);
                    if (wagon != null) {
                        freight.addWagon(wagon);
                    }
                }
            }
        }
    }


    // --- PERSISTÊNCIA ---

    public void saveTrainSchedule(Connection con, Train train) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call addTrain(?, ?, ?, ?, ?)}")) {
            cs.setInt(1, Integer.parseInt(train.getTrainId()));
            cs.setInt(2, train.getRoute().getRouteId());
            cs.setFloat(3, Float.MAX_VALUE);
            cs.setTimestamp(4, Timestamp.valueOf(train.getDepartureTime()));
            cs.setNull(5, java.sql.Types.TIMESTAMP);
            cs.execute();
        }
    }

    public void addLocomotiveTrain(Connection con, int trainId, int locomotiveId) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call insertLocomotiveTrain(?, ?)}")) {
            cs.setInt(1, trainId);
            cs.setInt(2, locomotiveId);
            cs.execute();
        }
    }

    public void addTrainEndTime(Connection con, int trainId, LocalDateTime endTime) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call updateTrainEndDate(?, ?)}")) {
            cs.setInt(1, trainId);
            cs.setTimestamp(2, Timestamp.valueOf(endTime));
            cs.execute();
        }
    }

    public void addTimeInSegment(Connection con, int trainId, int segmentId, LocalDateTime start, LocalDateTime end) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call addTimeInSegment(?, ?, ?, ?)}")) {
            cs.setInt(1, segmentId);
            cs.setInt(2, trainId);
            cs.setTimestamp(3, Timestamp.valueOf(start));
            cs.setTimestamp(4, Timestamp.valueOf(end));
            cs.execute();
        }
    }

    public void addFacilityTrain(Connection con, int trainId, int lineId, LocalDateTime start, LocalDateTime end) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call addFacilityTrain(?, ?, ?, ?)}")) {
            cs.setInt(1, lineId);
            cs.setInt(2, trainId);
            cs.setTimestamp(3, Timestamp.valueOf(start));
            cs.setTimestamp(4, Timestamp.valueOf(end));
            cs.execute();
        }
    }


    /**
     * Function will erase all the data from the database's table Time_In_Segment and Facility_Train
     * in which the train that tried to be scheduled were allocated at
     * @param con connection to the databse
     * @param trainId id of the train which were tried to schedule
     * @throws SQLException
     */
    public void cleanErrorInTrainScheduling(Connection con, int trainId) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call deleteAllFacilityTrainToGivenId(?)}")) {
            cs.setInt(1, trainId);
            cs.execute();
        }
        try (CallableStatement cs = con.prepareCall("{call deleteAllTimeInSegmentToGivenId(?)}")) {
            cs.setInt(1, trainId);
            cs.execute();
        }

        cleanTrain(con, trainId);
    }

    // --- MÉTODOS AUXILIARES & BOOTSTRAP ---

    public List<WagonWithStatus> getWagonsAtFacility(Connection con, int facilityId) throws SQLException {
        return getWagonsWithStatus(con, facilityId);
    }

    public List<LocomotiveWithStatus> getLocomotivesAtFacility(Connection con, int facilityId,LocalDateTime starTime) throws SQLException {
        return getLocomotivesWithStatus(con, facilityId,starTime);
    }

    public List<Locomotive> getAllLocomotivesFromDatabase(Connection con) throws SQLException {
        List<Locomotive> locomotives = new ArrayList<>();

        String sql = "{ ? = call getAllLocomotives() }";

        try (CallableStatement stmt = con.prepareCall(sql)) {

            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.execute();
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    // Determina o tipo da locomotiva
                    LocomotiveType type;
                    String typeStr = rs.getString("locomotiveType");
                    if ("ELECTRIC".equalsIgnoreCase(typeStr)) {
                        type = LocomotiveType.ELECTRIC;
                    } else {
                        type = LocomotiveType.DIESEL;
                    }
                    String gauge = rs.getString("gaugeName");
                    // Cria LocomotiveModel
                    LocomotiveModel model = new LocomotiveModel(
                            rs.getDouble("power"),
                            rs.getDouble("length"),
                            rs.getDouble("width"),
                            rs.getDouble("height"),
                            rs.getDouble("weight"),
                            rs.getDouble("maxSpeed"),
                           rs.getString("make"),
                            rs.getString("modelName"),
                            type,
                            gauge
                    );
                    // Cria Locomotive
                    Locomotive loco = new Locomotive(rs.getString("locomotiveId"));
                    loco.setServiceYear(rs.getInt("yearOfService"));
                    loco.setNumberBogies(rs.getInt("numberBogies"));
                    loco.setOperator(rs.getString("operatorId"));
                    loco.setModel(model);

                    locomotives.add(loco);
               }
           }
        }

        return locomotives;
    }

    public boolean isFreightAvailableInDB(Connection con, int freightId, LocalDateTime departureDate) throws SQLException {
        String sql = "{ ? = call is_freight_available(?, ?) }";
        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
            cstmt.setInt(2, freightId);
            cstmt.setTimestamp(3, java.sql.Timestamp.valueOf(departureDate));

            cstmt.execute();
            return cstmt.getInt(1) == 1;
        }
    }



    public List<Wagon> getAllWagonsFromDatabase(Connection con) throws SQLException {
        List<Wagon> wagons = new ArrayList<>();

        String sql = "{ ? = call getAllWagons() }";

        try (CallableStatement stmt = con.prepareCall(sql)) {

            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    // Cria WagonModel
                    WagonModel model = new WagonModel(
                            rs.getString("wagonModelId"),
                            rs.getDouble("length"),
                            rs.getDouble("height"),
                            rs.getDouble("width"),
                            rs.getDouble("weight"),
                            rs.getDouble("maxSpeed"),
                            rs.getString("gaugeName"),
                            rs.getInt("boxCapacity")
                    );

                    // Cria Wagon
                    Wagon wagon = new Wagon(rs.getString("wagonId"));
                    wagon.setWagonModel(model);

                    wagons.add(wagon);
                }
            }
        }

        return wagons;
    }


    public int getFacilityByWagon(Connection con, int wagonId) throws SQLException {
        String sql = "{ ? = call getFacilityByWagon(?) }";

        try (CallableStatement stmt = con.prepareCall(sql)) {
            // Parâmetro 1 = OUTPUT (o retorno da função)
            stmt.registerOutParameter(1, OracleTypes.NUMBER);
            stmt.setInt(2, wagonId);  // Índice 2, não 1!
            stmt.execute();
            int facilityId = stmt.getInt(1);


            if (stmt.wasNull()) {
                return -1;
            }

            return facilityId;
        }
    }

    public int getFacilityByLocomotive(Connection con, int locomotiveId) throws SQLException {
        String sql = "{ ? = call getFacilityByLocomotive(?) }";

        try (CallableStatement stmt = con.prepareCall(sql)) {
            // Parâmetro 1 = OUTPUT (o retorno da função)
            stmt.registerOutParameter(1, OracleTypes.NUMBER);
            stmt.setInt(2, locomotiveId);  // Índice 2, não 1!
            stmt.execute();
            int facilityId = stmt.getInt(1);


            if (stmt.wasNull()) {
                return -1;
            }

            return facilityId;
        }
    }

    public List<Freight> getAllFreights(Connection conn) throws SQLException {
        Map<String, Freight> freightMap = new LinkedHashMap<>();
        String sql = "{call getAllFreightDetails(?)}";

        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    String fId = rs.getString("freightId");

                    Freight freight = freightMap.get(fId);
                    if (freight == null) {

                        Facility startFac = new Facility(
                                rs.getInt("startId"),
                                rs.getString("startName"),
                                rs.getFloat("startLat"),
                                rs.getFloat("startLon")
                        );


                        Facility endFac = new Facility(
                                rs.getInt("endId"),
                                rs.getString("endName"),
                                rs.getFloat("endLat"),
                                rs.getFloat("endLon")
                        );


                        freight = new Freight(fId, startFac, endFac);
                        freightMap.put(fId, freight);
                    }

                    // 4. Adicionar Vagão se existir
                    String wId = rs.getString("wagonId");
                    if (wId != null) {
                        WagonModel model = new WagonModel(
                                rs.getString("wagonModelId"),
                                rs.getDouble("length"),
                                rs.getDouble("height"),
                                rs.getDouble("width"),
                                rs.getDouble("weight"),
                                rs.getDouble("maxSpeed"),
                                rs.getString("gaugeName"),
                                rs.getInt("volumeCapacity")
                        );

                        Wagon wagon = new Wagon(wId);
                        wagon.setWagonModel(model);
                        freight.getWagons().add(wagon);
                    }
                }
            }
        }
        return new ArrayList<>(freightMap.values());
    }

    public void associateFreightRoute(Connection con, int routeId, int freightId) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call associateFreightRoute(?)}")) {
            cs.setInt(1, routeId);
            cs.setInt(2, freightId);
            cs.execute();
        }
    }

    public void cleanTrain(Connection con, int trainId) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call cleanTrain(?)}")) {
            cs.setInt(1, trainId);
            cs.execute();
        }
    }

    public void setRouteInFreight(Connection con,int routeId,int freightId) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call updateRouteFreight(?,?)}")) {
            cs.setInt(1, routeId);
            cs.setInt(2, freightId);
            cs.execute();
        }
    }
}