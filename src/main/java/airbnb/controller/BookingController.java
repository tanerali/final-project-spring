package airbnb.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import airbnb.manager.BookingManager;

@Controller
public class BookingController {
	private BookingManager bookingManager = BookingManager.INSTANCE;

	@Autowired
	private JavaMailSenderImpl mailSender;
	
	//if it is checkout day, then the visit has concluded
	//@Scheduled(cron="*/10 * * * * *")
	@Scheduled(cron="0 0 12 * * *")
	public void changeStatusToVisited() throws SQLException {
		bookingManager.changeStatusToVisited();
	}
	
	//after checkout ask users to rate
	//@Scheduled(cron="*/10 * * * * *")
	@Scheduled(cron="0 30 12 * * *")
	public void askUsersToRatePlaceAfterVisit() throws SQLException {
		ArrayList<String> emails = bookingManager.askUsersToRatePlaceAfterVisit();
		
		for (String email : emails) {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			try {
				MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, true);
				mailMsg.setFrom("ittalents.airbnb@gmail.com");
				mailMsg.setTo(email);
				mailMsg.setSubject("Test mail");
				mailMsg.setText("Dear User, now you can rate the place you stayed at", true);
				String logoPath = "/Users/tanerali/Desktop/final-project photos/logo.png";
				FileSystemResource file = new FileSystemResource(new File(logoPath));
				mailMsg.addAttachment("myPic.jpg", file);
				mailSender.send(mimeMessage);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
}
