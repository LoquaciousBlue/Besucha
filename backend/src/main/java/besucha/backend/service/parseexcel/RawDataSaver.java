package besucha.backend.service.parseexcel;

import besucha.backend.exception.*;
import besucha.backend.model.excel.ExcelFile;
import besucha.backend.model.excel.ExcelOpener;
import besucha.backend.service.accessdao.PreferenceService;
import besucha.backend.service.accessdao.SectionService;
import besucha.backend.service.accessdao.StudentService;
import org.springframework.stereotype.Service;

/**
 * This class saves the data received from excel into the db.
 */
@Service
public class RawDataSaver {

	// beans
	private SectionService sectionService;
	private StudentService studentService;
	private PreferenceService preferenceService;

	// other attributes
	private ExcelFile sectionFile, studentFile;
	private final int numPreferences = 10;

	public RawDataSaver(SectionService sectionService, StudentService studentService, PreferenceService preferenceService) {
		this.sectionService = sectionService;
		this.studentService = studentService;
		this.preferenceService = preferenceService;
	}

	/**
	 * This is the method that should be called. Saves all data from Excel files.
	 * @param sectionFileName String name of section file
	 * @param studentFileName String name of student file
	 * @throws FileCouldNotBeAccessedException thrown if file could not be opened
	 * @throws DuplicationException thrown if section id already exits
	 * @throws SeniorityInvalidException thrown if Seniority object cannot be created
	 */
	public void saveAll(String sectionFileName, String studentFileName) throws FileCouldNotBeAccessedException, DuplicationException, SeniorityInvalidException, StudentDoesNotExistException, SectionDoesNotExistException, ExcelFileFormatInvalidException, StudentInvalidException {

		setFiles(sectionFileName, studentFileName);
		saveSectionData();
		saveStudentData();
		savePreferenceData();


	}

	/**
	 * Set the file params and open files.
	 * @param sectionFileName the name of the file that contains the sections
	 * @param studentFileName the name of the file that contains the students
	 * @throws FileCouldNotBeAccessedException thrown if files could not be opened
	 */
	private void setFiles(String sectionFileName, String studentFileName) throws FileCouldNotBeAccessedException {
		this.sectionFile = ExcelOpener.openFile(sectionFileName);
		this.studentFile = ExcelOpener.openFile(studentFileName);
	}


	/**
	 * Save section data from file.
	 * @throws DuplicationException if section with same ID already exists in db
	 */
	private void saveSectionData() throws DuplicationException {
		int row = 2;

		while(sectionFile.getCell(row, 0) != null) {
			int sectionId = (int) sectionFile.getCell(row,0).getNumericCellValue();
			String title = sectionFile.getCell(row,1).getStringCellValue();
			int capacity = (int) sectionFile.getCell(row,2).getNumericCellValue();
			double creditWeight = sectionFile.getCell(row,3).getNumericCellValue();

			sectionService.save(sectionId, title, capacity, creditWeight);
			row++;
		}
	}

	/**
	 * Save student data from excel file.
	 * @throws SeniorityInvalidException if Seniority object invalid
	 * @throws DuplicationException if student id already in db
	 */
	private void saveStudentData() throws SeniorityInvalidException, DuplicationException, StudentInvalidException {
		int row = 1;
		int studentIdCol = 13, nameCol = 4, seniorityCol = 16, emailCol = 3;
		int studentId; String name = "none"; String seniority = "none"; String email = "none";

		while(studentFile.getCell(row, 0) != null) {
			try {
				studentId = (int) studentFile.getCell(row, studentIdCol).getNumericCellValue();
			} catch (Exception e) {
				throw new StudentInvalidException("Student ID in preferences file improperly formatted. Previously retrieved row retrieved had student with name = " + name + ". Please fix formatting error and try again.");
			}

			try {
				name = studentFile.getCell(row, nameCol).getStringCellValue();
			} catch (Exception e) {
				throw new StudentInvalidException("Student name in preferences file improperly formatted. Currently retrieving row with student ID = " + studentId + ". Please fix formatting error and try again.");
			}

			try {
				seniority = studentFile.getCell(row,seniorityCol).getStringCellValue();
			} catch (Exception e) {
				throw new StudentInvalidException("Student seniority in preferences file improperly formatted. Currently retrieving row with student ID = " + studentId + ". Please fix formatting error and try again.");
			}

			try {
				email = studentFile.getCell(row, emailCol).getStringCellValue();
			} catch (Exception e) {
				throw new StudentInvalidException("Student seniority in preferences file improperly formatted. Currently retrieving row with student ID = " + studentId + ". Please fix formatting error and try again.");
			}


			studentService.save(studentId, name, seniority, email);
			row++;
		}
	}

	/**
	 * Save preference data from excel workbook in db
	 * @throws DuplicationException thrown if preference already exists in db
	 */
	private void savePreferenceData() throws DuplicationException, StudentDoesNotExistException, SectionDoesNotExistException {
		int row = 1;

		while(studentFile.getCell(row, 0) != null) {
			for(int j = 0; j < numPreferences; j++) {
				int unSureCheck = 25+6*j;
				int studentId = (int) studentFile.getCell(row, 13).getNumericCellValue();
				int sectionId = (int) studentFile.getCell(row, 22+6*j).getNumericCellValue();
				boolean isRequired = studentFile.getCell(row, unSureCheck).getStringCellValue().equals("Yes");
				int preferenceRank = j;

				preferenceService.isValidAndThrowsExceptionIfInvalid(studentId, sectionId);
				preferenceService.save(studentId, sectionId, isRequired, preferenceRank);

			}
			row++;
		}
	}
}
