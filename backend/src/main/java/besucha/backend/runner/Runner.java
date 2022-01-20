package besucha.backend.runner;

import besucha.backend.gui.GuiManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Temporary class to run test "real" access to db (as opposed to mocked versions in unit tests.)
 */
@Component
public class Runner implements CommandLineRunner {

	private final GuiManager gm;

	public Runner(GuiManager gm) {
		this.gm = gm;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("hello world");

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gm.launchMainWindow();
			}
		});



//		 test that can save objects to db using print statements
//		SaveObjectsRunner.saveObjects();

//		 test running the algorithm on basic model data only
//		AlgorithmRunner.runAlgorithm_returnsEnrolledStudent();

//		test getting data out of waitlist repo
//		DataAccessRunner.getWaitlistData();

//		make basic course system, kinda messy but whatever
//		CourseSystemCreatorRunner.getCourseSystem();

//		test generate statistics class
//		GenerateStatisticsRunner.test();

//		simulate running entire system
//		CompleteSystemRunner.runAll();
	}


}

