package besucha.backend.model.algorithm;

import besucha.backend.exception.DuplicationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a StudentList object used by CourseSystem.
 */
public class StudentList {

	private List<Student> students;

	public StudentList() {
		this.students = new ArrayList<>();
	}

	public StudentList(List<Student> students) {
		this.students = students;
	}

	/**
	 * Add new student.
	 * @param s student to be added to the system
	 * @throws DuplicationException if student already contained within list
	 */
	public void addStudent(Student s) throws DuplicationException {
		if (!students.contains(s)) {
			students.add(s);
		} else {
			throw new DuplicationException("Student ID" + s.getName() + " is a duplicate. Please remove duplicates from Excel spreadsheet and try again.");
		}
	}

	/**
	 * Get all students.
	 * @return list of students
	 */
	public List<Student> getStudents() {
		return this.students;
	}

	/**
	 * Get the size of the list.
	 * @return integer size of list
	 */
	public int size() {
		return students.size();
	}

	@Override
	public String toString() {
		return "StudentList{" +
				"students=" + students +
				'}';
	}
}

