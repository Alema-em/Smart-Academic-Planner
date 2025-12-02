package model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private String id;
    private List<Course> courses;

    public Student(String name, String id) {
        this.name = name;
        this.id = id;
        this.courses = new ArrayList<>();
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public List<Course> getCourses() { return courses; }

    public void addCourse(Course c) {
        courses.add(c);
    }

    public double calculateGpa() {
        double totalPoints = 0;
        int totalCredits = 0;
        for (Course c : courses) {
            String grade = c.getLetterGrade();
            double gp;
            switch (grade) {
                case "A": gp = 10; break;
                case "B": gp = 8; break;
                case "C": gp = 6; break;
                case "D": gp = 5; break;
                case "E": gp = 4; break;
                default:  gp = 0;
            }
            totalPoints += gp * c.getCredits();
            totalCredits += c.getCredits();
        }
        if (totalCredits == 0) return 0;
        return totalPoints / totalCredits;
    }
}
