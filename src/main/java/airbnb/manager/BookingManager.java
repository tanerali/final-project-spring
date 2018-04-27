package airbnb.manager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import airbnb.dao.BookingDAO;
import airbnb.model.Booking;
import airbnb.model.Notification;
import airbnb.model.User;


public enum BookingManager {
	instance;
	private BookingDAO bookingDao = BookingDAO.instance;

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
	
//	public boolean userHasVisited(User user, int postID) {
//		return bookingDao.userHasVisited(user, postID);
//	}
}
