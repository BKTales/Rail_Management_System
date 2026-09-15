package org.dei._Train;

public class EletricLocomotiveModel extends LocomotiveModel {
    private final double voltage;
    private final double frequency;

    public EletricLocomotiveModel(double power, double length, double width, double height, double weight,
                                  double maxSpeed, String make, String name, double voltage, double frequency, String gauge) {
        super(power, length, width, height, weight, maxSpeed, make, name, LocomotiveType.ELECTRIC, gauge);
        this.frequency = frequency;
        this.voltage = voltage;
    }

    public double getVoltage() { return voltage; }
    public double getFrequency() { return frequency; }

    @Override
    public String toString() {
        return "EletricLocomotiveModel{" +
                "voltage=" + voltage +
                ", frequency=" + frequency +
                '}' + super.toString();
    }
}
