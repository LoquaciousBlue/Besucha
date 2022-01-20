package besucha.backend.service.accessdao;

import besucha.backend.dao.StudentDao;
import besucha.backend.exception.DuplicationException;
import besucha.backend.exception.SeniorityInvalidException;
import besucha.backend.model.algorithm.Seniority;
import besucha.backend.model.algorithm.Student;
import besucha.backend.repo.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstracts access to StudentDao objects in database.
 */
@Service
public class StudentService implements SaveDao<StudentDao> {

	private final StudentRepo studentRepo;

	public StudentService(StudentRepo studentRepo) {
		this.studentRepo = studentRepo;
	}

	/** todo: add exception here
	 * Get a StudentDao object
	 * @param studentId integer id of StudentDao object
	 * @return StudentDao object with given id
	 */
	public StudentDao getStudentDao(int studentId) {
		return studentRepo.findStudentDaoByStudentId(studentId);
	}


	/**
	 * Create and save a StudentDao object to the repo.
	 * @param studentId id of student to be created as int
	 * @param name name of student as String
	 * @param seniority student seniority as String, which will be converted into Seniority enum. Seniority must be exactly: "Senior", "Junior", "Sophomore", "Freshman"
	 * @param email student email as String
	 * @throws SeniorityInvalidException throws exception of Seniority is not one of aforementioned values.
	 * @return StudentDao object created
	 */
	public StudentDao save(int studentId, String name, String seniority, String email) throws SeniorityInvalidException, DuplicationException {
		Seniority seniorityEnum;
		try {
			seniorityEnum = Seniority.valueOf(seniority);
		} catch(Exception e) {
			throw new SeniorityInvalidException("Given seniority is '" + seniority +
					"'. Seniority must be Senior, Junior, Sophomore, or Freshman.");
		}
		return save(studentId, name, seniorityEnum, email);
	}

	/**
	 * Create and save a StudentDao object to the repo.
	 * @param studentId id of student to be created as int
	 * @param name name of student as String
	 * @param seniority student seniority as Seniority object
	 * @param email email address as String
	 * @return created StudentDao object
	 */
	public StudentDao save(int studentId, String name, Seniority seniority, String email) throws DuplicationException {
		return save(new StudentDao(studentId, name, seniority, email));
	}

	/**
	 * Save StudentDao object.
	 * @param studentDao object to save.
	 * @return created StudentDao object
	 */
	public StudentDao save(StudentDao studentDao) throws DuplicationException {
		if (doesObjectExistInDb(studentDao)) {
			throw new DuplicationException("Student ID " +
					studentDao.getStudentId() + 
					" is a duplicate. Please remove from Excel spreadsheet and try again.");
		}
		return studentRepo.save(studentDao);
	}

	public long count() {
		return studentRepo.count();
	}

	/**
	 * Query db for all StudentDao objects (and PreferenceDao objects), convert to Student objects and return as List of Student objects.
	 * @return a List of Student objects that are located in the db.
	 */
	public List<Student> getAllStudents() {
		List<Student> studentList = new ArrayList<>();

		for (StudentDao studentDao : studentRepo.findAll()) {
			studentList.add(new Student(studentDao));
		}

		return studentList;
	}


	/**
	 * Check if a StudentDao object already exists in db with same id
	 * @param studentDao the StudentDao object
	 * @return true if it already exists
	 */
	public boolean doesObjectExistInDb(StudentDao studentDao)  {
		return doesObjectExistInDb(studentDao.getStudentId());
	}

	/**
	 * Check if a StudentDao object already exists in db with same id
	 * @param studentId int studentId
	 * @return true if it already exists
	 */
	public boolean doesObjectExistInDb(int studentId) {
		return studentRepo.existsById(studentId);
	}

}
