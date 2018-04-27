package airbnb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import airbnb.manager.DBManager;
import airbnb.model.Booking;
import airbnb.model.Notification;

public enum BookingDAO {

	instance;
	private Connection connection;
	
	private BookingDAO() {
		connection = DBManager.INSTANCE.getConnection();
	}
	
	public boolean createBooking(Booking booking) throws SQLException {
		String sql = 
				"INSERT INTO POSTS_BOOKINGS (post_id, customer_id, date_from, date_to, confirmed) " + 
				"VALUES (?, ?, ?, ?, false);";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, booking.getPostID());
			ps.setInt(2, booking.getCustomerID());
			ps.setObject(3, booking.getDateFrom());
			ps.setObject(4, booking.getDateTo());
			
			return ps.executeUpdate() > 0 ? true : false;
		}
	}
	
	public ArrayList<Notification> checkForNewBookingRequests(String email) throws SQLException {
		String sql = 
				"SELECT p.title, customer.email, CONCAT(customer.first_name, \" \", customer.last_name) "
						+ "as 'Full Name', pb.date_from, pb.date_to, pb.post_id, pb.customer_id, pb.ID " + 
				"FROM POSTS_BOOKINGS pb " + 
				"JOIN POSTS p " + 
				"ON pb.post_id = p.ID " + 
				"JOIN USERS customer " + 
				"ON pb.customer_id = customer.ID " + 
				"JOIN USERS host " + 
				"ON p.host_id = host.ID " + 
				"WHERE host.email = ?;";
		ArrayList<Notification> bookingRequestsNotifications = new ArrayList<>();
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, email);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				Notification notification = new Notification(
						resultSet.getString("title"),
						resultSet.getString("email"),
						resultSet.getString(3),
						LocalDate.parse(resultSet.getString("date_from")),
						LocalDate.parse(resultSet.getString("date_to")),
						resultSet.getInt("post_id"),
						resultSet.getInt("customer_id"),
						resultSet.getInt("ID"));
				bookingRequestsNotifications.add(notification);
			}
			return bookingRequestsNotifications;
		}
	}

	public boolean deleteBookingRequest(int notificationID) throws SQLException {
		String sql = "DELETE FROM POSTS_BOOKINGS WHERE ID = ?;";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, notificationID);
			return ps.executeUpdate() > 0 ? true : false;
		}
	}

	public boolean acceptBookingRequest(int notificationID) throws SQLException {
		String sql = "UPDATE POSTS_BOOKINGS SET confirmed = true WHERE ID=?;";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, notificationID);
			return ps.executeUpdate() > 0 ? true : false;
		}
	}
}
