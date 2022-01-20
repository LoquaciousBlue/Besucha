package besucha.backend.repo;

import besucha.backend.dao.EnrolledDao;
import besucha.backend.dao.StudentSectionKey;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Handles MySQL connection for the EnrolledDao class. Uses StudentSectionKey as ID.
 */
public interface EnrolledRepo extends PagingAndSortingRepository<EnrolledDao, StudentSectionKey> {
}
