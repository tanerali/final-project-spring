package airbnb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import airbnb.exceptions.UserDataException;
import airbnb.manager.DBManager;
import airbnb.model.Review;
import airbnb.model.User;

public enum UserDAO {
	INSTANCE;

	private Connection connection;

	BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

	private UserDAO() {
		connection = DBManager.INSTANCE.getConnection();
	}

	public User getUserByEmail(String email, String password) throws SQLException, UserDataException {
		String sqlQuery = "SELECT ID, first_name, last_name, email, password, gender, city, "
				+ "country, photo, description, birth_date, telephone_number " + "FROM USERS " + "WHERE email = ?";

		try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
			ps.setString(1, email);
			ResultSet resultSet = ps.executeQuery();
			resultSet.next();

			User user = null;
			// if the user exists
			if (!resultSet.getString("email").isEmpty() &&
			// and his password corresponds to the encoded password in the DB
					bCryptEncoder.matches(password, resultSet.getString("password"))) {
				user = new User(resultSet.getInt("ID"), resultSet.getString("first_name"),
						resultSet.getString("last_name"), resultSet.getString("email"), resultSet.getString("password"),
						resultSet.getString("gender"), resultSet.getString("city"), resultSet.getString("country"),
						resultSet.getString("photo"), resultSet.getString("description"),
						LocalDate.parse(resultSet.getString("birth_date")), resultSet.getString("telephone_number"));
			}
			return user;
		}
	}

	public boolean addUser(User user) throws SQLException {
		String sql = "INSERT INTO USERS (first_name, last_name, email, password, gender, city, "
				+ "country, photo, description, birth_date, telephone_number) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?);";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getEmail());
			System.out.println(user.getPassword());
			ps.setString(4, bCryptEncoder.encode(user.getPassword()));
			ps.setString(5, user.getGender());
			ps.setString(6, user.getCity());
			ps.setString(7, user.getCountry());
			ps.setString(8, user.getPhoto());
			ps.setString(9, user.getDescription());
			ps.setObject(10, user.getBirthDate());
			ps.setString(11, user.getTelNumber());
			return ps.executeUpdate() > 0 ? true : false;
		}

	}

	public ArrayList<Review> getReviewsFromHostsByEmail(String email) throws SQLException {
		String sql = "SELECT CONCAT(u.first_name, \" \", u.last_name) as Reviewer, "
				+ "CONCAT(u2.first_name, \" \", u2.last_name) as Reviewed, cr.content, cr.date "
				+ "FROM CLIENT_REVIEWS cr " + "JOIN USERS u ON cr.reviewer_id = u.ID "
				+ "JOIN USERS u2 ON cr.reviewed_id = u2.ID " + "WHERE u2.email = ?";

		ArrayList<Review> reviewsFromHosts = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, email);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				Review review = new Review(resultSet.getString(1), resultSet.getString(2),
						resultSet.getString("content"), LocalDate.parse(resultSet.getString("date")));
				reviewsFromHosts.add(review);
			}
			return reviewsFromHosts;
		}
	}

	public ArrayList<Review> getReviewsFromGuestsByEmail(String email) throws SQLException {
		String sql = "SELECT CONCAT(u.first_name, \" \", u.last_name), p.title, pc.content, date "
				+ "FROM POST_COMMENTS pc " + "JOIN POSTS p ON pc.post_id = p.ID " + "JOIN USERS u ON pc.user_id = u.id "
				+ "JOIN USERS h ON p.host_id = h.id " + "WHERE h.email = ?;";

		ArrayList<Review> reviewsFromGuests = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, email);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				Review review = new Review(resultSet.getString(1), resultSet.getString("title"),
						resultSet.getString("content"), LocalDate.parse(resultSet.getString("date")));

				reviewsFromGuests.add(review);
			}
			return reviewsFromGuests;
		}
	}

	public boolean editUserData(User user) throws SQLException {
		String sql = "UPDATE USERS " + "SET first_name=?, last_name=?, email=?, gender=?, city=?, country=?, "
				+ "description=?, birth_date=?, telephone_number=? " + "WHERE email = ?;";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getGender());
			ps.setString(5, user.getCity());
			ps.setString(6, user.getCountry());
			ps.setString(7, user.getDescription());
			ps.setObject(8, user.getBirthDate());
			ps.setString(9, user.getTelNumber());
			ps.setString(10, user.getEmail());
			return ps.executeUpdate() > 0 ? true : false;
		}
	}

	public User getUserByID(int ID) throws SQLException, UserDataException {
		String sqlQuery = "SELECT ID, first_name, last_name, email, password, gender, city, "
				+ "country, photo, description, birth_date, telephone_number " + "FROM USERS WHERE ID = ?";

		try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
			ps.setInt(1, ID);
			ResultSet resultSet = ps.executeQuery();
			resultSet.next();
			User user = null;
			user = new User(resultSet.getInt("ID"), resultSet.getString("first_name"), resultSet.getString("last_name"),
					resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("gender"),
					resultSet.getString("city"), resultSet.getString("country"), resultSet.getString("photo"),
					resultSet.getString("description"), LocalDate.parse(resultSet.getString("birth_date")),
					resultSet.getString("telephone_number"));

			return user;
		}
	}

	public String getPhotoPathById(int userID) throws SQLException {
		String sql = "SELECT photo FROM USERS WHERE ID=?;";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, userID);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("photo");
			}
			return null;
		}
	}
}
