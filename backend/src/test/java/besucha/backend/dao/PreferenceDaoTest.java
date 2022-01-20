package besucha.backend.dao;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.model.algorithm.Seniority;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
class PreferenceDaoTest {

	@Test
	public void testEquals_isTrueGivenSameObject() {
		PreferenceDao preferenceDao = new PreferenceDao();
		assertThat(preferenceDao.equals(preferenceDao)).isEqualTo(true);
	}

	@Test
	public void testEquals_isTrueGivenSameStudentAndSection() {
		StudentDao studentDao = new StudentDao(1, "student", Seniority.Senior, "email");
		SectionDao sectionDao = new SectionDao(2, "title", 25, 1.0);

		PreferenceDao p1 = new PreferenceDao(studentDao, sectionDao, false, 2);
		PreferenceDao p2 = new PreferenceDao(studentDao, sectionDao);

		assertThat(p1.equals(p2)).isEqualTo(true);
	}

	@Test
	public void testEquals_isTrueGivenSameStudentSectionId() {
		PreferenceDao p1 = new PreferenceDao(new StudentDao(1), new SectionDao(2), false , 3);
		PreferenceDao p2 = new PreferenceDao(new StudentDao(1), new SectionDao(2), true, 4);
		assertThat(p1.equals(p2)).isEqualTo(true);
	}
}