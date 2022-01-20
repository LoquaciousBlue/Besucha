package besucha.backend.service;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.exception.PreferenceDoesNotExistException;
import besucha.backend.exception.SectionDoesNotExistException;
import besucha.backend.exception.StudentDoesNotExistException;
import besucha.backend.model.algorithm.*;
import besucha.backend.service.accessdao.PreferenceService;
import besucha.backend.service.accessdao.SectionService;
import besucha.backend.service.accessdao.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class CourseSystemCreatorTest {

	@Mock
	private SectionService sectionService;

	@Mock
	private StudentService studentService;

	@Mock
	private PreferenceService preferenceService;

	@InjectMocks
	private CourseSystemCreator courseSystemCreator;


	@Test
	public void createCourseSystem_returnsSystem() throws SectionDoesNotExistException, StudentDoesNotExistException, PreferenceDoesNotExistException {
		List<Section> sectionList = new ArrayList<>();
		List<Student> studentList = new ArrayList<>();
		List<Preference> preferenceList = new ArrayList<>();

		sectionList.add(new Section());
		studentList.add(new Student());
		preferenceList.add(new Preference());

		// mocks for the method that verifies whether or not course system can be created
		when(studentService.count()).thenReturn((long) 1);
		when(sectionService.count()).thenReturn((long) 1);
		when(preferenceService.count()).thenReturn((long) 1);

		when(sectionService.getAllSections()).thenReturn(sectionList);
		when(studentService.getAllStudents()).thenReturn(studentList);
		when(preferenceService.getPreferences(studentList.get(0), sectionList))
				.thenReturn(preferenceList);

		studentList.get(0).setPreferences(preferenceList);
		CourseSystem expected = new CourseSystem(new SectionList(sectionList), new StudentList(studentList));

		CourseSystem actual = courseSystemCreator.createCourseSystem();

		assertThat(actual.getAllSections()).isEqualTo(expected.getAllSections());
		assertThat(actual.getAllStudents()).isEqualTo(expected.getAllStudents());
	}
}
