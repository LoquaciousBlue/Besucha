package besucha.backend.repo;

import besucha.backend.dao.SectionDao;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Handles MySQL connection for the SectionDao class. Uses Integer as ID.
 */
public interface SectionRepo extends PagingAndSortingRepository<SectionDao, Integer> {

	SectionDao findSectionDaoBySectionId(int sectionId);
}

