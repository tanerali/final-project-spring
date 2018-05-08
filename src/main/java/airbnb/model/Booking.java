package airbnb.model;

import java.time.LocalDate;

import airbnb.exceptions.UserDataException;

public class Booking {

	private int bookingID;
	private int postID;
	private int customerID;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	
	public Booking(int postID, int customerID, LocalDate dateFrom, LocalDate dateTo) throws UserDataException {
		setPostID(postID);
		setCustomerID(customerID);
		if (dateFrom.isBefore(dateTo)) {
			setDateFrom(dateFrom);
			setDateTo(dateTo);
		} else {
			throw new UserDataException("Date-from has to be before date-to");
		}
	}

	public Booking() {
		// TODO Auto-generated constructor stub
	}

	public int getBookingID() {
		return bookingID;
	}

	public int getPostID() {
		return postID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}

	public void setPostID(int postID) {
		this.postID = postID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public void setDateFrom(LocalDate dateFrom) throws UserDataException {
		if (dateFrom == null) {
			throw new UserDataException("You have to specify a date-from");
		}
		this.dateFrom = dateFrom;
	}

	public void setDateTo(LocalDate dateTo) throws UserDataException {
		if (dateTo == null) {
			throw new UserDataException("You have to specify a date-to");
		}
		this.dateTo = dateTo;
	}	
}
