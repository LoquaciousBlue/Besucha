package besucha.backend.dao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * PreferenceDao object that manages the "preference" table in MySQL.
 */
@Entity
@Table(name = "preference")
public class PreferenceDao {

    /**
     * PreferenceDao ID.
     */
    @Id
    @Column(name = "preference_id")
    @GeneratedValue
    private int preferenceId;


    /**
     * StudentDao who has a preference. Join on MySQL column "student_id".
     */
    @ManyToOne
    @JoinColumn(name = "student_id")
    @NotNull
    private StudentDao student;

    /** 
     * SectionDao that student wants to be enrolled in. Join on MySQL column "section_id".
     */
    @ManyToOne
    @JoinColumn(name = "section_id")
    @NotNull
    private SectionDao section;

    /**
     * Boolean, checked if section is a required course.
     */
    @Column(name = "is_required")
    private boolean isRequired;

    /**
     * PreferenceDao rank as an int. The lower the number, the more the student wants to take the course.
     */
    @Column(name = "preference_rank")
    private int preferenceRank;

    public PreferenceDao() {}

    public PreferenceDao(StudentDao student, SectionDao section, boolean isRequired, int preferenceRank) {
        this.student = student;
        this.section = section;
        this.isRequired = isRequired;
        this.preferenceRank = preferenceRank;
    }

    public PreferenceDao(StudentDao studentDao, SectionDao sectionDao) {
        this(studentDao, sectionDao, false, 0);
    }

    /**
     * Getter for preference ID.
     * @return int representing id (mostly for MySQL data handling purposes).
     */
    public int getPreferenceId() {
        return preferenceId;
    }

    /**
     * Setter for preference ID.
     * @param preferenceId int representing the new id (mostly for MySQL purposes).
     */
    public void setPreferenceId(int preferenceId) {
        this.preferenceId = preferenceId;
    }


    /**
     * Determine whether or not a section is required.
     * @return true if student MUST be enrolled in a section.
     */
    public boolean isRequired() {
        return isRequired;
    }


    /**
     * Getter for preferenceRank, or how much a student wants to take a class.
     * The lower the number, the more the student wants to take the class. Lowest number allowed is 0.
     * @return int representing how much student wants to take the class.
     */
    public int getPreferenceRank() {
        return preferenceRank;
    }

    /**
     * Setter for preference rank, or how much a student wants to take a class.
     * The lower the int, the more the student wants to take the class.
     * Lowest number allowed is 0, and no duplicates allowed (i.e. student cannot have 2 classes with preferenceRank of 2. It is assumed that this is handled at a higher level.
     * @param preferenceRank int representing the preferenceRank
     */
    public void setPreferenceRank(int preferenceRank) {
        this.preferenceRank = preferenceRank;
    }

    /**
     * Getter for the student who has a preference.
     * @return the student as a Student object.
     */
    public StudentDao getStudent() {
        return student;
    }


    /**
     * Getter for section that a student wants to be enrolled in.
     * @return section as a SectionDao object.
     */
    public SectionDao getSection() {
        return section;
    }


    @Override
    public String toString() {
        return "PreferenceDao{" +
                "preferenceId=" + preferenceId +
                ", student=" + student.getStudentId() +
                ", section=" + section.getSectionId() +
                ", isRequired=" + isRequired +
                ", preferenceRank=" + preferenceRank +
                '}';
    }

    /**
     * Two PreferenceDao objects are equal if they have the same studentId and sectionId.
     * @param o the object to compare to
     * @return true if a match
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreferenceDao that = (PreferenceDao) o;

        if (!student.equals(that.student)) return false;
        return section.equals(that.section);
    }


}
