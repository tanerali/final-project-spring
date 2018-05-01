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
import airbnb.manager.BookingManager;
import airbnb.manager.PostManager;
import airbnb.manager.UserManager;
import airbnb.model.Notification;
import airbnb.model.Post;
import airbnb.model.Review;
import airbnb.model.User;

@Controller
public class UserController {
	private UserManager userManager = UserManager.instance;
	private BookingManager bookingManager = BookingManager.instance;
	private PostManager postManager = PostManager.instance;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpServletRequest request, HttpSession session) {

		try {
			User user = userManager.login(email, password);
			System.out.println("==========================" + user + "==========================");
			if (user != null) {
				session.setAttribute("user", user);

				ArrayList<Review> reviewsFromHosts = userManager.getReviewsFromHosts(email);
				if (reviewsFromHosts != null) {
					session.setAttribute("reviewsFromHosts", reviewsFromHosts);
				}

				ArrayList<Review> reviewsFromGuests = userManager.getReviewsFromGuests(email);
				if (reviewsFromGuests != null) {
					session.setAttribute("reviewsFromGuests", reviewsFromGuests);
				}

				ArrayList<Post> hostedPosts = (ArrayList<Post>) postManager.getPostsByUsers().get(user.getUserID());
				if (hostedPosts != null) {
					session.setAttribute("hostedPosts", hostedPosts);
				}

				ArrayList<Notification> bookingRequestNotifications = bookingManager.checkNotifications(email);
				if (bookingRequestNotifications != null) {
					session.setAttribute("bookingRequests", bookingRequestNotifications);
				}

				return "redirect:personalProfile";
			} else {
				request.setAttribute("wrong_password", new Object());
				return "login";
			}
		} catch (SQLException e) {
			request.setAttribute("wrong_credentials", new Object());
			return "login";
		} catch (UserDataException e) {
			request.setAttribute("exception", e);
			return "error";
		}

	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerPage() {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(HttpServletRequest request, @RequestParam("photo") MultipartFile file)
			throws ServletException, IOException {
		User user = null;
		try {
			// if date is empty, or not exactly as it has to be it
			// throws a parsing exception
			LocalDate birthDate = null;
			try {
				System.out.println(request.getParameter("birthDate"));
				System.out.println(request.getParameter("firstName"));
				birthDate = LocalDate.parse(request.getParameter("birthDate"));
			} catch (Exception e) {
				throw new UserDataException("Invalid birth date entered");
			}

			// if password and confirm password dont match
			if (!request.getParameter("pass1").equals(request.getParameter("pass2"))) {
				throw new UserDataException("Password mismatch");
			}

			// String uploadFolder = "/Users/tanerali/Desktop/ServerUploads/";

			String uploadFolder = "/home/dnn/UPLOADAIRBNB";

			if (file.isEmpty()) {
				throw new UserDataException("Please select a file to upload");
			}

			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(uploadFolder + file.getOriginalFilename());
			Files.write(path, bytes);

			user = new User(request.getParameter("firstName"), 
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

			if (userManager.register(user)) {
				return "login";
			}
		} catch (UserDataException | IOException e) {
			System.out.println(e.getMessage());
			request.setAttribute("exception", e);
			return "register";
		} catch (SQLException e) {
			System.out.println("Couldnt add to database; " + e.getMessage());
			request.setAttribute("exception", e);
			return "error";
		}
		return "login";
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

	@RequestMapping(value = "/personalProfile", method = RequestMethod.GET)
	public String personalProfilePage(HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user != null) {
			return "personalProfile";
		} else {
			return "login";
		}
	}

	@RequestMapping(value = "/personalProfile", method = RequestMethod.POST)
	public String editUser(HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("user");

		try {
			LocalDate birthDate = null;
			try {
				birthDate = LocalDate.parse(request.getParameter("birthDate"));
			} catch (Exception e) {
				throw new UserDataException("Invalid birth date entered");
			}
			if (user != null) {
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
			request.setAttribute("exception", e.getMessage());
		} catch (SQLException e) {
			request.setAttribute("error", e.getMessage());
			return "error";
		}
		return "personalProfile";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "index";
	}

	@RequestMapping(value = "/getProfilePic", method = RequestMethod.GET)
	public String getProfilePic(HttpServletRequest req, HttpServletResponse resp, @RequestParam("id") int userID) {

		String path = null;
		try {
			path = userManager.getPhoto(userID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file = new File(path);
		try (InputStream filecontent = new FileInputStream(file); OutputStream out = resp.getOutputStream()) {

			byte[] bytes = new byte[1024];

			while ((filecontent.read(bytes)) != -1) {
				out.write(bytes);
			}
		} catch (IOException e) {
			req.setAttribute("exception", e.getMessage());
			return "error";
		}
		return null;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String getProfilePage(HttpServletRequest request, @RequestParam("id") int userID) {

		try {
			User user = userManager.getUserByID(userID);
			ArrayList<Review> reviewsFromHosts = userManager.getReviewsFromHosts(user.getEmail());
			ArrayList<Review> reviewsFromGuests = userManager.getReviewsFromGuests(user.getEmail());
			ArrayList<Post> hostedPosts = (ArrayList<Post>) postManager.getPostsByUsers().get(user.getUserID());
			
			request.setAttribute("user", user);
			request.setAttribute("reviewsFromHosts", reviewsFromHosts);
			request.setAttribute("reviewsFromGuests", reviewsFromGuests);
			request.setAttribute("hostedPosts", hostedPosts);
		} catch (UserDataException | SQLException e) {
			request.setAttribute("error", e.getMessage());
			return "error";
		}

		return "profile";
	}
}
