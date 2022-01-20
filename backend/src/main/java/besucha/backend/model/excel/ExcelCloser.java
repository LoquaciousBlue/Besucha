package besucha.backend.model.excel;

import besucha.backend.exception.FileCouldNotBeAccessedException;

import java.io.IOException;

/**
 * Closes given Excel file
 */
public class ExcelCloser {

	/**
	 * Close an Excel file.
	 * @throws IOException thrown if file cannot be closed.
	 */
	public static void close(ExcelFile file) throws FileCouldNotBeAccessedException {
		try {
			file.close();
		} catch (Exception e) {
			throw new FileCouldNotBeAccessedException("Given Excel files could not be read. Please close all Excel workbooks and try again.");
		}

	}
}
