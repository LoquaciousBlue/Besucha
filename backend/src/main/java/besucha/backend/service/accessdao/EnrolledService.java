package besucha.backend.service.accessdao;

import besucha.backend.dao.EnrolledDao;
import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.dao.StudentSectionKey;
import besucha.backend.repo.EnrolledRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstracts access to EnrolledDao objects in db.
 */
@Service
public class EnrolledService implements SaveDao<EnrolledDao> {

	private final EnrolledRepo enrolledRepo;
	private final StudentService studentService;
	private final SectionService sectionService;

	public EnrolledService(EnrolledRepo enrolledRepo, StudentService studentService, SectionService sectionService) {
		this.enrolledRepo = enrolledRepo;
		this.studentService = studentService;
		this.sectionService = sectionService;
	}

	/**
	 * Given IDs of student and section, create and save EnrolledDao object in db
	 * @param studentId the int id of the student
	 * @param sectionId the int id of the section
	 * @return the saved EnrolledDao object
	 */
	public EnrolledDao save(int studentId, int sectionId) {
		StudentDao studentDao = studentService.getStudentDao(studentId);
		SectionDao sectionDao = sectionService.getSectionDao(sectionId);
		return save(studentDao, sectionDao);
	}

	/**
	 * Given params to build an enrolledDao object, create and save it in the repo.
	 * @param studentDao the studentDao to be enrolled
	 * @param sectionDao the sectionDao that the student will be enrolled in
	 * @return the saved EnrolledDao object
	 */
	public EnrolledDao save(StudentDao studentDao, SectionDao sectionDao) {
		StudentSectionKey key = new StudentSectionKey(studentDao.getStudentId(), sectionDao.getSectionId());
		EnrolledDao enrolledDao = new EnrolledDao(key, studentDao, sectionDao);
		return save(enrolledDao);

	}

	/**
	 * Given EnrolledDao object, save it in repo.
	 * @param enrolledDao the object to save
	 * @return saved object
	 */
	public EnrolledDao save(EnrolledDao enrolledDao) {
		return enrolledRepo.save(enrolledDao);
	}

	/**
	 * Given student's ID, find the corresponding studentDao object and get the SectionDao objects they are enrolled in.
	 * @param studentId an int representing the student's id
	 * @return List of SectionDao objects student is enrolled in
	 */
	public List<SectionDao> getEnrolledSections(int studentId) {
		StudentDao studentDao = studentService.getStudentDao(studentId);
		return getEnrolledSections(studentDao);
	}

	/**
	 * Given a StudentDao object, find the SectionDao objects the student is enrolled in.
	 * @param studentDao studentDao object
	 * @return a List of SectionDao objects
	 */
	public List<SectionDao> getEnrolledSections(StudentDao studentDao) {
		List<SectionDao> sectionDaoList = new ArrayList<>();

		for (EnrolledDao enrolledDao : enrolledRepo.findAll()) {
			if (studentDao.equals(enrolledDao.getStudent())) {
				sectionDaoList.add(enrolledDao.getSection());
			}
		}

		return sectionDaoList;
	}
}
