package besucha.backend.model.excel;

import besucha.backend.exception.FileCouldNotBeAccessedException;

/**
 * Open an Excel file.
 */
public class ExcelOpener {

	/**
	 * Open a file given it's file name.
	 * @param fileName the file's name as a String
	 * @return the ExcelFile object
	 * @throws FileCouldNotBeAccessedException thrown if file could not be opened.
	 */
	public static ExcelFile openFile(String fileName) throws FileCouldNotBeAccessedException {
		try {
			ExcelFile file = new ExcelFile(fileName);
			file.setActiveSheet(0);
			file.getActiveSheet();
			return file;
		} catch (Exception e) {		// todo: figure out this exception
			throw new FileCouldNotBeAccessedException(fileName + " could not be accessed. Please check that the file given to the program is an Excel file and that all Excel files are closed.");
		}

	}
}
