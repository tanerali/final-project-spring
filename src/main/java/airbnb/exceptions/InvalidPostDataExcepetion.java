package airbnb.exceptions;

public class InvalidPostDataExcepetion extends Exception {

	private String message;
	
	public InvalidPostDataExcepetion(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
