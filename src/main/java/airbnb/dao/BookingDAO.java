package airbnb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import airbnb.manager.DBManager;
import airbnb.model.Booking;

public enum BookingDAO {

	instance;
	private Connection connection;
	
	private BookingDAO() {
		connection = DBManager.INSTANCE.getConnection();
	}
	
	public boolean createBooking(Booking booking) throws SQLException {
		String sql = 
				"INSERT INTO POSTS_BOOKINGS (post_id, customer_id, date_from, date_to, confimed) " + 
				"VALUES (?, ?, ?, ?, false);";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, booking.getPostID());
			ps.setInt(2, booking.getCustomerID());
			ps.setObject(3, booking.getDateFrom());
			ps.setObject(4, booking.getDateTo());
			
			return ps.executeUpdate() > 0 ? true : false;
		}
	}
}
