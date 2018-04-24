package airbnb.model;

import java.time.LocalDate;

public class Review {
	private String reviewerName;
	private String reviewedName;
	private String review;
	private LocalDate date;
	
	public Review(String reviewerName, String reviewedName, String review, LocalDate date) {
		this.reviewerName = reviewerName;
		this.reviewedName = reviewedName;
		this.review = review;
		this.date = date;
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
	
	
}
