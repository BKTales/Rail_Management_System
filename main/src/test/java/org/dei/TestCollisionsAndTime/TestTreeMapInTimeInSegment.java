package org.dei.TestCollisionsAndTime;

import org.dei._Facilities.Facility;
import org.dei._Path.Path;
import org.dei._Path.Route;
import org.dei._Time.TimeInSegment;
import org.dei._Train.Freight;
import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei._RailLineNetwork.*;
import org.dei._Train.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestTreeMapInTimeInSegment {
    public static class AllItems {
        public Facility from;
        public Facility to;
        public RailSegment railSegment;
        public RailSegment railSegment2;
        public LocalDateTime timeBeg;
        public Train train;
        public Route route;
        public Locomotive locomotive;
    }

    public AllItems creatTrain(){
        AllItems items = new AllItems();
        items.from = new Facility();
        items.to = new Facility();
        List<Facility> facilities = new ArrayList<>();
        facilities.add(items.from);  facilities.add(items.to);

        items.railSegment = new RailSegment("1", 50, 100, 2, true, 10);
        items.railSegment2 = new RailSegment("2", 50, 100, 2, true, 10);

        List<RailSegment> segments = new ArrayList<>();
        segments.add(items.railSegment);  segments.add(items.railSegment2);
        items.timeBeg = LocalDateTime.of(1000, 10, 10, 10, 10, 00, 00);

        RailLine railLine = new RailLine("1", items.from, items.to, segments);
        Path p = new Path(facilities, items.from, items.to, "1");
        items.route = new Route(p, 1, new Freight(p.getStartFacility(), p.getEndFacility()), items.timeBeg);
        items.from.getConnections().put(items.to, railLine);

        LocomotiveModel lModel = new LocomotiveModel(1000000, 10,10,10, 10, 100, "make", "name", LocomotiveType.DIESEL);
        items.locomotive = new Locomotive("1", 1990, 1, "adsda", lModel, 100);
        items.train = new Train("train1", items.route, items.timeBeg);
        items.train.addLocomotive(items.locomotive);
        return (items);
    }

    public AllItems creatTrain1Line(){
        AllItems items = new AllItems();
        items.from = new Facility();
        items.to = new Facility();
        List<Facility> facilities = new ArrayList<>();
        facilities.add(items.from);  facilities.add(items.to);

        items.railSegment = new RailSegment("1", 50, 100, 1, true, 10);
        items.railSegment2 = new RailSegment("2", 50, 100, 1, true, 10);

        List<RailSegment> segments = new ArrayList<>();
        segments.add(items.railSegment);  segments.add(items.railSegment2);
        items.timeBeg = LocalDateTime.of(1000, 10, 10, 10, 10);

        RailLine railLine = new RailLine("1", items.from, items.to, segments);
        Path p = new Path(facilities, items.from, items.to, "1");
        items.route = new Route(p, 1, new Freight(p.getStartFacility(), p.getEndFacility()), items.timeBeg);
        items.from.getConnections().put(items.to, railLine);

        LocomotiveModel lModel = new LocomotiveModel(1000000, 10,10,10, 10, 100, "make", "name", LocomotiveType.DIESEL);
        items.locomotive = new Locomotive("1", 1990, 1, "adsda", lModel, 100);
        items.train = new Train("train1", items.route, items.timeBeg);
        items.train.addLocomotive(items.locomotive);
        return (items);
    }

    public AllItems creatTrain1LineWithSiding(){
        AllItems items = new AllItems();
        items.from = new Facility();
        items.to = new Facility();
        List<Facility> facilities = new ArrayList<>();
        facilities.add(items.from);  facilities.add(items.to);

        items.railSegment = new RailSegment("1", 50, 100, 1, true, 10,  10, 2);
        items.railSegment2 = new RailSegment("2", 50, 100, 1, true, 10, 10, 3);

        List<RailSegment> segments = new ArrayList<>();
        segments.add(items.railSegment);  segments.add(items.railSegment2);
        items.timeBeg = LocalDateTime.of(1000, 10, 10, 10, 10);

        RailLine railLine = new RailLine("1", items.from, items.to, segments);
        Path p = new Path(facilities, items.from, items.to, "1");
        items.route = new Route(p, 1, new Freight(p.getStartFacility(), p.getEndFacility()), items.timeBeg);
        items.from.getConnections().put(items.to, railLine);

        LocomotiveModel lModel = new LocomotiveModel(109, 10,10,10, 10, 100, "make", "name", LocomotiveType.DIESEL);
        items.locomotive = new Locomotive("1", 1990, 1, "adsda", lModel, 100);
        items.train = new Train("train1", items.route, items.timeBeg);
        items.train.addLocomotive(items.locomotive);
        return (items);
    }

    public AllItems creatTrain3Line(){
        AllItems items = new AllItems();
        items.from = new Facility();
        items.to = new Facility();
        List<Facility> facilities = new ArrayList<>();
        facilities.add(items.from);  facilities.add(items.to);

        items.railSegment = new RailSegment("1", 50, 100, 3, true, 10);
        items.railSegment2 = new RailSegment("2", 50, 100, 3, true, 10);

        List<RailSegment> segments = new ArrayList<>();
        segments.add(items.railSegment);  segments.add(items.railSegment2);
        items.timeBeg = LocalDateTime.of(1000, 10, 10, 10, 10);

        RailLine railLine = new RailLine("1", items.from, items.to, segments);
        Path p = new Path(facilities, items.from, items.to, "1");
        items.route = new Route(p, 1, new Freight(p.getStartFacility(), p.getEndFacility()), items.timeBeg);
        items.from.getConnections().put(items.to, railLine);

        LocomotiveModel lModel = new LocomotiveModel(109, 10,10,10, 10, 100, "make", "name", LocomotiveType.DIESEL);
        items.locomotive = new Locomotive("1", 1990, 1, "adsda", lModel, 100);
        items.train = new Train("train1", items.route, items.timeBeg);
        items.train.addLocomotive(items.locomotive);
        return (items);
    }

    // ======= 5 no collisions tests  ======= //
    // 1. Starting at same time inside supported size of tracks
    // 2. Starting at same time inside supported size of tracks
    // 3. Starting at different time(before) inside supported size of tracks
    // 4. Starting at different time(after) inside supported size of tracks
    // 5. Starting at different time after other train went out of track

    @Test
    public void testOneTrainInTimeInSegment() throws Exception {
        /// Arrange
        AllItems items = creatTrain();

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);

        System.out.println(" ========== StartTime train 1:"+ items.train.getDepartureTime());
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 1);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 1);
    }

    @Test
    public void testTwoTrainStartingBefore() throws Exception {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.minusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        System.out.println("====================================");
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);

        Map.Entry<TimeInSegment, List<Train>> entry = items.railSegment.getArrivalsAtSegment().firstEntry();
        Map.Entry<TimeInSegment, List<Train>> entry2 = items.railSegment2.getArrivalsAtSegment().lastEntry();

        System.out.println(" ========== StartTime train 1:"+ items.train.getDepartureTime());
        System.out.println(" ========== StartTime train 2:"+ otherTrain.getDepartureTime());
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 3);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 3);
    }

    @Test
    public void testTwoTrainStartingAfter() throws Exception  {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.plusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);
        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);

        Map.Entry<TimeInSegment, List<Train>> entry = items.railSegment.getArrivalsAtSegment().firstEntry();
        Map.Entry<TimeInSegment, List<Train>> entry2 = items.railSegment2.getArrivalsAtSegment().firstEntry();

        System.out.println(" ========== StartTime train 1:"+ items.train.getDepartureTime());
        System.out.println(" ========== StartTime train 2:"+ otherTrain.getDepartureTime());
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }

        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 3);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 3);
        assert(entry.getValue().size() == 1);
        assert(entry2.getValue().size() == 1);
    }

    @Test
    public void testTwoTrainStartingAtSameTime() throws Exception  {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg);
        otherTrain.addLocomotive(items.locomotive);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);

        Map.Entry<TimeInSegment, List<Train>> entry = items.railSegment.getArrivalsAtSegment().firstEntry();
        Map.Entry<TimeInSegment, List<Train>> entry2 = items.railSegment2.getArrivalsAtSegment().firstEntry();

        System.out.println(" ========== StartTime train 1:"+ items.train.getDepartureTime());
        System.out.println(" ========== StartTime train 2:"+ otherTrain.getDepartureTime());
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 1);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 1);
        assert(entry.getValue().size() == 2);
        assert(entry2.getValue().size() == 2);
        assert (entry.getValue().getFirst() == items.train);
        assert (entry.getValue().getLast() == otherTrain);
    }

    @Test
    public void testTwoTrainStartingAfter1stPassedFull()  throws Exception  {
        /// Arrange
        AllItems items = creatTrain1Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.plusHours(5));
        otherTrain.addLocomotive(items.locomotive);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);

        Map.Entry<TimeInSegment, List<Train>> entry = items.railSegment.getArrivalsAtSegment().firstEntry();
        Map.Entry<TimeInSegment, List<Train>> entry2 = items.railSegment2.getArrivalsAtSegment().lastEntry();

        System.out.println(" ========== StartTime train 1:"+ items.train.getDepartureTime());
        System.out.println(" ========== StartTime train 2:"+ otherTrain.getDepartureTime());
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 2);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 2);
    }

    // great test!
    @Test
    public void testThreeTrainStartingAfterEachOtherPossible() throws Exception  {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.plusMinutes(10));
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.plusMinutes(31));
        otherTrain.addLocomotive(items.locomotive);
        otherTrain3.addLocomotive(items.locomotive);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);
        otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);

        Map.Entry<TimeInSegment, List<Train>> entry = items.railSegment.getArrivalsAtSegment().firstEntry();
        Map.Entry<TimeInSegment, List<Train>> entry2 = items.railSegment2.getArrivalsAtSegment().firstEntry();

        System.out.println(" ========== StartTime train 1:"+ items.train.getDepartureTime());
        System.out.println(" ========== StartTime train 2:"+ otherTrain.getDepartureTime());
        System.out.println(" ========== StartTime train 3:"+ otherTrain3.getDepartureTime());
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }

        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 5);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 5);
        assert(entry.getValue().size() == 1);
        assert(entry2.getValue().size() == 1);
    }

    // ======= 5 no collisions tests with wagons ======= //
    // 1. Starting at same time inside supported size of tracks
    // 2. Starting at same time inside supported size of tracks
    // 3. Starting at different time(before) inside supported size of tracks
    // 4. Starting at different time(after) inside supported size of tracks
    // 5. Starting at different time after other train went out of track

    @Test
    public void testOneTrainInTimeInSegmentWithWagons()  throws Exception     {
        /// Arrange
        AllItems items = creatTrain();

        Wagon w = new Wagon("asd");
        w.addBox(new Box("1", 1, new Item(Unit.BOTTLE, "1", 1, ItemType.CLEANING, 100), null, LocalDateTime.now()));
        w.setWagonModel(new WagonModel("1", 1000, 1000, 1000, 1000, 100, "gauge2", 20));
        //items.train.addWagon(w);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);

        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 1);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 1);
    }

    @Test
    public void testTwoTrainStartingBeforeWithWagons() throws Exception  {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.minusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);

        Wagon w = new Wagon("asd");
        w.addBox(new Box("1", 1, new Item(Unit.BOTTLE, "1", 1, ItemType.CLEANING, 100), null, LocalDateTime.now()));
        w.setWagonModel(new WagonModel("1", 1000, 1000, 1000, 1000, 100, "gauge2", 20));
        //items.train.addWagon(w);
        //otherTrain.addWagon(w);
        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(items.train.getDepartureTime(), false);

        Map.Entry<TimeInSegment, List<Train>> entry = items.railSegment.getArrivalsAtSegment().firstEntry();
        Map.Entry<TimeInSegment, List<Train>> entry2 = items.railSegment2.getArrivalsAtSegment().firstEntry();

        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 1);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 1);
        assert(entry.getValue().size() == 2);
        assert(entry2.getValue().size() == 2);
        assert (entry.getValue().getFirst() == items.train);
        assert (entry.getValue().getLast() == otherTrain);
    }

    @Test
    public void testTwoTrainStartingAfterWithWagons() throws Exception  {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.plusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);

        Wagon w = new Wagon("asd");
        w.addBox(new Box("1", 1, new Item(Unit.BOTTLE, "1", 1, ItemType.CLEANING, 100), null, LocalDateTime.now()));
        w.setWagonModel(new WagonModel("1", 1000, 1000, 1000, 1000, 100, "gauge2", 20));
        //items.train.addWagon(w);
        //otherTrain.addWagon(w);
        System.out.println(" ========== StartTime train 1:"+ items.train.getDepartureTime());
        System.out.println(" ========== StartTime train 2:"+ otherTrain.getDepartureTime());
        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);

        System.out.println("\n==================== Train 2 ====================");
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);

        for (Map.Entry<TimeInSegment, List<Train>> entry : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = entry.getKey();
            List<Train> trains = entry.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }

        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 3);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 3);
    }

    @Test
    public void testTwoTrainStartingAtSameTimeWithWagons() throws Exception  {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg);
        otherTrain.addLocomotive(items.locomotive);

        Wagon w = new Wagon("asd");
        w.addBox(new Box("1", 1, new Item(Unit.BOTTLE, "1", 1, ItemType.CLEANING, 100), null, LocalDateTime.now()));
        w.setWagonModel(new WagonModel("1", 1000, 1000, 1000, 1000, 100, "gauge2", 20));
        //items.train.addWagon(w);
        //otherTrain.addWagon(w);
        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);

        Map.Entry<TimeInSegment, List<Train>> entry = items.railSegment.getArrivalsAtSegment().firstEntry();
        Map.Entry<TimeInSegment, List<Train>> entry2 = items.railSegment2.getArrivalsAtSegment().firstEntry();

        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 1);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 1);
        assert(entry.getValue().size() == 2);
        assert(entry2.getValue().size() == 2);
        assert (entry.getValue().getFirst() == items.train);
        assert (entry.getValue().getLast() == otherTrain);
    }

    @Test
    public void testTwoTrainStartingAfter1stPassedFullWithWagons() throws Exception  {
        /// Arrange
        AllItems items = creatTrain1Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.plusHours(5));
        otherTrain.addLocomotive(items.locomotive);

        Wagon w = new Wagon("asd");
        w.addBox(new Box("1", 1, new Item(Unit.BOTTLE, "1", 1, ItemType.CLEANING, 100), null, LocalDateTime.now()));
        w.setWagonModel(new WagonModel("1", 1000, 1000, 1000, 1000, 100, "gauge2", 20));
        //items.train.addWagon(w);
        //otherTrain.addWagon(w);
        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);

        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 2);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 2);
    }


    // ======= Testing 3 types of collisions with ======= //
    // 1. Same start Time
    // 2. End time colliding
    // 3. Start Time colliding

    @Test
    public void testThreeTrainStartingAtSameTime() throws Exception  {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg);
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg);
        otherTrain.addLocomotive(items.locomotive);
        otherTrain3.addLocomotive(items.locomotive);

        /// Act
        try{
            items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
            otherTrain.calculateArrivalTime(items.train.getDepartureTime(), false);
            otherTrain3.calculateArrivalTime(items.train.getDepartureTime(), false);
            assert false;
        } catch(Exception e){
            /// Assert
            System.out.println("Exception caught! " + e.getMessage());
            assert true;
        }
    }

    @Test
    public void testThreeRightTrainCollidingWithDiffStartTime() throws Exception  {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg);
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.minusMinutes(1));
        otherTrain.addLocomotive(items.locomotive);
        otherTrain3.addLocomotive(items.locomotive);

        /// Act
        try{
            items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
            otherTrain.calculateArrivalTime(items.train.getDepartureTime(), false);
            otherTrain3.calculateArrivalTime(items.train.getDepartureTime(), false);
            assert false;
        } catch(Exception e){
            /// Assert
            System.out.println("Exception caught! " + e.getMessage());
            assert true;
        }
    }

    @Test
    public void testThreeLeftTrainCollidingWithDiffStartTime() throws Exception  {
        /// Arrange
        AllItems items = creatTrain();
        Train otherTrain = new Train("train2", items.route, items.timeBeg);
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.minusMinutes(2));
        otherTrain.addLocomotive(items.locomotive);
        otherTrain3.addLocomotive(items.locomotive);

        /// Act
        try{
            items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
            otherTrain.calculateArrivalTime(items.train.getDepartureTime(), false);
            otherTrain3.calculateArrivalTime(items.train.getDepartureTime(), false);
            assert false;
        } catch(Exception e){
            /// Assert
            System.out.println("Exception caught! " + e.getMessage());
            assert true;
        }
    }

    // ======= Testing 3 types which would be collision unless if it did not have siding ======= //
    // 1. Same start Time
    // 2. End time colliding
    // 3. Start Time colliding
    @Test
    public void testOneTrainInTimeInSegmentWithSiding() throws Exception  {
        /// Arrange
        AllItems items = creatTrain1LineWithSiding();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.minusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);

        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 1);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 1);
    }

    @Test
    public void testOneTrainInTimeInSegmentWithoutSiding() throws Exception  {
        /// Arrange
        AllItems items = creatTrain1Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.minusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);
        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);

        /// Assert
        assert(items.railSegment.getArrivalsAtSegment().size() == 1);
        assert(items.railSegment2.getArrivalsAtSegment().size() == 1);
    }

    // ======= Testing 3 types of inner time collisions ======= //
    // 1. two start at same time

    @Test
    public void testThreeTrainsInnerTime() throws Exception  {
        /// Arrange
        AllItems items = creatTrain3Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg);
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.plusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);

        LocomotiveModel lModel = new LocomotiveModel(1000, 10,10,10, 10, 300, "make", "name", LocomotiveType.DIESEL);
        Locomotive locomotiveFaster = new Locomotive("1", 1990, 1, "adsda", lModel, 300);
        otherTrain3.addLocomotive(locomotiveFaster);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);
        otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert true;
    }

    @Test
    public void testTwoTrainsInnerTime() throws Exception  {
        /// Arrange
        AllItems items = creatTrain3Line();
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.plusMinutes(10));

        LocomotiveModel lModel = new LocomotiveModel(1000, 10,10,10, 10, 300, "make", "name", LocomotiveType.DIESEL);
        Locomotive locomotiveFaster = new Locomotive("1", 1990, 1, "adsda", lModel, 300);
        otherTrain3.addLocomotive(locomotiveFaster);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert true;
    }

    @Test
    public void testThreeTrainsOuterTime() throws Exception  {
        /// Arrange
        AllItems items = creatTrain3Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg);
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.minusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);

        LocomotiveModel lModel = new LocomotiveModel(100, 10,10,10, 10, 30, "make", "name", LocomotiveType.DIESEL);
        Locomotive locomotiveSlower = new Locomotive("1", 1990, 1, "adsda", lModel, 30);
        otherTrain3.addLocomotive(locomotiveSlower);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);
        System.out.println("=============================");
        otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert true;
    }

    @Test
    public void testThreeTrainsOuterTimeTwoSlow() throws Exception  {
        /// Arrange
        AllItems items = creatTrain3Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg);
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.minusMinutes(10));

        LocomotiveModel lModel = new LocomotiveModel(100, 10,10,10, 10, 30, "make", "name", LocomotiveType.DIESEL);
        Locomotive locomotiveSlower = new Locomotive("1", 1990, 1, "adsda", lModel, 30);
        otherTrain.addLocomotive(locomotiveSlower);
        otherTrain3.addLocomotive(locomotiveSlower);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);
        System.out.println("=============================");
        otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert true;
    }

    @Test
    public void testThreeTrainsOuterTimeTwoSlowSameTime()  throws Exception {
        /// Arrange
        AllItems items = creatTrain3Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.minusMinutes(10));
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.minusMinutes(10));

        LocomotiveModel lModel = new LocomotiveModel(100, 10,10,10, 10, 30, "make", "name", LocomotiveType.DIESEL);
        Locomotive locomotiveSlower = new Locomotive("1", 1990, 1, "adsda", lModel, 30);
        otherTrain.addLocomotive(locomotiveSlower);
        otherTrain3.addLocomotive(locomotiveSlower);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);
        otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);

        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert true;
    }

    @Test
    public void testTwoTrainsOuterTime() throws Exception{
        /// Arrange
        AllItems items = creatTrain3Line();
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.minusMinutes(10));

        LocomotiveModel lModel = new LocomotiveModel(100, 10,10,10, 10, 30, "make", "name", LocomotiveType.DIESEL);
        Locomotive locomotiveSlower = new Locomotive("1", 1990, 1, "adsda", lModel, 30);
        otherTrain3.addLocomotive(locomotiveSlower);

        /// Act
        items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        System.out.println("=============================");

        otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);
        for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
        {
            TimeInSegment time = tmp.getKey();
            List<Train> trains = tmp.getValue();

            System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

            for (Train train : trains) {
                System.out.println("   Train: " + train.getTrainId());
            }
        }
        /// Assert
        assert true;
    }

    // ======= Testing 3 types of collisions with ======= //
    // 1. Same start Time
    // 2. End time colliding
    // 3. Start Time colliding

    @Test
    public void testThreeTrainStartingAtSameTimeNoCollision() throws Exception  {
        /// Arrange
        AllItems items = creatTrain3Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg);
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg);
        otherTrain.addLocomotive(items.locomotive);
        otherTrain3.addLocomotive(items.locomotive);

        /// Act
        try {
            items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
            otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);
            otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);
            for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet()) {
                TimeInSegment time = tmp.getKey();
                List<Train> trains = tmp.getValue();

                System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

                for (Train train : trains) {
                    System.out.println("   Train: " + train.getTrainId());
                }
            }
            /// Assert
            assert true;
        }catch (Exception e){}
    }

    @Test
    public void testThreeRightTrainCollidingWithDiffStartTimeNoCollision() throws Exception  {
        /// Arrange
        AllItems items = creatTrain3Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.minusMinutes(5));
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.minusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);
        otherTrain3.addLocomotive(items.locomotive);

        /// Act
        try {
            otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);
            items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
            for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet()) {
                TimeInSegment time = tmp.getKey();
                List<Train> trains = tmp.getValue();

                System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

                for (Train train : trains) {
                    System.out.println("   Train: " + train.getTrainId());
                }
            }
            System.out.println("===================");
            otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);
            for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet()) {
                TimeInSegment time = tmp.getKey();
                List<Train> trains = tmp.getValue();

                System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

                for (Train train : trains) {
                    System.out.println("   Train: " + train.getTrainId());
                }
            }
            /// Assert
            assert true;
        }
        catch (Exception e)
        {

        }
    }

    @Test
    public void testThreeLeftTrainCollidingWithDiffStartTimeNoCollisionFromOldestToNewest() throws Exception  {
        /// Arrange
        AllItems items = creatTrain3Line();
        Train otherTrain = new Train("train2", items.route, items.timeBeg.minusMinutes(5));
        Train otherTrain3 = new Train("train3", items.route, items.timeBeg.minusMinutes(10));
        otherTrain.addLocomotive(items.locomotive);
        otherTrain3.addLocomotive(items.locomotive);

        /// Act
        try {
            otherTrain3.calculateArrivalTime(otherTrain3.getDepartureTime(), false);
            otherTrain.calculateArrivalTime(otherTrain.getDepartureTime(), false);
            items.train.calculateArrivalTime(items.train.getDepartureTime(), false);
            for (Map.Entry<TimeInSegment, List<Train>> tmp : items.railSegment.getArrivalsAtSegment().entrySet())
            {
                TimeInSegment time = tmp.getKey();
                List<Train> trains = tmp.getValue();

                System.out.println("TimeInSegment: " + time.getStartTime() + " - " + time.getEndTime());

                for (Train train : trains) {
                    System.out.println("   Train: " + train.getTrainId());
                }
            }

            /// Assert
            assert true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}