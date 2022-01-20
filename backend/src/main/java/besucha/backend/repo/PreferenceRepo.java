package besucha.backend.repo;

import besucha.backend.dao.PreferenceDao;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Handles MySQL connection for the PreferenceDao class. Uses Integer as ID.
 */
public interface PreferenceRepo extends PagingAndSortingRepository<PreferenceDao, Integer> {
}
