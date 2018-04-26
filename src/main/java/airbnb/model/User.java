package airbnb.model;

import java.time.LocalDate;

import airbnb.exceptions.UserDataException;

public class User {

	private int userID;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String gender;
	private String city;
	private String country;
	private String photo;
	private String description;
	private LocalDate birthDate;
	private String telNumber;

	public User(String firstName, String lastName, String email, String password, String gender, String city,
			String country, String photo, String description, LocalDate birthDate, String telNumber)
			throws UserDataException {

		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setPassword(password);
		setGender(gender);
		setCity(city);
		setCountry(country);
		setPhoto(photo);
		setDescription(description);
		setBirthDate(birthDate);
		setTelNumber(telNumber);
	}

	public User(int userID, String firstName, String lastName, String email, String password, String gender,
			String city, String country, String photo, String description, LocalDate birthDate, String telNumber)
			throws UserDataException {

		this(firstName, lastName, email, password, gender, city, country, photo, description, birthDate, telNumber);
		this.userID = userID;
	}

	public int getUserID() {
		return userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String first_name) throws UserDataException {
		if (first_name.isEmpty() || !first_name.matches("[a-zA-Z]+")) {
			throw new UserDataException("Error setting first name");
		}
		this.firstName = first_name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String last_name) throws UserDataException {
		if (last_name.isEmpty() || !last_name.matches("[a-zA-Z]+")) {
			throw new UserDataException("Error setting last name");
		}
		this.lastName = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws UserDataException {
		/*
		 * ‘@’ symbol required in username and domain name A-Z characters allowed a-z
		 * characters allowed 0-9 numbers allowed Additionally email may contain only
		 * dot(.), dash(-) and underscore(_) Rest all characters are not allowed domain
		 * name must include at least one dot top-level domain to include only between
		 * 2-6 chars
		 */
		String regex = "^[A-Za-z0-9+._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

		if (email.isEmpty() || !email.matches(regex)) {
			throw new UserDataException("Error setting email");
		}
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws UserDataException {
		/*
		 * ^ # start-of-string (?=.*[0-9]) # a digit must occur at least once
		 * (?=.*[a-z]) # a lower case letter must occur at least once (?=.*[A-Z]) # an
		 * upper case letter must occur at least once (?=.*[@#$%^&+=]) # a special
		 * character must occur at least once (?=\S+$) # no whitespace allowed in the
		 * entire string .{8,} # anything, at least eight places though $ #
		 * end-of-string
		 */
		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

		if (password.isEmpty() || !password.matches(regex)) {
			throw new UserDataException("Error setting password");
		}
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) throws UserDataException {
		if (gender.isEmpty()) {
			throw new UserDataException("Error setting gender");
		}
		this.gender = gender;
	}

	public String getCity() {
		return city;
	}

	// TODO drop down menu for the given country
	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	// TODO drop down menu
	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	// TODO what happens if user changes input type
	// to text and enters random stuff
	public void setBirthDate(LocalDate birthDate) throws UserDataException {
		if (birthDate == null) {
			throw new UserDataException("Error setting birth date");
		}
		this.birthDate = birthDate;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) throws UserDataException {
		if (telNumber.isEmpty()) {
			throw new UserDataException("Error setting telephone number");
		}
		this.telNumber = telNumber;
	}
}
