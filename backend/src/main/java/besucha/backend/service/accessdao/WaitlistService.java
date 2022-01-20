package besucha.backend.service.accessdao;

import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.dao.WaitlistDao;
import besucha.backend.repo.WaitlistRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstracts access to WaitlistDao objects in database.
 */
@Service
public class WaitlistService implements SaveDao<WaitlistDao> {

	private final WaitlistRepo waitlistRepo;
	private final StudentService studentService;
	private final SectionService sectionService;

	public WaitlistService(WaitlistRepo waitlistRepo, StudentService studentService, SectionService sectionService) {
		this.waitlistRepo = waitlistRepo;
		this.studentService = studentService;
		this.sectionService = sectionService;
	}


	/**
	 * Build a Map correlating SectionDao objects and the number of students on the list
	 * @return a Map with keys of SectionDao objects and values of type int, representing num students on the waitlist
	 */
	public Map<SectionDao, Integer> buildWaitlistMap() {
		Map<SectionDao, Integer> map = new HashMap<>();
		
		for (WaitlistDao waitlistDao : waitlistRepo.findAll()) {
			SectionDao key = waitlistDao.getSection();
			if (!map.containsKey(key)) {
				map.put(key, 1);
			} else {
				map.put(key, map.get(key) + 1);
			}
		}

		return map;
	}



	/**
	 * Given ids as params, create and save WaitlistDao object
	 * @param studentId the id of the student as int
	 * @param sectionId the id of the section as int
	 * @param position the position of student on waitlist as int
	 * @return saved WaitlistDao object
	 */
	public WaitlistDao save(int studentId, int sectionId, int position) {
		StudentDao studentDao = studentService.getStudentDao(studentId);
		SectionDao sectionDao = sectionService.getSectionDao(sectionId);
		return save(studentDao, sectionDao, position);
	}

	/**
	 * Create waitlistDao object given attributes, and save to db.
	 * @param student as StudentDao object
	 * @param section as SectionDao object
	 * @param position position (including 0, and starting from 0) on the waitlist
	 * @return saved WaitlistDao object
	 */
	public WaitlistDao save(StudentDao student, SectionDao section, int position) {
		return save(new WaitlistDao(student, section, position));
	}

	/**
	 * Save WaitlistDao object to db.
	 * @param waitlistDao object to save.
	 * @return saved WaitlistDao object
	 */
	public WaitlistDao save(WaitlistDao waitlistDao) {
		return waitlistRepo.save(waitlistDao);
	}

	/**
	 * Count the number of WaitlistDao objects in database.
	 * @return long representing the count
	 */
	public long count() {
		return waitlistRepo.count();
	}

	/**
	 * Given a SectionDao object, get all sections they are waitlisted in.
	 * @param sectionDao the StudentDao object
	 * @return a List of SectionDao objects
	 */
	public List<StudentDao> getWaitlistedStudentsBySection(SectionDao sectionDao) {
		List<StudentDao> waitlistedStudents = new ArrayList<>();

		for (WaitlistDao waitlistDao : waitlistRepo.findAll()) {
			if (sectionDao.equals(waitlistDao.getSection())) {
				waitlistedStudents.add(waitlistDao.getStudent());
			}
		}

		return waitlistedStudents;
	}

	/**
	 * Given student Id, find the corresponding StudentDao object and the SectionDao objects student is waitlisted in
	 * @param studentId int studentId
	 * @return List of SectionDao objects student is waitlisted in
	 */
	public List<SectionDao> getWaitlistedSectionsByStudent(int studentId) {
		StudentDao studentDao = studentService.getStudentDao(studentId);
		return getWaitlistedSectionsByStudent(studentDao);
	}

	/**
	 * Given StudentDao object, get SectionDao objects student is waitlisted in
	 * @param studentDao the StudentDao object
	 * @return a List of SectionDao objects
	 */
	public List<SectionDao> getWaitlistedSectionsByStudent(StudentDao studentDao) {
		List<SectionDao> sectionsStudentIsWaitlistedIn = new ArrayList<>();

		for (WaitlistDao waitlistDao : waitlistRepo.findAll()) {
			if (studentDao.equals(waitlistDao.getStudent())) {
				sectionsStudentIsWaitlistedIn.add(waitlistDao.getSection());
			}
		}

		return sectionsStudentIsWaitlistedIn;
	}


}
