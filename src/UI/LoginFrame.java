package UI;

import Repository.StudentRepository;
import model.Student;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final StudentRepository studentRepository = new StudentRepository();
    private final JTextField idField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginFrame() {
        setTitle("University Register - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBorder(BorderFactory.createEmptyBorder(35, 45, 30, 45));
        root.setBackground(Color.WHITE);

        JLabel title = new JLabel("UNIVERSITY REGISTER", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(35, 65, 110));

        JLabel subtitle = new JLabel("Student Login", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 5));
        header.setOpaque(false);
        header.add(title);
        header.add(subtitle);

        JPanel form = new JPanel(new GridLayout(5, 1, 0, 8));
        form.setOpaque(false);
        form.add(new JLabel("Student ID"));
        form.add(idField);
        form.add(new JLabel("Password"));
        form.add(passwordField);

        JButton loginButton = new JButton("LOGIN");
        JButton registerButton = new JButton("Create an account");
        loginButton.setPreferredSize(new Dimension(120, 35));
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setForeground(new Color(35, 65, 110));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttons = new JPanel(new GridLayout(2, 1, 0, 8));
        buttons.setOpaque(false);
        buttons.add(loginButton);
        buttons.add(registerButton);
        form.add(buttons);

        loginButton.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());
        registerButton.addActionListener(e -> {
            new RegisterFrame(this).setVisible(true);
            setVisible(false);
        });

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void login() {
        String id = idField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (id.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Student ID and Password.", "Login", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = studentRepository.loginAndGetStudent(id, password);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID or Password.", "Login failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new StudentDashboard(student, this).setVisible(true);
        setVisible(false);
    }

    public void clearFields() {
        idField.setText("");
        passwordField.setText("");
    }
}
