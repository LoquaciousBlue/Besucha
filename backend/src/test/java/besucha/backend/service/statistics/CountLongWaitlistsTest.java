package besucha.backend.service.statistics;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.model.algorithm.Seniority;
import besucha.backend.repo.SectionRepo;
import besucha.backend.service.GenerateStatistics;
import besucha.backend.service.accessdao.WaitlistService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class CountLongWaitlistsTest {
	
	@Mock
	private SectionRepo sectionRepo;

	@Mock
	private WaitlistService waitlistService;
	
	@InjectMocks
	private GenerateStatistics generateStatistics;
	
	@Test
	public void mocksLoad() {}
	
	@Test
	public void countLongWaitlistForSectionWithLongWaitlist_returnsOne() {
		List<SectionDao> list = createSection();
		given(sectionRepo.findAll()).willReturn(list);
		given(waitlistService.getWaitlistedStudentsBySection(list.get(0))).willReturn(createWaitlistDaoList(10, list.get(0)));
		assertThat(generateStatistics.countSectionsWithLongWaitlist()).isEqualTo(1);
	}

	@Test
	public void countLongWaitlistForSectionWithNoWaitlist_returnsZero() {
		List<SectionDao> list = createSection();
		given(sectionRepo.findAll()).willReturn(list);
		given(waitlistService.getWaitlistedStudentsBySection(list.get(0))).willReturn(createWaitlistDaoList(0, list.get(0)));
		assertThat(generateStatistics.countSectionsWithLongWaitlist()).isEqualTo(0);
	}

	@Test
	public void countLongWaitlistForSectionWithShortWaitlist_returnsZero() {
		List<SectionDao> list = createSection();
		given(sectionRepo.findAll()).willReturn(list);
		given(waitlistService.getWaitlistedStudentsBySection(list.get(0))).willReturn(createWaitlistDaoList(2, list.get(0)));
		assertThat(generateStatistics.countSectionsWithLongWaitlist()).isEqualTo(0);
	}



	/**
	 * Create a single section and place in an iterable, for testing purposes.
	 * @return an Iterable of SectionDao objects.
	 */
	private List<SectionDao> createSection() {
		List<SectionDao> sectionDaoList = new ArrayList<>();
		SectionDao sectionDao = new SectionDao(1, "section", 25, 1.0);
		sectionDaoList.add(sectionDao);
		return sectionDaoList;
	}

	private List<StudentDao> createWaitlistDaoList(int size, SectionDao sectionDao) {
		List<StudentDao> studentList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			StudentDao studentDao = new StudentDao(i, "student", Seniority.Freshman, "email");
			studentList.add(studentDao);
		}
		return studentList;
	}
	
	
}
