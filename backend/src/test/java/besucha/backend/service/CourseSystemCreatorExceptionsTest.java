package besucha.backend.service;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.exception.PreferenceDoesNotExistException;
import besucha.backend.exception.SectionDoesNotExistException;
import besucha.backend.exception.StudentDoesNotExistException;
import besucha.backend.service.accessdao.PreferenceService;
import besucha.backend.service.accessdao.SectionService;
import besucha.backend.service.accessdao.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class CourseSystemCreatorExceptionsTest {

	@Mock
	private SectionService sectionService;

	@Mock
	private StudentService studentService;

	@Mock
	private PreferenceService preferenceService;

	@InjectMocks
	private CourseSystemCreator courseSystemCreator;

	@Test
	public void verify_doesNotThrowExceptions() throws SectionDoesNotExistException, PreferenceDoesNotExistException, StudentDoesNotExistException {
		when(studentService.count()).thenReturn((long) 1);
		when(sectionService.count()).thenReturn((long) 1);
		when(preferenceService.count()).thenReturn((long) 1);

		assertThat(courseSystemCreator.verifyPossibleToCreateCourseSystem()).isEqualTo(true);
	}

	@Test
	public void verify_throwsSectionDNEException() {
		when(studentService.count()).thenReturn((long) 1);
		when(preferenceService.count()).thenReturn((long) 1);
		assertThrows(SectionDoesNotExistException.class, () -> courseSystemCreator.verifyPossibleToCreateCourseSystem());
	}

	@Test
	public void verify_throwsStudentDNEException() {
		when(sectionService.count()).thenReturn((long) 1);
		when(preferenceService.count()).thenReturn((long) 1);
		assertThrows(StudentDoesNotExistException.class, () -> courseSystemCreator.verifyPossibleToCreateCourseSystem());
	}

	@Test
	public void verify_throwsPreferenceDNEException() {
		when(sectionService.count()).thenReturn((long) 1);
		when(studentService.count()).thenReturn((long) 1);
		assertThrows(PreferenceDoesNotExistException.class, () -> courseSystemCreator.verifyPossibleToCreateCourseSystem());
	}

}
