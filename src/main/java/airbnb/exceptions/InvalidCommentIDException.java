package airbnb.exceptions;

public class InvalidCommentIDException extends Exception {

	@Override
	public String getMessage() {
		return "ID must be positive !";
	}
}
