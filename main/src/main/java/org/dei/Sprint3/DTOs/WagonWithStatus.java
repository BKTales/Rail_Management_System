package org.dei.Sprint3.DTOs;

import org.dei._Train.Wagon;

/**
 * Wrapper class that contains a wagon and its transit status information
 */
public class WagonWithStatus {
    private final Wagon wagon;
    private final String transitStatus; // "IN_TRANSIT" or "PARKED"
    private final String locationInfo; // Final destination or current station
    private final Double distanceFromStart; // Distance from route start (only for parked wagons)
    
    public WagonWithStatus(Wagon wagon, String transitStatus, String locationInfo, Double distanceFromStart){
        this.wagon = wagon;
        this.transitStatus = transitStatus;
        this.locationInfo = locationInfo;
        this.distanceFromStart = distanceFromStart;
    }
    
    public Wagon getWagon(){
        return wagon;
    }
    
    public String getTransitStatus(){
        return transitStatus;
    }
    
    public boolean isInTransit(){
        return "IN TRANSIT".equals(transitStatus);
    }
    
    public boolean isParked(){
        return "PARKED".equals(transitStatus);
    }
    
    public String getLocationInfo(){
        return locationInfo;
    }
    
    public Double getDistanceFromStart(){
        return distanceFromStart;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Wagon ").append(wagon.getWagonId());
        sb.append(" - Status: ").append(transitStatus);
        sb.append(" - Location: ").append(locationInfo);
        if(distanceFromStart != null){
            sb.append(" - Distance: ").append(String.format("%.2f km", distanceFromStart));
        }
        return sb.toString();
    }
}