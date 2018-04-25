package airbnb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import airbnb.dao.LocationDao;
import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.manager.BookingManager;
import airbnb.manager.CommentManager;
import airbnb.manager.PostManager;
import airbnb.manager.UserManager;
import airbnb.model.Post;
import airbnb.model.User;
import airbnb.model.Booking;
import airbnb.exceptions.UserDataException;
import airbnb.model.Comment;

@Controller
public class PostController {

	private PostManager postManager = PostManager.instance;
	private LocationDao locationDao = LocationDao.instance;
	private UserManager userManager = UserManager.instance;
	private CommentManager commentManager = CommentManager.instance;
	private BookingManager bookingManager = BookingManager.instance;

	@RequestMapping(value = "/explore", method = RequestMethod.GET)
	public String explore(Model m) {
		ArrayList<Post> posts = null;
		try {
			posts = (ArrayList<Post>) postManager.getAllPosts();
			Map<String, TreeSet<String>> locations = locationDao.getLocations();
			System.out.println(posts.size());
			if (posts != null) {
				m.addAttribute("posts", posts);
				m.addAttribute("locations", locations);
			}
		} catch (SQLException | InvalidPostDataExcepetion e) {
			m.addAttribute("error", e.getMessage());
			return "error";
		}
		return "explore";

	}

	@RequestMapping(value = "/host", method = RequestMethod.GET)
	public String createPostPage() {
		return "host";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String savePost(HttpServletRequest request) {
		User currUser = (User) request.getSession().getAttribute("user");
		// not logged
		// if (currUser == null) {
		// System.out.println("not logged");
		// return "login";
		// } else {
		System.out.println("logged");
		// TODO UPLOAD PICTURE
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		// int price = Integer.valueOf(request.getParameter("price"));
		String type = request.getParameter("type");
		// int hostID = currUser.getUserID();

		try {
			Post newPost = new Post(title, description, 1, LocalDate.now(), Post.Type.getType(type), hostID);
			postManager.insertPost(newPost);
		} catch (InvalidPostDataExcepetion | SQLException e) {
			e.printStackTrace();
		}
		// }
		return "host";
	}
	
	@RequestMapping(value = "/post", method = RequestMethod.GET)
	public String specificPostPage(HttpServletRequest request, @RequestParam("id") int postID) {
		
		User hostUser = null;
		Post currPost = postManager.getPostsByID().get(postID);
		ArrayList<Comment> comments = new ArrayList<>();
		
		if (currPost != null) {
			try {
				hostUser = userManager.getUserByID(currPost.getHostID());
				comments = commentManager.getCommentsForPost(postID);
			} catch (SQLException | UserDataException e) {
				request.setAttribute("exception", e.getMessage());
				return "error";
			}
			
			request.setAttribute("user", hostUser);
			request.setAttribute("post", currPost);
			request.setAttribute("comments", comments);
		}
		return "post";
	}
	
	@RequestMapping(value = "/getThumbnail", method = RequestMethod.GET)
	public String getPostThumbnail(
			HttpServletRequest req, HttpServletResponse resp, @RequestParam("id") int postID) {
		
		if (postID != 0) {
			try {
				String path = postManager.getThumbnail(postID);
				if (path != null) {
					File file = new File(path);

					try (InputStream filecontent = new FileInputStream(file);
							OutputStream out = resp.getOutputStream()) {

						byte[] bytes = new byte[1024];

						while ((filecontent.read(bytes)) != -1) {
							out.write(bytes);
						}
					}
				}
			} catch (IOException | SQLException e) {
				req.setAttribute("error", e);
				return "error";
			}
		}
		return null;
	}
	
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public String leaveCommentOnPost(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam("comment") String commentText,
			@RequestParam("postID") int postID) {
		
		User user = (User) session.getAttribute("user");
		
		if (commentText != null && !commentText.isEmpty() && user != null) {
			Comment comment = new Comment(
					user.getUserID(), 
					(user.getFirstName()+ " "+ user.getLastName()), 
					postID, 
					commentText, 
					LocalDate.now());
			try {
				commentManager.addCommentToPost(comment);
//				return ResponseEntity.status(HttpStatus.SC_OK).body(null);
				
			} catch (SQLException e) {
				request.setAttribute("error", e.getMessage());
				return "error";
			}
		}
		return "redirect:post?id="+postID;
//		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> deleteCommentOnPost(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("id") int commentID) {
		
		try {
			if (commentManager.deleteComment(commentID)) {
				return ResponseEntity.status(HttpStatus.SC_OK).body(null);
			}
		} catch (SQLException e) {
			request.setAttribute("error", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(null);
		
	}
	
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
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String bookPost(HttpServletRequest request, @RequestParam("search") String search) {
		
		if (search != null) {
			List<Post> result = PostManager.instance.searchPost(search);
			if (!result.isEmpty()) {
				System.out.println("not empty");
				request.setAttribute("posts", result);
			}
			// String resultJSON = new Gson().toJson(result);
			// response.setContentType("text/html");
			// response.getWriter().write(resultJSON);
			// System.out.println(resultJSON);
		}
		return "explore";
	}
	
			
}