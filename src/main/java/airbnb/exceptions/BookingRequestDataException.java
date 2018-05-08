package airbnb.exceptions;

public class BookingRequestDataException extends Exception{
	private String message;

	public BookingRequestDataException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
