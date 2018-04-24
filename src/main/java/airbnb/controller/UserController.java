package airbnb.controller;



import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

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
	
	@RequestMapping(value = "/login", method=RequestMethod.GET)
	public String loginPage() {
		return "login";
	}
	
}
