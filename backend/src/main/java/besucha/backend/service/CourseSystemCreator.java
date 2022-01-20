package besucha.backend.service;

import besucha.backend.exception.PreferenceDoesNotExistException;
import besucha.backend.exception.SectionDoesNotExistException;
import besucha.backend.exception.StudentDoesNotExistException;
import besucha.backend.model.algorithm.*;
import besucha.backend.service.accessdao.PreferenceService;
import besucha.backend.service.accessdao.SectionService;
import besucha.backend.service.accessdao.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Creates a CourseSystem by querying the database.
 */
@Service
public class CourseSystemCreator {

	private final SectionService sectionService;
	private final StudentService studentService;
	private final PreferenceService preferenceService;

	public CourseSystemCreator(SectionService sectionService, StudentService studentService, PreferenceService preferenceService) {
		this.sectionService = sectionService;
		this.studentService = studentService;
		this.preferenceService = preferenceService;
	}

	/**
	 * Query the db to create the CourseSystem, which the algorithm can run on.
	 * @return CourseSystem that can run data
	 * @throws SectionDoesNotExistException thrown if no SectionDao objects found in db
	 * @throws StudentDoesNotExistException thrown if no StudentDao objects found in db
	 * @throws PreferenceDoesNotExistException thrown if no PreferenceDao objects found in db.
	 */
	public CourseSystem createCourseSystem() throws StudentDoesNotExistException,
			SectionDoesNotExistException, PreferenceDoesNotExistException {

		verifyPossibleToCreateCourseSystem();



		List<Section> sections = sectionService.getAllSections();
		List<Student> students = studentService.getAllStudents();

		for (Student s : students) {	// set student preferences
			s.setPreferences(preferenceService.getPreferences(s, sections));
		}

		return new CourseSystem(new SectionList(sections), new StudentList(students));
	}

	/**
	 * Verify that there is at least one student, section, and preference in db.
	 * @throws SectionDoesNotExistException thrown if num section in db == 0
	 * @throws StudentDoesNotExistException thrown if num students in db == 0
	 * @throws PreferenceDoesNotExistException thrown if num preferences in db == 0
	 * @return true if there is enough data to create a CourseSystem object to run the algorithm on.
	 */
	public boolean verifyPossibleToCreateCourseSystem() throws
			SectionDoesNotExistException,
			StudentDoesNotExistException,
			PreferenceDoesNotExistException {
		if (sectionService.count() == 0) {
			throw new SectionDoesNotExistException("No sections in database. Please close program and try again.");
		}

		if (studentService.count() == 0) {
			throw new StudentDoesNotExistException("No students in database. Please close program and try again.");
		}

		if (preferenceService.count() == 0) {
			throw new PreferenceDoesNotExistException("No preferences in database. Please close program and try again.");
		}

		return true;
	}
}
