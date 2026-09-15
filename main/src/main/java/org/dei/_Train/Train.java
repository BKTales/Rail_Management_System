package org.dei._Train;

import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
import org.dei.Sprint3.Services.DataBaseAccessService;
import org.dei._Facilities.Facility;
import org.dei._Location.Arrival;
import org.dei._Path.Path;
import org.dei._Path.Route;
import org.dei._RailLineNetwork.*;
import org.dei._Time.TimeInSegment;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class Train {
    private static final double IDEAL_POWER_TO_WEIGHT = 0.05;
    private static final long LOAD_AND_UNLOAD_TIME_PER_WAGON = 5; //minutes

    private final List<Locomotive>  locomotives;
    private final List<Freight>     freightsOnTrain; // for loading logic, real freight are inside the route

    private final String            trainId;
    private Route                   route;
    private LocalDateTime           departureTime;
    private final Arrival           arrival;

    // Lista para guardar os tempos calculados para a BD
    private List<TimeInSegment>     calculatedSegmentTimes;

    private double                  maxWeight;
    private double                  maxLength;    // total (freight + locomotives)
    private double                  maxSpeed;    // total (freight + locomotives)
    private double                  totalPower;

    private double                  currentWeight;    // current value based on freights on train in T
    private double                  currentLength;    // current value based on freights on train
    private double                  currentMaxSpeed;  // current value based on freights on train

    public Train(String trainId, Route route, LocalDateTime departureTime) {
        this.trainId = trainId;
        this.route = route;
        this.departureTime = departureTime;
        this.locomotives = new ArrayList<>();
        this.freightsOnTrain = new ArrayList<>();
        this.arrival = new Arrival(this);
        this.calculatedSegmentTimes = new ArrayList<>();

        this.totalPower = getTotalPower();
        this.currentWeight = 0.0;
        this.currentLength = 0.0;
        this.currentMaxSpeed = 0.0;
        this.maxWeight = 0.0;
        this.maxLength =  Float.MAX_VALUE;

        calculateBaseStats();
    }

    public Train(String traindId){
        this.trainId = traindId;
        this.locomotives = new ArrayList<>();
        this.freightsOnTrain = new ArrayList<>();
        this.calculatedSegmentTimes = new ArrayList<>();
        this.arrival = new Arrival(this);
        this.totalPower = getTotalPower();
        this.currentWeight = 0.0;
        this.currentLength = 0.0;
        this.currentMaxSpeed = 0.0;
        this.maxWeight = 0.0;
        this.maxLength =  Float.MAX_VALUE;
        calculateBaseStats();
    }

    public Train(Train anotherTrain) {
        this.trainId = anotherTrain.getTrainId();
        this.route = anotherTrain.getRoute();
        this.departureTime = anotherTrain.getDepartureTime();
        this.locomotives = new ArrayList<>();
        this.locomotives.addAll(anotherTrain.getLocomotives());
        this.freightsOnTrain = new ArrayList<>();
        this.freightsOnTrain.addAll(anotherTrain.getFreightsOnTrain());
        this.arrival = anotherTrain.getArrival();
        this.calculatedSegmentTimes = new ArrayList<>();
        this.calculatedSegmentTimes.addAll(anotherTrain.getCalculatedSegmentTimes());

        this.totalPower = getTotalPower();
        this.currentWeight = anotherTrain.getCurrentWeight();
        this.currentLength = anotherTrain.getCurrentLength();
        this.currentMaxSpeed = anotherTrain.getCurrentMaxSpeed();
        this.maxWeight = anotherTrain.getMaxWeight();
        this.maxLength = anotherTrain.getMaxLength();

        updateCurrentStats();
    }

    private void calculateBaseStats() {
        this.currentWeight = locomotives.stream()
                .mapToDouble(Locomotive::getWeight)
                .sum();
        this.currentLength = locomotives.stream()
                .mapToDouble(loco -> loco.getModel().getLength())
                .sum();
        this.totalPower = locomotives.stream()
                .mapToDouble(loco -> loco.getModel().getPower())
                .sum();

        this.maxWeight = this.currentWeight;
        this.maxLength = this.currentLength;
    }

    public double calculateCurrentWeight() {
        double weight = locomotives.stream()
                .mapToDouble(Locomotive::getWeight)
                .sum();

        for (Freight f : freightsOnTrain) {
            for (Wagon wagon : f.getWagons()) {
                weight += wagon.getTotalWeight() / 1000;
            }
        }
        return weight;
    }

    public double calculateCurrentLength() {
        double length = locomotives.stream()
                .mapToDouble(loco -> loco.getModel().getLength())
                .sum();
        for (Freight f : freightsOnTrain) {
            for (Wagon wagon : f.getWagons()) {
                length += wagon.getWagonModel().getLength();
            }
        }
        return length;
    }

    /**
     * Calculate total train power in kW
     */
    public double getTotalPower() {
        return locomotives.stream()
                .mapToDouble(Locomotive::getPower)
                .sum();
    }

    // ================= LOGIC =================

    public void addLocomotive(Locomotive locomotive) {
        if (!locomotives.isEmpty()) {
            String existingGauge = locomotives.get(0).getModel().getGauge();
            if (!locomotive.getModel().getGauge().equals(existingGauge)) {
                throw new IllegalArgumentException("Cannot add " + locomotive.getModel().getGauge() +
                        " locomotive to a " + existingGauge + " train.");
            }
        }
        locomotives.add(locomotive);
        updateCurrentStats();
    }

    public double lowestCurrentWagonMaxSpeed() {
        if (freightsOnTrain == null || freightsOnTrain.isEmpty()) {
            return 0;
        }

        double minSpeed = Double.MAX_VALUE;
        boolean hasWagons = false;

        for (Freight f : freightsOnTrain) {
            for (Wagon wagon : f.getWagons()) {
                hasWagons = true;
                double wagonSpeed = wagon.getWagonModel().getMaxSpeed();
                if (wagonSpeed < minSpeed) {
                    minSpeed = wagonSpeed;
                }
            }
        }

        return hasWagons ? minSpeed : 0;
    }

    /**
     * Calculates the current maximum reachable speed of the train.
     *
     * <p>This value represents the maximum speed the train can realistically
     * achieve under current conditions, considering mechanical limits and
     * available power. It is safe to be used later by track (via) calculations
     * such as curves, gradients, or signaling.</p>
     *
     * <h3>How the calculation works</h3>
     *
     * <ol>
     * <li><b>Mechanical limits</b><br>
     * The base maximum speed is determined by the lowest maximum speed
     * among all locomotives and wagons. This represents the absolute
     * mechanical limit of the train.</li>
     *
     * <li><b>Total weight</b><br>
     * The current total train weight is calculated in tons and converted
     * to kilograms for power-to-weight calculations.</li>
     *
     * <li><b>Power-to-weight limitation</b><br>
     * The power-to-weight ratio (<code>kW/kg</code>) is used only as a
     * limiting factor. It does <b>not</b> define the speed by itself.
     * <ul>
     * <li>If the ratio is equal to or greater than the ideal reference
     * value, the train can reach its full mechanical maximum speed.</li>
     * <li>If the ratio is lower than the ideal value, the train is not
     * powerful enough to reach that maximum speed, and the reachable
     * speed is proportionally reduced.</li>
     * </ul>
     * </li>
     * </ol>
     *
     * <h3>Power-to-weight reference</h3>
     *
     * <p><code>IDEAL_POWER_TO_WEIGHT</code> represents the reference amount of
     * power per unit of weight required for a train to reach its mechanical
     * maximum speed. Typical values (order of magnitude):</p>
     *
     * <ul>
     * <li>Heavy freight trains: 4–6 kW/ton</li>
     * <li>Mixed traffic: 6–8 kW/ton</li>
     * <li>Passenger trains: 8–12 kW/ton</li>
     * <li>High-speed trains: 15+ kW/ton</li>
     * </ul>
     *
     * <p>In code, this is expressed in <code>kW/kg</code>
     * (e.g. 10 kW/ton = 0.01 kW/kg).</p>
     *
     * @return the maximum reachable speed of the train under current conditions,
     * in km/h. Returns 0 if no locomotives are present.
     */
    public double calculateCurrentMaxSpeed() {
        if (locomotives.isEmpty()) {
            return 0;
        }

        double locoMaxSpeed = Double.MAX_VALUE;
        for (Locomotive loco : locomotives) {
            if (loco.getModel().getMaxSpeed() < locoMaxSpeed) {
                locoMaxSpeed = loco.getModel().getMaxSpeed();
            }
        }
        double wagonSpeedLimit = lowestCurrentWagonMaxSpeed();
        double baseMaxSpeed = wagonSpeedLimit > 0
                ? Math.min(locoMaxSpeed, wagonSpeedLimit)
                : locoMaxSpeed;
        this.currentWeight = calculateCurrentWeight(); // t
        double totalWeightKg = this.currentWeight;

        double powerToWeight = totalPower / totalWeightKg; // kW/kg
        double powerFactor = (powerToWeight >= IDEAL_POWER_TO_WEIGHT)
                ? 1.0
                : (powerToWeight / IDEAL_POWER_TO_WEIGHT);
        return baseMaxSpeed * powerFactor;
    }

    private void updateCurrentStats() {
        this.totalPower = getTotalPower();
        this.currentWeight = calculateCurrentWeight();
        this.currentLength = calculateCurrentLength();
        this.currentMaxSpeed = calculateCurrentMaxSpeed();


        this.maxWeight = Math.max(this.maxWeight, this.currentWeight);
        this.maxSpeed = Math.max(this.maxSpeed, this.currentMaxSpeed);

    }

    /**
     * Check if train requires electric track
     */
    public boolean requiresElectrification() {
        return locomotives.stream().anyMatch(Locomotive::isElectric);
    }


    /**
     * This function will calculate the expected arrival at each facility and to each
     * segment, it will add it to each segment(if possible, the time that it enters the segment and the
     * time expected to go out of it) if not, it will throw and exception(or  something like that)
     *
     * @param startTime startTimeAtFacility f1
     * @return (LocalDateTime of arrival)
     */
    public LocalDateTime calculateArrivalTime(LocalDateTime startTime, boolean db_write) throws Exception {
        if (route == null)
            throw new IllegalStateException("No route found");

        LocalDateTime currentTime = startTime;
        List<Facility> facilities = this.getRoute().getPath().getRailFacilities();

        // Faz a referência local apontar para a lista da classe
        List<TimeInSegment> timeInSegmentsForTrain = this.calculatedSegmentTimes;

        currentTime = handleLoad(facilities.getFirst(), currentTime);
        facilities.getFirst().addTrainInFacility(this, startTime, currentTime); // currentTime = startTime + loadTime(if not 0)
        timeInSegmentsForTrain.addLast(new TimeInSegment(startTime, currentTime)); // ! time in first facility

        // Update Arrival for first station
        this.arrival.addEstimatedArrival(facilities.getFirst(), currentTime);

        for (int i = 0; i < facilities.size() - 1; i++)
        {
            Facility f1 = facilities.get(i);
            Facility f2 = facilities.get(i + 1);
            RailLine railLine = f1.getConnections().get(f2);
            if (railLine == null)
                throw new IllegalArgumentException("No railLine between " + f1.getName() + " and " + f2.getName());
            currentTime = settingTimeInEachSegment(timeInSegmentsForTrain, railLine.getRailSegments(), currentTime, db_write);
            LocalDateTime arrivalAtF2 = currentTime;
            currentTime = handleUnload(f2, currentTime);
            currentTime = handleLoad(f2, currentTime);
            f2.addTrainInFacility(this, arrivalAtF2, currentTime);
            timeInSegmentsForTrain.addLast(new TimeInSegment(arrivalAtF2, currentTime)); // time in the facility

            if (db_write)
            {
                Connection con = DatabaseConnection.getInstance();
                new DataBaseAccessService().addFacilityTrain(con, Integer.parseInt(trainId),
                        f2.getId(), arrivalAtF2, currentTime);
            }
            this.arrival.addEstimatedArrival(f2, currentTime);
        }
        if(route.getArrivalDay() == null)
            route.setArrivalDay(currentTime);

        // Final update to Arrival object to avoid NULL in UI
        this.arrival.setArrivalTime(currentTime);

        if (db_write)
        {
            Connection con = DatabaseConnection.getInstance();
            new DataBaseAccessService().addTrainEndTime(con, Integer.parseInt(trainId), currentTime);
        }
        return (currentTime);
    }


    /**
     * Function will loop through each of the segments in a line, trying to allocate it for the train,
     * in case it could not do it, it will throw a exception.
     *
     * @param timeSegTrain
     * @param segments
     * @param currentTime
     * @return end time of last segment
     */
    public LocalDateTime settingTimeInEachSegment(List<TimeInSegment> timeSegTrain, List<RailSegment> segments, LocalDateTime currentTime, boolean db_write) throws Exception{
        for (RailSegment segment : segments)
        {
            try {
                TimeInSegment interval = segment.setArrivalsAtSegment(currentTime, this);
                timeSegTrain.addLast(interval);
                currentTime = interval.getEndTime();
                if (db_write)
                {
                    Connection con = DatabaseConnection.getInstance();
                    new DataBaseAccessService().addTimeInSegment(con, Integer.parseInt(trainId),
                            Integer.parseInt(segment.getSegmentId()),
                            interval.getStartTime(), interval.getEndTime());
                }
                interval.addTrain(this);
            }
            catch (TracksFullException e)
            {
                dealNotPossibleSchedule(timeSegTrain);
                throw new TracksFullException("All tracks are full! Cannot schedule train!");
            }
        }
        return (currentTime);
    }

    /**
     * Function will go through each segment and facility taking this train away from all the
     * timeInSegment in them
     * @param timeInSegmentsForTrain
     */
    private void dealNotPossibleSchedule(List<TimeInSegment> timeInSegmentsForTrain) throws Exception {
        int j = 0;
        List<Facility> facilities = route.getPath().getRailFacilities();
        for (int i = 0; i < facilities.size() - 1; i++)
        {
            Facility f1 = facilities.get(i);
            Facility f2 = facilities.get(i + 1);
            RailLine railLine = f1.getConnections().get(f2);
            f1.getTrainListInGivenTime(timeInSegmentsForTrain.get(j++)).remove(this);
            if (j == timeInSegmentsForTrain.size())
                return;
            for (RailSegment s : railLine.getRailSegments())
            {
                if (j == timeInSegmentsForTrain.size())
                    return;
                TimeInSegment timeInSeg = timeInSegmentsForTrain.get(j++);
                s.getTrainListInGivenTime(timeInSeg).remove(this);

                if (s.getSiding() != null) {
                    s.getSiding().removeSidingUsage(timeInSeg);
                }
            }
        }
        Connection con = DatabaseConnection.getInstance();
        new DataBaseAccessService().cleanErrorInTrainScheduling(con, Integer.parseInt(trainId));
    }

    private LocalDateTime handleLoad(Facility facility, LocalDateTime currentTime) {
        List<Freight> freightsToLoad = new ArrayList<>();
        //if start facility of freight equals ours and freight isn't in train yet, then load
        for (Freight f : route.getFreights()) {
            if (f.getStartFacility().equals(facility) && !freightsOnTrain.contains(f)) freightsToLoad.add(f);
        }

        if (!freightsToLoad.isEmpty()) {
            int totalWagons = freightsToLoad.stream().mapToInt(f -> f.getWagons().size()).sum();
            currentTime = currentTime.plusMinutes(totalWagons * LOAD_AND_UNLOAD_TIME_PER_WAGON);
            freightsOnTrain.addAll(freightsToLoad);
            updateCurrentStats(); // update weight/length and speed with new freights
        }
        return currentTime;
    }

    private LocalDateTime handleUnload(Facility facility, LocalDateTime currentTime) {
        List<Freight> freightsToUnload = new ArrayList<>();
        //if end facility of freight equals ours then unload
        for (Freight f : freightsOnTrain) {
            if (f.getEndFacility().equals(facility)) freightsToUnload.add(f);
        }

        if (!freightsToUnload.isEmpty()) {
            int totalWagons = freightsToUnload.stream().mapToInt(f -> f.getWagons().size()).sum();
            currentTime = currentTime.plusMinutes(totalWagons * LOAD_AND_UNLOAD_TIME_PER_WAGON);
            freightsOnTrain.removeAll(freightsToUnload);
            updateCurrentStats(); // update weight/length and speed after unloading freight
        }
        return currentTime;
    }

    // ================= GAUGE LOGIC =================

    /**
     * Determines the gauge of the train based on its locomotives.
     */
    public String getTrainGauge() {
        if (locomotives.isEmpty()) return null;
        return locomotives.get(0).getModel().getGauge();
    }

    /**
     * Validates that Locomotives, Freight(Wagons), and the Route all share the same gauge.
     */
    public void validateGaugeConsistency() {
        if (locomotives.isEmpty()) return;

        String locoGauge = locomotives.get(0).getModel().getGauge();

        // 1. Check all locomotives match each other
        for (Locomotive l : locomotives) {
            if (!l.getModel().getGauge().equals(locoGauge)) {
                throw new IllegalStateException("Mixed gauges detected in Locomotives: " + l.getNumber());
            }
        }

        // 2. Check Freight (Wagons) matches Locomotives
        for (Freight freight : route.getFreights()) {
            if (freight != null && !freight.getWagons().isEmpty()) {
                String freightGauge = freight.getUniformGauge();
                if (freightGauge != null && !freightGauge.equals(locoGauge)) {
                    throw new IllegalStateException("Gauge Mismatch: Train is " + locoGauge +
                            " but Freight is " + freightGauge);
                }
            }
        }


        // 3. Check Route (RailLines) matches Train
        if (route != null) {
            Path path = route.getPath();
            List<Facility> facilities = path.getRailFacilities();
            for (int i = 0; i < facilities.size() - 1; i++) {
                Facility f1 = facilities.get(i);
                Facility f2 = facilities.get(i + 1);
                RailLine line = f1.getConnections().get(f2);

                if (line != null && !line.getGauge().equals(locoGauge)) {
                    throw new IllegalStateException("Gauge Mismatch: Train (" + locoGauge +
                            ") cannot travel on RailLine " + line.getRailLineId() +
                            " (" + line.getGauge() + ")");
                }
            }
        }
    }

    // Getters
    public Route getRoute() { return route; }
    public String getTrainId() { return trainId; }
    public Arrival getArrival() { return arrival; }
    public double getMaxSpeed() { return maxSpeed; }
    public double getCurrentMaxSpeed() { return currentMaxSpeed; }
    public double getCurrentLength() { return currentLength; }
    public double getCurrentWeight() { return currentWeight; }
    public double getMaxWeight() { return maxWeight; }
    public double getMaxLength() { return maxLength; }
    public List<Locomotive> getLocomotives() { return locomotives; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public List<Freight> getFreightsOnTrain() { return freightsOnTrain; }

    // Getter para a base de dados
    public List<TimeInSegment> getCalculatedSegmentTimes() { return calculatedSegmentTimes; }

    public void setRoute(Route route){
        this.route = route;
    }
    public void setDepartureTime(LocalDateTime departureTime){
        this.departureTime = departureTime;
    }

    public void setMaxLength(double maxLength){
        this.maxLength = maxLength;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id='" + trainId + '\'' +
                ", weight=" + maxWeight +
                ", length=" + maxLength +
                '}';
    }
}