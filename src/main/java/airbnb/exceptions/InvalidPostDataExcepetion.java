package airbnb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
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
