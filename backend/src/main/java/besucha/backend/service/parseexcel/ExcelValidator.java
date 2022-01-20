package besucha.backend.service.parseexcel;

import besucha.backend.exception.ExcelFileExtensionInvalidException;
import besucha.backend.exception.ExcelFileFormatInvalidException;
import besucha.backend.exception.FileCouldNotBeAccessedException;
import besucha.backend.model.excel.ExcelCloser;
import besucha.backend.model.excel.ExcelFile;
import besucha.backend.model.excel.ExcelOpener;

/**
 * This class validates the data in the Excel file.
 * The only method in this class that should be called is validate().
 * validate() does the following:
 * - checks that both files have an .xlsx extension
 * - checks that both files can be opened
 * - checks that the sectionFile contains data in correct cells
 * - checks that the studentFile has the arbitrary markers denoting a Microsoft Form file
 * - checks that the studentFile contains data in the correct cells
 */
public class ExcelValidator {
	
	String sectionFileName, studentFileName;
	ExcelFile sectionFile, studentFile;
	
	public ExcelValidator(String sectionFileName, String studentFileName){
		this.sectionFileName = sectionFileName;
		this.studentFileName = studentFileName;
	}

	/**
	 * Method that does basic validation on Excel files passed to Besucha.
	 * This is the only method in this class that should be called.
	 * @throws ExcelFileExtensionInvalidException thrown if file extension is not .xlsx
	 * @throws ExcelFileFormatInvalidException thrown if data in file is invalid
	 * @throws FileCouldNotBeAccessedException thrown if file could not be opened or closed
	 */
	public void validate() throws
			ExcelFileExtensionInvalidException,
			ExcelFileFormatInvalidException,
			FileCouldNotBeAccessedException {
		isXLSXFile(sectionFile, sectionFileName);
		isXLSXFile(studentFile, studentFileName);

		this.sectionFile = ExcelOpener.openFile(sectionFileName);
		this.studentFile = ExcelOpener.openFile(studentFileName);

		checkSectionFileContainsData();
		isMicrosoftFormsFile(studentFile);
		checkStudentFileContainsData();

		ExcelCloser.close(sectionFile);
		ExcelCloser.close(studentFile);
	}


	/**
	 * Check that Excel file has desired extension.
	 * @param file the ExcelFile
	 * @param fileName filename as a String
	 * @return
	 */
	protected void isXLSXFile(ExcelFile file, String fileName) throws ExcelFileExtensionInvalidException {
		String name = fileName.toUpperCase();
		if (!name.endsWith(".XLSX")) {
			throw new ExcelFileExtensionInvalidException(file.getRelativeFileName() + " is not an .xlsx file. Please try again with a proper Excel file.");
		}
	}

	/**
	 * Check that the file containing sections contains data in desired fields.
	 * @throws ExcelFileFormatInvalidException thrown if there is null where there should be data
	 */
	protected void checkSectionFileContainsData() throws ExcelFileFormatInvalidException {
		final int row = 2;
		int[] colsThatShouldBeFilled = {0, 1, 2, 3};

		for (int col : colsThatShouldBeFilled) {
			if (sectionFile.getCell(row, col) == null) {
				throw new ExcelFileFormatInvalidException(sectionFile.getRelativeFileName() + " data is in an invalid format. Please correct format and try again.");
			}
		}
		
	}

	/**
	 * Check that the student File contains data in appropriate columns.
	 * @throws ExcelFileFormatInvalidException thrown if null where there should be data
	 */
	protected void checkStudentFileContainsData() throws ExcelFileFormatInvalidException {
		int row = 1;
		int[] colsThatShouldBeFilled = {3, 4, 13, 16};

		for (int col : colsThatShouldBeFilled) {
			if (this.studentFile.getCell(row, col) == null) {
				throw new ExcelFileFormatInvalidException(studentFile.getRelativeFileName() + " data is in an invalid format. Please correct format and try again.");
			}
		}

	}

	/**
	 * Check that the student file contains hallmarks of a file created by Microsoft forms.
	 * @throws ExcelFileFormatInvalidException thrown if Excel file format invalid
	 */
	protected void isMicrosoftFormsFile(ExcelFile file) throws ExcelFileFormatInvalidException {
		int row = 1;
		int colToBeChecked = 22;  //start of
		int finalColToBeChecked = 80;

		while(colToBeChecked < finalColToBeChecked) {
			if(file.getCell(row,colToBeChecked) == null) {
				throw new ExcelFileFormatInvalidException(studentFile.getRelativeFileName() + " data is not a valid spreadsheet. Please try again with a Microsoft Form spreadsheet containing students and preferences.");
			}

			colToBeChecked += 3;
		}
	}

}
