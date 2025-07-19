import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Student implements Serializable {
    private String studentId;
    private String name;
    private String className;
    private Map<String, Integer> subjectsMarks;
    
    public Student(String studentId, String name, String className) {
        this.studentId = studentId;
        this.name = name;
        this.className = className;
        this.subjectsMarks = new HashMap<>();
    }
    
    // Getters and setters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getClassName() { return className; }
    public Map<String, Integer> getSubjectsMarks() { return subjectsMarks; }
    
    public void setName(String name) { this.name = name; }
    public void setClassName(String className) { this.className = className; }
    
    public void addMark(String subject, int mark) {
        subjectsMarks.put(subject, mark);
    }
    
    public void updateMark(String subject, int newMark) {
        if (subjectsMarks.containsKey(subject)) {
            subjectsMarks.put(subject, newMark);
        }
    }
    
    public void removeSubject(String subject) {
        subjectsMarks.remove(subject);
    }
    
    @Override
    public String toString() {
        return "ID: " + studentId + ", Name: " + name + ", Class: " + className;
    }
}
