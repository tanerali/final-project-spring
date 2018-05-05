package airbnb.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import airbnb.dao.LocationDao;
import airbnb.exceptions.UserDataException;
import airbnb.manager.BookingManager;
import airbnb.model.Booking;
import airbnb.model.Notification;
import airbnb.model.User;

@RestController
public class BookingRestController {
	
	private LocationDao locationDao = LocationDao.INSTANCE;
	private BookingManager bookingManager = BookingManager.INSTANCE;
	
	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public ResponseEntity bookPost(Model m, HttpSession session, HttpServletRequest request, 
			@RequestParam("postID") int postID,
			@RequestParam("dateFrom") @DateTimeFormat(iso = ISO.DATE) LocalDate dateFrom,
			@RequestParam("dateTo") @DateTimeFormat(iso = ISO.DATE) LocalDate dateTo) throws SQLException {

		User user = (User) session.getAttribute("user");

		if (user != null) {
			try {
				Booking booking = new Booking(postID, user.getUserID(), dateFrom, dateTo);
				bookingManager.requestBooking(booking);

			} catch (UserDataException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@RequestMapping(value = "/notification/{id}", method = RequestMethod.DELETE)
	public ResponseEntity rejectBookingRequest(
			HttpServletRequest request,
			HttpSession session,
			@PathVariable("id") int notificationID) throws SQLException {

		User user = (User)session.getAttribute("user");
		
		if (user != null && bookingManager.deleteBookingRequest(notificationID)) {
			
			ArrayList<Notification> bookingRequestsInSession = 
					(ArrayList<Notification>)session.getAttribute("bookingRequests");

			if (bookingRequestsInSession != null) {
				//using iterator to avoid ConcurrentModificationException
				for (Iterator<Notification> iterator = bookingRequestsInSession.iterator(); iterator.hasNext();) {
					Notification notification = (Notification) iterator.next();
					if (notification.getNotificationID() == notificationID) {
						iterator.remove();
					}
				}
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value = "/notification/{id}", method = RequestMethod.POST)
	public ResponseEntity acceptBookingRequest(
			HttpServletRequest request,
			HttpSession session,
			@PathVariable("id") int notificationID) throws SQLException {
		
		User user = (User)session.getAttribute("user");
		
		if (user != null && bookingManager.acceptBookingRequest(notificationID)) {
			ArrayList<Notification> bookingRequestsInSession = 
					(ArrayList<Notification>)session.getAttribute("bookingRequests");
			
			if (bookingRequestsInSession != null) {
				for (ListIterator<Notification> iterator = 
						bookingRequestsInSession.listIterator(); iterator.hasNext();) {

					Notification notification = (Notification) iterator.next();
					if (notification.getNotificationID() == notificationID) {
						iterator.remove();
					}
				}
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value = "/locations", method = RequestMethod.GET)
	public Map<String, TreeSet<String>> getLocations() {
		Map<String, TreeSet<String>> locations = locationDao.getLocations();
		return locations;
	}
}
