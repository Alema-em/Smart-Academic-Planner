package model;

import java.time.LocalDate;

public class Exam extends Assessment {
    private String examType; // "Midsem", "Compre"

    public Exam(String name, double maxMarks, double weight, LocalDate dueDate, String examType) {
        super(name, maxMarks, weight, dueDate);
        this.examType = examType;
    }

    public String getExamType() { return examType; }

    @Override
    public String getType() {
        return "Exam(" + examType + ")";
    }
}

