package airbnb.manager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import airbnb.dao.BookingDAO;
import airbnb.model.Booking;
import airbnb.model.Notification;


public enum BookingManager {
	INSTANCE;
	private BookingDAO bookingDao = BookingDAO.INSTANCE;
	
	public boolean requestBooking(Booking booking) throws SQLException {
		return bookingDao.createBooking(booking);
	}

	public ArrayList<Notification> checkNotifications(String email) throws SQLException {
		return bookingDao.checkForNewBookingRequests(email);
	}

	public boolean deleteBookingRequest(int notificationID) throws SQLException {
		return bookingDao.deleteBookingRequest(notificationID);
	}

	public boolean acceptBookingRequest(int notificationID) throws SQLException {
		return bookingDao.acceptBookingRequest(notificationID);
	}

	public ArrayList<LocalDate> getUnavailableDates(int postID) throws SQLException {
		return bookingDao.getUnavailableDates(postID);
	}

	public boolean ratePost(int postID, int userID, int rating) throws SQLException {
		return bookingDao.ratePost(postID, userID, rating);
	}

	public ArrayList<String> askUsersToRatePlaceAfterVisit() throws SQLException {
		return bookingDao.askUsersToRatePlaceAfterVisit();
	}

	public void changeStatusToVisited() throws SQLException {
		bookingDao.changeStatusToVisited();
	}
}
