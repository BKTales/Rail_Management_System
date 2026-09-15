package org.dei._Train;

public class LocomotiveModel {
    protected final double power; // kW
    protected final double length; // m
    protected final double width; // m
    protected final double height; // m
    protected final double weight; // tons
    protected final double maxSpeed; // km/h
    protected final String make;
    protected final String name;
    protected final LocomotiveType type;
    protected final String gauge;

    public LocomotiveModel(double power, double length, double width, double height, double weight, double maxSpeed, String make, String name,  LocomotiveType type) {
        this.power = power;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.maxSpeed = maxSpeed;
        this.make = make;
        this.name = name;
        this.type = type;
        this.gauge = "all";
    }

    public LocomotiveModel(double power, double length, double width, double height, double weight, double maxSpeed, String make, String name,  LocomotiveType type, String gauge) {
        this.power = power;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.maxSpeed = maxSpeed;
        this.make = make;
        this.name = name;
        this.type = type;
        this.gauge = gauge;
    }

    public double getPower() { return power; }
    public double getLength() { return length; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public double getMaxSpeed() { return maxSpeed; }
    public String getMake() { return make; }
    public String getName() { return name; }
    public String getGauge() { return gauge; }
    public LocomotiveType getType() { return type; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("\u001B[34m"); // Blue
        sb.append("┌──────────────────────────────────────────────┐\n");
        sb.append("│              LOCOMOTIVE MODEL               │\n");
        sb.append("└──────────────────────────────────────────────┘\n");
        sb.append("\u001B[0m"); // Reset

        // Basic Info
        sb.append(String.format("%sModel:%s %s\n", "\u001B[1m", "\u001B[0m", name));
        sb.append(String.format("%sManufacturer:%s %s\n", "\u001B[1m", "\u001B[0m", make));

        sb.append("\u001B[32m"); // Green
        sb.append("┌───────────── SPECIFICATIONS ──────────────┐\n");
        sb.append("\u001B[0m"); // Reset

        // Power and Performance
        sb.append(String.format("│ %sPower:%s %-8.0f kW %-20s │\n",
                "\u001B[33m", "\u001B[0m", power, ""));
        sb.append(String.format("│ %sWeight:%s %-7.1f t %-20s │\n",
                "\u001B[35m", "\u001B[0m", weight, ""));

        // Dimensions
        sb.append(String.format("│ %sLength:%s %-6.1f m %sWidth:%s %-4.1f m   │\n",
                "\u001B[36m", "\u001B[0m", length,
                "\u001B[36m", "\u001B[0m", width));
        sb.append(String.format("│ %sHeight:%s %-5.1f m %-25s │\n",
                "\u001B[36m", "\u001B[0m", height, ""));

        sb.append("\u001B[32m"); // Green
        sb.append("└──────────────────────────────────────────────┘\n");
        sb.append("\u001B[0m"); // Reset

        // Classification
        sb.append("\n");
        sb.append("\u001B[90m"); // Gray
        sb.append("Classification: ");
        if (power > 4000) {
            sb.append("\u001B[31mHEAVY HAUL"); // Red for high power
        } else if (power > 2000) {
            sb.append("\u001B[33mMEDIUM DUTY"); // Yellow for medium
        } else {
            sb.append("\u001B[36mLIGHT DUTY"); // Cyan for light
        }
        sb.append("\u001B[0m"); // Reset

        return sb.toString();
    }
}
