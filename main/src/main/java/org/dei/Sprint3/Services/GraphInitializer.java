package org.dei.Sprint3.Services;

import oracle.jdbc.OracleTypes;
import org.dei._Facilities.Facility;
import org.dei._RailLineNetwork.RailLine;
import org.dei._RailLineNetwork.RailSegment;
import org.dei.Repository.FacilityRepository;
import org.dei.Repository.LinesRepository;
import org.dei.Sprint3.Graph.RailNetworkGraphs.FacilityVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeightWithLine;
import org.dei.Sprint3.Graph.map.MapGraph;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphInitializer {
    public static final int RETURN_INDEX = 1;
    public static final int INPUT_INDEX = 2;

    public static MapGraph<FacilityVertex, TrackWeightWithLine> createGraph(Connection con) throws SQLException {
        MapGraph<FacilityVertex, TrackWeightWithLine> graph = new MapGraph<>(false);

        FacilityRepository facilitiesRepo = FacilityRepository.getInstance();
        LinesRepository railLinesRepo = LinesRepository.getInstance();

        // 1. Carregar Instalações (Evita duplicados)
        List<Facility> dbFacilities = getFacilitiesFromDatabase(con);
        for (Facility f : dbFacilities) {
            boolean exists = false;
            for(Facility existing : facilitiesRepo.getFacilities()) {
                if(existing.getId() == f.getId()) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                facilitiesRepo.getFacilities().add(f);
            }
        }


        // 2. Criar Vértices
        Map<Integer, FacilityVertex> vertexMap = new HashMap<>();
        for (Facility f : facilitiesRepo.getFacilities()) {
            FacilityVertex v = new FacilityVertex(f);
            graph.addVertex(v);
            vertexMap.put(f.getId(), v);
        }
        // 3. Carregar Linhas
        List<RailLine> dbLines = getRailLinesFromDatabase(con, facilitiesRepo.getFacilities());
        railLinesRepo.getAllRailLines().clear();
        railLinesRepo.getAllRailLines().addAll(dbLines);
        // 4. Ligar Arestas
        for (RailLine line : railLinesRepo.getAllRailLines()) {
            Facility startFac = line.getStartFacility();
            Facility endFac = line.getEndFacility();

            if (startFac != null && endFac != null) {
                FacilityVertex vStart = vertexMap.get(startFac.getId());
                FacilityVertex vEnd = vertexMap.get(endFac.getId());

                if (vStart != null && vEnd != null) {
                    TrackWeightWithLine weight = new TrackWeightWithLine(line);
                    graph.addEdge(vStart, vEnd, weight);

                    // Atualiza conexões bidirecionais para a UI funcionar
                    vStart.getStation().getConnections().put(vEnd.getStation(), line);
                    vEnd.getStation().getConnections().put(vStart.getStation(), line);
                }
            }
        }
        return graph;
    }

//    public static void loadRollingStock(Connection con) {
//        try {
//            loadLocomotives(con);
//            loadWagons(con);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void loadLocomotives(Connection con) throws SQLException {
//        // CORREÇÃO: Adicionado '120 AS maxSpeed' porque a coluna não existe na tabela da DB
//        String sql = "SELECT l.locomotiveId, l.operatorId, l.yearOfService, " +
//                "lm.name AS modelName, lm.power, lm.length, lm.width, lm.height, lm.weight, " +
//                "120 AS maxSpeed, " + // <--- FIX ORA-00904
//                "lm.numOfBogies, " +
//                "m.name AS makeName, " +
//                "g.gaugeName, " +
//                "elm.voltage, dlm.fuelCapacity " +
//                "FROM Locomotive l " +
//                "JOIN Locomotive_Model lm ON l.locomotiveModelId = lm.locomotiveModelId " +
//                "JOIN Make m ON lm.makeId = m.makeId " +
//                "JOIN Locomotive_Model_Gauge lmg ON lm.locomotiveModelId = lmg.locomotiveModelId " +
//                "JOIN Gauge g ON lmg.gaugeName = g.gaugeName " +
//                "LEFT JOIN Electric_Locomotive_Model elm ON lm.locomotiveModelId = elm.locomotiveModelId " +
//                "LEFT JOIN Diesel_Locomotive_Model dlm ON lm.locomotiveModelId = dlm.locomotiveModelId";
//
//        try (PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            TrainRepository repo = TrainRepository.getInstance();
//            // repo.clear(); // Opcional: limpar antes de carregar
//
//            while (rs.next()) {
//                String number = String.valueOf(rs.getInt("locomotiveId"));
//                String operator = rs.getString("operatorId");
//                int serviceYear = rs.getInt("yearOfService");
//                int numBogies = rs.getInt("numOfBogies");
//
//                String modelName = rs.getString("modelName");
//                double power = rs.getDouble("power");
//                double length = rs.getDouble("length");
//                double width = rs.getDouble("width");
//                double height = rs.getDouble("height");
//                double weight = rs.getDouble("weight");
//                double maxSpeed = rs.getDouble("maxSpeed");
//                String make = rs.getString("makeName");
//                String gauge = rs.getString("gaugeName");
//
//                LocomotiveType type;
//                if (rs.getObject("voltage") != null) {
//                    type = LocomotiveType.ELECTRIC;
//                } else {
//                    type = LocomotiveType.DIESEL;
//                }
//
//                LocomotiveModel model = new LocomotiveModel(
//                        power, length, width, height, weight, maxSpeed, make, modelName, type, gauge
//                );
//
//                Locomotive loco = new Locomotive(
//                        number, serviceYear, numBogies, operator, model, maxSpeed
//                );
//
//                repo.addLocomotive(loco);
//            }
//        }
//    }
//
//    private static void loadWagons(Connection con) throws SQLException {
//        String sql = "SELECT w.wagonId, " +
//                "wm.name AS modelName, wm.length, wm.height, wm.width, wm.weight, wm.maxSpeed, wm.volumeCapacity, " +
//                "g.gaugeName " +
//                "FROM Wagon w " +
//                "JOIN Wagon_Model wm ON w.wagonModelId = wm.wagonModelId " +
//                "JOIN Wagon_Model_Gauge wmg ON wm.wagonModelId = wmg.wagonModelId " +
//                "JOIN Gauge g ON wmg.gaugeName = g.gaugeName";
//
//        try (PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            TrainRepository repo = TrainRepository.getInstance();
//
//            while (rs.next()) {
//                String wagonId = String.valueOf(rs.getInt("wagonId"));
//                String modelName = rs.getString("modelName");
//
//                // Conversão: DB tem metros, Java espera metros (mas construtor WagonModel divide por 1000)
//                // Se a DB já estiver em metros, multiplicamos por 1000 para anular a divisão do construtor,
//                // OU assumimos que o construtor é que manda.
//                // Assumindo que o construtor faz `length / 1000` (converte mm para m),
//                // e a DB tem metros, então temos de passar mm (metros * 1000).
//                double length = rs.getDouble("length") * 1000;
//                double height = rs.getDouble("height");
//                double width = rs.getDouble("width");
//                double weight = rs.getDouble("weight");
//                double maxSpeed = rs.getDouble("maxSpeed");
//                String gauge = rs.getString("gaugeName");
//                int boxCapacity = (int) rs.getDouble("volumeCapacity");
//
//                WagonModel model = new WagonModel(
//                        modelName, length, height, width, weight, maxSpeed, gauge, boxCapacity
//                );
//
//                // Adiciona o modelo ao repo também, se desejar
//                repo.addWagonModel(model);
//
//                Wagon wagon = new Wagon(wagonId);
//                wagon.setWagonModel(model);
//
//                repo.addWagon(wagon);
//            }
//        }
//    }

    // --- Métodos Auxiliares de BD (PL/SQL Calls) ---

    private static List<Facility> getFacilitiesFromDatabase(Connection con) throws SQLException {
        CallableStatement callFacilities = con.prepareCall("{? = CALL getAllFacilities()}");
        callFacilities.registerOutParameter(RETURN_INDEX, OracleTypes.CURSOR);
        List<Facility> facilities = new ArrayList<>();
        int i = 0;

        callFacilities.execute();
        ResultSet facilitiesSet = (ResultSet) callFacilities.getObject(RETURN_INDEX);
        while (facilitiesSet.next()) {
            int facilityId = facilitiesSet.getInt("facilityId");
            String name = facilitiesSet.getString("name");
            facilities.add(new Facility(facilityId, name, i));
            i++;
        }
        facilitiesSet.close();
        callFacilities.close();
        return facilities;
    }

    private static List<RailLine> getRailLinesFromDatabase(Connection con, List<Facility> facilities) throws SQLException {
        List<RailLine> railLines = new ArrayList<>();
        CallableStatement callRailLines = con.prepareCall("{? = CALL getRailLines()}");
        callRailLines.registerOutParameter(RETURN_INDEX, OracleTypes.CURSOR);

        callRailLines.execute();
        ResultSet lineSet = (ResultSet) callRailLines.getObject(RETURN_INDEX);
        while (lineSet.next()) {
            String railLineId = String.valueOf(lineSet.getInt("railLineId"));
            String ownerId = lineSet.getString("ownerId");
            String name = lineSet.getString("name");
            int startFacilityId = lineSet.getInt("startFacilityId");
            int endFacilityId = lineSet.getInt("endFacilityId");

            RailLine temp = new RailLine(railLineId, getFacilityById(facilities, startFacilityId),
                    getFacilityById(facilities, endFacilityId), name, ownerId);
            attachSegmentsToLine(con, temp, Integer.parseInt(railLineId));
            railLines.add(temp);

        }
        lineSet.close();
        callRailLines.close();
        return railLines;
    }

    private static void attachSegmentsToLine(Connection con, RailLine line, int railLineId) throws SQLException {
        CallableStatement callSegments = con.prepareCall("{? = CALL getRailSegmentsForLine(?)}");
        callSegments.registerOutParameter(RETURN_INDEX, OracleTypes.CURSOR);
        callSegments.setInt(INPUT_INDEX, railLineId);

        callSegments.execute();
        ResultSet segmentsSet = (ResultSet) callSegments.getObject(RETURN_INDEX);

        while (segmentsSet.next()) {
            String segmentId = String.valueOf(segmentsSet.getInt("segmentId"));
            float gauge = segmentsSet.getFloat("gaugeWidth");
            int eletric = segmentsSet.getInt("trackElectrification");
            int numOfTracks = segmentsSet.getInt("numberOfTracks");
            double dist = segmentsSet.getDouble("length");
            double weight = segmentsSet.getDouble("maxWeight");
            int speedLimit = segmentsSet.getInt("speedLimit");

            Object sidingStartObj = segmentsSet.getObject("startPosition");
            RailSegment tmp;
            if (sidingStartObj == null) {
                tmp = new RailSegment(segmentId, dist, speedLimit, numOfTracks, (eletric == 1),
                        weight, false, gauge);
            } else {
                int sidingSPos = ((Number) sidingStartObj).intValue();
                int sidingLen = segmentsSet.getInt("sidingLength");
                tmp = new RailSegment(segmentId, dist, speedLimit, numOfTracks, (eletric == 1), weight, true,
                        gauge, sidingSPos, sidingLen);
            }
            line.addSegment(tmp);
        }
        segmentsSet.close();
        callSegments.close();
    }

    private static Facility getFacilityById(List<Facility> facilities, int id) {
        for (Facility f : facilities) {
            if (f.getId() == id)
                return f;
        }
        return null;
    }

    public static void initializeTimeInSegments(Connection con, List<RailLine> lines) throws SQLException {
        CallableStatement cTime = con.prepareCall("{? = CALL getAllTimeInSegments()}");
        cTime.registerOutParameter(RETURN_INDEX, OracleTypes.CURSOR);

        cTime.execute();
        ResultSet timeSegSet = (ResultSet) cTime.getObject(RETURN_INDEX);

        while (timeSegSet.next()) {
            Timestamp arriveTs = timeSegSet.getTimestamp("arriveTime");
            Timestamp leaveTs = timeSegSet.getTimestamp("leaveTime");
            if (arriveTs != null && leaveTs != null) {
                LocalDateTime arriveTime = arriveTs.toLocalDateTime();
                LocalDateTime leaveTime = leaveTs.toLocalDateTime();
            }
        }
        timeSegSet.close();
        cTime.close();
    }
}