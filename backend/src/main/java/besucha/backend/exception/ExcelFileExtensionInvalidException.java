package besucha.backend.exception;

/**
 * Exception that is thrown when the Excel file extension is NOT .xlsx
 */
public class ExcelFileExtensionInvalidException extends Exception{

	public ExcelFileExtensionInvalidException(String message) {
		super(message);
	}
}
