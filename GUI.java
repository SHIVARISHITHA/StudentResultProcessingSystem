import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class GUI extends JFrame {
    private List<Student> students;
    private DefaultListModel<String> studentListModel;
    private JList<String> studentList;
    
    public GUI() {
        students = FileHandler.loadStudents();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Student Result Processing System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Student list on the left
        studentListModel = new DefaultListModel<>();
        updateStudentList();
        studentList = new JList<>(studentListModel);
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentList.getSelectedIndex() != -1) {
                showStudentDetails(students.get(studentList.getSelectedIndex()));
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(studentList);
        listScrollPane.setPreferredSize(new Dimension(200, 0));
        mainPanel.add(listScrollPane, BorderLayout.WEST);
        
        // Details panel on the right
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.add(new JLabel("Select a student to view/edit details", JLabel.CENTER), BorderLayout.NORTH);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        
        // Button panel at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> addStudent());
        buttonPanel.add(addButton);
        
        JButton editButton = new JButton("Edit Student");
        editButton.addActionListener(e -> {
            int index = studentList.getSelectedIndex();
            if (index != -1) {
                editStudent(students.get(index));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student first");
            }
        });
        buttonPanel.add(editButton);
        
        JButton deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(e -> {
            int index = studentList.getSelectedIndex();
            if (index != -1) {
                deleteStudent(students.get(index));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student first");
            }
        });
        buttonPanel.add(deleteButton);
        
        JButton marksButton = new JButton("Manage Marks");
        marksButton.addActionListener(e -> {
            int index = studentList.getSelectedIndex();
            if (index != -1) {
                manageMarks(students.get(index));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student first");
            }
        });
        buttonPanel.add(marksButton);
        
        JButton resultButton = new JButton("View Result");
        resultButton.addActionListener(e -> {
            int index = studentList.getSelectedIndex();
            if (index != -1) {
                viewResult(students.get(index));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student first");
            }
        });
        buttonPanel.add(resultButton);
        
        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportToCSV());
        buttonPanel.add(exportButton);
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudents());
        buttonPanel.add(searchButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void updateStudentList() {
        studentListModel.clear();
        students.forEach(student -> studentListModel.addElement(student.getStudentId() + " - " + student.getName()));
    }
    
    private void showStudentDetails(Student student) {
        // This would update the details panel with student information
        // Implementation omitted for brevity
    }
    
    private void addStudent() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField classField = new JTextField();
        
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Class:"));
        panel.add(classField);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Add New Student", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String className = classField.getText().trim();
            
            if (id.isEmpty() || name.isEmpty() || className.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }
            
            if (students.stream().anyMatch(s -> s.getStudentId().equals(id))) {
                JOptionPane.showMessageDialog(this, "Student with this ID already exists!");
                return;
            }
            
            Student student = new Student(id, name, className);
            students.add(student);
            FileHandler.saveStudents(students);
            updateStudentList();
            JOptionPane.showMessageDialog(this, "Student added successfully!");
        }
    }
    
    private void editStudent(Student student) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        
        JTextField idField = new JTextField(student.getStudentId());
        idField.setEditable(false);
        JTextField nameField = new JTextField(student.getName());
        JTextField classField = new JTextField(student.getClassName());
        
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Class:"));
        panel.add(classField);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Edit Student", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String className = classField.getText().trim();
            
            if (name.isEmpty() || className.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }
            
            student.setName(name);
            student.setClassName(className);
            FileHandler.saveStudents(students);
            updateStudentList();
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
        }
    }
    
    private void deleteStudent(Student student) {
        int confirm = JOptionPane.showConfirmDialog(
            this, "Are you sure you want to delete " + student.getName() + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            students.remove(student);
            FileHandler.saveStudents(students);
            updateStudentList();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
        }
    }
    
    private void manageMarks(Student student) {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Current marks
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Subject", "Mark"}, 0);
        student.getSubjectsMarks().forEach((subj, mark) -> 
            tableModel.addRow(new Object[]{subj, mark}));
            
        JTable marksTable = new JTable(tableModel);
        panel.add(new JScrollPane(marksTable), BorderLayout.CENTER);
        
        // Add/Edit/Remove buttons
        JPanel buttonPanel = new JPanel();
        
        JButton addButton = new JButton("Add/Update");
        addButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(0, 2));
            
            JTextField subjectField = new JTextField();
            JTextField markField = new JTextField();
            
            inputPanel.add(new JLabel("Subject:"));
            inputPanel.add(subjectField);
            inputPanel.add(new JLabel("Mark (0-100):"));
            inputPanel.add(markField);
            
            int result = JOptionPane.showConfirmDialog(
                this, inputPanel, "Add/Update Mark", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String subject = subjectField.getText().trim();
                    int mark = Integer.parseInt(markField.getText().trim());
                    
                    if (subject.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Subject is required!");
                        return;
                    }
                    
                    if (mark < 0 || mark > 100) {
                        JOptionPane.showMessageDialog(this, "Mark must be between 0 and 100!");
                        return;
                    }
                    
                    student.addMark(subject, mark);
                    FileHandler.saveStudents(students);
                    updateMarksTable(tableModel, student);
                    JOptionPane.showMessageDialog(this, "Mark added/updated successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number for mark!");
                }
            }
        });
        buttonPanel.add(addButton);
        
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            int row = marksTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a subject to remove");
                return;
            }
            
            String subject = (String) tableModel.getValueAt(row, 0);
            student.removeSubject(subject);
            FileHandler.saveStudents(students);
            updateMarksTable(tableModel, student);
            JOptionPane.showMessageDialog(this, "Subject removed successfully!");
        });
        buttonPanel.add(removeButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JOptionPane.showMessageDialog(this, panel, "Manage Marks for " + student.getName(), 
            JOptionPane.PLAIN_MESSAGE);
    }
    
    private void updateMarksTable(DefaultTableModel model, Student student) {
        model.setRowCount(0);
        student.getSubjectsMarks().forEach((subj, mark) -> 
            model.addRow(new Object[]{subj, mark}));
    }
    
    private void viewResult(Student student) {
        if (student.getSubjectsMarks().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No marks entered for this student yet!");
            return;
        }
        
        JTextArea textArea = new JTextArea(ResultCalculator.generateResult(student));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), 
            "Result for " + student.getName(), JOptionPane.PLAIN_MESSAGE);
    }
    
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");
        fileChooser.setSelectedFile(new File("results.csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            FileHandler.exportToCSV(students, file.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Data exported to CSV successfully!");
        }
    }
    
    private void searchStudents() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        
        JComboBox<String> searchType = new JComboBox<>(new String[]{"By ID", "By Name", "By Grade"});
        JTextField searchField = new JTextField();
        
        panel.add(new JLabel("Search Type:"));
        panel.add(searchType);
        panel.add(new JLabel("Search Term:"));
        panel.add(searchField);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Search Students", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
        if (result == JOptionPane.OK_OPTION) {
            String term = searchField.getText().trim();
            if (term.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term");
                return;
            }
            
            List<Student> searchResults;
            switch (searchType.getSelectedIndex()) {
                case 0: // By ID
                    searchResults = students.stream()
                        .filter(s -> s.getStudentId().equalsIgnoreCase(term))
                        .collect(Collectors.toList());
                    break;
                case 1: // By Name
                    searchResults = students.stream()
                        .filter(s -> s.getName().toLowerCase().contains(term.toLowerCase()))
                        .collect(Collectors.toList());
                    break;
                case 2: // By Grade
                    searchResults = students.stream()
                        .filter(s -> {
                            if (s.getSubjectsMarks().isEmpty()) return false;
                            int total = ResultCalculator.calculateTotal(s.getSubjectsMarks());
                            double percentage = ResultCalculator.calculatePercentage(total, s.getSubjectsMarks().size());
                            return ResultCalculator.calculateGrade(percentage).equalsIgnoreCase(term);
                        })
                        .collect(Collectors.toList());
                    break;
                default:
                    searchResults = new ArrayList<>();
            }
            
            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No students found matching the criteria");
            } else {
                StringBuilder sb = new StringBuilder("Search Results:\n\n");
                searchResults.forEach(s -> sb.append(s).append("\n"));
                JOptionPane.showMessageDialog(this, sb.toString());
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}