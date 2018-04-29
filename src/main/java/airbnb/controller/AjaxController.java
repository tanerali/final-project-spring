package airbnb.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import airbnb.dao.PostDAO;
import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.manager.PostManager;
import airbnb.model.Post;
import airbnb.model.User;

@RestController
@MultipartConfig
public class AjaxController {

	private PostManager postManager = PostManager.instance;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<String> uploadPost(HttpServletRequest request, HttpSession session,
			@RequestParam("file") MultipartFile file) {
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		int price = Integer.valueOf(request.getParameter("price"));
		String type = request.getParameter("type");
		User user = (User) session.getAttribute("user");
		int hostID = user.getUserID();
		Post newPost;
		int ID = -1;
		String uploadFolder = "/home/dnn/UPLOADAIRBNB";
		if (user != null) {
			try {
				// Create new post From PostForm
				newPost = new Post(title, description, price, LocalDate.now(), Post.Type.getType(type),
						user.getUserID());
				// insert new post in DB and cached
				ID = postManager.insertPost(newPost);
				postManager.addPostToCache(newPost);
				// Save Image and insert into DB
				// 1.Save
				File fileOnDisk = new File(uploadFolder + file.getOriginalFilename());
				Files.copy(file.getInputStream(), fileOnDisk.toPath(), StandardCopyOption.REPLACE_EXISTING);
				// 2.Insert
				PostDAO.instance.insertImageToPost(fileOnDisk.toPath().toString(), ID);
			} catch (InvalidPostDataExcepetion | SQLException | IOException e) {
				System.out.println(e.getMessage());
			}
		}

		return new ResponseEntity<String>(Integer.toString(ID), HttpStatus.OK);
	}

}
