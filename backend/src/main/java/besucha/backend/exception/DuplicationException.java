package besucha.backend.exception;

/**
 * DuplicationException manages exception when there are duplicates of something.
 */
public class DuplicationException extends Exception {

	public DuplicationException(String message) {
		super(message);
	}
}
