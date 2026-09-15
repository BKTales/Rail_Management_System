package org.dei._Train;

public class Locomotive {
    private int             serviceYear;
    private int             numberBogies;
    private double          maxSpeed;
    private String          operator;
    private String          number;
    private LocomotiveModel model;

    /**
     * Partial Constructor for Database Loading.
     */
    public Locomotive(String number) {
        this.number = number;
        this.operator = "Unknown";
        this.serviceYear = 0;
        this.maxSpeed = 0;
    }

    /**
     * Full Constructor.
     */
    public Locomotive(String number, int serviceYear, int numberBogies, String operator, LocomotiveModel model, double maxSpeed) {
        this.number = number;
        this.serviceYear = serviceYear;
        this.numberBogies = numberBogies;
        this.operator = operator;
        this.model = model;
        this.maxSpeed = maxSpeed;
    }

    public String getNumber() { return number; }

    public LocomotiveType getType() {
        return (model != null) ? model.getType() : LocomotiveType.DIESEL;
    }

    public String getOperator() { return operator; }
    public LocomotiveModel getModel() { return model; }
    public int getServiceYear() { return serviceYear; }
    public int getNumberBogies() { return numberBogies; }

    public double getWeight() { return (model != null) ? model.getWeight() : 0; }
    public double getPower() { return (model != null) ? model.getPower() : 0; }
    public double getMaxSpeed() { return maxSpeed; }
    public double getLength() {
        return (model != null) ? model.getLength() : 20.0;
    }

    public void setNumber(String number) { this.number = number; }
    public void setModel(LocomotiveModel model) { this.model = model; }
    public void setOperator(String operator) { this.operator = operator; }
    public void setServiceYear(int serviceYear) { this.serviceYear = serviceYear; }
    public void setNumberBogies(int numberBogies) { this.numberBogies = numberBogies; }
    public void setMaxSpeed(double maxSpeed) { this.maxSpeed = maxSpeed; }

    public boolean isElectric() { return getType() == LocomotiveType.ELECTRIC; }

    private boolean isOperational() {
        int currentYear = java.time.Year.now().getValue();
        return currentYear - serviceYear < 40;
    }

    private String truncate(String text, int length) {
        if (text == null) return "N/A";
        if (text.length() <= length) return text;
        return text.substring(0, length - 3) + "...";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String typeColor = isElectric() ? "\u001B[36m" : "\u001B[33m";
        String typeLabel = isElectric() ? "ELECTRIC LOCOMOTIVE" : "DIESEL LOCOMOTIVE";

        sb.append(typeColor)
                .append("  ╔══════════════════════════════════════════════╗\n")
                .append("  ║           ").append(String.format("%-25s", typeLabel)).append("          ║\n")
                .append("  ╚══════════════════════════════════════════════╝\n")
                .append("\u001B[0m");

        sb.append("  Identity: ").append(number).append("\n");
        sb.append("  Operator: ").append(operator).append("\n");

        if (model != null) {
            sb.append("  Model: ").append(model.getName()).append("\n");
            sb.append("  Power: ").append(model.getPower()).append(" kW\n");
        } else {
            sb.append("  Model: [Data Not Loaded]\n");
        }

        return sb.toString();
    }
}