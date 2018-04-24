package airbnb.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import airbnb.dao.LocationDao;
import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.manager.PostManager;
import airbnb.model.Post;
import airbnb.model.User;

@Controller
public class PostController {

	private PostManager postManager = PostManager.instance;
	private LocationDao locationDao = LocationDao.instance;

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
			m.addAttribute("error", e);
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
}
