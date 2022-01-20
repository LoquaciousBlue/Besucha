package besucha.backend.dao;

import besucha.backend.model.algorithm.Seniority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDao object that manages the "student" object in MySQL.
 */
@Entity
@Table(name = "student")
public class StudentDao {

    /** StudentDao id, as an int. Id of table. */
    @Id
    @Column(name = "student_id")
    private int studentId;
    
    /** StudentDao name, as a string. */
    private String name;
    
    /** StudentDao seniority, as a Seniority object */
    private Seniority seniority;

    /** String representing student's email address */
    private String email;


    /** Enrolled, as a set of sections. Represents the sections a student is enrolled in.
     * In MySQL, this is represented as a table named "enrolled" containing both a student ID and section ID.
     */
    @ManyToMany
    @JoinTable(
            name = "enrolled",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private List<SectionDao> enrolled;

    /**
     * Waitlist, as a set of sections. Represents the sections a student is waitlisted in.
     * In MySQL, this is represented as a table named "waitlist" containing both a student ID and section ID.
     */
    @ManyToMany
    @JoinTable(
            name = "waitlist",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private List<SectionDao> waitlist;

    /**
     * A List of PreferenceDao objects.
     * Has @OneToMany tag.
     */
    @OneToMany(mappedBy = "student")
    private List<PreferenceDao> preferences;

    public StudentDao() {
        enrolled = new ArrayList<>();
        waitlist = new ArrayList<>();
        preferences = new ArrayList<>();
    }

    public StudentDao(int studentId, String name, Seniority seniority, String email) {
        this();
        this.studentId = studentId;
        this.name = name;
        this.seniority = seniority;
        this.email = email;
    }

	public StudentDao(int studentId) {
        this.studentId = studentId;
	}

	/**
     * Getter for student ID
     * @return int the student id
     */
    public int getStudentId() {
        return studentId;
    }


    /**
     * Getter for student name
     * @return string representing student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for student name
     * @param name the name of the student
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for student seniority, as a Seniority object.
     * @return Seniority object representing student's seniority.
     */
    public Seniority getSeniority() {
        return this.seniority;
    }

    /**
     * Setter for student seniority, as a Seniority object.
     * @param seniority the Seniority object representing student's seniority
     */
    public void setSeniority(Seniority seniority) {
        this.seniority = seniority;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    /**
     * Two objects are equal if their student id's match.
     * @param o the Object to compare to
     * @return true if match
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentDao that = (StudentDao) o;

        return studentId == that.studentId;
    }

    @Override
    public int hashCode() {
        return studentId;
    }

    @Override
    public String toString() {
        return "StudentDao{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", seniority=" + seniority +
                '}';
    }
}
