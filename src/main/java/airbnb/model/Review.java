package airbnb.model;

import java.time.LocalDate;

public class Review {
	private String reviewerName;
	private String reviewedName;
	private String review;
	private LocalDate date;
	private int reviewedPropertyID;
	
	public Review(String reviewerName, String reviewedName, String review, LocalDate date) {
		this.reviewerName = reviewerName;
		this.reviewedName = reviewedName;
		this.review = review;
		this.date = date;
	}

	public Review(String reviewerName, String reviewedName, String review, LocalDate date, int reviewedPropertyID) {
		this(reviewerName, reviewedName, review, date);
		this.reviewedPropertyID = reviewedPropertyID;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public String getReviewedName() {
		return reviewedName;
	}

	public String getReview() {
		return review;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public int getReviewedPropertyID() {
		return reviewedPropertyID;
	}
}
