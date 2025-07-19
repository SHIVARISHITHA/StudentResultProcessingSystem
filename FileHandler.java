import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "students.dat";

    public static void saveStudents(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Student> loadStudents() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading students: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void exportToCSV(List<Student> students, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Student ID,Name,Class,Subjects,Total,Percentage,Grade");

            for (Student student : students) {
                int total = ResultCalculator.calculateTotal(student.getSubjectsMarks());
                double percentage = ResultCalculator.calculatePercentage(total, student.getSubjectsMarks().size());
                String grade = ResultCalculator.calculateGrade(percentage);

                writer.print(student.getStudentId() + ",");
                writer.print(student.getName() + ",");
                writer.print(student.getClassName() + ",");

                StringBuilder subjects = new StringBuilder();
                student.getSubjectsMarks().forEach((subj, mark) -> {
                    subjects.append(subj).append(":").append(mark).append(";");
                });
                writer.print(subjects.toString() + ",");

                writer.print(total + ",");
                writer.print(String.format("%.2f", percentage) + ",");
                writer.println(grade);
            }
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
        }
    }
}
