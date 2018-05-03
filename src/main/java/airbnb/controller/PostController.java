package airbnb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.manager.BookingManager;
import airbnb.dao.PostDAO;
import airbnb.exceptions.UserDataException;
import airbnb.manager.CommentManager;
import airbnb.manager.PostManager;
import airbnb.manager.UserManager;
import airbnb.model.Comment;
import airbnb.model.Post;
import airbnb.model.User;

@Controller
public class PostController {

	private PostManager postManager = PostManager.INSTANCE;
	private UserManager userManager = UserManager.INSTANCE;
	private CommentManager commentManager = CommentManager.INSTANCE;
	private BookingManager bookingManager = BookingManager.INSTANCE;

	@RequestMapping(value = "/explore", method = RequestMethod.GET)
	public String explore(HttpServletRequest request) throws SQLException {
		try {
			ArrayList<Post> posts = (ArrayList<Post>) postManager.getAllPosts();

			if (posts != null) {
				request.setAttribute("posts", posts);
			}
		} catch (InvalidPostDataExcepetion e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			return "error";
		}
		return "explore";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(Model m, @RequestParam("search") String search, HttpServletRequest req,
			HttpServletResponse resp) {
		ArrayList<Post> posts = (ArrayList<Post>) postManager.searchPost(search);

		if (posts != null) {
			req.setAttribute("posts", posts);
		}
		return "explore";

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

	@RequestMapping(value = "/post", method = RequestMethod.GET)
	public String specificPostPage(Model m, HttpServletRequest request, HttpSession session,
			@RequestParam("id") int postID) {

		Post currPost = postManager.getPostsByID().get(postID);
		User hostUser = null;
		ArrayList<Comment> comments = new ArrayList<>();
		ArrayList<LocalDate> unavailableDates = new ArrayList<>();
		ArrayList<String> allPhotos = new ArrayList<>();
		double postRating;

		if (currPost != null) {
			try {
				hostUser = userManager.getUserByID(currPost.getHostID());
				comments = commentManager.getCommentsForPost(postID);
				postRating = postManager.getPostRating(postID);
				allPhotos = postManager.getAllPhotos(postID);
				if (session.getAttribute("user") != null) {
					unavailableDates = bookingManager.getUnavailableDates(postID);

					ArrayList<String> unavailableDatesString = new ArrayList<>();
					for (LocalDate unavailableDate : unavailableDates) {
						unavailableDatesString.add("\'" + unavailableDate.toString() + "\'");
					}

					request.setAttribute("unavailableDatesString", unavailableDatesString);
				}
			} catch (SQLException | UserDataException e) {
				e.printStackTrace();
				request.setAttribute("exception", e.getMessage());
				return "error";
			}

			request.setAttribute("rating", postRating);
			request.setAttribute("user", hostUser);
			request.setAttribute("post", currPost);
			request.setAttribute("comments", comments);
			request.setAttribute("photos", allPhotos);

			return "post";
		}
		return "redirect:html/404.html";
	}

	@RequestMapping(value = "/getThumbnail", method = RequestMethod.GET)
	public String getPostThumbnail(HttpServletRequest req, HttpServletResponse resp, @RequestParam("id") int postID) {

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
	@ResponseBody
	public ResponseEntity<Object> leaveCommentOnPost(HttpServletRequest request, HttpSession session,
			@RequestBody Comment comment) {

		User user = (User) session.getAttribute("user");

		if (comment.getContent() != null && !comment.getContent().isEmpty() && user != null
		// && bookingManager.userHasVisited(user, comment.getPostID())
		) {

			try {
				comment.setUserID(user.getUserID());
				comment.setFullName(user.getFirstName() + " " + user.getLastName());
				comment.setDate(LocalDate.now());

				int commentID = commentManager.addCommentToPost(comment);
				comment.setCommentID(commentID);

				if (commentID > 0) {
					return ResponseEntity.status(HttpStatus.SC_OK).body(comment);
				}

			} catch (SQLException e) {
				request.setAttribute("error", e.getMessage());
				// return "error";
			} catch (InvalidPostDataExcepetion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// return "redirect:post?id=" + comment.getPostID();
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(null);
	}

	@RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> deleteCommentOnPost(HttpServletRequest request, HttpServletResponse response,
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

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deletePost(HttpServletRequest request, HttpSession session, @RequestParam("id") int postID) {
		try {
			PostDAO.INSTANCE.removePost(postID);
		} catch (SQLException e) {
			request.setAttribute("error", e);
			return "error";
		}
		return "index";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String showEditForm(@RequestParam("id") int postID, Model m) {
		Post currPost = postManager.getPostsByID().get(postID);
		m.addAttribute("post", currPost);
		return "editPost";
	}

	@ResponseBody
	@RequestMapping(value = "/multipleUpload", method = RequestMethod.POST)
	public String uploadMultipleImgs(@RequestParam("file") MultipartFile file, @RequestParam("ID") int ID,
			HttpServletRequest req) {
		System.out.println("================" + file.getOriginalFilename() + "================");
		String uploadFolder = "/home/dnn/UPLOADAIRBNB/";
		System.out.println("ID = " + ID);
		File fileOnDisk = new File(uploadFolder + file.getOriginalFilename());
		try {
			Files.copy(file.getInputStream(), fileOnDisk.toPath(), StandardCopyOption.REPLACE_EXISTING);
			PostDAO.INSTANCE.insertImageToPost(fileOnDisk.toPath().toString(), ID);
		} catch (SQLException | IOException e) {
			req.setAttribute("error", e);
			return "forward:error";
		}
		return fileOnDisk.toString();
	}

	@RequestMapping(value = "/editPost", method = RequestMethod.POST)
	public String editPost(HttpServletRequest request) {
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
		System.out.println("========" + post + "========");
		try {
			PostDAO.INSTANCE.editPost(post);
		} catch (SQLException e) {
			request.setAttribute("error", e);
			return "error";
		}
		request.setAttribute("post", post);
		return "explore";
	}

	@RequestMapping(value = "/getPhoto", method = RequestMethod.GET)
	public String getPhoto(HttpServletRequest req, HttpServletResponse resp, @RequestParam("path") String path) {

		try {
			if (path != null) {
				File file = new File(path);
				try (InputStream filecontent = new FileInputStream(file); OutputStream out = resp.getOutputStream()) {

					byte[] bytes = new byte[1024];

					while ((filecontent.read(bytes)) != -1) {
						out.write(bytes);
					}
				}
			}
		} catch (IOException e) {
			req.setAttribute("error", e);
			return "error";
		}
		return null;
	}
}
