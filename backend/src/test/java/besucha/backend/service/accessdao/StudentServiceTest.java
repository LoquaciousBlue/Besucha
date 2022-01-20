package besucha.backend.service.accessdao;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.dao.StudentDao;
import besucha.backend.exception.DuplicationException;
import besucha.backend.exception.SeniorityInvalidException;
import besucha.backend.model.algorithm.Seniority;
import besucha.backend.model.algorithm.Student;
import besucha.backend.repo.StudentRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class StudentServiceTest {

	@Mock
	private StudentRepo studentRepo;

	@InjectMocks
	private StudentService studentService;


	@Test
	public void createStudentDaoWithSeniorityObject_savesObject() throws DuplicationException {
		when(studentRepo.save(new StudentDao())).thenReturn(new StudentDao());
		studentService.save(1, "student", Seniority.Senior, "");
		verify(studentRepo, times(1)).save(Mockito.any(StudentDao.class));
	}

	@Test
	public void createStudentDaoWithStringSeniority_savesObject() throws SeniorityInvalidException, DuplicationException {
		when(studentRepo.save(new StudentDao())).thenReturn(new StudentDao());
		studentService.save(1, "student", "Junior", "");
		verify(studentRepo, times(1)).save(Mockito.any(StudentDao.class));
	}

	@Test
	public void createStudentDaoWithStrangeStringSeniority_savesObject() throws SeniorityInvalidException {
		assertThrows(SeniorityInvalidException.class,
				() -> studentService.save(1, "student", "sEnIoR", ""));
	}

	@Test
	public void createStudentDaoWithStringSeniority_throwsException() throws SeniorityInvalidException {
		assertThrows(SeniorityInvalidException.class,
				() -> studentService.save(1, "student", "random", ""));
	}

	@Test
	public void getAllStudents_returnsStudentList() {
		List<StudentDao> studentDaoList = new ArrayList<>();
		StudentDao studentDao = new StudentDao(1, "student", Seniority.Senior, "");
		studentDaoList.add(studentDao);
		when(studentRepo.findAll()).thenReturn(studentDaoList);

		List<Student> expected = new ArrayList<>();
		expected.add(new Student(studentDao));

		assertThat(studentService.getAllStudents()).isEqualTo(expected);
	}
}
