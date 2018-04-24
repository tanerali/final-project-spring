package airbnb.exceptions;

public class InvalidPostDataExcepetion extends Exception {

	@Override
	public String getMessage() {
		return "Oops, Something went wrong with creating a Post object";
	}
}
