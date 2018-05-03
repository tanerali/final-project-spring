package airbnb.manager;

import java.sql.SQLException;
import java.util.ArrayList;

import airbnb.dao.UserDAO;
import airbnb.exceptions.UserDataException;
import airbnb.exceptions.UserDoesNotExistException;
import airbnb.model.Review;
import airbnb.model.User;

public enum UserManager {
	INSTANCE;
	private UserDAO userDAO = UserDAO.INSTANCE;
	
	public User login(String email, String password) throws SQLException, UserDataException, UserDoesNotExistException {
		return userDAO.getUserByEmail(email, password);
	}

	public boolean register(User user) throws SQLException {
		return userDAO.addUser(user);
	}

	public ArrayList<Review> getReviewsFromHosts(String email) throws SQLException {
		ArrayList<Review> reviewsFromHosts = userDAO.getReviewsFromHostsByEmail(email);
		return reviewsFromHosts;
	}

	public ArrayList<Review> getReviewsFromGuests(String email) throws SQLException {
		ArrayList<Review> reviewsFromGuests = userDAO.getReviewsFromGuestsByEmail(email);
		return reviewsFromGuests;
	}

	public boolean editUser(User user) throws SQLException {
		return userDAO.editUserData(user);
	}

	public User getUserByID(int userID) throws SQLException, UserDataException {
		return userDAO.getUserByID(userID);
	}

	public String getPhoto(int userID) throws SQLException {
		return userDAO.getPhotoPathById(userID);
	}
	
	
}
