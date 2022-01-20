package besucha.backend.model.algorithm;

import besucha.backend.exception.DuplicationException;

import java.util.List;

/**
 * Manages CourseSystem that the algorithm runs on.
 */
public class CourseSystem {

	// Attributes
	private SectionList allSections;
	private StudentList allStudents;
	private final double MAX_CREDITS = 5;	// max credits a student is eligible to enroll in

	// Constructor(s)
	/**
	 * Basic Constructor
	 */
	public CourseSystem() {
		this.allSections = new SectionList();
		this.allStudents = new StudentList();
	}

	/**
	 * Create course system given already complete SectionList and StudentList objects, but enrollment has not been run yet.
	 * @param sectionList a List of Section objects
	 * @param studentList a List of Student objects to enroll in courses
	 */
	public CourseSystem(SectionList sectionList, StudentList studentList) {
		this();
		this.allSections = sectionList;
		this.allStudents = studentList;
	}

	// Methods

	/**
	 * Enroll a student in a section, assuming they are eligible.
	 * @param student the student to enroll
	 * @param section the section to enroll the student in
	 * @return true if student successfully enrolled
	 */
	public boolean enroll(Student student, Section section) {
		if (canEnroll(student, section)) {
			section.getEnrolled().add(student);
			return true;
		}

		return false;
	}


	/**
	 * Adds student to the waitlist.
	 * @param stud Student object
	 * @param sect Section object
	 */
	public void addToWaitlist(Student stud, Section sect){
		List<Student> waitlist = sect.getWaitlist();
		if (!waitlist.contains(stud))
			waitlist.add(stud);
	}

	/**
	 * Determine if a student can enroll in section.
	 * @param student student in system
	 * @param section section in system
	 * @return true if both student and section are in course system, student is not already enrolled, the student has space, and the section has space
	 */
	public boolean canEnroll(Student student, Section section) {
		return (allSections.getSections().contains(section)
				&& allStudents.getStudents().contains(student)
				&& !isEnrolled(student, section)
				&& studentHasSpace(student)
				&& sectionHasSpace(section));
	}

	/**
	 * For sake of continuity, include hasOpenSeat in the course system. This method is also in Section.java.
	 * Determine if a section still has open seats, or is currently under capacity for enrollment.
	 * @param section the section to evaluate
	 * @return true if seats filled in section less than class capacity
	 */
	public boolean sectionHasSpace(Section section) {
		return (section.hasOpenSeat());
	}


	/**
	 * Determine if student already has max number of credits
	 * @param student the student to check
	 * @return true if student already has max number of credits
	 */
	public boolean hasMaxCredits(Student student) {
		return (getNumEnrolledCredits(student) >= MAX_CREDITS);
	}

	/**
	 * Make sure student has space in their schedule, i.e. they can enroll in more classes.
	 * @param student the student to evaluate
	 * @return true if student can enroll in more classes
	 */
	public boolean studentHasSpace(Student student) {
		return (getNumEnrolledCredits(student) < MAX_CREDITS);
	}


	/**
	 * Calculate the number of credits (sections) a student is enrolled in.
	 * @param student the student to evaluate
	 * @return integer representing number of sections they are enrolled in
	 */
	public int getNumEnrolledCredits(Student student) {
		int enrolledCredits = 0;

		for (Section section : allSections.getSections())
			if (isEnrolled(student, section))
				enrolledCredits+= section.getCreditWeight();

		return enrolledCredits;
	}

	/**
	 * Determine if given student in course system is enrolled in given section (also in course system).
	 * @param student the student to check
	 * @param section the section that the student may or may not be enrolled in
	 * @return true if student is already enrolled in section
	 */
	public boolean isEnrolled(Student student, Section section) {
		return section.getEnrolled().contains(student);
	}

	/**
	 * Adds a student to the System
	 * @param s the student to add
	 * @throws DuplicationException if the student already exists
	 */
	public void addStudent(Student s) throws DuplicationException{
		allStudents.addStudent(s);
	}


	/**
	 * Adds a section to the system.
	 * @param s the section to add
	 * @throws DuplicationException exception thrown if Section already exists
	 */
	public void addSection(Section s) throws DuplicationException {
		allSections.addSection(s);
	}


	/**
	 * Getter for the list of all students in the System
	 * @return a list of all students
	 */
	public List<Student> getAllStudents(){
		return allStudents.getStudents();
	}

	/**
	 * Getter for the list of all sections in the System
	 * @return a list of sections
	 */
	public List<Section> getAllSections() {
		return allSections.getSections();
	}

	@Override
	public String toString() {
		return "CourseSystem{" +
				"allSections=" + allSections +
				", allStudents=" + allStudents +
				", MAX_CREDITS=" + MAX_CREDITS +
				'}';
	}
}

