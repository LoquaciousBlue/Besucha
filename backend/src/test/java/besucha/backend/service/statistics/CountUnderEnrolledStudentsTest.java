package besucha.backend.service.statistics;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.model.algorithm.Seniority;
import besucha.backend.repo.StudentRepo;
import besucha.backend.service.GenerateStatistics;
import besucha.backend.service.accessdao.EnrolledService;
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
public class CountUnderEnrolledStudentsTest {

	@Mock
	private StudentRepo studentRepo;

	
	@Mock
	private EnrolledService enrolledService;

	@InjectMocks
	private GenerateStatistics generateStatistics;

	@Test
	public void mocksLoad() {
	}

	@Test
	public void countUnderEnrolledStudentsWithValidStudent_returnsZero() {
		List<StudentDao> studentList = createStudentAsList();
		List<SectionDao> enrolledSections = createEnrolledSections(generateStatistics.MIN_CREDITS);

		given(studentRepo.findAll()).willReturn(studentList);
		given(enrolledService.getEnrolledSections(studentList.get(0))).willReturn(enrolledSections);

		assertThat(generateStatistics.countUnderEnrolledStudents()).isEqualTo(0);
	}

	@Test
	public void countUnderEnrolledStudentsWithInvalidStudent_returnsOne() {
		List<StudentDao> studentList = createStudentAsList();
		List<SectionDao> enrolledSections = createEnrolledSections(generateStatistics.MIN_CREDITS - 1);

		given(studentRepo.findAll()).willReturn(studentList);
		given(enrolledService.getEnrolledSections(studentList.get(0))).willReturn(enrolledSections);

		assertThat(generateStatistics.countUnderEnrolledStudents()).isEqualTo(1);
	}

	@Test
	public void countUnderEnrolledStudentsWhenStudentUnenrolled_returnsOne() {
		List<StudentDao> studentList = createStudentAsList();

		given(studentRepo.findAll()).willReturn(studentList);
		given(enrolledService.getEnrolledSections(studentList.get(0))).willReturn(new ArrayList<>());

		assertThat(generateStatistics.countUnderEnrolledStudents()).isEqualTo(1);
	}



	private List<StudentDao> createStudentAsList() {
		List<StudentDao> list = new ArrayList<>();
		list.add(new StudentDao(1, "student", Seniority.Senior, "email"));
		return list;
	}

	private List<SectionDao> createEnrolledSections(int numSections) {
		List<SectionDao> list = new ArrayList<>();
		for (int i = 0; i < numSections; i++) {
			list.add(new SectionDao(i, "section"+i, 25, 1.0));
		}
		return list;
	}
}

