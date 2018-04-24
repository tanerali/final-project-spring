package airbnb.model;

import java.time.LocalDate;

import airbnb.exceptions.InvalidCommentIDException;

public class Comment {

	private int commentID;
	private int userID;
	private String fullName;
	private int postID;
	private String content;
	private LocalDate date;

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

	public void setCommentID(int commentID) throws InvalidCommentIDException {
		if (commentID > 0) {
			this.commentID = commentID;
		} else {
			throw new InvalidCommentIDException();
		}
	}

	public int getPostID() {
		return postID;
	}

	public void setPostID(int postID) throws InvalidCommentIDException {
		if (postID > 0) {
			this.postID = postID;
		} else {
			throw new InvalidCommentIDException();
		}
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) throws InvalidCommentIDException {
		if (userID > 0) {
			this.userID = userID;
		} else {
			throw new InvalidCommentIDException();
		}
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
