package airbnb.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import airbnb.dao.LocationDao;
import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.manager.PostManager;
import airbnb.model.AddPostForm;
import airbnb.model.Post;
import airbnb.model.User;

@RestController
public class AjaxController {

	private PostManager postManager = PostManager.instance;
	private LocationDao locationDao = LocationDao.instance;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<String> uploadPost(@RequestBody AddPostForm form, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Post newPost;
		int ID = -1;
		
		if (user != null) {
			try {
				// Create new post From PostForm
				newPost = new Post(
						form.getTitle(), 
						form.getDescription(), 
						form.getPrice(), 
						LocalDate.now(),
						Post.Type.getType(form.getType()), 
						user.getUserID(),
						form.getCountry(),
						form.getCity());
				// insert new post in DB
				ID = postManager.insertPost(newPost);
			} catch (InvalidPostDataExcepetion | SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		
		return new ResponseEntity<String>(Integer.toString(ID), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/locations", method = RequestMethod.GET)
	public Map<String, TreeSet<String>> getLocations() {
		Map<String, TreeSet<String>> locations = locationDao.getLocations();
		ArrayList<String> countries = new ArrayList<>(locations.keySet());
		System.out.println( countries);
		
		return locations;
	}
}
