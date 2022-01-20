package besucha.backend.service.statistics;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.dao.SectionDao;
import besucha.backend.service.GenerateStatistics;
import besucha.backend.service.accessdao.WaitlistService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class FindLongestWaitlistsTest {

	@Mock
	private WaitlistService waitlistService;

	@InjectMocks
	private GenerateStatistics generateStatistics;

	@Test
	public void findLongestWaitlist_createsMap() {
		Map<SectionDao, Integer> map = new HashMap<>();
		SectionDao section1 = new SectionDao(1, "", 25, 1.0);
		SectionDao section2 = new SectionDao(2, "", 25, 1.0);
		map.put(section1, 100);
		map.put(section2, 2);

		Map<SectionDao, Integer> expected = new HashMap<>();
		expected.put(section1, 100);

		given(waitlistService.buildWaitlistMap()).willReturn(map);

		Map<SectionDao, Integer> actual = generateStatistics.findLongestWaitlists();

		assertThat(actual.keySet().size()).isEqualTo(1);
		assertThat(actual.get(section1)).isEqualTo(100);

	}
}
