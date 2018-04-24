package airbnb.exceptions;

public class UserDataException extends Exception{

	private String message;
	
	public UserDataException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
}
