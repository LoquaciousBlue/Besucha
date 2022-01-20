package besucha.backend.repo;

import besucha.backend.dao.StudentSectionKey;
import besucha.backend.dao.WaitlistDao;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Handles MySQL connection for the WaitlistDao class. Uses StudentSectionKey as ID.
 */
public interface WaitlistRepo extends PagingAndSortingRepository<WaitlistDao, StudentSectionKey> {
}
