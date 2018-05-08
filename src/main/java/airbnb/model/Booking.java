package airbnb.model;

import java.time.LocalDate;

import airbnb.exceptions.BookingRequestDataException;

public class Booking {

	private int bookingID;
	private int postID;
	private int customerID;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	
	public Booking(
			int postID, 
			int customerID, 
			LocalDate dateFrom, 
			LocalDate dateTo) throws BookingRequestDataException {
		
		setPostID(postID);
		setCustomerID(customerID);
		if (dateFrom.isBefore(dateTo)) {
			setDateFrom(dateFrom);
			setDateTo(dateTo);
		} else {
			throw new BookingRequestDataException("Date-from has to be before date-to");
		}
	}

	public Booking() {
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

	public void setBookingID(int bookingID) throws BookingRequestDataException {
		if (bookingID < 1) {
			throw new BookingRequestDataException("Error setting booking id");
		}
		this.bookingID = bookingID;
	}

	public void setPostID(int postID) throws BookingRequestDataException {
		if (postID < 1) {
			throw new BookingRequestDataException("Error setting post id");
		}
		this.postID = postID;
	}

	public void setCustomerID(int customerID) throws BookingRequestDataException {
		if (customerID < 1) {
			throw new BookingRequestDataException("Error setting customer id");
		}
		this.customerID = customerID;
	}

	public void setDateFrom(LocalDate dateFrom) throws BookingRequestDataException {
		if (dateFrom == null) {
			throw new BookingRequestDataException("You have to specify a date-from");
		}
		this.dateFrom = dateFrom;
	}

	public void setDateTo(LocalDate dateTo) throws BookingRequestDataException {
		if (dateTo == null) {
			throw new BookingRequestDataException("You have to specify a date-to");
		}
		this.dateTo = dateTo;
	}	
}
