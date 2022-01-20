package besucha.backend.dao;

import javax.persistence.*;

/**
 * WaitlistDao object that manages the "waitlist" table in MySQL.
 */
@Entity
@Table(name = "waitlist")
public class WaitlistDao {

	@EmbeddedId
	StudentSectionKey waitlistId;

	@ManyToOne
	@MapsId("studentId")
	@JoinColumn(name = "student_id")
	private StudentDao student;

	@ManyToOne
	@MapsId("sectionId")
	@JoinColumn(name = "section_id")
	private SectionDao section;

	@Column(name = "position")
	private int position;

	public WaitlistDao() {}

	public WaitlistDao(StudentSectionKey waitlistId) {
		this();
		this.waitlistId = waitlistId;
	}


	public WaitlistDao(StudentSectionKey waitlistId, StudentDao student, SectionDao section, int position) {
		this(waitlistId);
		this.student = student;
		this.section = section;
		this.position = position;
	}

	public WaitlistDao(StudentDao student, SectionDao section, int position) {
		this(new StudentSectionKey(student.getStudentId(), section.getSectionId()),
				student, section, position);
	}

	public StudentSectionKey getWaitlistId() {
		return waitlistId;
	}

	public void setWaitlistId(StudentSectionKey waitlistId) {
		this.waitlistId = waitlistId;
	}

	public StudentDao getStudent() {
		return student;
	}

	public void setStudent(StudentDao student) {
		this.student = student;
	}

	public SectionDao getSection() {
		return section;
	}

	public void setSection(SectionDao section) {
		this.section = section;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "WaitlistDao{" +
				"waitlistId=" + waitlistId +
				", student=" + student +
				", section=" + section +
				", position=" + position +
				'}';
	}

	/**
	 * Two WaitlistDao items are equal if the StudentSectionKey is equal, aka both the student Id and section Id are the same.
	 * @param o the Object to compare to
	 * @return true if same object
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		WaitlistDao that = (WaitlistDao) o;

		return waitlistId.equals(that.waitlistId);
	}

	@Override
	public int hashCode() {
		return waitlistId.hashCode();
	}
}
