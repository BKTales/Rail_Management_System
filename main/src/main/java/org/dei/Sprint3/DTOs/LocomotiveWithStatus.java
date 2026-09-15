package org.dei.Sprint3.DTOs;

import org.dei._Train.Locomotive;

/**
 * Wrapper class that contains a locomotive and its transit status information
 */
public class LocomotiveWithStatus{
    private final Locomotive locomotive;
    private final String transitStatus; // "IN_TRANSIT" or "PARKED"
    private final String locationInfo; // Final destination or current station
    private final Double distanceFromStart; // Distance from route start (only for parked locomotives)
    
    public LocomotiveWithStatus(Locomotive locomotive, String transitStatus, String locationInfo, Double distanceFromStart){
        this.locomotive = locomotive;
        this.transitStatus = transitStatus;
        this.locationInfo = locationInfo;
        this.distanceFromStart = distanceFromStart;
    }
    
    public Locomotive getLocomotive(){
        return locomotive;
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
        sb.append("Locomotive ").append(locomotive.getNumber());
        sb.append(" - Status: ").append(transitStatus);
        sb.append(" - Location: ").append(locationInfo);
        if(distanceFromStart != null){
            sb.append(" - Distance: ").append(String.format("%.2f km", distanceFromStart));
        }
        return sb.toString();
    }
}
