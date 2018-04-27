package airbnb.model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.exceptions.InvalidPostDateException;
import airbnb.exceptions.InvalidPostDescriptionException;
import airbnb.exceptions.InvalidPostPriceException;
import airbnb.exceptions.InvalidPostTitleException;
import airbnb.exceptions.InvalidPostTypeException;
import airbnb.manager.PostManager;

public class Post {
	// In DB IDs (HOTEL-1)(APARTMENT-2)(HOUSE-3)(COTTAGE-4)
	public enum Type {
		HOTEL, APARTMENT, HOUSE, COTTAGE;
		public static Type getType(int DB_ID) {
			switch (DB_ID) {
			case 1:
				return Type.HOTEL;
			case 2:
				return Type.APARTMENT;
			case 3:
				return Type.HOUSE;
			case 4:
				return Type.COTTAGE;
			default:
				return null;
			}
		}

		public static Type getType(String name) {
			return Type.valueOf(name.toUpperCase());
		}
	}

	private int postID;
	private String title;
	private String description;
	private int price;
	private LocalDate dateOfPosting;
	private Type type;
	private int hostID;
	private List<Comment> comments;
	private int rating; // 0->5

	public Post(String title, String description, int price, LocalDate dateOfPosting, Type type, int hostID)
			throws InvalidPostDataExcepetion {
		this.setTitle(title);
		this.setDescription(description);
		this.setPrice(price);
		this.setDateOfPosting(dateOfPosting);
		this.setType(type);
		this.hostID = hostID;
		comments = new ArrayList<>();
	}

	public Post(int postID, String title, String description, int price, LocalDate dateOfPosting, Type type, int hostID)
			throws InvalidPostDataExcepetion {
		this(title, description, price, dateOfPosting, type, hostID);
		this.postID = postID;
	}

	public String getTitle() {
		return title;
	}

	public void setPostID(int postID) {
		this.postID = postID;
	}

	public int getPostID() {
		return postID;
	}

	public void setTitle(String title) throws InvalidPostTitleException {
		if (title.isEmpty()) {
			throw new InvalidPostTitleException();
		}
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws InvalidPostDescriptionException {
		if (description.isEmpty()) {
			throw new InvalidPostDescriptionException();
		}
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) throws InvalidPostPriceException {
		if (price <= 0) {
			throw new InvalidPostPriceException();
		}
		this.price = price;
	}

	public LocalDate getDateOfPosting() {
		return dateOfPosting;
	}

	public void setDateOfPosting(LocalDate dateOfPosting) throws InvalidPostDateException {
		this.dateOfPosting = dateOfPosting;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) throws InvalidPostTypeException {
		this.type = type;
	}

	public int getTypeLikeID() {
		switch (this.getType()) {
		case HOTEL:
			return 1;
		case APARTMENT:
			return 2;
		case HOUSE:
			return 3;
		case COTTAGE:
			return 4;
		default:
			return -1;
		}
	}

	public void setHostID(int hostID) {
		this.hostID = hostID;
	}

	public int getHostID() {
		return hostID;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void addComment(Comment m) {
		this.comments.add(m);
	}

	public int getRating() {
		return rating;
	}

	@Override
	public String toString() {
		return this.title + " " + this.description + " " + this.price + " " + this.type + " " + this.dateOfPosting;
	}

}
