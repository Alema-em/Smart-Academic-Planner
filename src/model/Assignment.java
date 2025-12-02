package model;

import java.time.LocalDate;

public class Assignment extends Assessment {

    public Assignment(String name, double maxMarks, double weight, LocalDate dueDate) {
        super(name, maxMarks, weight, dueDate);
    }

    @Override
    public String getType() {
        return "Assignment";
    }
}
