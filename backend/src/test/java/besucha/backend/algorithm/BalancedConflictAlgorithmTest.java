package besucha.backend.algorithm;

import besucha.backend.BackendApplication;
import besucha.backend.TestContextLoader;
import besucha.backend.exception.DuplicationException;
import besucha.backend.model.algorithm.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class, loader = TestContextLoader.class)
public class BalancedConflictAlgorithmTest {

	private final int numSections = 10;
	private final int capacity = 10;
	private final int numStudents = 20;
	private final int numPreferences = 10;
	private final int creditWeight = 1;

	@Test
	public void createValidAlgorithmObject_isNotNull() throws DuplicationException {
		CourseSystem system = new CourseSystem();

		Student student = new Student();
		List<Preference> preferences = new ArrayList<>();
		Preference preference = new Preference();
		preferences.add(preference);
		student.setPreferences(preferences);
		Section section = new Section();

		system.addStudent(student);
		system.addSection(section);

		Algorithm algorithm = new BalancedConflictAlgorithm(system);
		assertThat(algorithm).isNotNull();

	}

	@Test
	public void runAlgorithm_returnsEnrolledStudent() throws Exception {
		CourseSystem system = createCourseSystem();
		Algorithm algorithm = new BalancedConflictAlgorithm(system);
		algorithm.runAlgorithm();
		printEnrolledStudents(system);
	}


	// methods to create a simple course system

	private void printEnrolledStudents(CourseSystem system) {
		System.out.println("Printing enrolled students: ");
		for (Section section : system.getAllSections()) {
			System.out.println("Enrolled in " + section.getTitle() + ":");
			for (Student student : section.getEnrolled()) {
				System.out.println(student.getName());
			}
		}
	}

	private CourseSystem createCourseSystem() throws Exception {
		CourseSystem system = new CourseSystem();
		List<Section> sections = createSections();
		List<Student> students = createStudents(sections);

		for (Section section : sections) {
			system.addSection(section);
		}

		for (Student student : students) {
			system.addStudent(student);
		}

		return system;
	}

	private List<Section> createSections() {
		List<Section> sections = new ArrayList<>();
		for (int i = 0; i < numSections; i++) {
			Section s = new Section(i, "section" + i, capacity, creditWeight);
			s.setCreditWeight(1.0);
			sections.add(s);
		}
		return sections;
	}

	private List<Student> createStudents(List<Section> sections) throws Exception {
		List<Student> students = new ArrayList<>();

		for (int i = 0; i < numStudents; i++) {
			Student s = new Student(i, "student" + i, generateSeniority(), createPreferences(sections));
			students.add(s);
		}
		return students;
	}

	private List<Preference> createPreferences(List<Section> sections) {
		List<Preference> preferences = new ArrayList<>();
		Random rand = new Random();

		for (int i = 0; i < numPreferences; i++) {
			Preference p = new Preference();
			p.setRequired(i % 2 == 0);
			p.setSection(sections.get(rand.nextInt(sections.size())));
			preferences.add(p);
		}

		return preferences;
	}

	private Seniority generateSeniority() throws Exception {
		Random rand = new Random();
		int num = rand.nextInt(4);

		switch(num % 4) {
			case 0:
				return Seniority.Freshman;
			case 1:
				return Seniority.Sophomore;
			case 2:
				return Seniority.Junior;
			case 3:
				return Seniority.Senior;
		}

		throw new Exception("ERROR: Seniority was not generated.");
	}


}
