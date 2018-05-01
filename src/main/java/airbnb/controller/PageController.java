package airbnb.controller;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import airbnb.dao.BookingDAO;

@Controller
public class PageController {
	private BookingDAO bookingDAO = BookingDAO.instance;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String homepage() {
		return "index";
	}
}
