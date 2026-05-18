import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class StudentManagementGUI extends JFrame {

    private final JTextField rollField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JComboBox<String> genderCombo = new JComboBox<>(new String[] {"Male", "Female", "Other"});
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField courseField = new JTextField();
    private final JTextField sectionField = new JTextField();
    private final JTextField marksField = new JTextField();
    private final JTextField searchField = new JTextField();
    private final JRadioButton searchByRollRadio = new JRadioButton("Roll");
    private final JRadioButton searchByNameRadio = new JRadioButton("Name");
    private final ButtonGroup searchModeGroup = new ButtonGroup();
    private final JLabel totalStudentsLabel = new JLabel("Total Students: 0");
    private final JLabel statusLabel = new JLabel("Ready.");

    private final DefaultTableModel tableModel;
    private final JTable studentTable;
    private final ArrayList<Student> students = new ArrayList<>();
    private boolean editMode = false;
    private int editingRoll = -1;

    public StudentManagementGUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Student Management System", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 24f));
        header.setBorder(new EmptyBorder(12, 12, 12, 12));
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(content, BorderLayout.CENTER);

        JPanel formPanel = createFormPanel();
        content.add(formPanel, BorderLayout.WEST);

        // Add tooltips for better user guidance
        rollField.setToolTipText("Enter a unique positive roll number");
        nameField.setToolTipText("Enter the student's full name");
        ageField.setToolTipText("Enter age between 15 and 35");
        genderCombo.setToolTipText("Select gender");
        emailField.setToolTipText("Enter a valid email address");
        phoneField.setToolTipText("Enter 10-digit phone number");
        courseField.setToolTipText("Enter the course name");
        sectionField.setToolTipText("Enter the section (e.g., A, B)");
        marksField.setToolTipText("Enter marks between 0 and 100");
        searchField.setToolTipText("Enter roll number or name to search");

        tableModel = new DefaultTableModel(new String[] {
            "Roll", "Name", "Age", "Gender", "Email",
            "Course", "Section", "Marks", "Grade", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        content.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        bottomPanel.add(totalStudentsLabel, BorderLayout.WEST);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable(students);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(360, 0));

        JPanel fields = new JPanel(new GridLayout(10, 2, 8, 8));
        fields.setBorder(BorderFactory.createTitledBorder("Student Details"));
        fields.add(new JLabel("Roll Number:"));
        fields.add(rollField);
        fields.add(new JLabel("Name:"));
        fields.add(nameField);
        fields.add(new JLabel("Age:"));
        fields.add(ageField);
        fields.add(new JLabel("Gender:"));
        fields.add(genderCombo);
        fields.add(new JLabel("Email:"));
        fields.add(emailField);
        fields.add(new JLabel("Phone:"));
        fields.add(phoneField);
        fields.add(new JLabel("Course:"));
        fields.add(courseField);
        fields.add(new JLabel("Section:"));
        fields.add(sectionField);
        fields.add(new JLabel("Marks:"));
        fields.add(marksField);
        fields.setMaximumSize(new Dimension(Integer.MAX_VALUE, fields.getPreferredSize().height));
        panel.add(fields);
        panel.add(Box.createVerticalStrut(12));

        JPanel buttons = new JPanel(new GridLayout(5, 1, 8, 8));
        buttons.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttons.add(createButton("Add Student", e -> handleAddStudent()));
        buttons.add(createButton("Edit Selected", e -> handleEditStudent()));
        buttons.add(createButton("Remove Selected", e -> handleRemoveStudent()));
        buttons.add(createButton("Clear Form", e -> clearForm()));
        buttons.add(createButton("Show All Students", e -> showAllStudents()));
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttons.getPreferredSize().height));
        panel.add(buttons);
        panel.add(Box.createVerticalStrut(12));

        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Students"));
        searchPanel.add(new JLabel("Enter Roll or Name:"), BorderLayout.NORTH);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JPanel searchModePanel = new JPanel(new GridLayout(1, 2, 8, 8));
        searchModePanel.add(searchByRollRadio);
        searchModePanel.add(searchByNameRadio);
        searchModeGroup.add(searchByRollRadio);
        searchModeGroup.add(searchByNameRadio);
        searchByRollRadio.setSelected(true);
        searchPanel.add(searchModePanel, BorderLayout.WEST);

        JPanel searchButtons = new JPanel(new GridLayout(1, 3, 8, 8));
        searchButtons.add(createButton("Search Student", e -> searchStudent()));
        searchButtons.add(createButton("Clear Search", e -> showAllStudents()));
        searchButtons.add(createButton("Exit", e -> confirmExit()));
        searchPanel.add(searchButtons, BorderLayout.SOUTH);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPanel.getPreferredSize().height));
        panel.add(searchPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    private void handleEditStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            showError("Please select a student from the table to edit.");
            return;
        }

        int roll = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        Student student = findByRoll(roll);
        if (student == null) {
            showError("Selected student not found.");
            return;
        }

        // Populate form fields
        rollField.setText(String.valueOf(student.getRollNumber()));
        nameField.setText(student.getName());
        ageField.setText(String.valueOf(student.getAge()));
        genderCombo.setSelectedItem(student.getGender());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhone());
        courseField.setText(student.getCourse());
        sectionField.setText(student.getSection());
        marksField.setText(String.format("%.1f", student.getMarks()));

        editMode = true;
        editingRoll = roll;
        statusLabel.setText("Editing student " + student.getName() + ". Click 'Add Student' to save changes.");
    }

    private void handleAddStudent() {
        try {
            int roll = parsePositiveInt(rollField.getText().trim(), "Roll Number");
            if (!editMode && findByRoll(roll) != null) {
                showError("A student with roll number " + roll + " already exists.");
                return;
            }
            if (editMode && roll != editingRoll && findByRoll(roll) != null) {
                showError("A student with roll number " + roll + " already exists.");
                return;
            }

            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showError("Please enter the student's name.");
                return;
            }

            int age = parsePositiveInt(ageField.getText().trim(), "Age");
            if (age < 15 || age > 35) {
                showError("Age must be between 15 and 35.");
                return;
            }

            String gender = genderCombo.getSelectedItem().toString();
            String email = emailField.getText().trim();
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                showError("Please enter a valid email address (e.g., name@example.com).");
                return;
            }

            String phone = phoneField.getText().trim();
            if (!phone.matches("\\d{10}")) {
                showError("Phone number must contain exactly 10 digits.");
                return;
            }

            String course = courseField.getText().trim();
            if (course.isEmpty()) {
                showError("Please enter the course name.");
                return;
            }

            String section = sectionField.getText().trim().toUpperCase();
            if (section.isEmpty()) {
                showError("Please enter the section.");
                return;
            }

            double marks = parseDoubleInRange(marksField.getText().trim(), "Marks", 0, 100);

            if (editMode) {
                // Update existing student
                Student student = findByRoll(editingRoll);
                if (student != null) {
                    student.setRollNumber(roll);
                    student.setName(name);
                    student.setAge(age);
                    student.setGender(gender);
                    student.setEmail(email);
                    student.setPhone(phone);
                    student.setCourse(course);
                    student.setSection(section);
                    student.setMarks(marks);
                    refreshTable(students);
                    statusLabel.setText("Updated student " + name + " (Roll " + roll + ").");
                }
                editMode = false;
                editingRoll = -1;
            } else {
                // Add new student
                Student student = new Student(roll, name, age, gender, email, phone, course, section, marks);
                students.add(student);
                refreshTable(students);
                statusLabel.setText("Added student " + name + " (Roll " + roll + ").");
            }
            clearForm();
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void handleRemoveStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int roll = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            confirmAndRemoveRoll(roll);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Enter Roll Number to remove:", "Remove Student", JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.trim().isEmpty()) {
            statusLabel.setText("Removal cancelled.");
            return;
        }

        try {
            int roll = Integer.parseInt(input.trim());
            confirmAndRemoveRoll(roll);
        } catch (NumberFormatException ex) {
            showError("Roll number must be a valid number.");
        }
    }

    private void confirmAndRemoveRoll(int roll) {
        Student found = findByRoll(roll);
        if (found == null) {
            showError("No student found with roll number " + roll + ".");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
            "Delete student " + found.getName() + " (Roll " + roll + ")?",
            "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            students.remove(found);
            refreshTable(students);
            statusLabel.setText("Removed student roll " + roll + ".");
        } else {
            statusLabel.setText("Removal cancelled.");
        }
    }

    private void searchByRoll() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            showError("Enter a roll number to search.");
            return;
        }

        try {
            int roll = Integer.parseInt(query);
            Student found = findByRoll(roll);
            if (found == null) {
                showError("No student found with roll number " + roll + ".");
            } else {
                refreshTable(java.util.Collections.singletonList(found));
                statusLabel.setText("Showing search result for roll " + roll + ".");
            }
        } catch (NumberFormatException ex) {
            showError("Roll number must be a valid integer.");
        }
    }

    private void searchStudent() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            showError("Enter a roll number or name to search.");
            return;
        }

        ArrayList<Student> results = new ArrayList<>();

        if (searchByRollRadio.isSelected()) {
            try {
                int roll = Integer.parseInt(query);
                Student found = findByRoll(roll);
                if (found != null) {
                    results.add(found);
                }
            } catch (NumberFormatException ex) {
                showError("Roll number must be a valid integer.");
                return;
            }
        } else {
            String queryLower = query.toLowerCase();
            for (Student student : students) {
                if (student.getName().toLowerCase().contains(queryLower)) {
                    results.add(student);
                }
            }
        }

        if (results.isEmpty()) {
            showError("No students found matching '" + query + "'.");
        } else {
            refreshTable(results);
            statusLabel.setText("Found " + results.size() + " student(s) matching '" + query + "'.");
        }
    }

    private void showAllStudents() {
        refreshTable(students);
        statusLabel.setText("Showing all " + students.size() + " students.");
    }

    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit the application?",
            "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private Student findByRoll(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    private void refreshTable(java.util.List<Student> studentsToDisplay) {
        tableModel.setRowCount(0);
        for (Student student : studentsToDisplay) {
            tableModel.addRow(new Object[] {
                student.getRollNumber(),
                student.getName(),
                student.getAge(),
                student.getGender(),
                student.getEmail(),
                student.getCourse(),
                student.getSection(),
                String.format("%.1f", student.getMarks()),
                student.getGrade(),
                student.getStatus()
            });
        }
        totalStudentsLabel.setText("Total Students: " + students.size());
    }

    private void clearForm() {
        rollField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderCombo.setSelectedIndex(0);
        emailField.setText("");
        phoneField.setText("");
        courseField.setText("");
        sectionField.setText("");
        marksField.setText("");
        searchField.setText("");
        studentTable.clearSelection();
        editMode = false;
        editingRoll = -1;
        statusLabel.setText("Form cleared. Ready.");
    }

    private int parsePositiveInt(String value, String fieldName) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than zero.");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer.");
        }
    }

    private double parseDoubleInRange(String value, String fieldName, double min, double max) {
        try {
            double parsed = Double.parseDouble(value);
            if (parsed < min || parsed > max) {
                throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max + ".");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
        statusLabel.setText("Error: " + message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI frame = new StudentManagementGUI();
            frame.setVisible(true);
        });
    }
}
