package airbnb.manager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.scheduling.annotation.Scheduled;

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

	public ArrayList<LocalDate> getUnavailableDates(int postID) throws SQLException {
		return bookingDao.getUnavailableDates(postID);
	}
}
