
public class Student {

    // ─── Attributes 
    private int    rollNumber;
    private String name;
    private int    age;
    private String gender;
    private String email;
    private String phone;
    private String course;
    private String section;
    private double marks;       // out of 100
    private String grade;       // auto-computed from marks
    private String status;      // PASS / FAIL

    // ─── Constructor 
    /**
     * Creates a Student with all details.
     * Grade and status are automatically computed from marks.
     *
     * @param rollNumber unique student roll number
     * @param name       full name of the student
     * @param age        age of the student
     * @param gender     gender (Male / Female / Other)
     * @param email      email address
     * @param phone      10-digit phone number
     * @param course     course/program name (e.g. B.Tech CSE)
     * @param section    section (e.g. A, B, C)
     * @param marks      marks obtained out of 100
     */
    public Student(int rollNumber, String name, int age, String gender,
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
        setMarks(marks);   // computes grade + status automatically
    }

    // ─── Grade & Status Logic 
    /**
     * Computes grade based on marks (out of 100).
     *
     * Marks Range  =>  Grade
     * 90 - 100     =>  A+  (Outstanding)
     * 80 - 89      =>  A   (Excellent)
     * 70 - 79      =>  B+  (Very Good)
     * 60 - 69      =>  B   (Good)
     * 50 - 59      =>  C   (Average)
     * 40 - 49      =>  D   (Below Average)
     *  0 - 39      =>  F   (Fail)
     */
    private String computeGrade(double marks) {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B+";
        if (marks >= 60) return "B";
        if (marks >= 50) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    /** Returns "PASS" if marks >= 40, otherwise "FAIL". */
    private String computeStatus(double marks) {
        return marks >= 40 ? "PASS" : "FAIL";
    }

    // ─── Getters 
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

    // ─── Setters 
    public void setRollNumber(int rollNumber)   { this.rollNumber = rollNumber; }
    public void setName(String name)            { this.name = name;             }
    public void setAge(int age)                 { this.age = age;               }
    public void setGender(String gender)        { this.gender = gender;         }
    public void setEmail(String email)          { this.email = email;           }
    public void setPhone(String phone)          { this.phone = phone;           }
    public void setCourse(String course)        { this.course = course;         }
    public void setSection(String section)      { this.section = section;       }

    /**
     * Sets marks and automatically re-computes grade and status.
     * Always use this method to update marks.
     */
    public void setMarks(double marks) {
        this.marks  = marks;
        this.grade  = computeGrade(marks);
        this.status = computeStatus(marks);
    }

    // ─── Display Methods 
    /**
     * Prints a full, formatted student profile card to the console.
     */
    public void printProfile() {
        System.out.println("  +-----------------------------------------+");
        System.out.println("  |           STUDENT PROFILE               |");
        System.out.println("  +-----------------------------------------+");
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

    /**
     * Returns a compact one-line table row representation.
     * Used when displaying all students in a table.
     */
    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-20s | %-3d | %-6s | %-20s | %-18s | %-4s | %5.1f | %-3s | %-4s |",
            rollNumber, name, age, gender, email, course, section, marks, grade, status
        );
    }

    // ─── CSV Serialization (for File Storage) 
    /**
     * Converts the student object to a CSV string for saving to a file.
     * Format: roll,name,age,gender,email,phone,course,section,marks
     */
    public String toCSV() {
        return rollNumber + "," + name    + "," + age     + "," +
               gender     + "," + email   + "," + phone   + "," +
               course      + "," + section + "," + marks;
    }

    /**
     * Creates a Student object from a CSV string (read from file).
     * Returns null if the line is malformed.
     *
     * @param csv a comma-separated line from the data file
     * @return Student object, or null on parse error
     */
    public static Student fromCSV(String csv) {
        try {
            String[] p = csv.split(",", 9);
            return new Student(
                Integer.parseInt(p[0].trim()),   // rollNumber
                p[1].trim(),                      // name
                Integer.parseInt(p[2].trim()),   // age
                p[3].trim(),                      // gender
                p[4].trim(),                      // email
                p[5].trim(),                      // phone
                p[6].trim(),                      // course
                p[7].trim(),                      // section
                Double.parseDouble(p[8].trim())  // marks
            );
        } catch (Exception e) {
            System.out.println("  [Warning] Skipping malformed CSV line: " + csv);
            return null;
        }
    }

    // ─── Equality Check 
    /**
     * Two students are considered equal if they share the same roll number.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student other = (Student) obj;
        return this.rollNumber == other.rollNumber;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(rollNumber);
    }

    // ─── Main (Quick Test) 
    /**
     * Quick demo — remove this main() when integrating with
     * StudentManagementSystem.java
     */
    public static void main(String[] args) {

        // Create two students
        Student s1 = new Student(101, "Priya Sharma", 20, "Female",
                                  "priya@email.com", "9876543210",
                                  "B.Tech CSE", "A", 91.5);

        Student s2 = new Student(102, "Arjun Mehta", 21, "Male",
                                  "arjun@email.com", "9812345678",
                                  "B.Tech ECE", "B", 35.0);

        // Print full profiles
        s1.printProfile();
        System.out.println();
        s2.printProfile();

        // Test CSV round-trip
        System.out.println("\n  CSV Export:");
        System.out.println("  " + s1.toCSV());

        System.out.println("\n  CSV Import:");
        Student loaded = Student.fromCSV(s1.toCSV());
        if (loaded != null) loaded.printProfile();

        // Test setMarks updates grade automatically
        System.out.println("\n  Updating Arjun's marks from 35 to 72...");
        s2.setMarks(72);
        System.out.println("  New Grade  : " + s2.getGrade());
        System.out.println("  New Status : " + s2.getStatus());
    }
}