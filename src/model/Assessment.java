package model;

import java.time.LocalDate;

public abstract class Assessment {
    protected String name;
    protected double maxMarks;
    protected Double obtainedMarks;
    protected double weight;
    protected LocalDate dueDate;

    public Assessment(String name, double maxMarks, double weight, LocalDate dueDate) {
        this.name = name;
        this.maxMarks = maxMarks;
        this.weight = weight;
        this.dueDate = dueDate;
        this.obtainedMarks = null;
    }

    public String getName() { return name; }
    public Double getObtainedMarks() { return obtainedMarks; }
    public void setObtainedMarks(Double obtainedMarks) { this.obtainedMarks = obtainedMarks; }
    public double getWeight() { return weight; }
    public LocalDate getDueDate() { return dueDate; }

    public double getMaxMarks() { return maxMarks; }

    public boolean isCompleted() { return obtainedMarks != null; }

    public double getWeightedScore() {
        if (obtainedMarks == null) return 0.0;
        return (obtainedMarks / maxMarks) * weight;
    }

    public abstract String getType();
}
