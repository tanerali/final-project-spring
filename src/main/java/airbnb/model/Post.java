package airbnb.model;

import java.time.LocalDate;

import airbnb.exceptions.InvalidPostDataExcepetion;

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
	private int rating; // 0->5
	private String country;
	private String city;

	public Post() {

	}

	public Post(
			String title, 
			String description, 
			int price, 
			LocalDate dateOfPosting, 
			Type type, 
			int hostID) throws InvalidPostDataExcepetion {
		this.setTitle(title);
		this.setDescription(description);
		this.setPrice(price);
		this.setDateOfPosting(dateOfPosting);
		this.setType(type);
		this.setHostID(hostID);
	}

	public Post(
			String title, 
			String description, 
			int price, 
			LocalDate dateOfPosting, 
			Type type, 
			int hostID,
			String country, 
			String city) throws InvalidPostDataExcepetion {
		
		this.setTitle(title);
		this.setDescription(description);
		this.setPrice(price);
		this.setDateOfPosting(dateOfPosting);
		this.setType(type);
		this.setHostID(hostID);
		setCountry(country);
		setCity(city);
	}

	public Post(
			int postID, 
			String title, 
			String description, 
			int price, 
			LocalDate dateOfPosting, 
			Type type, 
			int hostID,
			String country, 
			String city) throws InvalidPostDataExcepetion {
		this(title, description, price, dateOfPosting, type, hostID, country, city);
		this.postID = postID;
	}

	public Post(
			int postID, 
			String title, 
			String description, 
			int price, 
			LocalDate dateOfPosting, 
			Type type, 
			int hostID) throws InvalidPostDataExcepetion {
		this(title, description, price, dateOfPosting, type, hostID);
		this.postID = postID;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
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

	public void setTitle(String title) throws InvalidPostDataExcepetion {
		if (title.isEmpty()) {
			throw new InvalidPostDataExcepetion("Title empty");
		}
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws InvalidPostDataExcepetion {
		if (description.isEmpty()) {
			throw new InvalidPostDataExcepetion("Description empty");
		}
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) throws InvalidPostDataExcepetion {
		if (price <= 0) {
			throw new InvalidPostDataExcepetion("Cannot enter negative numbers in price field");
		}
		this.price = price;
	}

	public LocalDate getDateOfPosting() {
		return dateOfPosting;
	}

	public void setDateOfPosting(LocalDate dateOfPosting) throws InvalidPostDataExcepetion {
		this.dateOfPosting = dateOfPosting;
	}

	public int getTypeLikeID() {
		switch (this.type) {
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

	public int getRating() {
		return rating;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return this.title + 
				" " + this.description + 
				" " + this.price + 
				" " + this.type + 
				" " + this.dateOfPosting;
	}
}
