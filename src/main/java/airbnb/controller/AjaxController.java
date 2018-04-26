package airbnb.controller;

import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> uploadPost(@RequestBody AddPostForm form) {
		Post newPost;
		int ID = -1;
		try {
			// Create new post From PostForm
			newPost = new Post(form.getTitle(), form.getDescription(), form.getPrice(), LocalDate.now(),
					Post.Type.getType(form.getType()), 1);
			// insert new post in DB
			ID = postManager.insertPost(newPost);
		} catch (InvalidPostDataExcepetion | SQLException e) {
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<String>(Integer.toString(ID), HttpStatus.OK);
	}
}
