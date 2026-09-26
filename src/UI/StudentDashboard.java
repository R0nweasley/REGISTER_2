package UI;

import Repository.CourseRepository;
import Repository.EnrollmentRepository;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {
    private final Student student;
    private final LoginFrame loginFrame;
    private final CourseRepository courseRepository = new CourseRepository();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepository();

    private final DefaultTableModel availableModel = new DefaultTableModel(
            new Object[]{"Course ID", "Course Name", "Credit", "Action"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final DefaultTableModel enrolledModel = new DefaultTableModel(
            new Object[]{"Course ID", "Course Name", "Credit", "Action"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable availableTable = new JTable(availableModel);
    private final JTable enrolledTable = new JTable(enrolledModel);
    private final JLabel totalCreditsLabel = new JLabel("Total Credits: 0");

    public StudentDashboard(Student student, LoginFrame loginFrame) {
        this.student = student;
        this.loginFrame = loginFrame;
        setTitle("University Register - Student Dashboard");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);
        buildUI();
        refreshTables();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        root.setBackground(new Color(245, 247, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel welcome = new JLabel("Welcome, " + student.getName());
        welcome.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcome.setForeground(new Color(35, 65, 110));

        JLabel info = new JLabel("Student ID: " + student.getID() + "    |    " + student.getEmail());
        info.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel identity = new JPanel(new GridLayout(2, 1));
        identity.setOpaque(false);
        identity.add(welcome);
        identity.add(info);

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> logout());
        header.add(identity, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        JPanel availablePanel = createCoursePanel("AVAILABLE COURSES", availableTable);
        JPanel enrolledPanel = createCoursePanel("MY COURSES", enrolledTable);

        JPanel center = new JPanel(new GridLayout(1, 2, 15, 0));
        center.setOpaque(false);
        center.add(availablePanel);
        center.add(enrolledPanel);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        totalCreditsLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        footer.add(totalCreditsLabel, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);

        // Double-click row to perform action.
        availableTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    enrollSelected();
                }
            }
        });

        enrolledTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    withdrawSelected();
                }
            }
        });
    }

    private JPanel createCoursePanel(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        titleLabel.setForeground(new Color(35, 65, 110));

        table.setRowHeight(34);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void refreshTables() {
        availableModel.setRowCount(0);
        enrolledModel.setRowCount(0);

        List<String> enrolledIds = enrollmentRepository.getEnrolledCourseIds(student.getID());
        List<String> courses = courseRepository.getAllCourses();

        // แยกวิชาที่ลงทะเบียนแล้วออกจากวิชาที่ยังลงทะเบียนได้
        for (String course : courses) {
            String[] parts = course.split(" - ", 2);
            String courseId = parts[0].trim();
            String courseName = parts.length > 1 ? parts[1].trim() : "";
            int credit = courseRepository.getCourseCreditById(courseId);

            if (enrolledIds.contains(courseId)) {
                enrolledModel.addRow(new Object[]{courseId, courseName, credit, "Withdraw"});
            } else {
                availableModel.addRow(new Object[]{courseId, courseName, credit, "Enroll"});
            }
        }

        totalCreditsLabel.setText("Total Credits: " + calculateCredits(enrolledIds));
    }

    private int calculateCredits(List<String> courseIds) {
        int total = 0;

        for (String id : courseIds) {
            total += courseRepository.getCourseCreditById(id);
        }

        return total;
    }

    private void enrollSelected() {
        int row = availableTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course first.",
                    "Enroll", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = availableModel.getValueAt(row, 0).toString();
        String courseName = availableModel.getValueAt(row, 1).toString();
        String credit = availableModel.getValueAt(row, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Register for " + courseId + " - " + courseName +
                        " (" + credit + " credits)?",
                "Confirm Enrollment", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (enrollmentRepository.enrollCourse(student.getID(), courseId)) {
                JOptionPane.showMessageDialog(this,
                        "Course registered successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTables();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Could not register this course.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void withdrawSelected() {
        int row = enrolledTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course first.",
                    "Withdraw", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = enrolledModel.getValueAt(row, 0).toString();
        String courseName = enrolledModel.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Withdraw from " + courseId + " - " + courseName + "?",
                "Confirm Withdrawal", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (enrollmentRepository.withdrawCourse(student.getID(), courseId)) {
                JOptionPane.showMessageDialog(this,
                        "Course withdrawn successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTables();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Could not withdraw this course.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Logout from this account?",
                "Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            loginFrame.clearFields();
            loginFrame.setVisible(true);
        }
    }
}
