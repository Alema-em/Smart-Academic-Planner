package ui;

import model.*;
import exceptions.InvalidMarksException;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// ---------- Background panel ----------
class BackgroundPanel extends JPanel {
    private final Image bgImage;

    public BackgroundPanel(ImageIcon icon) {
        if (icon != null) {
            this.bgImage = icon.getImage();
        } else {
            this.bgImage = null; 
        }
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

// ---------- Main GUI ----------
public class PlannerGUI extends JFrame {

    // --- Color theme ---
    private static final Color BG         = new Color(24, 26, 32);
    private static final Color FG         = new Color(240, 240, 244);
    private static final Color PANEL      = new Color(38, 41, 55);
    private static final Color ACC_BLUE   = new Color(44, 130, 201);
    private static final Color ACC_GREEN  = new Color(72, 201, 176);
    private static final Color ACC_RED    = new Color(220, 80, 80);
    private static final Color ACC_YELLOW = new Color(250, 230, 110);
    private static final Color ACC_ORANGE = new Color(255, 185, 77);
    private static final Color ACC_PURPLE = new Color(150, 110, 220);
    private static final Color DARK_BG    = new Color(22, 24, 32);

    // --- Shared UI helpers ---
    private Font uiFont(int size, boolean bold) {
        return new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size);
    }

    private JTextField niceField() {
        // narrower fields
        JTextField tf = new JTextField(14);
        tf.setFont(uiFont(15, false));
        tf.setBackground(new Color(40, 43, 55, 230));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return tf;
    }

    private JLabel niceLabel(String text) {
    JLabel lbl = new JLabel(text, SwingConstants.CENTER);
    lbl.setFont(uiFont(15, true));
    lbl.setForeground(Color.WHITE);
    return lbl;
}


    private JButton niceButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(uiFont(15, true));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

    // --- Model ---
    private Student student;
    private DefaultTableModel coursesModel;
    private JTable coursesTable;
    private DefaultTableModel assessmentsModel;
    private JTable assessmentsTable;
    private JComboBox<String> courseSelector;
    private JLabel gpaLabel;

    public PlannerGUI() {
        askForStudentInfo();
        setTitle("Smart Academic Planner & Grade Tracker");
        setSize(960, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        buildUI();
    }

    // ---------- Login Dialog ----------
    private void askForStudentInfo() {
        LoginDialog dialog = new LoginDialog(this);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) {
            System.exit(0);
        }
        student = new Student(dialog.getStudentName(), dialog.getBitsId());
    }

    // ---------- Main layout ----------
    private void buildUI() {
        setLayout(new BorderLayout());

        JLabel studentInfoLabel =
                new JLabel(student.getName() + " (" + student.getId() + ")", JLabel.RIGHT);
        studentInfoLabel.setFont(font(16));
        studentInfoLabel.setForeground(FG);
        studentInfoLabel.setBorder(
                BorderFactory.createEmptyBorder(10, 40, 10, 40));
        add(studentInfoLabel, BorderLayout.SOUTH);
        UIManager.put("TabbedPane.selected", new Color(255, 0, 0));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(font(16));
        tabs.setBackground(PANEL);
        tabs.setForeground(FG);

        tabs.addTab("Courses",     buildCoursesPanel());
        tabs.addTab("Assessments", buildAssessmentsPanel());
        tabs.addTab("Dashboard",   buildDashboardPanel());

        add(tabs, BorderLayout.CENTER);
    }

    // ---------- Courses Tab ----------
private JPanel buildCoursesPanel() {
    ImageIcon bgIcon = safeIcon("/images/Dash.png", 960, 660);
    BackgroundPanel bgPanel = new BackgroundPanel(bgIcon);
    bgPanel.setLayout(new BorderLayout());
    
   bgPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));


    JPanel card = new JPanel(new BorderLayout());
card.setOpaque(false);                             
card.setBorder(BorderFactory.createEmptyBorder(
        40,   
        80,   
        40,   
        80    
));

   


   
    JLabel title = new JLabel("Courses", SwingConstants.LEFT);
    title.setFont(uiFont(20, true));
    title.setForeground(Color.WHITE);
    title.setOpaque(true);
    title.setBackground(new Color(0, 32, 64));
    title.setBorder(BorderFactory.createEmptyBorder(8, 50, 8, 0)); 
    card.add(title, BorderLayout.NORTH);

    // Table
    String[] cols = {"Code", "Title", "Credits", "Instructor"};
    coursesModel = new DefaultTableModel(cols, 0);
    coursesTable = new JTable(coursesModel);

