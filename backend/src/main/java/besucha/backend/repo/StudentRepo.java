package besucha.backend.repo;

import besucha.backend.dao.StudentDao;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Handles MySQL connection for the StudentDao class. Uses Integer as ID.
 */
public interface StudentRepo extends PagingAndSortingRepository<StudentDao, Integer> {

	StudentDao findStudentDaoByStudentId(int id);
}
