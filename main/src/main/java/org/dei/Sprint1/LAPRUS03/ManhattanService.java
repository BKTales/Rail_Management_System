package org.dei.Sprint1.LAPRUS03;

public class ManhattanService {
    private double manhattanDistance;

    public double manhattan(double xVariation, double yVariation) {
        manhattanDistance = Math.abs(xVariation) + Math.abs(yVariation);
        return manhattanDistance;
    }

    public double speedToManhattan(double speed) {
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed must be positive");
        }
        return manhattanDistance / speed;
    }

    public double getLastCalculatedDistance() {
        return manhattanDistance;
    }
}