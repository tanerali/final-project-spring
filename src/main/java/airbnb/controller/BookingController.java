package airbnb.controller;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import airbnb.dao.BookingDAO;
import airbnb.exceptions.UserDataException;
import airbnb.manager.BookingManager;
import airbnb.model.Booking;
import airbnb.model.User;

@Controller
public class BookingController {
	private BookingManager bookingManager = BookingManager.INSTANCE;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private PostController postController;

	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public String bookPost(Model m, HttpSession session, HttpServletRequest request, @RequestParam("postID") int postID,
			@RequestParam("dateFrom") @DateTimeFormat(iso = ISO.DATE) LocalDate dateFrom,
			@RequestParam("dateTo") @DateTimeFormat(iso = ISO.DATE) LocalDate dateTo) throws SQLException {

		User user = (User) session.getAttribute("user");

		if (user != null) {
			try {
				Booking booking = new Booking(postID, user.getUserID(), dateFrom, dateTo);
				bookingManager.requestBooking(booking);

			} catch (UserDataException e) {
				e.printStackTrace();
				request.setAttribute("error", e.getMessage());
				return postController.specificPostPage(m, request, session, postID);
			}
		}
		return "redirect:post?id=" + postID;
	}

	// if it is checkout day, then the visit has concluded
	// @Scheduled(cron="*/10 * * * * *")
	@Scheduled(cron = "0 0 12 * * *")
	public void changeStatusToVisited() {
		try {
			BookingDAO.INSTANCE.changeStatusToVisited();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// after checkout ask users to rate
	// @Scheduled(cron="*/10 * * * * *")
	@Scheduled(cron = "0 30 12 * * *")
	public void askUsersToRatePlaceAfterVisit() {
		ArrayList<String> emails = new ArrayList<>();
		try {
			emails = BookingDAO.INSTANCE.askUsersToRatePlaceAfterVisit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (String email : emails) {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			try {
				MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, true);
				mailMsg.setFrom("ittalents.airbnb@gmail.com");
				mailMsg.setTo(email);
				mailMsg.setSubject("Test mail");
				mailMsg.setText("Dear User, now you can rate the place you stayed at", true);
				String logoPath = "/Users/tanerali/Desktop/finalProject Photos/logo.png";
				FileSystemResource file = new FileSystemResource(new File(logoPath));
				mailMsg.addAttachment("myPic.jpg", file);
				mailSender.send(mimeMessage);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
}
