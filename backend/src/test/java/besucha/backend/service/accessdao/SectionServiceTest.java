package besucha.backend.service.accessdao;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.dao.SectionDao;
import besucha.backend.model.algorithm.Section;
import besucha.backend.exception.DuplicationException;
import besucha.backend.repo.SectionRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class SectionServiceTest {

	@Mock
	private SectionRepo sectionRepo;

	@InjectMocks
	private SectionService sectionService;

	@Test
	public void createSectionDao_savesObject() throws DuplicationException {
		when(sectionRepo.save(new SectionDao())).thenReturn(new SectionDao());
		sectionService.save(1, "title", 25, 1.0);
		verify(sectionRepo, times(1)).save(Mockito.any(SectionDao.class));
	}

	@Test
	public void getAllSections_returnsSectionList() {
		List<SectionDao> sectionDaoList = new ArrayList<>();
		SectionDao sectionDao = new SectionDao(1, "title", 10, 1.0);
		sectionDaoList.add(sectionDao);
		when(sectionRepo.findAll()).thenReturn(sectionDaoList);

		List<Section> expected = new ArrayList<>();
		expected.add(new Section(sectionDao));

		List<Section> actual = sectionService.getAllSections();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void doesObjectExistInDb_returnsTrue() {
		when(sectionRepo.existsById(1)).thenReturn(true);
		assertThat(sectionService.doesObjectExistInDb(1)).isEqualTo(true);
	}

	@Test
	public void doesObjectExistInDb_returnsFalse() {
		when(sectionRepo.existsById(1)).thenReturn(false);
		assertThat(sectionService.doesObjectExistInDb(1)).isEqualTo(false);
	}

}
