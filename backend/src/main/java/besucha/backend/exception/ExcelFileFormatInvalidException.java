package besucha.backend.exception;

/**
 * Exception thrown if Excel file's format is invalid.
 */
public class ExcelFileFormatInvalidException extends Exception {

	public ExcelFileFormatInvalidException(String message) {
		super(message);
	}
}
