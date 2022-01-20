package besucha.backend.exception;

/**
 * Thrown when PreferenceDao objects not found in database.
 */
public class PreferenceDoesNotExistException extends Exception {

	public PreferenceDoesNotExistException(String message) {
		super(message);
	}
}
