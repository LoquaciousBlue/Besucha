package besucha.backend.service.accessdao;

import besucha.backend.dao.SectionDao;
import besucha.backend.exception.DuplicationException;
import besucha.backend.model.algorithm.Section;
import besucha.backend.repo.SectionRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstracts access to PreferenceDao objects in database.
 */
@Service
public class SectionService implements SaveDao<SectionDao> {

	private final SectionRepo sectionRepo;

	public SectionService(SectionRepo sectionRepo) {
		this.sectionRepo = sectionRepo;
	}

	/**
	 * Get SectionDao object in db given ID.
	 * @param sectionId the id to search for
	 * @return a SectionDao object
	 */
	public SectionDao getSectionDao(int sectionId) {
		return sectionRepo.findSectionDaoBySectionId(sectionId);
	}

	/**
	 * Create sectionDao object given attributes, and save to db.
	 * @param sectionId section id
	 * @param title title of section
	 * @param capacity number of students who can be enrolled
	 * @param creditWeight number of credits section is worth
	 * @return the created SectionDao object
	 */
	public SectionDao save(int sectionId, String title, int capacity, double creditWeight) throws DuplicationException {
		return save(new SectionDao(sectionId, title, capacity, creditWeight));
	}

	/**
	 * Save section dao object to db.
	 * @param sectionDao created sectionDao object
	 */
	public SectionDao save(SectionDao sectionDao) throws DuplicationException {
		if (doesObjectExistInDb(sectionDao)) {
			throw new DuplicationException("Section ID "
					+ sectionDao.getSectionId() 
					+ " is a duplicate. Please remove from Excel spreadsheet and try again.");
		};
		return sectionRepo.save(sectionDao);
	}

	/**
	 * Count the number of SectionDao objects in database
	 * @return long representing the count
	 */
	public long count() {
		return sectionRepo.count();
	}

	/**
	 * Get all SectionDao objects from repo, convert to Section objects, and return list.
	 * @return a List of Section objects.
	 */
	public List<Section> getAllSections() {
		List<Section> sectionList = new ArrayList<>();
		Iterable<SectionDao> sectionDaoIterable = sectionRepo.findAll();

		for (SectionDao sectionDao : sectionDaoIterable) {
			Section section = new Section(sectionDao);
			sectionList.add(section);
		}

		return sectionList;
	}

	/**
	 * Throws exception if repo already contains given section id
	 * @param sectionDao the SectionDao object to check the id of
	 * @return true if id already in db
	 */
	public boolean doesObjectExistInDb(SectionDao sectionDao) {
		return doesObjectExistInDb(sectionDao.getSectionId());
	}
	
	/**
	 * Throws exception if repo already contains a given section id.
	 * @param sectionId the int representing the section id to look for
	 * @return true if id does exist
	 */
	public boolean doesObjectExistInDb(int sectionId) {
		return sectionRepo.existsById(sectionId);
	}

}
