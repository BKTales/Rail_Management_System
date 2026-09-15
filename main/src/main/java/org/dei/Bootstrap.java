package org.dei;

import org.dei.Sprint1.Repository.*;
import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
import org.dei.Sprint3.Services.DataBaseAccessService;
import org.dei._Facilities.Facility;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint1.Parser.FacilitiesParser;
import org.dei.Sprint1.Parser.LinesParser;
import org.dei.Sprint1.Parser.TrainParser;
import org.dei.Repository.*;
import org.dei._Facilities.Terminal.Terminal;
import org.dei.Sprint1.Parser.Parser;
import org.dei.Sprint3.Services.GraphInitializer;
import org.dei._Train.Freight;
import org.dei._Train.Locomotive;
import org.dei._Train.Train;
import org.dei._Train.Wagon;

import java.sql.Connection;

/**
 * Bootstrap class responsible for initializing the application with necessary data.
 * Loads facilities, rail lines, rolling stock, and initializes the graph from the database.
 *
 * @author Rail Logistics Team
 * @version 1.0
 */
public class Bootstrap {

    /**
     * Default constructor.
     */
    public Bootstrap() {
        // Empty constructor
    }

    /**
     * Runs the bootstrap process with mock data for testing purposes.
     * This method is used for development and testing without database connection.
     */
    public void run() {
        // --- Starting Repositories --- //
        ItemRepository itemRepository = ItemRepository.getInstance();
        TerminalRepository terminalRepository = TerminalRepository.getInstance();
        TrainRepository trainRepository = TrainRepository.getInstance();
        LinesRepository linesRepository = LinesRepository.getInstance();
        FacilityRepository facilitiesRepository = FacilityRepository.getInstance();

        // Initialize terminal
        terminalRepository.add(new Terminal(new GeographicalLocation(11, 11), TimeZoneGroup.CET,
                new TimeZone("ala/aka"), new Country("IT"), "asd", 1));

        // --- Parse Train Data --- //
        TrainParser.parseLocomotives(trainRepository);
        TrainParser.parseWagons(trainRepository);

        // --- Parse Excel Facilities --- //
        FacilitiesParser.parseFacility(facilitiesRepository);

        // --- Parse Lines Data --- //
        LinesParser.parseLine(linesRepository);

        // --- Parse SPRINT 1 --- //
        Parser.parseItems(itemRepository);
        Parser.parseOrder(itemRepository, terminalRepository.getTerminal(0));
        Parser.parseBays(terminalRepository.getTerminal(0));
        Parser.parseReturns(terminalRepository.getTerminal(0));

        // Print statistics
        System.out.println("=== Bootstrap Complete ===");
        System.out.println("Locomotives: " + trainRepository.getAllLocomotives().size());
        System.out.println("Wagons: " + trainRepository.getAllWagons().size());
        System.out.println("Wagon Models: " + trainRepository.getAllWagonModels().size());
        System.out.println("Facilities: " + facilitiesRepository.getFacilities().size());
        System.out.println("Line Segments: " + linesRepository.getAllRailSegments().size());
        System.out.println("Lines: " + linesRepository.getAllRailLines().size());
    }

    /**
     * Runs the bootstrap with mock data for isolated testing.
     */
    public void runMock() {
        // --- Starting Repositories --- //
        ItemRepository itemRepository = ItemRepository.getInstance();
        TerminalRepository terminalRepository = TerminalRepository.getInstance();
        TrainRepository trainRepository = TrainRepository.getInstance();
        LinesRepository linesRepository = LinesRepository.getInstance();
        FacilityRepository facilitiesRepository = FacilityRepository.getInstance();

        // Initialize terminal
        terminalRepository.add(new Terminal(new GeographicalLocation(11, 11), TimeZoneGroup.CET,
                new TimeZone("ala/aka"), new Country("IT"), "asd", 1));

        // --- Parse Train Data --- //
        TrainParser.parseLocomotives(trainRepository);
        TrainParser.parseWagons(trainRepository);

        // --- Parse Excel Facilities --- //
        FacilitiesParser.parseFacility(facilitiesRepository);

        // --- Parse Lines Data --- //
        LinesParser.parseLine(linesRepository);

        // --- Parse SPRINT 1 --- //
        Parser.parseItems(itemRepository);
        Parser.parseOrder(itemRepository, terminalRepository.getTerminal(0));
        Parser.parseBays(terminalRepository.getTerminal(0));
        Parser.parseReturns(terminalRepository.getTerminal(0));

        int i = 1;
        for (Facility f : facilitiesRepository.getFacilities()) {
            f.setGeographicalLocation(new GeographicalLocation(i, i));
            System.out.println(f.getName() + " has " + f.getConnections().size() + " connections");
            i++;
        }
    }

    /**
     * Bootstraps the program by loading all necessary data from the database.
     * This includes facilities, rail lines, segments, rolling stock, and initializing the graph.
     * All wagons and locomotives are loaded from the database to ensure persistence.
     *
     * @throws Exception if database connection or data loading fails
     */
    public void bootProgram() throws Exception {
        Connection con = DatabaseConnection.getInstance();
        // --- Starting Repositories --- //
        TrainRepository trainRepository = TrainRepository.getInstance();
        FreightRepository freightRepository = FreightRepository.getInstance();

        // --- Initialize Graph from Database --- //
        GraphRepository graphRepository = GraphRepository.getInstance();
        graphRepository.setGraph(GraphInitializer.createGraph(con));
        // --- Load Rolling Stock from Database (PERSISTENCE) --- //
        DataBaseAccessService dbService = new DataBaseAccessService();

        // Clear existing in-memory data
        trainRepository.clear();

        // Load locomotives from database
        for (Locomotive locomotive : dbService.getAllLocomotivesFromDatabase(con)) {
            trainRepository.addLocomotive(locomotive);
        }

        // Load wagons from database
        for (Wagon wagon : dbService.getAllWagonsFromDatabase(con)) {
            trainRepository.addWagon(wagon);
        }

        for (Train train : dbService.getAllTrains(con)) {
            train.calculateArrivalTime(train.getDepartureTime(), false);
            trainRepository.addTrain(train);
        }

        for (Freight freight : dbService.getAllFreights(con)){
            freightRepository.addFreight(freight);
        }

        RouteRepository.getInstance().loadRoutesFromDB();


        System.out.println("=== Database Bootstrap Complete ===");
        System.out.println("Locomotives loaded: " + trainRepository.getAllLocomotives().size());
        System.out.println("Wagons loaded: " + trainRepository.getAllWagons().size());


    }
}