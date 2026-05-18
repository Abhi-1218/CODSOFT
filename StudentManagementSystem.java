import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.SwingUtilities;



//  CONSOLE STUDENT CLASS — represents one student used by the console menu

class ConsoleStudent {

    // Attributes
    private int    rollNumber;
    private String name;
    private int    age;
    private String gender;
    private String email;
    private String phone;
    private String course;
    private String section;
    private double marks;
    private String grade;    
    private String status;   

    // Constructor
    public ConsoleStudent(int rollNumber, String name, int age, String gender,
                   String email, String phone, String course,
                   String section, double marks) {
        this.rollNumber = rollNumber;
        this.name       = name;
        this.age        = age;
        this.gender     = gender;
        this.email      = email;
        this.phone      = phone;
        this.course     = course;
        this.section    = section;
        setMarks(marks);
    }

    // Grade Logic 
    private String computeGrade(double m) {
        if (m >= 90) return "A+";
        if (m >= 80) return "A";
        if (m >= 70) return "B+";
        if (m >= 60) return "B";
        if (m >= 50) return "C";
        if (m >= 40) return "D";
        return "F";
    }

    // Getters 
    public int    getRollNumber() { return rollNumber; }
    public String getName()       { return name;       }
    public int    getAge()        { return age;        }
    public String getGender()     { return gender;     }
    public String getEmail()      { return email;      }
    public String getPhone()      { return phone;      }
    public String getCourse()     { return course;     }
    public String getSection()    { return section;    }
    public double getMarks()      { return marks;      }
    public String getGrade()      { return grade;      }
    public String getStatus()     { return status;     }

    // ── Setters 
    public void setName(String name)       { this.name = name;     }
    public void setAge(int age)            { this.age = age;       }
    public void setGender(String gender)   { this.gender = gender; }
    public void setEmail(String email)     { this.email = email;   }
    public void setPhone(String phone)     { this.phone = phone;   }
    public void setCourse(String course)   { this.course = course; }
    public void setSection(String section) { this.section = section; }

    // setMarks also re-computes grade and status automatically
    public void setMarks(double marks) {
        this.marks  = marks;
        this.grade  = computeGrade(marks);
        this.status = (marks >= 40) ? "PASS" : "FAIL";
    }

    // ── Print full profile card 
    public void printProfile() {
        System.out.println("STUDENT PROFILE");
        System.out.printf ("  |  %-12s : %-27s|%n", "Roll No",  rollNumber);
        System.out.printf ("  |  %-12s : %-27s|%n", "Name",     name);
        System.out.printf ("  |  %-12s : %-27s|%n", "Age",      age);
        System.out.printf ("  |  %-12s : %-27s|%n", "Gender",   gender);
        System.out.printf ("  |  %-12s : %-27s|%n", "Email",    email);
        System.out.printf ("  |  %-12s : %-27s|%n", "Phone",    phone);
        System.out.printf ("  |  %-12s : %-27s|%n", "Course",   course);
        System.out.printf ("  |  %-12s : %-27s|%n", "Section",  section);
        System.out.println("  +-----------------------------------------+");
        System.out.printf ("  |  %-12s : %-27.2f|%n", "Marks",  marks);
        System.out.printf ("  |  %-12s : %-27s|%n", "Grade",    grade);
        System.out.printf ("  |  %-12s : %-27s|%n", "Status",   status);
        System.out.println("  +-----------------------------------------+");
    }

    // ── One-line table row 
    @Override
    public String toString() {
        return String.format(
            "  | %-4d | %-18s | %-3d | %-6s | %-20s | %-14s | %-3s | %5.1f | %-3s | %-4s |",
            rollNumber, name, age, gender, email, course, section, marks, grade, status
        );
    }
}



//  STUDENT MANAGEMENT SYSTEM CLASS — manages all students
class SMS {

