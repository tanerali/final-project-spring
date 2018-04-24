package airbnb.exceptions;

public class InvalidPostDescriptionException extends InvalidPostDataExcepetion {
	@Override
	public String getMessage() {
		return super.getMessage() + "INVALID DESCRIPTION! Description cannot be empty.";
	}
}
