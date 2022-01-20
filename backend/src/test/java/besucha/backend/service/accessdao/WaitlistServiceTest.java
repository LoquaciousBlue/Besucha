package besucha.backend.service.accessdao;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.dao.WaitlistDao;
import besucha.backend.model.algorithm.Seniority;
import besucha.backend.repo.WaitlistRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class WaitlistServiceTest {

	@Mock
	private WaitlistRepo waitlistRepo;

	@Mock
	private StudentService studentService;

	@Mock
	private SectionService sectionService;

	@InjectMocks
	private WaitlistService waitlistService;

	@Test
	public void saveWaitlistDao_returnsWaitlistDao() {
		WaitlistDao waitlistDao = createWaitlistDao();
		when(waitlistRepo.save(waitlistDao)).thenReturn(waitlistDao);
		waitlistService.save(waitlistDao);
		verify(waitlistRepo, times(1)).save(Mockito.any(WaitlistDao.class));
	}

	@Test
	public void saveWaitlistDaoGivenObjects_returnsWaitlistDao() {
		WaitlistDao waitlistDao = createWaitlistDao();
		when(waitlistRepo.save(waitlistDao)).thenReturn(waitlistDao);
		waitlistService.save(new StudentDao(), new SectionDao(), 1);
		verify(waitlistRepo, times(1)).save(Mockito.any(WaitlistDao.class));
	}

	@Test
	public void saveWaitlistDaoGivenAttributes_returnsWaitlistDao() {
		WaitlistDao waitlistDao = createWaitlistDao();
		when(studentService.getStudentDao(1)).thenReturn(new StudentDao());
		when(sectionService.getSectionDao(1)).thenReturn(new SectionDao());
		when(waitlistRepo.save(waitlistDao)).thenReturn(waitlistDao);
		waitlistService.save(1, 1, 1);
		verify(waitlistRepo, times(1)).save(Mockito.any(WaitlistDao.class));
	}

	@Test
	public void buildWaitlistMap_returnsMap() {
		SectionDao sectionDao = new SectionDao(1, "title", 25, 1.0);
		StudentDao studentDao = new StudentDao(1, "name", Seniority.Senior, "email");
		WaitlistDao waitlistDao = new WaitlistDao(studentDao, sectionDao, 0);
		ArrayList<WaitlistDao> list = new ArrayList<>();
		list.add(waitlistDao);

		when(waitlistRepo.findAll()).thenReturn(list);

		Map<SectionDao, Integer> actual = waitlistService.buildWaitlistMap();
		assertThat(actual.containsKey(sectionDao)).isEqualTo(true);
		assertThat(actual.size()).isEqualTo(1);
		assertThat(actual.get(sectionDao)).isEqualTo(1);

	}

	public WaitlistDao createWaitlistDao() {
		SectionDao sectionDao = new SectionDao(1, "section", 25, 1.0);
		StudentDao studentDao = new StudentDao(1, "student", Seniority.Senior, "email");
		return new WaitlistDao(studentDao, sectionDao, 0);
	}

}