    private ArrayList<ConsoleStudent> studentList = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    // ── MENU LOOP 
    public void run() {
        printBanner();
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("  Enter your choice (1-5): ");
            String choice = scanner.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1": addStudent();         break;
                case "2": removeStudent();      break;
                case "3": searchStudent();      break;
                case "4": displayAllStudents(); break;
                case "5":
                    running = false;
                    System.out.println("  Goodbye! — CodSoft Internship Task 5");
                    break;
                default:
                    System.out.println("  [!] Invalid choice. Enter 1 to 5.");
            }
        }
    }

    // ── METHOD 1 : ADD STUDENT 
    public void addStudent() {
        System.out.println("  ADD NEW STUDENT");

        // Roll Number — must be positive & unique
        int rollNumber = 0;
        while (true) {
            System.out.print("  Roll Number       : ");
            try {
                rollNumber = Integer.parseInt(scanner.nextLine().trim());
                if (rollNumber <= 0) {
                    System.out.println("  [!] Must be greater than 0."); continue;
                }
                if (findByRoll(rollNumber) != null) {
                    System.out.println("  [!] Roll " + rollNumber + " already exists."); continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Enter a valid number.");
            }
        }

        // Name — cannot be empty
        System.out.print("  Full Name         : ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.println("  [!] Name cannot be empty.");
            System.out.print("  Full Name         : ");
            name = scanner.nextLine().trim();
        }

        // Age — 15 to 35
        int age = 0;
        while (true) {
            System.out.print("  Age               : ");
            try {
                age = Integer.parseInt(scanner.nextLine().trim());
                if (age < 15 || age > 35) {
                    System.out.println("  [!] Age must be between 15 and 35."); continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Enter a valid age.");
            }
        }

        // Gender — M / F / O
        System.out.print("  Gender (M/F/O)    : ");
        String gender = scanner.nextLine().trim();
        while (!gender.equalsIgnoreCase("M") &&
               !gender.equalsIgnoreCase("F") &&
               !gender.equalsIgnoreCase("O")) {
            System.out.println("  [!] Enter M for Male, F for Female, O for Other.");
            System.out.print("  Gender (M/F/O)    : ");
            gender = scanner.nextLine().trim();
        }
        if      (gender.equalsIgnoreCase("M")) gender = "Male";
        else if (gender.equalsIgnoreCase("F")) gender = "Female";
        else                                   gender = "Other";

        // Email — basic format check
        System.out.print("  Email             : ");
        String email = scanner.nextLine().trim();
        while (!email.matches("^[\\w.+\\-]+@[\\w\\-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("  [!] Invalid email. Example: name@gmail.com");
            System.out.print("  Email             : ");
            email = scanner.nextLine().trim();
        }

        // Phone — exactly 10 digits
        System.out.print("  Phone (10 digits) : ");
        String phone = scanner.nextLine().trim();
        while (!phone.matches("\\d{10}")) {
            System.out.println("  [!] Phone must be exactly 10 digits.");
            System.out.print("  Phone (10 digits) : ");
            phone = scanner.nextLine().trim();
        }

        // Course — cannot be empty
        System.out.print("  Course            : ");
        String course = scanner.nextLine().trim();
        while (course.isEmpty()) {
            System.out.println("  [!] Course cannot be empty.");
            System.out.print("  Course            : ");
            course = scanner.nextLine().trim();
        }

        // Section — cannot be empty
        System.out.print("  Section (A/B/C)   : ");
        String section = scanner.nextLine().trim().toUpperCase();
        while (section.isEmpty()) {
            System.out.println("  [!] Section cannot be empty.");
            System.out.print("  Section (A/B/C)   : ");
            section = scanner.nextLine().trim().toUpperCase();
        }

        // Marks — 0 to 100
        double marks = 0;
        while (true) {
            System.out.print("  Marks (0 - 100)   : ");
            try {
                marks = Double.parseDouble(scanner.nextLine().trim());
                if (marks < 0 || marks > 100) {
                    System.out.println("  [!] Marks must be between 0 and 100."); continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Enter a valid number.");
            }
        }

        // Create and add student
        ConsoleStudent s = new ConsoleStudent(rollNumber, name, age, gender,
                                 email, phone, course, section, marks);
        studentList.add(s);

        System.out.println("\n  [+] Student Added Successfully!");
        System.out.println("  +---------------------------+");
        System.out.printf ("  |  Name   : %-16s|%n", name);
        System.out.printf ("  |  Roll   : %-16d|%n", rollNumber);
        System.out.printf ("  |  Grade  : %-16s|%n", s.getGrade());
        System.out.printf ("  |  Status : %-16s|%n", s.getStatus());
        System.out.println("  +---------------------------+");
    }

    // ── METHOD 2 : REMOVE STUDENT 
    public void removeStudent() {
        System.out.println("  REMOVE STUDENT");

        if (studentList.isEmpty()) {
            System.out.println("  [!] No students in the system yet."); return;
        }

        System.out.print("  Enter Roll Number to remove : ");
        int rollNumber;
        try {
            rollNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid input."); return;
        }

        ConsoleStudent found = findByRoll(rollNumber);
        if (found == null) {
            System.out.println("  [!] No student found with Roll No: " + rollNumber); return;
        }

        // Show student before deleting
        System.out.println("\n  Student found:");
        found.printProfile();

        // Confirm deletion
        System.out.print("\n  Confirm remove? (yes / no) : ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            studentList.remove(found);
            System.out.println("  [+] Student (Roll: " + rollNumber + ") removed successfully.");
        } else {
            System.out.println("  [-] Remove cancelled.");
        }
        System.out.println("  ----------------------------------------");
    }

    // ── METHOD 3 : SEARCH STUDENT 
    public void searchStudent() {
        System.out.println("  ----------------------------------------");
        System.out.println("  SEARCH STUDENT");
        System.out.println("  ----------------------------------------");

        if (studentList.isEmpty()) {
            System.out.println("  [!] No students in the system yet."); return;
        }

        System.out.println("  1. Search by Roll Number");
        System.out.println("  2. Search by Name");
        System.out.print("  Choose (1 or 2) : ");
        String type = scanner.nextLine().trim();

        if (type.equals("1")) {
            System.out.print("  Enter Roll Number : ");
            int roll;
            try {
                roll = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid roll number."); return;
            }

            ConsoleStudent found = findByRoll(roll);
            if (found != null) {
                System.out.println("\n  [+] Student Found:");
                found.printProfile();
            } else {
                System.out.println("  [!] No student with Roll No: " + roll);
            }

        } else if (type.equals("2")) {
            System.out.print("  Enter Name (or part of name) : ");
            String query = scanner.nextLine().trim().toLowerCase();

            ArrayList<ConsoleStudent> results = new ArrayList<>();
            for (ConsoleStudent s : studentList) {
                if (s.getName().toLowerCase().contains(query)) {
                    results.add(s);
                }
            }

            if (results.isEmpty()) {
                System.out.println("  [!] No student found with name: " + query);
            } else {
                System.out.println("\n  [+] Found " + results.size() + " result(s):\n");
                for (ConsoleStudent s : results) {
                    s.printProfile();
                    System.out.println();
                }
            }
        } else {
            System.out.println("  [!] Invalid choice. Enter 1 or 2.");
        }
        System.out.println("  ----------------------------------------");
    }

    // ── METHOD 4 : DISPLAY ALL STUDENTS 
    public void displayAllStudents() {
        System.out.println("  ----------------------------------------");
        System.out.println("  ALL STUDENTS   (Total: " + studentList.size() + ")");
        System.out.println("  ----------------------------------------");

        if (studentList.isEmpty()) {
            System.out.println("  [!] No students found. Add students first."); return;
        }

        printTableHeader();
        for (ConsoleStudent s : studentList) {
            System.out.println(s.toString());
        }
        printTableFooter();
        System.out.println("  Total Records : " + studentList.size());
    }

    // ── HELPER : find student by roll number 
    private ConsoleStudent findByRoll(int rollNumber) {
        for (ConsoleStudent s : studentList) {
            if (s.getRollNumber() == rollNumber) return s;
        }
        return null;
    }

    // ── UI HELPERS 
    private void printBanner() {
        System.out.println();
        System.out.println("  +=========================================+");
        System.out.println("  |     STUDENT MANAGEMENT SYSTEM          |");
        System.out.println("  +=========================================+");
        System.out.println();
    }

    private void printMenu() {
        System.out.println("  +----------------------------------------+");
        System.out.println("  |              MAIN MENU                 |");
        System.out.println("  +----------------------------------------+");
        System.out.println("  |  1.  Add Student                       |");
        System.out.println("  |  2.  Remove Student                    |");
        System.out.println("  |  3.  Search Student                    |");
        System.out.println("  |  4.  Display All Students              |");
        System.out.println("  |  5.  Exit                              |");
        System.out.println("  +----------------------------------------+");
    }

    private void printTableHeader() {
        System.out.println();
        System.out.println("  +------+--------------------+-----+--------+----------------------+----------------+-----+-------+-----+------+");
        System.out.println("  | Roll | Name               | Age | Gender | Email                | Course         | Sec | Marks | Grd | Stat |");
        System.out.println("  +------+--------------------+-----+--------+----------------------+----------------+-----+-------+-----+------+");
    }

    private void printTableFooter() {
        System.out.println();
    }
}

//  MAIN CLASS — starting point of the program
public class StudentManagementSystem {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("-console")) {
            SMS sms = new SMS();
            sms.run();
        } else {
            SwingUtilities.invokeLater(() -> {
                StudentManagementGUI gui = new StudentManagementGUI();
                gui.setVisible(true);
            });
        }
    }
}
