package airbnb.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import airbnb.dao.PostDAO;
import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.manager.PostManager;
import airbnb.model.AddPostForm;
import airbnb.model.Post;
import airbnb.model.User;

@RestController
public class AjaxController {

	private PostManager postManager = PostManager.instance;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<String> uploadPost(@RequestBody AddPostForm form, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Post newPost;
		int ID = -1;
		String uploadFolder = "/home/dnn/UPLOADAIRBNB";
		if (user != null) {
			try {
				// Create new post From PostForm
				newPost = new Post(form.getTitle(), form.getDescription(), form.getPrice(), LocalDate.now(),
						Post.Type.getType(form.getType()), user.getUserID());
				// insert new post in DB and cached
				ID = postManager.insertPost(newPost);
				postManager.addPostToCache(newPost);
				// Save Image and insert into DB
				// 1.Save
				byte[] bytes = form.getImage().getBytes();
				Path path = Paths.get(uploadFolder + form.getImage().getOriginalFilename());
				Files.write(path, bytes);
				// 2.Insert
		
				PostDAO.instance.insertImageToPost(path.toString(), ID);
			} catch (InvalidPostDataExcepetion | SQLException | IOException e) {
				System.out.println(e.getMessage());
			}
		}

		return new ResponseEntity<String>(Integer.toString(ID), HttpStatus.OK);
	}
}
