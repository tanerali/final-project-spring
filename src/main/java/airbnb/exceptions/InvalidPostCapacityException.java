package airbnb.exceptions;

public class InvalidPostCapacityException extends InvalidPostDataExcepetion {
	@Override
	public String getMessage() {
		return super.getMessage() + " INVALID CAPACITY! Capacity must be greater then 0.";
	}
}
