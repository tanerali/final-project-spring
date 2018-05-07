package airbnb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import airbnb.exceptions.UserDataException;
import airbnb.exceptions.UserDoesNotExistException;
import airbnb.manager.BookingManager;
import airbnb.manager.PostManager;
import airbnb.manager.UserManager;
import airbnb.model.Notification;
import airbnb.model.Post;
import airbnb.model.Review;
import airbnb.model.User;

@Controller
public class UserController {
	private UserManager userManager = UserManager.INSTANCE;
	private BookingManager bookingManager = BookingManager.INSTANCE;
	private PostManager postManager = PostManager.INSTANCE;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(
			@RequestParam("email") String email, 
			@RequestParam("password") String password,
			HttpServletRequest request, 
			HttpSession session) throws SQLException {

		try {
			User user = userManager.login(email, password);
			if (user != null) {
				session.setAttribute("user", user);
				
				ArrayList<Review> reviewsFromHosts = userManager.getReviewsFromHosts(user.getEmail());
				if (reviewsFromHosts != null) {
					session.setAttribute("reviewsFromHosts", reviewsFromHosts);
				}

				ArrayList<Review> reviewsFromGuests = userManager.getReviewsFromGuests(user.getEmail());
				if (reviewsFromGuests != null) {
					session.setAttribute("reviewsFromGuests", reviewsFromGuests);
				}

				ArrayList<Post> hostedPosts = (ArrayList<Post>) postManager.getPostsByUsers().get(user.getUserID());
				if (hostedPosts != null) {
					session.setAttribute("hostedPosts", hostedPosts);
				}

				ArrayList<Notification> bookingRequestNotifications = bookingManager.checkNotifications(user.getEmail());
				if (bookingRequestNotifications != null) {
					session.setAttribute("bookingRequests", bookingRequestNotifications);
				}
				
				return "redirect:personalProfile";
			} else {
				request.setAttribute("error", "Wrong credentials");
				return "login";
			}
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			return "login";
		} catch (UserDataException e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			return "error";
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(HttpServletRequest request, @RequestParam("photo") MultipartFile file)
			throws SQLException {
		
		try {
			// if date is empty, or not exactly as it has to be it
			// throws a parsing exception
			LocalDate birthDate = null;
			try {
				birthDate = LocalDate.parse(request.getParameter("birthDate"));
			} catch (Exception e) {
				throw new UserDataException("Invalid birth date entered");
			}

			// if password and confirm password dont match
			if (!request.getParameter("pass1").equals(request.getParameter("pass2"))) {
				throw new UserDataException("Password mismatch");
			}

			String uploadFolder = "/Users/tanerali/Desktop/ServerUploads/";
			//String uploadFolder = "/home/dnn/UPLOADAIRBNB/";

			if (file.isEmpty()) {
				throw new UserDataException("Please select a file to upload");
			}

			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(uploadFolder + file.getOriginalFilename());
			Files.write(path, bytes);
			
			User newUser = new User(request.getParameter("firstName"), 
					request.getParameter("lastName"),
					request.getParameter("email"),
					request.getParameter("pass1"),
					request.getParameter("gender"),
					request.getParameter("city"),
					request.getParameter("country"),
					path.toString(),
					request.getParameter("description"), 
					birthDate,
					request.getParameter("telNumber"));

			if (userManager.register(newUser)) {
				return "login";
			}
		} catch (UserDataException e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			return "register";
		} catch (IOException e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			return "error";
		} 
		return "register";
	}

	// Utility method to get file name from HTTP header content-disposition
	public static String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return "";
	}

	@RequestMapping(value = "/personalProfile", method = RequestMethod.POST)
	public String editUser(HttpSession session, HttpServletRequest request) throws SQLException {
		User user = (User) session.getAttribute("user");
		
		try {
			if (user != null) {
				LocalDate birthDate = null;
				try {
					birthDate = LocalDate.parse(request.getParameter("birthDate"));
				} catch (Exception e) {
					throw new UserDataException("Invalid birth date entered");
				}
				
				user.setFirstName(request.getParameter("firstName"));
				user.setLastName(request.getParameter("lastName"));
				user.setEmail(request.getParameter("email"));
				user.setGender(request.getParameter("gender"));
				user.setCity(request.getParameter("city"));
				user.setCountry(request.getParameter("country"));
				user.setDescription(request.getParameter("description"));
				user.setBirthDate(birthDate);
				user.setTelNumber(request.getParameter("telNumber"));

				userManager.editUser(user);
			}
		} catch (UserDataException e) {
			// this exception goes to personalProfile and gets displayed
			// nicely to user in red font
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
		}
		return "personalProfile";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "index";
	}

	@RequestMapping(value = "/getProfilePic", method = RequestMethod.GET)
	public String getProfilePic(
			HttpServletRequest req, 
			HttpServletResponse resp, 
			@RequestParam("id") int userID) throws SQLException {

		String path = userManager.getPhoto(userID);
		
		if (path != null) {
			File file = new File(path);
			try (InputStream filecontent = new FileInputStream(file); 
				 OutputStream out = resp.getOutputStream()) {

				byte[] bytes = new byte[1024];

				while ((filecontent.read(bytes)) != -1) {
					out.write(bytes);
				}
			} catch (IOException e) {
				e.printStackTrace();
				req.setAttribute("error", e.getMessage());
				return "error";
			}
		}
		return null;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String getProfilePage(HttpServletRequest request, @RequestParam("id") int userID) 
			throws SQLException {

		try {
			User user = userManager.getUserByID(userID);
			ArrayList<Review> reviewsFromHosts = userManager.getReviewsFromHosts(user.getEmail());
			ArrayList<Review> reviewsFromGuests = userManager.getReviewsFromGuests(user.getEmail());
			ArrayList<Post> hostedPosts = (ArrayList<Post>) postManager.getPostsByUsers().get(user.getUserID());
			
			request.setAttribute("user", user);
			request.setAttribute("reviewsFromHosts", reviewsFromHosts);
			request.setAttribute("reviewsFromGuests", reviewsFromGuests);
			request.setAttribute("hostedPosts", hostedPosts);
		} catch (UserDataException e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			return "error";
		}
		return "profile";
	}
}
