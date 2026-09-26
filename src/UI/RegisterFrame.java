package UI;

import Repository.StudentRepository;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final StudentRepository studentRepository = new StudentRepository();
    private final LoginFrame loginFrame;
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmField = new JPasswordField();

    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        setTitle("University Register - Register");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(480, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(BorderFactory.createEmptyBorder(30, 45, 30, 45));
        root.setBackground(Color.WHITE);

        JLabel title = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(35, 65, 110));

        JPanel form = new JPanel(new GridLayout(10, 1, 0, 5));
        form.setOpaque(false);
        form.add(new JLabel("Student ID"));
        form.add(idField);
        form.add(new JLabel("Name"));
        form.add(nameField);
        form.add(new JLabel("Email"));
        form.add(emailField);
        form.add(new JLabel("Password"));
        form.add(passwordField);
        form.add(new JLabel("Confirm Password"));
        form.add(confirmField);

        JButton registerButton = new JButton("REGISTER");
        JButton backButton = new JButton("Back to Login");
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(new Color(35, 65, 110));

        JPanel buttons = new JPanel(new GridLayout(2, 1, 0, 7));
        buttons.setOpaque(false);
        buttons.add(registerButton);
        buttons.add(backButton);

        registerButton.addActionListener(e -> register());
        backButton.addActionListener(e -> backToLogin());

        root.add(title, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void register() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Register", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Register", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = studentRepository.register(id, name, email, password);
        if (!success) {
            JOptionPane.showMessageDialog(this, "Could not create account.", "Register failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
        backToLogin();
    }

    private void backToLogin() {
        dispose();
        loginFrame.clearFields();
        loginFrame.setVisible(true);
    }
}
