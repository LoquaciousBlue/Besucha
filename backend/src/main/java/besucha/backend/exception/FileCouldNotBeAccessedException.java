package besucha.backend.exception;

/**
 * Thrown if Excel file could not be accessed.
 */
public class FileCouldNotBeAccessedException extends Exception {

	public FileCouldNotBeAccessedException(String message) {
		super(message);
	}
}
