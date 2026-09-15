package org.dei._Train;

public class DieselLocomotiveModel extends LocomotiveModel {
    private final double fuelCapacity;

    public DieselLocomotiveModel(double power, double length, double width, double height, double weight,
                                    double maxSpeed, String make, String name, double fuelCapacity, String gauge) {
        super(power, length, width, height, weight, maxSpeed, make, name, LocomotiveType.DIESEL, gauge);
        this.fuelCapacity = fuelCapacity;
    }

    public double getFuelCapacity() { return fuelCapacity; }

    @Override
    public String toString() {
        return "DieselLocomotiveModel{" +
                "fuelCapacity=" + fuelCapacity +
                super.toString() + '}';
    }
}
