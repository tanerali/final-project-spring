package airbnb.model;

import java.time.LocalDate;

public class Notification {
	private int notificationID;
	private String title;
	private String email;
	private String fullName;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	private int postID;
	private int customerID;
	
	public Notification(
			String title, 
			String email, 
			String fullName, 
			LocalDate dateFrom, 
			LocalDate dateTo) {
		
		this.title = title;
		this.email = email;
		this.fullName = fullName;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}
	
	public Notification(
			String title, 
			String email, 
			String fullName, 
			LocalDate dateFrom, 
			LocalDate dateTo, 
			int postID, 
			int customerID,
			int notificationID) {
		
		this(title, email, fullName, dateFrom, dateTo);
		this.postID = postID;
		this.customerID = customerID;
		this.notificationID = notificationID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public LocalDate getDateFrom() {
		return dateFrom;
	}
	
	public LocalDate getDateTo() {
		return dateTo;
	}

	public int getPostID() {
		return postID;
	}
	
	public int getCustomerID() {
		return customerID;
	}
	
	public int getNotificationID() {
		return notificationID;
	}
}
