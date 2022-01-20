package besucha.backend.dao;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * StudentSectionKey is the composite key for EnrolledDao and WaitlistDao.
 */
@Embeddable
public class StudentSectionKey implements Serializable {

	@Column(name = "student_id")
	private int studentId;

	@Column(name = "section_id")
	private int sectionId;


	public StudentSectionKey() {
	}

	public StudentSectionKey(int studentId, int sectionId) {
		this.studentId = studentId;
		this.sectionId = sectionId;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StudentSectionKey that = (StudentSectionKey) o;

		if (studentId != that.studentId) return false;
		return sectionId == that.sectionId;
	}


	@Override
	public String toString() {
		return "StudentSectionKey{" +
				"studentId=" + studentId +
				", sectionId=" + sectionId +
				'}';
	}


}
