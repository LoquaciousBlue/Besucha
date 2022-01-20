package besucha.backend.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StudentSectionKeyTest {

	@Test
	public void equals_returnsTrueWithSameStudentAndSection() {
		StudentSectionKey key = new StudentSectionKey(1, 5);
		StudentSectionKey key1 = new StudentSectionKey(1, 5);
		assertThat(key1.equals(key)).isEqualTo(true);
	}

	@Test
	public void equals_returnsFalseWithDifferentIds() {
		StudentSectionKey key = new StudentSectionKey(1, 2);
		StudentSectionKey key1 = new StudentSectionKey(2, 3);
		assertThat(key.equals(key1)).isEqualTo(false);
	}
}
