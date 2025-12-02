package model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String code;
    private String title;
    private int credits;
    private String instructor;
    private List<Assessment> assessments;

    public Course(String code, String title, int credits, String instructor) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.instructor = instructor;
        this.assessments = new ArrayList<>();
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructor() { return instructor; }
    public List<Assessment> getAssessments() { return assessments; }

    public void addAssessment(Assessment a) {
        assessments.add(a);
    }

    public double calculateCoursePercentage() {
        double sum = 0.0;
        for (Assessment a : assessments) {
            sum += a.getWeightedScore();
        }
        return sum; // out of 100
    }

    public String getLetterGrade() {
        double p = calculateCoursePercentage();
        if (p >= 85) return "A";
        if (p >= 75) return "B";
        if (p >= 65) return "C";
        if (p >= 55) return "D";
        if (p >= 45) return "E";
        return "F";
    }
}
