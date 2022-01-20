package besucha.backend.service.accessdao;

import besucha.backend.dao.PreferenceDao;
import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.exception.DuplicationException;
import besucha.backend.exception.SectionDoesNotExistException;
import besucha.backend.exception.StudentDoesNotExistException;
import besucha.backend.model.algorithm.Preference;
import besucha.backend.model.algorithm.Section;
import besucha.backend.model.algorithm.Student;
import besucha.backend.repo.PreferenceRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Abstracts access to PreferenceDao objects in database.
 */
@Service
public class PreferenceService implements SaveDao<PreferenceDao> {

	private final PreferenceRepo preferenceRepo;
	private final StudentService studentService;
	private final SectionService sectionService;

	public PreferenceService(PreferenceRepo preferenceRepo, StudentService studentService, SectionService sectionService) {
		this.preferenceRepo = preferenceRepo;
		this.studentService = studentService;
		this.sectionService = sectionService;
	}

	/**
	 * Convert params into PreferenceDao object and save in db.
	 * @param studentId the int value of the student's ID
	 * @param sectionId the int value of section's ID
	 * @param isRequired boolean representing whether class is required
	 * @param preferenceRank rank in list of preferences
	 * @throws DuplicationException if studentid/sectionid pair already exists in db
	 * @return created PreferenceDao object
	 */
	public PreferenceDao save(int studentId, int sectionId, boolean isRequired, int preferenceRank) throws DuplicationException {
		StudentDao studentDao = studentService.getStudentDao(studentId);
		SectionDao sectionDao = sectionService.getSectionDao(sectionId);
		return save(studentDao, sectionDao, isRequired, preferenceRank);
	}

	/**
	 * Convert attributes into PreferenceDao object and save in db.
	 * @param studentDao a StudentDao object
	 * @param sectionDao a SectionDao object
	 * @param isRequired boolean representing whether section is required
	 * @param preferenceRank rank in list of how much student wants to take the course
	 * @throws DuplicationException if studentid/sectionid pair already exists in db
	 * @return created PreferenceDao object
	 */
	public PreferenceDao save(StudentDao studentDao, SectionDao sectionDao, boolean isRequired, int preferenceRank) throws DuplicationException {
		return save(new PreferenceDao(studentDao, sectionDao, isRequired, preferenceRank));
	}

	/**
	 * Save a PreferenceDao object in db.
	 * @param preferenceDao a PreferenceDao object.
	 * @throws DuplicationException if studentid/sectionid pair already exists in db
	 * @return created PreferenceDao object
	 */
	public PreferenceDao save(PreferenceDao preferenceDao) throws DuplicationException {
		if (isValid(preferenceDao)) {
			return preferenceRepo.save(preferenceDao);
		}

		throw new DuplicationException("Student ID "
				+ preferenceDao.getStudent().getStudentId() +
				" or desired preference section ID "
				+ preferenceDao.getSection().getSectionId()
				+ " is a duplicate item. Please correct error in Excel spreadsheet containing student preferences and try again.");

	}

	public List<PreferenceDao> mostRequestedPreferences(int numPreferences) {
		Map<PreferenceDao, Integer> requestedPreferences = new HashMap<>();
		for (PreferenceDao preferenceDao : preferenceRepo.findAll()) {
			if (requestedPreferences.containsKey(preferenceDao)) {
				requestedPreferences.put(preferenceDao, requestedPreferences.get(preferenceDao) + 1);
			} else {
				requestedPreferences.put(preferenceDao, 1);
			}
		}

		return requestedPreferences.entrySet().stream().sorted(Map.Entry.<PreferenceDao, Integer>comparingByValue().reversed()).limit(numPreferences).map(Map.Entry::getKey).collect(Collectors.toList());
	}


	/**
	 * Query the db for PreferenceDao objects and return a list of Preference objects in the correct order, that are associated with the correct Student and Section.
	 * @param student Section to find preferences for.
	 * @param sections List of Section objects needed to create the Preference objects
	 * @throws SectionDoesNotExistException throws exception if desired section cannot be found. See findDesiredSection()
	 * @return a List of Preference objects.
	 */
	public List<Preference> getPreferences(Student student, List<Section> sections) throws SectionDoesNotExistException {
		List<Preference> preferences = new ArrayList<>();

		for (PreferenceDao preferenceDao : preferenceRepo.findAll()) {
			if (preferenceDao.getStudent().getStudentId() == student.getId()) {
				Section desiredSection = findDesiredSection(preferenceDao, sections);
				Preference preference = new Preference(desiredSection, preferenceDao.isRequired());
				preferences.add(preferenceDao.getPreferenceRank(), preference);

			}

		}

		return preferences;
	}

