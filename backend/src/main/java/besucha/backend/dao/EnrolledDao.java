package besucha.backend.dao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * An EnrolledDao object that manages the "enrolled" table in MySQL.
 */
@Entity
@Table(name = "enrolled")
public class EnrolledDao {

	@EmbeddedId
	StudentSectionKey enrolledId;

	@ManyToOne
	@MapsId("studentId")
	@JoinColumn(name = "student_id")
	@NotNull
	private StudentDao student;

	@ManyToOne
	@MapsId("sectionId")
	@JoinColumn(name = "section_id")
	@NotNull
	private SectionDao section;


	public EnrolledDao() {}

	public EnrolledDao(StudentSectionKey enrolledId) {
		this();
		this.enrolledId = enrolledId;
	}

	public EnrolledDao(StudentSectionKey enrolledId, StudentDao student, SectionDao section) {
		this(enrolledId);
		this.enrolledId = enrolledId;
		this.student = student;
		this.section = section;
	}

	public EnrolledDao(StudentDao student, SectionDao section) {
		this(new StudentSectionKey(student.getStudentId(), section.getSectionId()), student, section);
	}

	/**
	 * Getter for StudentSection Key.
	 * @return
	 */
	public StudentSectionKey getEnrolledId() {
		return enrolledId;
	}

	/**
	 * Getter for StudentDao object
	 * @return StudentDao object
	 */
	public StudentDao getStudent() {
		return student;
	}

	/**
	 * Getter for SectionDao object.
	 * @return SectionDao object
	 */
	public SectionDao getSection() {
		return section;
	}


	/**
	 * Two EnrolledDao objects are the same if their StudentSectionKey is the same.
	 * @param o the Object to compare to
	 * @return true if a match
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EnrolledDao that = (EnrolledDao) o;

		return enrolledId.equals(that.enrolledId);
	}

}
