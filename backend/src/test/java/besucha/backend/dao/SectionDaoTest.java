package besucha.backend.dao;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class SectionDaoTest {

	@Test
	public void equals_returnsTrueOnSameObject() {
		SectionDao sectionDao = new SectionDao(1, "title", 25, 1.0);
		assertThat(sectionDao.equals(sectionDao)).isEqualTo(true);
	}

	@Test
	public void equals_returnsTrueWhenIdMatches() {
		SectionDao s1 = new SectionDao(1);
		SectionDao s2 = new SectionDao(1, "title", 25, 1.0);
		assertThat(s1.equals(s2)).isEqualTo(true);
	}

	@Test
	public void equals_returnsFalseWhenIdDoesNotMatch() {
		SectionDao s1 = new SectionDao(2, "title", 25, 1.0);
		SectionDao s2 = new SectionDao(1, "title", 25, 1.0);
		assertThat(s1.equals(s2)).isEqualTo(false);
	}


}
