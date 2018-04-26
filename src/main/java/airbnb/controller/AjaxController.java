package airbnb.controller;

import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.manager.PostManager;
import airbnb.model.AddPostForm;
import airbnb.model.Post;

@RestController
public class AjaxController {

	private PostManager postManager = PostManager.instance;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadPost(@RequestBody AddPostForm form) {
		Post newPost;
		try {
			// Create new post From PostForm
			newPost = new Post(form.getTitle(), form.getDescription(), form.getPrice(), LocalDate.now(),
					Post.Type.getType(form.getType()), 1);
			// insert new post in DB
			System.out.println("Generated new Post " + newPost);
			postManager.insertPost(newPost);
		} catch (InvalidPostDataExcepetion | SQLException e) {
			System.out.println(e.getMessage());
		}
		return "index";
	}
}
