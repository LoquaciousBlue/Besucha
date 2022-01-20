package besucha.backend.service.accessdao;

import besucha.backend.BackendApplication;
import besucha.backend.dao.EnrolledDao;
import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.model.algorithm.Seniority;
import besucha.backend.repo.EnrolledRepo;
import besucha.backend.TestContextLoader;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class EnrolledServiceTest {


	@Mock
	private EnrolledRepo enrolledRepo;

	@Mock
	private StudentService studentService;

	@Mock
	private SectionService sectionService;

	@InjectMocks
	private EnrolledService enrolledService;



	@Test
	public void saveEnrolledDao_returnsEnrolledDao() {
		EnrolledDao enrolledDao = createEnrolledDao();
		when(enrolledRepo.save(enrolledDao)).thenReturn(enrolledDao);
		enrolledService.save(enrolledDao);
		verify(enrolledRepo, times(1)).save(Mockito.any(EnrolledDao.class));
	}

	@Test
	public void saveEnrolledDaoGivenObjects_returnsEnrolledDao() {
		EnrolledDao enrolledDao = createEnrolledDao();
		when(enrolledRepo.save(enrolledDao)).thenReturn(enrolledDao);
		enrolledService.save(new StudentDao(), new SectionDao());
		verify(enrolledRepo, times(1)).save(Mockito.any(EnrolledDao.class));
	}

	@Test
	public void saveEnrolledDaoGivenAttributes_returnsEnrolledDao() {
		EnrolledDao enrolledDao = createEnrolledDao();
		when(studentService.getStudentDao(1)).thenReturn(new StudentDao());
		when(sectionService.getSectionDao(1)).thenReturn(new SectionDao());
		when(enrolledRepo.save(enrolledDao)).thenReturn(enrolledDao);
		enrolledService.save(1, 1);
		verify(enrolledRepo, times(1)).save(Mockito.any(EnrolledDao.class));
	}

	@Test
	public void getEnrolledSectionsGivenDao_returnsList() {
		StudentDao studentDao = new StudentDao(1, "name", Seniority.Senior, "email");
		SectionDao sectionDao = new SectionDao(1, "title", 25, 1.0);
		EnrolledDao enrolledDao = new EnrolledDao(studentDao, sectionDao);
		ArrayList<EnrolledDao> list = new ArrayList<>();
		list.add(enrolledDao);

		when(enrolledRepo.findAll()).thenReturn(list);

		List<SectionDao> actual = enrolledService.getEnrolledSections(studentDao);
		assertThat(actual.size()).isEqualTo(1);
		assertThat(actual.get(0)).isEqualTo(sectionDao);
	}

	@Test
	public void getEnrolledSectionsGivenId_returnsList() {
		StudentDao studentDao = new StudentDao(1, "name", Seniority.Senior, "email");
		SectionDao sectionDao = new SectionDao(1, "title", 25, 1.0);
		EnrolledDao enrolledDao = new EnrolledDao(studentDao, sectionDao);
		ArrayList<EnrolledDao> list = new ArrayList<>();
		list.add(enrolledDao);

		when(studentService.getStudentDao(1)).thenReturn(studentDao);
		when(enrolledRepo.findAll()).thenReturn(list);

		List<SectionDao> actual = enrolledService.getEnrolledSections(studentDao);
		assertThat(actual.size()).isEqualTo(1);
		assertThat(actual.get(0)).isEqualTo(sectionDao);
	}
	
	public EnrolledDao createEnrolledDao() {
		SectionDao sectionDao = new SectionDao(1, "section", 25, 1.0);
		StudentDao studentDao = new StudentDao(1, "student", Seniority.Senior, "email");
		return new EnrolledDao(studentDao, sectionDao);
	}
}
