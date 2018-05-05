package airbnb.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import airbnb.dao.PostDAO;
import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.manager.BookingManager;
import airbnb.manager.CommentManager;
import airbnb.manager.PostManager;
import airbnb.model.Comment;
import airbnb.model.Post;
import airbnb.model.User;

@RestController
@MultipartConfig
public class PostRestController {
	
	private PostManager postManager = PostManager.INSTANCE;
	private BookingManager bookingManager = BookingManager.INSTANCE;
	private CommentManager commentManager = CommentManager.INSTANCE;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<String> uploadPost(
			HttpServletRequest request, 
			HttpSession session,
			@RequestParam("file") MultipartFile file) throws SQLException {
		
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		int price = Integer.valueOf(request.getParameter("price"));
		String type = request.getParameter("type");
		User user = (User) session.getAttribute("user");
		String country = request.getParameter("country");
		String city = request.getParameter("city");
		
		Post newPost;
		int ID = -1;
		//String uploadFolder = "/home/dnn/UPLOADAIRBNB";
		String uploadFolder = "/Users/tanerali/Desktop/ServerUploads/";
		if (user != null) {
			try {
				// Create new post From PostForm
				newPost = new Post(
						title, 
						description, 
						price, 
						LocalDate.now(),
						Post.Type.getType(type), 
						user.getUserID(),
						country,
						city);
						
				// insert new post in DB and cached
				ID = postManager.insertPost(newPost);
				// Save Image and insert into DB
				// 1.Save
				File fileOnDisk = new File(uploadFolder + file.getOriginalFilename());
				Files.copy(file.getInputStream(), fileOnDisk.toPath(), StandardCopyOption.REPLACE_EXISTING);
				// 2.Insert
				PostDAO.INSTANCE.insertImageToPost(fileOnDisk.toPath().toString(), ID);
			} catch (InvalidPostDataExcepetion | IOException e) {
				e.printStackTrace();
			}
		}

		return new ResponseEntity<String>(Integer.toString(ID), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/editPost", method = RequestMethod.POST)
	public ResponseEntity editPost(HttpServletRequest request) throws SQLException {
		// New data
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		int price = Integer.valueOf(request.getParameter("price"));
		String type = request.getParameter("type");
		int postID = Integer.valueOf(request.getParameter("ID"));
		int userID = Integer.valueOf(request.getParameter("userID"));
		LocalDate date = LocalDate.parse(request.getParameter("date"));

		Post post = null;
		try {
			post = new Post(postID, title, description, price, date, Post.Type.getType(type), userID);
		} catch (InvalidPostDataExcepetion e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (PostManager.INSTANCE.editPost(post)) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public ResponseEntity leaveCommentOnPost(
			HttpServletRequest request, 
			HttpSession session,
			@RequestBody Comment comment) throws SQLException {

		User user = (User) session.getAttribute("user");

		if (comment.getContent() != null && !comment.getContent().isEmpty() && user != null) {

			try {
				comment.setUserID(user.getUserID());
				comment.setFullName(user.getFirstName() + " " + user.getLastName());
				comment.setDate(LocalDate.now());

				int commentID = commentManager.addCommentToPost(comment);
				comment.setCommentID(commentID);

				if (commentID > 0) {
					return ResponseEntity.status(HttpStatus.OK).body(comment);
				}
			} catch (InvalidPostDataExcepetion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
	public ResponseEntity deleteCommentOnPost(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable("id") int commentID) throws SQLException {

		if (commentManager.deleteComment(commentID)) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value = "/rate", method = RequestMethod.POST)
	public ResponseEntity ratePost(
			HttpSession session,
			@RequestParam("rating") int rating,
			@RequestParam("postID") int postID) throws SQLException {
		
		User user = (User) session.getAttribute("user");
		
		if (user != null && bookingManager.ratePost(postID, user.getUserID(), rating)) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value = "/getRating", method = RequestMethod.GET)
	public double getRating(@RequestParam("id") int postID) throws SQLException {
		return postManager.getPostRating(postID);
	}
}
