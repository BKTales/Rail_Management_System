//package org.dei;
//
//import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
//import org.dei.Sprint3.DTOs.LocomotiveWithStatus;
//import org.dei.Sprint3.DTOs.WagonWithStatus;
//import org.dei.Sprint3.Services.DataBaseAccessService;
//import org.dei._Facilities.Facility;
//import org.dei._Path.Route;
//import org.dei._RailLineNetwork.RailLine;
//import org.dei._RailLineNetwork.RailSegment;
//import org.dei._Train.Locomotive;
//import org.dei._Train.Train;
//import org.dei._Train.Wagon;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
///**
// * Test class for PL/SQL functions in the database.
// * This class provides a simple way to test all four PL/SQL functions.
// */
//public class TestPLSQLFunctions{
//
//    public static void main(String[] args){
//        System.out.println(" === TESTING PL/SQL FUNCTIONS === 🧪");
//
//        try(Connection connection = DatabaseConnection.getInstance()){
//            DataBaseAccessService service = new DataBaseAccessService();
//
//            System.out.println(" \nTEST 1: getAllLocomotives()");
//            testGetAllLocomotives(service, connection);
//
//            System.out.println(" \nTEST 2: getAllWagons()");
//            testGetAllWagons(service, connection);
//
//            System.out.println(" \nTEST 3: getLocomotivesWithTransitStatus()");
//            testGetLocomotivesWithTransitStatus(service, connection);
//
//            System.out.println(" \nTEST 4: getWagonsWithTransitStatus()");
//            testGetWagonsWithTransitStatus(service, connection);
//
//            System.out.println(" \nTEST 5: getAllFacilities()");
//            testGetAllFacilities(service, connection);
//
//            System.out.println(" \nTEST 6: getAllRailLines()");
//            testGetAllRailLines(service, connection);
//
//            System.out.println(" \nTEST 7: getRoutes()");
//            testGetRoutes(service, connection);
//
//            System.out.println(" \nTEST 8: getRailSegmentsForLine()");
//            testGetRailSegmentsForLine(service, connection);
//
//            System.out.println(" \nTEST 9: getAllTrains()");
//            testGetAllTrains(service, connection);
//
//            System.out.println(" \nTEST COMPLETE! All functions have been tested.");
//
//        }catch(SQLException e){
//            System.err.println(" Database connection error: " + e.getMessage());
//            e.printStackTrace();
//        }catch(Exception e){
//            System.err.println(" Unexpected error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private static void testGetAllLocomotives(DataBaseAccessService service, Connection connection){
//        try{
//            List<Locomotive> locomotives = service.getAllLocomotives(connection);
//            System.out.println(" Function executed successfully");
//            System.out.println(" Locomotives found: " + locomotives.size());
//
//            if(!locomotives.isEmpty()){
//                System.out.println(" First locomotive: " + locomotives.get(0).getNumber());
//            }
//
//        }catch(SQLException e){
//            System.err.println(" Error in getAllLocomotives function: " + e.getMessage());
//        }
//    }
//
//    private static void testGetAllWagons(DataBaseAccessService service, Connection connection){
//        try{
//            List<Wagon> wagons = service.getAllWagons(connection);
//            System.out.println(" Function executed successfully");
//            System.out.println(" Wagons found: " + wagons.size());
//
//            if(!wagons.isEmpty()){
//                System.out.println(" First wagon: " + wagons.get(0).getWagonId());
//            }
//
//        }catch(SQLException e){
//            System.err.println(" Error in getAllWagons function: " + e.getMessage());
//        }
//    }
//
//    private static void testGetLocomotivesWithTransitStatus(DataBaseAccessService service, Connection connection){
//        try{
//            List<LocomotiveWithStatus> locomotives = service.getLocomotivesWithTransitStatus(connection, null);
//            System.out.println(" Function executed successfully");
//            System.out.println(" Locomotives with status: " + locomotives.size());
//
//            if(!locomotives.isEmpty()){
//                LocomotiveWithStatus first = locomotives.get(0);
//                System.out.println(" First: ID=" + first.getLocomotive().getNumber() +
//                        ", Status=" + first.getTransitStatus());
//            }
//
//        }catch(SQLException e){
//            System.err.println(" Error in getLocomotivesWithTransitStatus function: " + e.getMessage());
//        }
//    }
//
//    private static void testGetWagonsWithTransitStatus(DataBaseAccessService service, Connection connection){
//        try{
//            List<WagonWithStatus> wagons = service.getWagonsWithTransitStatus(connection, null);
//            System.out.println(" Function executed successfully");
//            System.out.println(" Wagons with status: " + wagons.size());
//
//            if(!wagons.isEmpty()){
//                WagonWithStatus first = wagons.get(0);
//                System.out.println(" First: ID=" + first.getWagon().getWagonId() +
//                        ", Status=" + first.getTransitStatus());
//            }
//
//        }catch(SQLException e){
//            System.err.println(" Error in getWagonsWithTransitStatus function: " + e.getMessage());
//        }
//    }
//
//    private static void testGetAllFacilities(DataBaseAccessService service, Connection connection){
//        try{
//            List<Facility> facilities = service.getAllFacilities(connection);
//            System.out.println(" Function executed successfully");
//            System.out.println(" Facilities found: " + facilities.size());
//
//            if(!facilities.isEmpty()){
//                Facility first = facilities.get(0);
//                System.out.println(" First facility: ID=" + first.getId() +
//                        ", Name=" + first.getName());
//
//                if(facilities.size() > 1){
//                    System.out.println(" Sample facilities:");
//                    int count = Math.min(5, facilities.size());
//                    for(int i = 0; i < count; i++){
//                        Facility fac = facilities.get(i);
//                        System.out.println("   [" + (i+1) + "] ID: " + fac.getId() +
//                                ", Name: " + fac.getName());
//                    }
//                }
//            }
//
//        }catch(SQLException e){
//            System.err.println(" Error in getAllFacilities function: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private static void testGetAllRailLines(DataBaseAccessService service, Connection connection){
//        try{
//            List<RailLine> railLines = service.getAllRailLines(connection);
//            System.out.println(" Function executed successfully");
//            System.out.println(" Rail lines found: " + railLines.size());
//
//            if(!railLines.isEmpty()){
//                RailLine first = railLines.get(0);
//                System.out.println(" First rail line: ID=" + first.getRailLineId() +
//                        ", Name=" + first.getName() +
//                        ", Owner=" + first.getOwner());
//
//                if(railLines.size() > 1){
//                    System.out.println(" Sample rail lines:");
//                    int count = Math.min(5, railLines.size());
//                    for(int i = 0; i < count; i++){
//                        RailLine rl = railLines.get(i);
//                        System.out.println("   [" + (i+1) + "] ID: " + rl.getRailLineId() +
//                                ", Name: " + rl.getName() +
//                                ", Start: " + (rl.getStartFacility() != null ? rl.getStartFacility().getName() : "null") +
//                                ", End: " + (rl.getEndFacility() != null ? rl.getEndFacility().getName() : "null"));
//                    }
//                }
//            }
//
//        }catch(SQLException e){
//            System.err.println(" Error in getAllRailLines function: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private static void testGetRoutes(DataBaseAccessService service, Connection connection){
//        try{
//            List<Route> routes = service.getRoutes(connection);
//            System.out.println(" Function executed successfully");
//            System.out.println(" Routes found: " + routes.size());
//
//            if(!routes.isEmpty()){
//                Route first = routes.get(0);
//                System.out.println(" First route: ID=" + first.getRouteId() +
//                        ", Start: " + (first.getPath().getStartFacility() != null ? first.getPath().getStartFacility().getName() : "null") +
//                        ", End: " + (first.getPath().getEndFacility() != null ? first.getPath().getEndFacility().getName() : "null") +
//                        ", Freights: " + first.getFreights().size() +
//                        ", Path facilities: " + first.getPath().getRailFacilities().size());
//
//                if(routes.size() > 1){
//                    System.out.println(" Sample routes:");
//                    int count = Math.min(5, routes.size());
//                    for(int i = 0; i < count; i++){
//                        Route route = routes.get(i);
//                        System.out.println("   [" + (i+1) + "] Route ID: " + route.getRouteId() +
//                                ", Start: " + (route.getPath().getStartFacility() != null ? route.getPath().getStartFacility().getName() : "null") +
//                                ", End: " + (route.getPath().getEndFacility() != null ? route.getPath().getEndFacility().getName() : "null") +
//                                ", Freights: " + route.getFreights().size() +
//                                ", Complex: " + route.isComplex() +
//                                ", Path points: " + route.getPath().getRailFacilities().size());
//                    }
//                }
//            }
//
//        }catch(SQLException e){
//            System.err.println(" Error in getRoutes function: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private static void testGetRailSegmentsForLine(DataBaseAccessService service, Connection connection){
//        try{
//            // Test with rail line ID 1
//            int railLineId = 1;
//            List<RailSegment> segments = service.getRailSegmentsForLine(connection, railLineId);
//            System.out.println(" Function executed successfully");
//            System.out.println(" Segments found for rail line " + railLineId + ": " + segments.size());
//
//            // Test with invalid ID
//            int invalidId = 99999;
//            List<RailSegment> invalidSegments = service.getRailSegmentsForLine(connection, invalidId);
//            System.out.println(" Segments found for invalid rail line " + invalidId + ": " + invalidSegments.size() + " (should be 0)");
//
//            if(!segments.isEmpty()){
//                RailSegment first = segments.get(0);
//                System.out.println(" First segment: ID=" + first.getSegmentId() +
//                        ", Distance=" + first.getDistanceKm() + " km" +
//                        ", Speed limit=" + first.getMaxSpeedKmh() + " km/h");
//
//                if(segments.size() > 1){
//                    System.out.println(" Sample segments:");
//                    int count = Math.min(5, segments.size());
//                    for(int i = 0; i < count; i++){
//                        RailSegment seg = segments.get(i);
//                        System.out.println("   [" + (i+1) + "] Segment ID: " + seg.getSegmentId() +
//                                ", Distance: " + seg.getDistanceKm() + " km" +
//                                ", Speed: " + seg.getMaxSpeedKmh() + " km/h" +
//                                ", Electrified: " + seg.isElectrified() +
//                                ", Tracks: " + seg.getManyTracks());
//                    }
//                }
//            }
//
//        }catch(SQLException e){
//            System.err.println(" Error in getRailSegmentsForLine function: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private static void testGetAllTrains(DataBaseAccessService service, Connection connection){
//        try{
//            List<Train> trains = service.getAllTrains(connection);
//            System.out.println(" Function executed successfully");
//            System.out.println(" Trains found: " + trains.size());
//
//            if(!trains.isEmpty()){
//                Train first = trains.get(0);
//                System.out.println(" First train: ID=" + first.getTrainId() +
//                        ", Route ID=" + first.getRoute().getRouteId() +
//                        ", Departure=" + first.getDepartureTime());
//
//                if(trains.size() > 1){
//                    System.out.println(" Sample trains:");
//                    int count = Math.min(5, trains.size());
//                    for(int i = 0; i < count; i++){
//                        Train train = trains.get(i);
//                        System.out.println("   [" + (i+1) + "] Train ID: " + train.getTrainId() +
//                                ", Route ID: " + train.getRoute().getRouteId() +
//                                ", Departure: " + train.getDepartureTime() +
//                                ", Max Speed: " + train.getMaxSpeed() + " km/h" +
//                                ", Max Weight: " + train.getMaxWeight() + " tons");
//                    }
//                }
//            }
//
//        }catch(SQLException e){
//            System.err.println(" Error in getAllTrains function: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}