    coursesTable.setFont(uiFont(14, false));
    coursesTable.setRowHeight(26);
    coursesTable.setBackground(new Color(245, 245, 245));   
    coursesTable.setForeground(Color.BLACK);
    coursesTable.setSelectionBackground(ACC_BLUE);
    coursesTable.setSelectionForeground(Color.WHITE);
    coursesTable.setGridColor(new Color(200, 200, 200));

    DefaultTableCellRenderer center = new DefaultTableCellRenderer();
    center.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 0; i < coursesTable.getColumnCount(); i++) {
        coursesTable.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    JTableHeader header = coursesTable.getTableHeader();
    header.setFont(uiFont(15, true));
    header.setBackground(new Color(25, 27, 35));
    header.setForeground(Color.WHITE);

    JScrollPane scroll = new JScrollPane(coursesTable);
    scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    card.add(scroll, BorderLayout.CENTER);

    
    JPanel form = new JPanel(new GridBagLayout());
    form.setOpaque(false);
    form.setBorder(BorderFactory.createEmptyBorder(
        0,   
        180, 
        20,  
        260  
));
    form.setBackground(new Color(0, 32, 64));  
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(6, 8, 6, 8);
    gbc.anchor = GridBagConstraints.EAST;

    JLabel codeL    = niceLabel("Code:");
    JLabel titleL   = niceLabel("Title:");
    JLabel creditsL = niceLabel("Credits:");
    JLabel instL    = niceLabel("Instructor:");

    
    JTextField codeField       = new JTextField(16);
    JTextField titleField      = new JTextField(16);
    JTextField creditsField    = new JTextField(16);
    JTextField instructorField = new JTextField(16);
    for (JTextField tf : new JTextField[]{codeField, titleField, creditsField, instructorField}) {
        tf.setFont(uiFont(15, false));
        tf.setBackground(new Color(40, 43, 55)); 
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
    }

    JButton addCourseBtn = niceButton("Add Course", new Color(44, 130, 201));

    
gbc.gridy = 0;
gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;  form.add(codeL, gbc);
gbc.gridx = 2; gbc.anchor = GridBagConstraints.WEST;  form.add(codeField, gbc);

gbc.gridy = 1;
gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;  form.add(titleL, gbc);
gbc.gridx = 2; gbc.anchor = GridBagConstraints.WEST;  form.add(titleField, gbc);

gbc.gridy = 2;
gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;  form.add(creditsL, gbc);
gbc.gridx = 2; gbc.anchor = GridBagConstraints.WEST;  form.add(creditsField, gbc);

gbc.gridy = 3;
gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;  form.add(instL, gbc);
gbc.gridx = 2; gbc.anchor = GridBagConstraints.WEST;  form.add(instructorField, gbc);


gbc.gridy = 4;
gbc.gridx = 1;                          
gbc.gridwidth = 2;                     
gbc.anchor = GridBagConstraints.CENTER;   
gbc.insets = new Insets(12, 90, 0, 0);   
form.add(addCourseBtn, gbc);




    card.add(form, BorderLayout.SOUTH);
    bgPanel.add(card, BorderLayout.CENTER);

    addCourseBtn.addActionListener(e -> {
        try {
            String code        = codeField.getText().trim();
            String courseTitle = titleField.getText().trim();
            int credits        = Integer.parseInt(creditsField.getText().trim());
            String inst        = instructorField.getText().trim();

            if (code.isEmpty() || courseTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Code and Title required.");
                return;
            }
            Course c = new Course(code, courseTitle, credits, inst);
            student.addCourse(c);
            coursesModel.addRow(new Object[]{code, courseTitle, credits, inst});
            if (courseSelector != null) {
                courseSelector.addItem(code);
            }
            codeField.setText("");
            titleField.setText("");
            creditsField.setText("");
            instructorField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits must be a number.");
        }
    });

    return bgPanel;
}


    // ---------- Assessments Tab ----------
    private JPanel buildAssessmentsPanel() {
        ImageIcon bgIcon = safeIcon("/images/Dash.png", 960, 660);
        BackgroundPanel bgPanel = new BackgroundPanel(bgIcon);
        
        bgPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 40, 80)); 


        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(10, 12, 20, 200));
        
        card.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.EAST;

        String[] types = {"Quiz 1", "Quiz 2", "Assignment", "Midsem", "Compre"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setFont(uiFont(15, false));

        JTextField maxField    = niceField();
        JTextField weightField = niceField();
        JTextField dueField    = niceField();
        JTextField marksField  = niceField();
        dueField.setText("2025-12-10");

        JLabel courseLabel   = niceLabel("Course:");
        courseSelector       = new JComboBox<>();
        courseSelector.setFont(uiFont(15, false));

        JLabel typeLabel     = niceLabel("Type:");
        JLabel maxLabel      = niceLabel("Max Marks:");
        JLabel weightLabel   = niceLabel("Weight (%):");
        JLabel dueLabel      = niceLabel("Due Date:");
        JLabel obtainedLabel = niceLabel("Obtained:");
        JButton addBtn       = niceButton("Add / Update", ACC_GREEN);

        gbc.gridx = 0; gbc.gridy = 0; form.add(courseLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(courseSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; form.add(typeLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(typeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; form.add(maxLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(maxField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; form.add(weightLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(weightField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST; form.add(dueLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(dueField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST; form.add(obtainedLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(marksField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; form.add(new JLabel(), gbc);
        gbc.gridx = 1; form.add(addBtn, gbc);

        card.add(form, BorderLayout.NORTH);

        String[] cols = {"Type", "MaxMarks", "Obtained", "Weight", "DueDate"};
        assessmentsModel = new DefaultTableModel(cols, 0);
        assessmentsTable = new JTable(assessmentsModel);

        assessmentsTable.setFont(uiFont(14, false));
        assessmentsTable.setRowHeight(30);       
        assessmentsTable.setBackground(new Color(40, 43, 55, 230));
        assessmentsTable.setForeground(Color.WHITE);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < assessmentsTable.getColumnCount(); i++) {
            assessmentsTable.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JTableHeader header = assessmentsTable.getTableHeader();
        header.setFont(uiFont(15, true));
        header.setBackground(new Color(25, 27, 35));
        header.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(assessmentsTable);
        scroll.getViewport().setBackground(new Color(0, 0, 0, 0));
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        card.add(scroll, BorderLayout.CENTER);
        bgPanel.add(card, BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try {
                String courseCode = (String) courseSelector.getSelectedItem();
                if (courseCode == null) {
                    JOptionPane.showMessageDialog(this, "Add a course first.");
                    return;
                }
                Course c = findCourseByCode(courseCode);
                if (c == null) return;

                String type    = (String) typeBox.getSelectedItem();
                String name    = type;
                double max     = Double.parseDouble(maxField.getText().trim());
                double weight  = Double.parseDouble(weightField.getText().trim());
                LocalDate due  = LocalDate.parse(dueField.getText().trim());
                String marks   = marksField.getText().trim();
                Double obtained = marks.isEmpty() ? null : Double.parseDouble(marks);

                if (obtained != null) validateMarks(obtained, max);

                int selectedRow = assessmentsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Assessment updated = "Assignment".equals(type)
                            ? new Assignment(name, max, weight, due)
                            : new Exam(name, max, weight, due, type);
                    updated.setObtainedMarks(obtained);
                    c.getAssessments().set(selectedRow, updated);
                } else {
                    Assessment a = "Assignment".equals(type)
                            ? new Assignment(name, max, weight, due)
                            : new Exam(name, max, weight, due, type);
                    a.setObtainedMarks(obtained);
                    c.addAssessment(a);
                }
                refreshAssessmentsTable(c);
                assessmentsTable.clearSelection();
                maxField.setText("");
                weightField.setText("");
                dueField.setText("2025-12-10");
                marksField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Marks/weight/max must be numbers.");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid date format (use yyyy-MM-dd).");
            } catch (InvalidMarksException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        courseSelector.addActionListener(e -> {
            String code = (String) courseSelector.getSelectedItem();
            Course c = findCourseByCode(code);
            if (c != null) refreshAssessmentsTable(c);
        });

        return bgPanel;
    }

    private Course findCourseByCode(String code) {
        if (code == null) return null;
        for (Course c : student.getCourses()) {
            if (c.getCode().equals(code)) return c;
        }
        return null;
    }

    private void refreshAssessmentsTable(Course c) {
        assessmentsModel.setRowCount(0);
        for (Assessment a : c.getAssessments()) {
            assessmentsModel.addRow(new Object[]{
                    a.getType(),
                    a.getMaxMarks(),
                    a.getObtainedMarks(),
                    a.getWeight(),
                    a.getDueDate()
            });
        }
    }

    private void validateMarks(double obtained, double max)
            throws InvalidMarksException {
        if (obtained < 0 || obtained > max) {
            throw new InvalidMarksException("Marks must be between 0 and " + max);
        }
    }

    // ---------- Dashboard Tab ----------
    private JPanel buildDashboardPanel() {
    // Background with image
    ImageIcon bgIcon = safeIcon("/images/Dash.png", 960, 660);
    BackgroundPanel bgPanel = new BackgroundPanel(bgIcon);
    bgPanel.setLayout(new BorderLayout());
    
    bgPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 40, 80));

    
    JPanel card = new JPanel(new BorderLayout());
    card.setOpaque(true);
    card.setBackground(new Color(0, 0, 0, 160));               
    card.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));

    String[] cols = {"Course", "Credits", "Percentage", "Grade"};
    DefaultTableModel dashModel = new DefaultTableModel(cols, 0);
    JTable dashTable = new JTable(dashModel);

    dashTable.setFont(font(15));
    dashTable.setRowHeight(27);
    dashTable.setBackground(PANEL);
    dashTable.setForeground(FG);
    dashTable.setSelectionBackground(ACC_BLUE);
    dashTable.setSelectionForeground(Color.WHITE);

    DefaultTableCellRenderer center = new DefaultTableCellRenderer();
    center.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 1; i <= 3; i++) {
        dashTable.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    dashTable.getColumnModel()
             .getColumn(3)
             .setCellRenderer(new GradeCellRenderer());
    dashTable.getTableHeader().setFont(font(16, true));
    dashTable.getTableHeader().setBackground(DARK_BG);
    dashTable.getTableHeader().setForeground(FG);

    JScrollPane scroll = new JScrollPane(dashTable);
    scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));//
    card.add(scroll, BorderLayout.CENTER);

    gpaLabel = new JLabel("CGPA: 0.0");
    gpaLabel.setOpaque(true);
    gpaLabel.setHorizontalAlignment(SwingConstants.CENTER);
    gpaLabel.setFont(font(34, true));
    gpaLabel.setPreferredSize(new Dimension(0, 90));
    gpaLabel.setBorder(BorderFactory.createEmptyBorder(24, 12, 24, 12));
    gpaLabel.setBackground(PANEL);
    gpaLabel.setForeground(ACC_BLUE);
    card.add(gpaLabel, BorderLayout.SOUTH);

    JButton refreshBtn = niceButton("Refresh", ACC_BLUE);
    refreshBtn.addChangeListener(e -> {
        ButtonModel m = refreshBtn.getModel();
        if (m.isPressed())
            refreshBtn.setBackground(ACC_BLUE.darker());
        else if (m.isRollover())
            refreshBtn.setBackground(ACC_BLUE.brighter());
        else
            refreshBtn.setBackground(ACC_BLUE);
    });
    card.add(refreshBtn, BorderLayout.NORTH);

    refreshBtn.addActionListener(e -> {
        dashModel.setRowCount(0);
        for (Course c : student.getCourses()) {
            double pct   = c.calculateCoursePercentage();
            String grade = c.getLetterGrade();
            dashModel.addRow(new Object[]{
                    c.getCode() + " - " + c.getTitle(),
                    c.getCredits(),
                    String.format("%.1f", pct),
                    grade
            });
        }
        double gpa = student.calculateGpa();
        gpaLabel.setText("CGPA: " + String.format("%.2f", gpa));
    });

    bgPanel.add(card, BorderLayout.CENTER);
    return bgPanel;
}

    // ---------- Generic helpers ----------
    private Font font(int sz) { return new Font("Segoe UI", Font.PLAIN, sz); }
    private Font font(int sz, boolean bold) {
        return new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, sz);
    }

    private JTextField textField() {
        JTextField tf = new JTextField();
        tf.setBackground(PANEL);
        tf.setForeground(FG);
        tf.setCaretColor(ACC_BLUE);
        tf.setFont(font(15));
        tf.setBorder(BorderFactory.createLineBorder(ACC_BLUE, 1, true));
        return tf;
    }

    private JTextField textField(String txt) {
        JTextField tf = textField();
        tf.setText(txt);
        return tf;
    }

    private JLabel formLabel(String text, String iconPath) {
        ImageIcon icon = safeIcon(iconPath, 20, 20);
        JLabel lbl = new JLabel(text, icon, SwingConstants.RIGHT);
        lbl.setFont(font(15, true));
        lbl.setForeground(FG);
        return lbl;
    }

    private JButton iconButton(String text, String iconPath, Color bg) {
        JButton btn = new JButton(text, safeIcon(iconPath, 20, 20));
        btn.setFont(font(15, true));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));
        return btn;
    }

    private ImageIcon safeIcon(String path, int w, int h) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Image not found: " + path);
                return null;
            }
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage();
            return new ImageIcon(
                    img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------- Grade renderer ----------
    private class GradeCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {
            super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
            setHorizontalAlignment(SwingConstants.CENTER);
            if (value != null) {
                Color bg = PANEL;
                switch (value.toString()) {
                    case "A": bg = ACC_GREEN;  break;
                    case "B": bg = ACC_BLUE;   break;
                    case "C": bg = ACC_YELLOW; break;
                    case "D": bg = ACC_ORANGE; break;
                    case "E": bg = ACC_PURPLE; break;
                    case "F": bg = ACC_RED;    break;
                    default:  bg = Color.WHITE;
                }
                setBackground(bg);
                setForeground(Color.BLACK);
                if (isSelected) setBackground(bg.darker());
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlannerGUI gui = new PlannerGUI();
            gui.setVisible(true);
        });
    }
}
