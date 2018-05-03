package airbnb.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import airbnb.model.User;

@Controller
public class PageController {
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String homePage() {
		return "index";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registrationPage() {
		return "register";
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
	
	@RequestMapping(value = "/host", method = RequestMethod.GET)
	public String createPostPage(HttpSession session) {
		User currUser = (User) session.getAttribute("user");
		// check session if logged to return to host
		if (currUser != null) {
			return "host";
		}
		// otherwise to "login"
		return "login";
	}
}
