package besucha.backend.service;

import besucha.backend.model.algorithm.CourseSystem;
import besucha.backend.model.algorithm.Section;
import besucha.backend.model.algorithm.Student;
import besucha.backend.service.accessdao.EnrolledService;
import besucha.backend.service.accessdao.WaitlistService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Save results in CourseSystem after Algorithm has been run.
 */
@Service
public class CourseSystemSaver {

	private final EnrolledService enrolledService;
	private final WaitlistService waitlistService;

	public CourseSystemSaver(EnrolledService enrolledService, WaitlistService waitlistService) {
		this.enrolledService = enrolledService;
		this.waitlistService = waitlistService;
	}

	/**
	 *  Given enrollment results, save all results (as EnrolledDao and WaitlistDao objects) in db
	 * @param courseSystem CourseSystem object after enrollment has completed
	 */
	public void saveEnrollmentResults(CourseSystem courseSystem) {
		for (Section section : courseSystem.getAllSections()) {
			saveEnrolled(section.getEnrolled(), section);
			saveWaitlist(section.getWaitlist(), section);
		}
	}

	/**
	 * Given section and list of students to place on waitlist, save in db.
	 * @param waitlistedStudents a List of Student objects used by CourseSystem
	 * @param section the Section object used in the CourseSystem
	 */
	protected void saveWaitlist(List<Student> waitlistedStudents, Section section) {
		for (int i = 0; i < waitlistedStudents.size(); i++) {
			waitlistService.save(waitlistedStudents.get(i).getId(), section.getId(), i);
		}
	}

	/**
	 * Given the section and list of students to enroll, save in db
	 * @param enrolledStudents a List of Student objects used by the CourseSystem
	 * @param section the Section object used in the CourseSystem algorithm
	 */
	protected void saveEnrolled(List<Student> enrolledStudents, Section section) {
		for (Student student : enrolledStudents) {
			saveEnrolled(student, section);
		}
	}

	/**
	 * Save a single Student and Section as EnrolledDao object in db
	 * @param student a Student object
	 * @param section a Section object
	 */
	protected void saveEnrolled(Student student, Section section) {
		enrolledService.save(student.getId(), section.getId());
	}
}
