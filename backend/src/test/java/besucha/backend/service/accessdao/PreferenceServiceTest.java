package besucha.backend.service.accessdao;

import besucha.backend.BackendApplication;
import besucha.backend.dao.PreferenceDao;
import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.exception.DuplicationException;
import besucha.backend.exception.SectionDoesNotExistException;
import besucha.backend.model.algorithm.Section;
import besucha.backend.model.algorithm.Seniority;
import besucha.backend.model.algorithm.Student;
import besucha.backend.repo.PreferenceRepo;
import besucha.backend.TestContextLoader;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
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
public class PreferenceServiceTest {

	@Mock
	private PreferenceRepo preferenceRepo;

	@Mock
	private SectionService sectionService;

	@Mock
	private StudentService studentService;

	@InjectMocks
	private PreferenceService preferenceService;

	@BeforeEach
	public void setUp() {
		preferenceRepo.deleteAll();
	}

	@Test
	public void isValid_returnsFalseIfPreferenceAlreadyInDb() {
		PreferenceDao preferenceDao = new PreferenceDao(new StudentDao(1), new SectionDao(2), true, 5);
		List<PreferenceDao> list = new ArrayList<>();
		list.add(preferenceDao);

		when(studentService.doesObjectExistInDb(preferenceDao.getStudent())).thenReturn(true);
		when(sectionService.doesObjectExistInDb(preferenceDao.getSection())).thenReturn(true);
		when(preferenceRepo.findAll()).thenReturn(list);
		assertThat(preferenceService.isValid(preferenceDao)).isEqualTo(false);

	}

	@Test
	public void isValid_returnsTrue() {
		PreferenceDao preferenceDao = new PreferenceDao(new StudentDao(1), new SectionDao(2), true, 5);

		when(studentService.doesObjectExistInDb(preferenceDao.getStudent())).thenReturn(true);
		when(sectionService.doesObjectExistInDb(preferenceDao.getSection())).thenReturn(true);
		when(preferenceRepo.findAll()).thenReturn(new ArrayList<>());
		assertThat(preferenceService.isValid(preferenceDao)).isEqualTo(true);
	}

	@Test
	public void doesExistInDb_returnsFalse() {
		when(preferenceRepo.findAll()).thenReturn(new ArrayList<>());
		assertThat(preferenceService.doesObjectExistInDb(new PreferenceDao())).isEqualTo(false);
	}

	@Test
	public void doesExistInDb_returnsTrue() {
		StudentDao studentDao = new StudentDao(1);
		SectionDao sectionDao = new SectionDao(2);
		PreferenceDao preferenceDao = new PreferenceDao(studentDao, sectionDao);
		List<PreferenceDao> preferenceDaoList = new ArrayList<>();
		preferenceDaoList.add(preferenceDao);

		when(preferenceRepo.findAll()).thenReturn(preferenceDaoList);

		assertThat(preferenceService.doesObjectExistInDb(preferenceDao)).isEqualTo(true);

	}

	@Test
	public void savePreferenceDao_doesNotThrowException() {
		PreferenceDao preferenceDao = new PreferenceDao(new StudentDao(1), new SectionDao(2), true, 3);
		when(studentService.doesObjectExistInDb(preferenceDao.getStudent())).thenReturn(true);
		when(sectionService.doesObjectExistInDb(preferenceDao.getSection())).thenReturn(true);
		when(preferenceRepo.findAll()).thenReturn(new ArrayList<>());
		assertDoesNotThrow(() -> preferenceService.save(preferenceDao));
	}

	@Test
	public void savePreferenceDao_throwsNullPointerException() {
		PreferenceDao preferenceDao = new PreferenceDao();
		List<PreferenceDao> list = new ArrayList<>();
		list.add(preferenceDao);

		when(studentService.doesObjectExistInDb(preferenceDao.getStudent())).thenReturn(true);
		when(sectionService.doesObjectExistInDb(preferenceDao.getSection())).thenReturn(true);
		when(preferenceRepo.findAll()).thenReturn(list);

		assertThrows(NullPointerException.class, () -> preferenceService.save(preferenceDao));
	}

