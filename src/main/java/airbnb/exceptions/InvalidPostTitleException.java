package airbnb.exceptions;

public class InvalidPostTitleException extends InvalidPostDataExcepetion {

	@Override
	public String getMessage() {
		return super.getMessage() + "INVALID TITLE! Title cannot be empty.";
	}
}
