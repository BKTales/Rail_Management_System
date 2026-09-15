package org.dei._Time;

import org.dei._Train.Train;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TimeInSegment implements Comparable<TimeInSegment> {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ArrayList<Train> trains;

    public TimeInSegment(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.trains = new ArrayList<>();
    }

    public LocalDateTime timeBeforeOrSame(LocalDateTime time1, LocalDateTime time2) {
        if (time1.isBefore(time2) || time1.equals(time2))
            return time1;
        return time2;
    }

    /**
     * Check if given time is >= startTime and <= endTime
     * @param time
     * @return (TRUE - is inside the interval)
     * @return (FALSE - is not inside the interval)
     */
    public boolean inBetween(LocalDateTime time){
        return time.isAfter(startTime) && time.isBefore(endTime);
    }

    /**
     * Function will check if there is any time intersection between other
     * and this time, if so it will create a variable with the time of the intersection
     * @param other
     * @return
     */
    public TimeInSegment hasIntersection(TimeInSegment other) {
        LocalDateTime start;
        LocalDateTime end;
        if (other.equals(this))
            return this;

        if (other.getStartTime().isAfter(this.endTime))
            return null;
        if (other.getEndTime().isBefore(this.startTime))
            return null;

        start = this.startTime;
        if (this.startTime.isBefore(other.getStartTime()))
            start = other.getStartTime();

        end = other.endTime;
        if (this.endTime.isBefore(other.getEndTime()))
            end = this.endTime;

        if (start.equals(end))
            return null;
        return new TimeInSegment(start, end);
    }

    /**
     * Functions will separate the collision time on timeEntered with
     * timeInMap and return A group of TimeInSegments which have the
     * remainer of the time entered.
     *
     * @param timeEntered
     * @param timeCollided
     * @return group of TimeInSegments
     */
    public CollisionTimes separateCollisionTime(TimeInSegment timeEntered, TimeInSegment timeCollided){
        CollisionTimes c =  new CollisionTimes();
        c.collided = timeCollided; c.before = null; c.after = null;

        // left
        if (timeCollided.getStartTime().equals(timeEntered.getStartTime()))
            c.before = null;
        else if (timeCollided.getStartTime().isBefore(timeEntered.getStartTime()))
            c.before = new TimeInSegment(timeCollided.getStartTime(), timeEntered.getStartTime());
        else if (timeEntered.getStartTime().isBefore(timeCollided.getStartTime()))
            c.before = new TimeInSegment(timeEntered.getStartTime(), timeCollided.getStartTime());
        // right
        if (timeCollided.getEndTime().equals(timeEntered.getEndTime()))
            c.after = null;
        else if (timeCollided.getEndTime().isBefore(timeEntered.getEndTime()))
            c.after = new TimeInSegment(timeCollided.getEndTime(), timeEntered.getEndTime());
        else if (timeEntered.getEndTime().isBefore(timeCollided.getEndTime()))
            c.after = new TimeInSegment(timeEntered.getEndTime(), timeCollided.getEndTime());
        return (c);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Train> getTrains() {
        return trains;
    }

    public void addTrain(Train train) {
        Train trainCopy = new Train(train);
        trains.add(trainCopy);
    }

    public Train getTrainById(String id){
        for (Train train : trains) {
            if (train.getTrainId().equals(id))
                return train;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeInSegment) {
            TimeInSegment time = (TimeInSegment) obj;
            return startTime.equals(time.startTime) && endTime.equals(time.endTime);
        }
        return false;
    }

    /**
     * @param o other segment
     * @return  0, in the track at the same time
     * @return -1, other time end before the current one
     * @return  1, other time start before the current one
     */
    @Override
    public int compareTo(TimeInSegment o) {

        if (startTime.equals(o.startTime) &&  endTime.equals(o.endTime)) // exactly the same
        {
            //System.out.println("5. Returning = 0");
            return 0;
        }
        if (startTime.isBefore(o.startTime) && endTime.isAfter(o.startTime)) // [s1  --- (s2) ---- e1] s2 between s1 and e1
        {
            //System.out.println("1. Returning = 0");
            return 0;
        }
        if (startTime.isBefore(o.endTime) && endTime.isAfter(o.endTime)) //  [s1  --- (e2) ---- e1] e2 between s1 and e1
        {
            //System.out.println("2. Returning = 0");
            return 0;
        }
        if (startTime.isAfter(o.endTime) || startTime.equals(o.endTime) ) //  [s1  --- e1/(s2) ---- e2] start after or in e1
        {
            //System.out.println("3. Returning = 1");
            return 1;
        }
        if (endTime.isBefore(o.startTime) || endTime.equals(o.startTime)) //  [s2 ------  (e2)/s1  ---- e1] end before or in s1
        {
            //System.out.println("4. Returning = -1");
            return -1;
        }
        return 1;
    }

    @Override
    public String toString() {
        return  startTime.toString() + " " + endTime.toString();
    }
}