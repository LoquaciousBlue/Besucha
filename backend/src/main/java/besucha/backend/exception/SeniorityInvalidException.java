package besucha.backend.exception;

/**
 * Thrown when conversion to the Seniority enum does not work.
 */
public class SeniorityInvalidException extends Exception{

	public SeniorityInvalidException(String message) {
		super(message);
	}

}
