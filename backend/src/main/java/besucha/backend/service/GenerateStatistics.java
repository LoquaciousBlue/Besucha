package besucha.backend.service;

import besucha.backend.dao.PreferenceDao;
import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.repo.SectionRepo;
import besucha.backend.repo.StudentRepo;
import besucha.backend.service.accessdao.EnrolledService;
import besucha.backend.service.accessdao.PreferenceService;
import besucha.backend.service.accessdao.WaitlistService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates statistics to be displayed after algorithm runs.
 */
@Service
public class GenerateStatistics {

	private SectionRepo sectionRepo;
	private StudentRepo studentRepo;
	private WaitlistService waitlistService;
	private EnrolledService enrolledService;
	private PreferenceService preferenceService;

	/** todo: this variable probably should be stored somewhere else
	 * Minimum number of classes a student can be enrolled in is 3.
	 */
	public final int MIN_CREDITS = 4;

	/**
	 * Randomly decided that a "large waitlist" is going to be 5 students or more.
	 */
	public final int LARGE_WAITLIST_SIZE = 5;

	public GenerateStatistics(SectionRepo sectionRepo, StudentRepo studentRepo, WaitlistService waitlistService, EnrolledService enrolledService, PreferenceService preferenceService) {
		this.sectionRepo = sectionRepo;
		this.studentRepo = studentRepo;
		this.waitlistService = waitlistService;
		this.enrolledService = enrolledService;
		this.preferenceService = preferenceService;
	}

	/**
	 * This method prints statistics related to course system by querying the db.
	 * @return a String containing messages to be printed to screen
	 */
	public String printStatistics() {
		String result = "";
		result += "Printing relevant statistics...\n";
		result += "There are " + countUnderEnrolledStudents() + " students who are enrolled in fewer than " + MIN_CREDITS + " classes.\n";

		result += "There are " + countSectionsWithLongWaitlist() + " sections with a waitlist of more than " + LARGE_WAITLIST_SIZE + " students.\n";

		result += "Most requested classes are:\n";

		List<PreferenceDao> mostRequested = preferenceService.mostRequestedPreferences(3);

		for (PreferenceDao preferenceDao : mostRequested) {
			result += preferenceDao.getSection().getTitle() + "\n";
		}

		Map<SectionDao, Integer> map = findLongestWaitlists();

		if (!map.isEmpty()) {
			result += "\nSections with the long waitlists are...\n";

			for (Map.Entry<SectionDao, Integer> entry : map.entrySet()) {
				result += entry.getKey().getSectionId() + " " + entry.getKey().getTitle() + " ";
				result += "has waitlist of size " + entry.getValue() + "\n";
			}
		}

		return result;
	}


	/**
	 * Count the number of students enrolled in 3 classes or less.
	 * @return int representing number of students who are under-enrolled.
	 */
	public int countUnderEnrolledStudents() {
		int underEnrolledStudents = 0;

		for (StudentDao student : studentRepo.findAll()){
			double totalEnrolledCredits = 0;
			for (SectionDao section : enrolledService.getEnrolledSections(student))
				totalEnrolledCredits += section.getCreditWeight();

			if (totalEnrolledCredits < MIN_CREDITS) { underEnrolledStudents++; }
		}

		return underEnrolledStudents;
	}

	/**
	 * Count the number of classes with waitlists with more than 5 students.
	 * @return int representing num sections with long waitlists
	 */
	public int countSectionsWithLongWaitlist() {
		int count = 0;
		for (SectionDao sectionDao : sectionRepo.findAll()) {
			List<StudentDao> waitlist = waitlistService.getWaitlistedStudentsBySection(sectionDao);
			if (waitlist.size() >= LARGE_WAITLIST_SIZE) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Get all sections with a large waitlist
	 * @return a Map of SectionDao and Integer corresponding to sections with a waitlist of more than LARGE_WAITLIST_SIZE students
	 */
	public Map<SectionDao, Integer> findLongestWaitlists() {
		Map<SectionDao, Integer> map = waitlistService.buildWaitlistMap();
		Map<SectionDao, Integer> sectionsWithLongWaitlistMap = new HashMap<>();

		for (Map.Entry<SectionDao, Integer> entry : map.entrySet()) {
			if (entry.getValue() >= LARGE_WAITLIST_SIZE) {
				sectionsWithLongWaitlistMap.put(entry.getKey(), entry.getValue());
			}
		}

		return sectionsWithLongWaitlistMap;
	}


}
