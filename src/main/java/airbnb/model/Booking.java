package airbnb.model;

import java.time.LocalDate;

public class Booking {

	private int bookingID;
	private int postID;
	private int customerID;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	
	public Booking(int postID, int customerID, LocalDate dateFrom, LocalDate dateTo) {
		this.postID = postID;
		this.customerID = customerID;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
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
	
	
}
