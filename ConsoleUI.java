import java.util.*;
import java.util.stream.Collectors;

public class ConsoleUI {
    private Scanner scanner;
    private List<Student> students;
    
    public ConsoleUI() {
        scanner = new Scanner(System.in);
        students = FileHandler.loadStudents();
    }
    
    public void start() {
        while (true) {
            System.out.println("\nStudent Result Processing System");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Delete Student");
            System.out.println("4. View All Students");
            System.out.println("5. Add/Edit Marks");
            System.out.println("6. View Student Result");
            System.out.println("7. Generate All Results");
            System.out.println("8. Search Students");
            System.out.println("9. Export to CSV");
            System.out.println("0. Exit");
            
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1: addStudent(); break;
                case 2: editStudent(); break;
                case 3: deleteStudent(); break;
                case 4: viewAllStudents(); break;
                case 5: manageMarks(); break;
                case 6: viewResult(); break;
                case 7: generateAllResults(); break;
                case 8: searchStudents(); break;
                case 9: exportToCSV(); break;
                case 0: 
                    FileHandler.saveStudents(students);
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default: 
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void addStudent() {
        System.out.println("\nAdd New Student");
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        if (students.stream().anyMatch(s -> s.getStudentId().equals(id))) {
            System.out.println("Student with this ID already exists!");
            return;
        }
        
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Class: ");
        String className = scanner.nextLine();
        
        Student student = new Student(id, name, className);
        students.add(student);
        FileHandler.saveStudents(students);
        System.out.println("Student added successfully!");
    }
    
    private void editStudent() {
        System.out.println("\nEdit Student");
        System.out.print("Enter Student ID to edit: ");
        String id = scanner.nextLine();
        
        Student student = students.stream()
            .filter(s -> s.getStudentId().equals(id))
            .findFirst()
            .orElse(null);
            
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("Current details: " + student);
        System.out.print("Enter new name (leave blank to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) student.setName(name);
        
        System.out.print("Enter new class (leave blank to keep current): ");
        String className = scanner.nextLine();
        if (!className.isEmpty()) student.setClassName(className);
        
        FileHandler.saveStudents(students);
        System.out.println("Student updated successfully!");
    }
    
    private void deleteStudent() {
        System.out.println("\nDelete Student");
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine();
        
        boolean removed = students.removeIf(s -> s.getStudentId().equals(id));
        if (removed) {
            FileHandler.saveStudents(students);
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student not found!");
        }
    }
    
    private void viewAllStudents() {
        System.out.println("\nAll Students");
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        students.forEach(System.out::println);
    }
    
    private void manageMarks() {
        System.out.println("\nManage Marks");
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        Student student = students.stream()
            .filter(s -> s.getStudentId().equals(id))
            .findFirst()
            .orElse(null);
            
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("Current subjects and marks:");
        student.getSubjectsMarks().forEach((subj, mark) -> 
            System.out.println(subj + ": " + mark));
        
        System.out.println("\n1. Add/Update Mark");
        System.out.println("2. Remove Subject");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        if (choice == 1) {
            System.out.print("Enter Subject Name: ");
            String subject = scanner.nextLine();
            
            System.out.print("Enter Mark (0-100): ");
            int mark = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            if (mark < 0 || mark > 100) {
                System.out.println("Invalid mark! Must be between 0 and 100.");
                return;
            }
            
            student.addMark(subject, mark);
            FileHandler.saveStudents(students);
            System.out.println("Mark added/updated successfully!");
        } else if (choice == 2) {
            System.out.print("Enter Subject Name to remove: ");
            String subject = scanner.nextLine();
            
            if (student.getSubjectsMarks().containsKey(subject)) {
                student.removeSubject(subject);
                FileHandler.saveStudents(students);
                System.out.println("Subject removed successfully!");
            } else {
                System.out.println("Subject not found for this student!");
            }
        } else {
            System.out.println("Invalid choice!");
        }
    }
    
    private void viewResult() {
        System.out.println("\nView Student Result");
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        Student student = students.stream()
            .filter(s -> s.getStudentId().equals(id))
            .findFirst()
            .orElse(null);
            
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        if (student.getSubjectsMarks().isEmpty()) {
            System.out.println("No marks entered for this student yet!");
            return;
        }
        
        System.out.println(ResultCalculator.generateResult(student));
    }
    
    private void generateAllResults() {
        System.out.println("\nAll Student Results");
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        students.forEach(student -> {
            if (!student.getSubjectsMarks().isEmpty()) {
                System.out.println(ResultCalculator.generateResult(student));
                System.out.println("-----------------------------------");
            }
        });
    }
    
    private void searchStudents() {
        System.out.println("\nSearch Students");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Grade");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        List<Student> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                System.out.print("Enter Student ID: ");
                String id = scanner.nextLine();
                results = students.stream()
                    .filter(s -> s.getStudentId().equalsIgnoreCase(id))
                    .collect(Collectors.toList());
                break;
            case 2:
                System.out.print("Enter Name (partial match): ");
                String name = scanner.nextLine();
                results = students.stream()
                    .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
                break;
            case 3:
                System.out.print("Enter Grade (A+, A, B, etc.): ");
                String grade = scanner.nextLine().toUpperCase();
                results = students.stream()
                    .filter(s -> {
                        if (s.getSubjectsMarks().isEmpty()) return false;
                        int total = ResultCalculator.calculateTotal(s.getSubjectsMarks());
                        double percentage = ResultCalculator.calculatePercentage(total, s.getSubjectsMarks().size());
                        return ResultCalculator.calculateGrade(percentage).equals(grade);
                    })
                    .collect(Collectors.toList());
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("No students found matching the criteria.");
        } else {
            System.out.println("\nSearch Results:");
            results.forEach(System.out::println);
        }
    }
    
    private void exportToCSV() {
        System.out.print("Enter filename to export (e.g., results.csv): ");
        String filename = scanner.nextLine();
        
        FileHandler.exportToCSV(students, filename);
        System.out.println("Data exported to " + filename + " successfully!");
    }
    
    public static void main(String[] args) {
        new ConsoleUI().start();
    }
}