package ui;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {

    private JTextField nameField;
    private JTextField idField;
    private boolean confirmed = false;

    public LoginDialog(Frame parent) {
        super(parent, "Student Login", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        JLabel bg = new JLabel();
        bg.setLayout(null); 
        ImageIcon img = loadImage("/images/login.png"); 
        if (img != null) {
            bg.setIcon(img);
            setContentPane(bg);
            setSize(img.getIconWidth(), img.getIconHeight());
        } else {
            
            setContentPane(bg);
            setSize(800, 450);
            bg.setBackground(new Color(30, 32, 40));
            bg.setOpaque(true);
        }

        
        nameField = new JTextField();
        idField   = new JTextField();

        styleField(nameField);
        styleField(idField);

        JLabel nameLabel = new JLabel("NAME");
        JLabel idLabel   = new JLabel("BITS ID");
        styleLabel(nameLabel);
        styleLabel(idLabel);

        JButton okButton     = new JButton("OK");
        
        styleButton(okButton);
        
        
        

 int fieldWidth  = 260;
int fieldHeight = 36;
int labelX = 520;
int fieldX = 520;
int nameY = 210;
int idY   = 270;

nameLabel.setBounds(labelX, nameY - 26, fieldWidth, 22);
nameField.setBounds(fieldX, nameY, fieldWidth, fieldHeight);
idLabel.setBounds(labelX, idY - 26, fieldWidth, 22);
idField.setBounds(fieldX, idY, fieldWidth, fieldHeight);


int buttonWidth  = 120;
int buttonHeight = 36;
int okX = fieldX + (fieldWidth - buttonWidth) / 2;
int okY = idY + 60;

okButton.setBounds(okX, okY, buttonWidth, buttonHeight);



        bg.add(nameLabel);
        bg.add(nameField);
        bg.add(idLabel);
        bg.add(idField);
        bg.add(okButton);
        

        okButton.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() ||
                idField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Name and BITS ID are required.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                confirmed = true;
                dispose();
            }
        });

        

        setLocationRelativeTo(parent);
    }

    private void styleField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tf.setBackground(new Color(40, 43, 55, 220));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
    }

    private void styleLabel(JLabel lbl) {
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(255, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private ImageIcon loadImage(String path) {
        try {
            var url = getClass().getResource(path);
            if (url == null) return null;
            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(
                    900, 500, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getStudentName() {
        return nameField.getText().trim();
    }

    public String getBitsId() {
        return idField.getText().trim();
    }
}

