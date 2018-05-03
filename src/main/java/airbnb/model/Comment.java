package airbnb.model;

import java.time.LocalDate;

import airbnb.exceptions.InvalidPostDataExcepetion;

public class Comment {

	private int commentID;
	private int userID;
	private String fullName;
	private int postID;
	private String content;
	private LocalDate date;
	
	public Comment() {
	}
	
	public Comment(int userID, String fullName, int postID, String content, LocalDate date) {
		this.userID = userID;
		this.fullName = fullName;
		this.postID = postID;
		this.content = content;
		this.date = date;
	}

	public Comment(int commentID, int userID, String fullName, int postID, String content, LocalDate date) {
		this(userID, fullName, postID, content, date);
		this.commentID = commentID;
	}

	public String getFullName() {
		return fullName;
	}

	public int getCommentID() {
		return commentID;
	}

	public void setCommentID(int commentID) throws InvalidPostDataExcepetion {
		if (commentID > 0) {
			this.commentID = commentID;
		} else {
			throw new InvalidPostDataExcepetion("");
		}
	}

	public int getPostID() {
		return postID;
	}

	public void setPostID(int postID) throws InvalidPostDataExcepetion {
		if (postID > 0) {
			this.postID = postID;
		} else {
			throw new InvalidPostDataExcepetion("");
		}
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) throws InvalidPostDataExcepetion {
		if (userID > 0) {
			this.userID = userID;
		} else {
			throw new InvalidPostDataExcepetion("");
		}
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
