package besucha.backend.dao;

import besucha.backend.model.algorithm.Seniority;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StudentDaoTest {

	@Test
	public void equals_returnsTrueOnSameObject() {
		StudentDao studentDao = new StudentDao(1, "student", Seniority.Senior, "email");
		assertThat(studentDao.equals(studentDao)).isEqualTo(true);
	}

	@Test
	public void equals_returnsTrueWhenIdMatches() {
		StudentDao s1 = new StudentDao(1);
		StudentDao s2 = new StudentDao(1, "student", Seniority.Freshman, "email");
		assertThat(s1.equals(s2)).isEqualTo(true);
	}

	@Test
	public void equals_returnsFalseWhenIdDoesNotMatch() {
		StudentDao s1 = new StudentDao(2, "student", Seniority.Freshman, "email");
		StudentDao s2 = new StudentDao(1, "student", Seniority.Freshman, "email");
		assertThat(s1.equals(s2)).isEqualTo(false);
	}


}
