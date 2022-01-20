package besucha.backend.exception;

/**
 * Thrown when StudentDao object not found in database.
 */
public class StudentDoesNotExistException extends Exception {

	public StudentDoesNotExistException(String message) {
		super(message);
	}
}