	@Test
	public void savePreferenceDao_throwsDuplicationException() {
		PreferenceDao preferenceDao = new PreferenceDao(new StudentDao(1), new SectionDao(2), true, 3);
		List<PreferenceDao> list = new ArrayList<>();
		list.add(preferenceDao);

		when(studentService.doesObjectExistInDb(preferenceDao.getStudent())).thenReturn(true);
		when(sectionService.doesObjectExistInDb(preferenceDao.getSection())).thenReturn(true);
		when(preferenceRepo.findAll()).thenReturn(list);

		assertThrows(DuplicationException.class, () -> preferenceService.save(preferenceDao));
	}

	
	@Test
	public void savePreferenceDaoGivenBasicValues_savesPreferenceDao() throws DuplicationException {
		StudentDao studentDao = new StudentDao(1, "student", Seniority.Freshman, "");
		SectionDao sectionDao = new SectionDao(1, "section", 25, 1.0);
		PreferenceDao expected = new PreferenceDao(studentDao, sectionDao, false, 0);

		when(sectionService.getSectionDao(1)).thenReturn(sectionDao);
		when(studentService.getStudentDao(1)).thenReturn(studentDao);
		when(preferenceRepo.save(expected)).thenReturn(expected);

		when(studentService.doesObjectExistInDb(studentDao)).thenReturn(true);
		when(sectionService.doesObjectExistInDb(sectionDao)).thenReturn(true);
		when(preferenceRepo.findAll()).thenReturn(new ArrayList<>());

		preferenceService.save(1, 1, false, 0);

		verify(preferenceRepo, times(1)).save(Mockito.any(PreferenceDao.class));

	}

	@Test
	public void savePreferenceDaoGivenDaoObjects_savesPreferenceDao() throws DuplicationException {
		StudentDao studentDao = new StudentDao(1, "student", Seniority.Freshman, "");
		SectionDao sectionDao = new SectionDao(1, "section", 25, 1.0);
		PreferenceDao expected = new PreferenceDao(studentDao, sectionDao, false, 0);

		when(preferenceRepo.save(expected)).thenReturn(expected);
		when(studentService.doesObjectExistInDb(studentDao)).thenReturn(true);
		when(sectionService.doesObjectExistInDb(sectionDao)).thenReturn(true);
		when(preferenceRepo.findAll()).thenReturn(new ArrayList<>());

		preferenceService.save(studentDao, sectionDao, false, 0);

		verify(preferenceRepo, times(1)).save(Mockito.any(PreferenceDao.class));
	}

	@Test
	public void savePreferenceDaoObject_savesObject() throws DuplicationException {
		PreferenceDao preferenceDao = new PreferenceDao(new StudentDao(1), new SectionDao(2), false, 5);

		when(preferenceRepo.save(preferenceDao)).thenReturn(preferenceDao);
		when(studentService.doesObjectExistInDb(preferenceDao.getStudent())).thenReturn(true);
		when(sectionService.doesObjectExistInDb(preferenceDao.getSection())).thenReturn(true);
		when(preferenceRepo.findAll()).thenReturn(new ArrayList<>());

		assertThat(preferenceService.save(preferenceDao)).isEqualTo(preferenceDao);
		verify(preferenceRepo, times(1)).save(Mockito.any(PreferenceDao.class));
	}

	@Test
	public void getPreferences_returnsListPreferences() throws SectionDoesNotExistException {
		List<Section> sections = createSectionList(5);
		List<PreferenceDao> preferenceDaoList = createPreferenceDaoList(5);
		Student student = new Student();
		student.setId(2);

		when(preferenceRepo.findAll()).thenReturn(preferenceDaoList);
		assertThat(preferenceService.getPreferences(student, sections).size()).isEqualTo(1);

	}

	@Test
	public void findDesiredSection_returnsSection() throws SectionDoesNotExistException {
		List<Section> sections = createSectionList(5);

		Section expected = sections.get(2);
		SectionDao sectionDao = new SectionDao(expected.getId(), expected.getTitle(),
				expected.getCapacity(), expected.getCreditWeight());

		PreferenceDao preferenceDao = new PreferenceDao(new StudentDao(),
				sectionDao, true, 3);

		assertThat(preferenceService.findDesiredSection(preferenceDao, sections)).isEqualTo(expected);
	}

	@Test
	public void findDesiredSection_throwsException() {
		List<Section> sections = createSectionList(5);

		SectionDao sectionDao = new SectionDao(25, "title", 20, 1.0);

		PreferenceDao preferenceDao = new PreferenceDao(new StudentDao(),
				sectionDao, true, 3);

		assertThrows(SectionDoesNotExistException.class,
				() -> preferenceService.findDesiredSection(preferenceDao, sections));
	}


	private List<Section> createSectionList(int size) {
		List<Section> sections = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			sections.add(new Section(i, "section"+i, 25, 1.0));
		}

		return sections;
	}

	private List<PreferenceDao> createPreferenceDaoList(int size) {
		List<PreferenceDao> preferences = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			SectionDao sectionDao = new SectionDao(i, "section"+i, 25, 1.0);
			StudentDao studentDao = new StudentDao(i, "student"+i, Seniority.Senior, "");
			PreferenceDao preferenceDao = new PreferenceDao(studentDao, sectionDao, true, 0);
			preferences.add(preferenceDao);
		}
		return preferences;
	}


}
