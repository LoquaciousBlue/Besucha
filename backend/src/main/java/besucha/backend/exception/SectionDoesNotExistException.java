package besucha.backend.exception;

/**
 * Thrown when SectionDao object not found in database.
 */
public class SectionDoesNotExistException extends Exception {

	public SectionDoesNotExistException(String message) {
		super(message);
	}
}