	/**
	 * Finds the desired section. Helper method to get a student's preferences.
	 * @param preferenceDao the PreferenceDao object that we want to complete building
	 * @param sections a List of Section objects that should contain an ID matching a sectionID found in the PreferenceDao object
	 * @return Section object that matches the criteria
	 * @throws SectionDoesNotExistException thrown if a section with the desired ID is not in list
	 */
	Section findDesiredSection(PreferenceDao preferenceDao, List<Section> sections) throws SectionDoesNotExistException {
		for (Section s : sections) {
			if (s.getId() == preferenceDao.getSection().getSectionId()) {
				return s;
			}
		}

		throw new SectionDoesNotExistException("Desired section's ID is not in database. Please close program and try again.");
	}

	/**
	 * Count the number of PreferenceDao objects in database.
	 * @return long representing the count
	 */
	public long count() {
		return preferenceRepo.count();
	}

	/**
	 * Run the isValid method except throw a specific exception if NOT valid
	 * @param studentId int studentId
	 * @param sectionId int sectionId
	 */
	public void isValidAndThrowsExceptionIfInvalid(int studentId, int sectionId) throws StudentDoesNotExistException, SectionDoesNotExistException, DuplicationException {
		if (!isValid(studentId, sectionId)) {
			if (!studentService.doesObjectExistInDb(studentId)) {
				throw new StudentDoesNotExistException("Student with ID " + studentId + " does not exist in database. Therefore, a preference with given student ID could not be saved. Please fix error, close program, and try again.");
			}

			if (!sectionService.doesObjectExistInDb(sectionId)) {
				throw new SectionDoesNotExistException("Section with ID " + sectionId + " does not exist in the database. Therefore, a preference with given section id could not be saved. Please add section ID to course offerings or remove section ID from student preferences in Excel and try again.");
			}

			if (doesObjectExistInDb(studentId, sectionId)) {
				throw new DuplicationException("Preference with student ID " + studentId
				+ " and section ID " + sectionId + " is a duplicate item. Please remove duplicates and try again.");
			}
		}
	}

	/**
	 * Verify that you can create a preferenceDao object given attributes
	 * @param studentId the int student Id
	 * @param sectionId the int section id
	 * @return true if object with given student and section ids could be saved without problem
	 */
	public boolean isValid(int studentId, int sectionId) {
		PreferenceDao temp = new PreferenceDao(new StudentDao(studentId), new SectionDao(sectionId));
		return isValid(temp);
	}

	/**
	 * Verify that preferenceDao object is valid and can be saved
	 * @param preferenceDao PreferenceDao object
	 * @return true if object can be saved without messing things up   
	 */
	public boolean isValid(PreferenceDao preferenceDao) {
		return (studentService.doesObjectExistInDb(preferenceDao.getStudent()) &&
				sectionService.doesObjectExistInDb(preferenceDao.getSection()) &&
				!doesObjectExistInDb(preferenceDao));
	}

	/**
	 * Check to see if a studentId sectionId pair already exists in db for preferences requested
	 * @param studentId int representing student Id
	 * @param sectionId int representing section Id
	 * @return true if Id does exist
	 */
	public boolean doesObjectExistInDb(int studentId, int sectionId)  {
		StudentDao tempStudent = new StudentDao(studentId);
		SectionDao tempSection = new SectionDao(sectionId);
		return doesObjectExistInDb(tempStudent, tempSection);
	}

	/**
	 * Check to see if a studentId sectionId pair already exists in db for preferences requested
	 * @param studentDao StudentDao object
	 * @param sectionDao SectionDao object
	 * @return true if student id and section id already exist in db
	 */
	public boolean doesObjectExistInDb(StudentDao studentDao, SectionDao sectionDao)  {
		return doesObjectExistInDb(new PreferenceDao(studentDao, sectionDao));
	}


	/**
	 * Check to see if a studentId sectionId pair already exists in db for preferences requested
	 * @param preferenceDao PreferenceDao object
	 * @return true if student id and section id already exist in db
	 */
	public boolean doesObjectExistInDb(PreferenceDao preferenceDao) {
		for (PreferenceDao p : preferenceRepo.findAll()) {
			if (preferenceDao.equals(p)) {
				return true;
			}
		}
		
		return false;
	}


}
