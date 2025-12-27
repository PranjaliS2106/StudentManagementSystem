import java.util.*;

// ===================== ABSTRACT PERSON CLASS =====================
abstract class Person {
    private String name;
    private int id;
    private int age;

    public Person() {}

    public Person(String name, int id, int age) {
        this.name = name;
        this.id = id;
        this.age = age;
    }

    public abstract String getRole();

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Age: " + age;
    }
}

// ===================== STUDENT CLASS =====================
class Student extends Person {
    private int[] grades;
    private static int totalStudents = 0;

    public Student() {
        super();
        grades = new int[3];
        totalStudents++;
    }

    public Student(String name, int id, int age, int[] grades) {
        super(name, id, age);
        this.grades = grades;
        totalStudents++;
    }

    public int[] getGrades() {
        return grades;
    }

    public void setGrades(int[] grades) {
        this.grades = grades;
    }

    public double getAverage() {
        int sum = 0;
        for (int g : grades) sum += g;
        return sum / (double) grades.length;
    }

    public static int getTotalStudents() {
        return totalStudents;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String toString() {
        return super.toString() + ", Avg Grade: " + getAverage();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            return this.getId() == ((Student) obj).getId();
        }
        return false;
    }
}

// ===================== TEACHER CLASS =====================
class Teacher extends Person {
    public Teacher(String name, int id, int age) {
        super(name, id, age);
    }

    @Override
    public String getRole() {
        return "Teacher";
    }

    @Override
    public String toString() {
        return "Teacher: " + getName();
    }
}

// ===================== MAIN CLASS =====================
public class Main {  // Use Main for online compilers
    private static LinkedList<Student> studentList = new LinkedList<>();
    private static Stack<Student> undoStack = new Stack<>();
    private static Queue<Student> pendingQueue = new LinkedList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            displayMenu();
            choice = getInt("Enter your choice: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addPendingAdmission();
                case 3 -> processPendingAdmission();
                case 4 -> removeStudent();
                case 5 -> searchStudentMenu();
                case 6 -> displayAllStudents();
                case 7 -> sortStudentsMenu();
                case 8 -> showStatistics();
                case 9 -> undoLastOperation();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }

    // ===================== MENU =====================
    private static void displayMenu() {
        System.out.println("\n===== Student Management System =====");
        System.out.println("1. Add new student");
        System.out.println("2. Add to pending admissions");
        System.out.println("3. Process pending admission");
        System.out.println("4. Remove student");
        System.out.println("5. Search student");
        System.out.println("6. Display all students");
        System.out.println("7. Sort students");
        System.out.println("8. Show statistics");
        System.out.println("9. Undo last operation");
        System.out.println("0. Exit");
    }

    // ===================== OPERATIONS =====================
    private static void addStudent() {
        Student s = createStudent();
        studentList.add(s);
        undoStack.push(s);
        System.out.println("Student added successfully!");
    }

    private static void addPendingAdmission() {
        Student s = createStudent();
        pendingQueue.add(s);
        System.out.println("Student added to pending admissions.");
    }

    private static void processPendingAdmission() {
        if (pendingQueue.isEmpty()) {
            System.out.println("No pending admissions.");
            return;
        }
        Student s = pendingQueue.poll();
        studentList.add(s);
        undoStack.push(s);
        System.out.println("Pending student admitted.");
    }

    private static void removeStudent() {
        int id = getInt("Enter student ID to remove: ");
        Student found = null;
        for (Student s : studentList) {
            if (s.getId() == id) {
                found = s;
                break;
            }
        }
        if (found != null) {
            studentList.remove(found);
            System.out.println("Student removed.");
        } else {
            System.out.println("Student not found.");
        }
    }

    // ===================== SEARCH =====================
    private static void searchStudentMenu() {
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        int ch = getInt("Choose search type: ");
        if (ch == 1) {
            int id = getInt("Enter ID: ");
            Student s = binarySearchByID(id);
            System.out.println(s != null ? s : "Student not found.");
        } else if (ch == 2) {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            linearSearchByName(name);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void linearSearchByName(String name) {
        boolean found = false;
        for (Student s : studentList) {
            if (s.getName().equalsIgnoreCase(name)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("No student found with this name.");
    }

    private static Student binarySearchByID(int id) {
        studentList.sort(Comparator.comparingInt(Student::getId));
        int low = 0, high = studentList.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (studentList.get(mid).getId() == id) return studentList.get(mid);
            else if (studentList.get(mid).getId() < id) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    // ===================== DISPLAY =====================
    private static void displayAllStudents() {
        if (studentList.isEmpty()) System.out.println("No students available.");
        else for (Student s : studentList) System.out.println(s);
    }

    // ===================== SORT =====================
    private static void sortStudentsMenu() {
        System.out.println("1. Sort by Name");
        System.out.println("2. Sort by Average Grade");
        int ch = getInt("Choose sorting type: ");
        if (ch == 1) studentList.sort(Comparator.comparing(Student::getName));
        else if (ch == 2) studentList.sort(Comparator.comparingDouble(Student::getAverage).reversed());
        else System.out.println("Invalid option.");
        System.out.println("Sorting done.");
    }

    // ===================== STATISTICS =====================
    private static void showStatistics() {
        if (studentList.isEmpty()) {
            System.out.println("No students to show statistics.");
            return;
        }
        double total = 0;
        Student top = studentList.getFirst();
        for (Student s : studentList) {
            total += s.getAverage();
            if (s.getAverage() > top.getAverage()) top = s;
        }
        System.out.println("Average of all students: " + (total / studentList.size()));
        System.out.println("Top performer: " + top);
        System.out.println("Total students: " + Student.getTotalStudents());
    }

    // ===================== UNDO =====================
    private static void undoLastOperation() {
        if (undoStack.isEmpty()) System.out.println("Nothing to undo.");
        else {
            Student s = undoStack.pop();
            studentList.remove(s);
            System.out.println("Last operation undone.");
        }
    }

    // ===================== HELPER METHODS =====================
    private static Student createStudent() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        int id = getInt("Enter ID: ");
        int age = getInt("Enter age: ");
        int[] grades = new int[3];
        for (int i = 0; i < 3; i++) grades[i] = getInt("Enter grade " + (i + 1) + ": ");
        return new Student(name, id, age, grades);
    }

    private static int getInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }
    }
}
