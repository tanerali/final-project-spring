package airbnb.model;

import java.time.LocalDate;

import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.exceptions.UserDataException;

public class Comment {

	private int commentID;
	private int userID;
	private String fullName;
	private int postID;
	private String content;
	private LocalDate date;
	
	public Comment() {
	}
	
	public Comment(
			int userID, 
			String fullName, 
			int postID, 
			String content, 
			LocalDate date) throws InvalidPostDataExcepetion {
		
		setUserID(userID);
		setFullName(fullName);
		setPostID(postID);
		setContent(content);
		setDate(date);
	}

	public Comment(
			int commentID, 
			int userID, 
			String fullName, 
			int postID, 
			String content, 
			LocalDate date) throws InvalidPostDataExcepetion {
		
		this(userID, fullName, postID, content, date);
		setCommentID(commentID);
	}

	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getCommentID() {
		return commentID;
	}

	public void setCommentID(int commentID) throws InvalidPostDataExcepetion {
		if (commentID > 0) {
			this.commentID = commentID;
		} else {
			throw new InvalidPostDataExcepetion("Invalid id set for comment");
		}
	}

	public int getPostID() {
		return postID;
	}

	public void setPostID(int postID) throws InvalidPostDataExcepetion {
		if (postID > 0) {
			this.postID = postID;
		} else {
			throw new InvalidPostDataExcepetion("Invalid post id set for comment");
		}
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) throws InvalidPostDataExcepetion {
		if (userID > 0) {
			this.userID = userID;
		} else {
			throw new InvalidPostDataExcepetion("Invalid user id set for comment");
		}
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) throws InvalidPostDataExcepetion {
		if (date == null) {
			throw new InvalidPostDataExcepetion("Date not specified for comment");
		}
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
