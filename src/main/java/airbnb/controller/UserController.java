package airbnb.controller;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import airbnb.manager.UserManager;
import airbnb.model.Review;
import airbnb.model.User;
import airbnb.exceptions.UserDataException;

@Controller
public class UserController {
	private UserManager userManager = UserManager.instance;
	
	@RequestMapping(value = "/login", method=RequestMethod.GET)
	public String loginPage() {
		return "login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(HttpServletRequest request) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		try {
			User user = userManager.login(email, password);
			if (user != null) {
				request.getSession().setAttribute("user", user);
				
				ArrayList<Review> reviewsFromHosts = userManager.getReviewsFromHosts(email);
				ArrayList<Review> reviewsFromGuests = userManager.getReviewsFromGuests(email);
				if (reviewsFromHosts != null && !reviewsFromHosts.isEmpty()) {
					request.getSession().setAttribute("reviewsFromHosts", reviewsFromHosts);
					request.getSession().setAttribute("reviewsFromGuests", reviewsFromGuests);
				}
				
				return "personalProfile";
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
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerPage() {
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String register(HttpServletRequest request) throws ServletException, IOException {
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

			// photo upload
//			String path = "/home/dnn";
			String path = "/Users/tanerali/Desktop/ServerUploads";
			Part filePart = request.getPart("photo");
			String fileName = getFileName(filePart);
			String absoluteFilePath = path + File.separator + fileName;

			try (InputStream filecontent = filePart.getInputStream();
					OutputStream out = new FileOutputStream(absoluteFilePath)) {

				byte[] bytes = new byte[1024];

				while ((filecontent.read(bytes)) != -1) {
					out.write(bytes);
				}
			} catch (FileNotFoundException fne) {
				throw new UserDataException("You did not specify a photo to upload");
			}

			user = new User(
					request.getParameter("firstName"), 
					request.getParameter("lastName"),
					request.getParameter("email"), 
					request.getParameter("pass1"), 
					request.getParameter("gender"),
					request.getParameter("city"), 
					request.getParameter("country"), 
					absoluteFilePath,
					request.getParameter("description"), 
					birthDate, 
					request.getParameter("telNumber"));
			if (userManager.register(user)) {
				return "login";
			}

		} catch (UserDataException e) {
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

	/**
	 * Utility method to get file name from HTTP header content-disposition
	 */
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
	
	
}
