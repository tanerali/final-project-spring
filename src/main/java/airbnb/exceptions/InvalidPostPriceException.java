package airbnb.exceptions;

public class InvalidPostPriceException extends InvalidPostDataExcepetion {

	@Override
	public String getMessage() {
		return super.getMessage() + "INVALID PRICE! Price must be greater then 0.";
	}
}
