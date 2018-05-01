package airbnb.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import airbnb.dao.BookingDAO;
import airbnb.manager.BookingManager;
import airbnb.model.Booking;
import airbnb.model.Notification;
import airbnb.model.User;

@Controller
public class BookingController {
	private BookingManager bookingManager = BookingManager.instance;

	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public String bookPost(HttpSession session, HttpServletRequest request,
			@RequestParam("postID") int postID,
			@RequestParam("dateFrom") @DateTimeFormat(iso = ISO.DATE) LocalDate dateFrom,
			@RequestParam("dateTo") @DateTimeFormat(iso = ISO.DATE) LocalDate dateTo) {
				
		User user = (User)session.getAttribute("user");
		
		Booking booking = new Booking(postID, user.getUserID(), dateFrom, dateTo);
		
		try {
			bookingManager.requestBooking(booking);
		} catch (SQLException e) {
			request.setAttribute("error", e.getMessage());
			return "error";
		}
		return "redirect:post?id="+ postID;
	}
	
	@RequestMapping(value = "/notification/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> rejectBookingRequest(
			HttpServletRequest request,
			HttpSession session,
			@PathVariable("id") int notificationID) {
		
		try {
			if (bookingManager.deleteBookingRequest(notificationID)) {
				ArrayList<Notification> bookingRequestsInSession = 
						(ArrayList<Notification>)session.getAttribute("bookingRequests");
				
				for (Iterator<Notification> iterator = bookingRequestsInSession.iterator(); iterator.hasNext();) {
					Notification notification = (Notification) iterator.next();
					if (notification.getNotificationID() == notificationID) {
						iterator.remove();
					}
				}
				return ResponseEntity.status(HttpStatus.SC_OK).body(null);
			}
		} catch (SQLException e) {
			request.setAttribute("error", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(null);

	}
	
	@RequestMapping(value = "/notification/{id}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> acceptBookingRequest(
			HttpServletRequest request,
			HttpSession session,
			@PathVariable("id") int notificationID) {
		
		try {
			if (bookingManager.acceptBookingRequest(notificationID)) {
				ArrayList<Notification> bookingRequestsInSession = 
						(ArrayList<Notification>)session.getAttribute("bookingRequests");
				
				for (ListIterator<Notification> iterator = 
						bookingRequestsInSession.listIterator(); iterator.hasNext();) {
					
					Notification notification = (Notification) iterator.next();
					if (notification.getNotificationID() == notificationID) {
						iterator.remove();
					}
				}
				return ResponseEntity.status(HttpStatus.SC_OK).body(null);
			}
		} catch (SQLException e) {
			request.setAttribute("error", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(null);
	}
	
	//if it is checkout day, then the visit has concluded
	//@Scheduled(cron="*/10 * * * * *")
	@Scheduled(cron="0 0 12 * * *")
	public void changeStatusToVisited() {
		try {
			BookingDAO.instance.changeStatusToVisited();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
