package besucha.backend.service;


import besucha.backend.exception.PreferenceDoesNotExistException;
import besucha.backend.exception.SectionDoesNotExistException;
import besucha.backend.exception.StudentDoesNotExistException;
import besucha.backend.gui.GuiManager;
import besucha.backend.model.algorithm.Algorithm;
import besucha.backend.model.algorithm.BalancedConflictAlgorithm;
import besucha.backend.model.algorithm.CourseSystem;
import besucha.backend.service.parseexcel.EnrolledDataSaver;
import besucha.backend.service.parseexcel.ExcelValidator;
import besucha.backend.service.parseexcel.RawDataSaver;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class IOInterfacer {


	private static CourseSystemCreator courseSystemCreator;
	private static CourseSystemSaver courseSystemSaver;
	private static GenerateStatistics generateStatistics;
	private static RawDataSaver rawDataSaver;
	private static GuiManager guiManager;
	private static EnrolledDataSaver enrolledDataSaver;
	private static EmailSender emailSender;

	public IOInterfacer(CourseSystemCreator courseSystemCreator, CourseSystemSaver courseSystemSaver, GenerateStatistics generateStatistics, RawDataSaver rawDataSaver, GuiManager guiManager, EnrolledDataSaver enrolledDataSaver, EmailSender emailSender) {
		this.courseSystemCreator = courseSystemCreator;
		this.courseSystemSaver = courseSystemSaver;
		this.generateStatistics = generateStatistics;
		this.rawDataSaver = rawDataSaver;
		this.guiManager = guiManager;
		this.enrolledDataSaver = enrolledDataSaver;
		this.emailSender = emailSender;
	}


	/**
	 * Validates input excel files based on their path. Gives error info to GUI to display to user
	 *  First, checks if files are excel files
	 *  Second, confirms if files are filled with readable data and formatted correctly
	 *  private static statically, if nothing else has failed, call save methods on file
	 * @param sectionFileName A xlsx file containing sections info
	 * @param studentFileName A xlsx file containing students info
	 * @return String of the error if there is one. Return null if validate is able to run smoothly
	 */
	public static String validate(String sectionFileName, String studentFileName) {
		try {
			ExcelValidator excelValidator = new ExcelValidator(sectionFileName, studentFileName);
			excelValidator.validate();
			rawDataSaver.saveAll(sectionFileName, studentFileName);

		} catch (Exception e) {
			return e.getMessage();
		}

		return null;
	}

	public String runAlgorithm() throws
			IOException,
			SectionDoesNotExistException,
			PreferenceDoesNotExistException,
			StudentDoesNotExistException,
			InvalidFormatException {
		CourseSystem system = courseSystemCreator.createCourseSystem();
		Algorithm algorithm = new BalancedConflictAlgorithm(system);
		algorithm.runAlgorithm();
		courseSystemSaver.saveEnrollmentResults(system);

		File enrollmentResults = enrolledDataSaver.createExcel();
		guiManager.setResultFile(enrollmentResults);
		return generateStatistics.printStatistics();
	}

	public String sendEmails(String username, String password)  {
		return emailSender.send(username, password);
	}




}
