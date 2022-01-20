package besucha.backend.service;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.model.algorithm.Section;
import besucha.backend.model.algorithm.Seniority;
import besucha.backend.model.algorithm.Student;
import besucha.backend.service.accessdao.EnrolledService;
import besucha.backend.service.accessdao.WaitlistService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class CourseSystemSaverTest {


	@Mock
	private EnrolledService enrolledService;

	@Mock
	private WaitlistService waitlistService;

	@InjectMocks
	private CourseSystemSaver courseSystemSaver;


	@Test
	public void saveWaitlistListOfObjects_saves() {
		List<Student> students = createListStudent(5);
		Section section = new Section(1, "title", 25, 1.0);

		courseSystemSaver.saveWaitlist(students, section);
		verify(waitlistService, times(5)).save(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
	}

	@Test
	public void saveEnrolledListOfObjects_saves() {
		List<Student> students = createListStudent(5);
		Section section = new Section(1, "title", 25, 1.0);

		courseSystemSaver.saveEnrolled(students, section);
		verify(enrolledService, times(5)).save(Mockito.anyInt(), Mockito.anyInt());
	}

	@Test
	public void saveEnrolledObject_saves() {
		Section section = new Section(1, "title", 25, 1.0);
		Student student = new Student(1, "name", Seniority.Senior, new ArrayList<>());

		courseSystemSaver.saveEnrolled(student, section);
		verify(enrolledService, times(1)).save(1, 1);
	}

	private List<Student> createListStudent(int size) {
		List<Student> list = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			list.add(new Student(i, "name", Seniority.Senior, new ArrayList<>()));
		}

		return list;
	}
}
