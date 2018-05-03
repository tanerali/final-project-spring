<%@page import="airbnb.model.Comment"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="airbnb.model.Post"%>
<%@page import="airbnb.model.User"%>
<%@page import="java.time.LocalDate"%>
<!DOCTYPE html>
<html>
<head>
<%
	Post currPost = (Post) request.getAttribute("post");
	User postUser = (User) request.getAttribute("user");
	User currUser = (User) session.getAttribute("user");
	boolean myPost = false;
	if (currUser != null) {
		myPost = (currPost.getHostID() == currUser.getUserID()) ? true : false;
	}
%>
<title>${post.title}</title>
<link href="css/bootstrap.css" rel="stylesheet" type="text/css"
	media="all" />
<!--// bootstrap-css -->
<!-- css -->
<link rel="stylesheet" href="css/style.css" type="text/css" media="all" />
<!--// css -->
<!-- font-awesome icons -->
<link href="css/font-awesome.css" rel="stylesheet">
<!-- //font-awesome icons -->
<!-- font -->
<link
	href="//fonts.googleapis.com/css?family=Crimson+Text:400,400i,600,600i,700,700i"
	rel="stylesheet">
<link
	href="//fonts.googleapis.com/css?family=Raleway:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i"
	rel="stylesheet">
<link
	href='//fonts.googleapis.com/css?family=Roboto+Condensed:400,700italic,700,400italic,300italic,300'
	rel='stylesheet' type='text/css'>

<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<!-- //font -->
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.js"></script>

<!-- Include Date Picker -->

<link rel="stylesheet" type="text/css"
	href="css/bootstrap-datepicker.standalone.min.css">

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.js"></script>

</head>
<body>

	<%@ include file="header.jsp"%>

	<!-- single -->
	<div class="single">
		<div class="container">
			<div class="agileits-single-img">

				<!-- POST'S PICTURES -->
				<div class="container">
					<div class="row">
						<div class="col-md-12">
							<div id="myCarousel" class="carousel slide" data-ride="carousel">
								<div class="carousel-inner">
									<div class="item carousel-item active">
										<img src=<c:url value="getThumbnail?id=${post.postID}"/>
											alt="">
									</div>
									<c:forEach items="${photos}" var="item">
										<div class="item carousel-item">
											<img src="getPhoto?path=${item} " alt="">
										</div>
									</c:forEach>
								</div>
								<a class="carousel-control left carousel-control-prev"
									href="#myCarousel" data-slide="prev"> <i
									class="fa fa-angle-left"></i>
								</a> <a class="carousel-control right carousel-control-next"
									href="#myCarousel" data-slide="next"> <i
									class="fa fa-angle-right"></i>
								</a>
							</div>
						</div>
					</div>
				</div>
				<!-- POST'S PICTURES -->
				<h4>${ post.title}</h4>
				<div class="agileinfo-single-icons">
					<ul>
						<li><a href="profile?id=${user.userID }"><i
								class="fa fa-user" aria-hidden="true"></i> <span>Host:
									${user.firstName} ${user.lastName}</span></a></li>
						<li><i class="fa fa-calendar" aria-hidden="true"></i><span>Date
								of posting: ${post.dateOfPosting }</span></li>
						<li><i class="fa fa-heart" aria-hidden="true"></i><span id="postRating">${rating}/5
								Rating</span></li>
					</ul>

					<c:if test="${ error != null }">
						<h4 style="color: red">${ error }</h4>
					</c:if>
					
					<!-- BOOK IF NOT LOGGED IN AND MY POST -->					
					<% if (session.getAttribute("user") != null && !myPost) { %>
						<form action="book" method="post">
							<div class="input-group input-daterange">
								<input id="startDate1" name="dateFrom" type="text" class="form-control" readonly="readonly"> 
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-calendar"></span>
									</span> 
									<span class="input-group-addon">to</span> 
									<input id="endDate1" name="dateTo" type="text" class="form-control" readonly="readonly"> 
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
							</div>
							<input type="hidden" name="postID" value="${post.postID }">
							<!-- BOOK BUTTON -->
							<input type="submit" value="Request Booking"
								style="background-color: #4CAF50; border: none; color: white; padding: 15px 32px;">
						</form>
						
						<div class="input-group" id="ratingStars">
							<h1>Rate property by clicking on star</h1>
							<button onclick="rate(1, ${post.postID})" class="btn"> <span id="rate1" class="fa fa-star checked"></span> </button>
							<button onclick="rate(2, ${post.postID})" class="btn"> <span id="rate2" class="fa fa-star checked"></span> </button>
							<button onclick="rate(3, ${post.postID})" class="btn"> <span id="rate3" class="fa fa-star checked"></span> </button>
							<button onclick="rate(4, ${post.postID})" class="btn"> <span id="rate4" class="fa fa-star checked"></span> </button>
							<button onclick="rate(5, ${post.postID})" class="btn"> <span id="rate5" class="fa fa-star checked"></span> </button>
						</div>
					<% } %>
					
					<% if (myPost) { %>
					<br>
					<a href="edit?id=${post.postID }"
						style="background-color: #4CAF50; border: none; color: white;">EDIT POST
					</a> 
					<a href="delete?id=${post.postID }"><i class="fa fa-trash"></i></a>
					<% } %>

				</div>
				<h3>Description</h3>
				<p>${post.description }</p>
				<p>
					Price: <b>${post.price }</b>
				</p>
			</div>

			<%
				ArrayList<Comment> comments = new ArrayList();
				if (request.getAttribute("comments") != null) {
					comments = (ArrayList<Comment>) request.getAttribute("comments");
				}
			%>

			<!-- comments -->
			<div class="agileits_three_comments" id="comments">
				<h3>Comments</h3>

				<%
					for (Comment comment : comments) {
						int commentID = comment.getCommentID();
						int userID = comment.getUserID();
						String fullName = comment.getFullName();
						LocalDate date = comment.getDate();
						String content = comment.getContent();
						int postID = comment.getPostID();
				%>

				<div class="agileits_three_comment_grid" id="comment<%=commentID%>">
					<div class="agileits_tom" id="profilePic">
						<a href="profile?id=<%=userID%>"> <img
							src="getProfilePic?id=<%=userID%>"
							class="img-responsive img-circle"></a>
					</div>
					<div class="agileits_tom_right">
						<div class="hardy" id="commenterAndDate">
							<a href="profile?id=<%=userID%>"><h4><%=fullName%></h4></a>
							<p><%=date%></p>
						</div>
						<div class="clearfix"></div>
						<p class="lorem"><%=content%></p>
					</div>
					<div class="clearfix">
						<%
							if (session.getAttribute("user") != null
										&& ((User) session.getAttribute("user")).getUserID() == userID) {
						%>
						<button
							style="float: right; background-color: #4CAF50; border: none; color: white; padding: 15px 32px;"
							onclick="deleteComment(<%=commentID%>, <%=postID%>)">DELETE
							COMMENT</button>
						<%
							}
						%>
					</div>
				</div>
				<%
					}
				%>
			</div>
			<!-- //comments -->


			<!-- leave-comments -->
			<%
				if (session.getAttribute("user") != null) {
			%>
			<div class="w3_leave_comment">
				<h3>Leave your comment</h3>

				<!-- AJAX -->
				<textarea placeholder="Comment" id="comment" required></textarea>
				<input type="hidden" id="postID" value="${post.postID }">

				<button class="form-control" id="leaveComment"
					onclick="postComment()">SUBMIT</button>
			</div>
			<%
				}
			%>
			<!-- //leave-comments -->
		</div>
	</div>

	<template>
		<div class="agileits_three_comment_grid" id="comment">
			<div class="agileits_tom" id="profilePic">
				<a href="profile?id="> <img src="getProfilePic?id="
					class="img-responsive img-circle"></a>
			</div>
			<div class="agileits_tom_right">
				<div class="hardy" id="commenterAndDate">
					<a href="profile?id="><h4></h4></a>
					<p></p>
				</div>
				<div class="clearfix"></div>
				<p class="lorem"></p>
			</div>
			<div class="clearfix">
				<button
					style="float: right; background-color: #4CAF50; border: none; color: white; padding: 15px 32px;"
					onclick="deleteComment()">DELETE COMMENT</button>
			</div>
		</div>
	</template>

	<%@ include file="footer.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			var disabledDates = {};
			disabledDates = ${unavailableDatesString};
			
			$('.input-daterange').datepicker({
				format: 'yyyy-mm-dd',
				datesDisabled: disabledDates,
				startDate: new Date()
			});
		});
	</script>

	<script type="text/javascript">
		 function postComment() {
			var req = new XMLHttpRequest();
			
			req.open("Post", "comment");
			req.setRequestHeader("Content-Type", "application/json");
			
			req.send(JSON.stringify(
					{
						"postID": parseInt(document.getElementById("postID").value),
						"content": document.getElementById("comment").value
					}
					));
			
			req.onreadystatechange = function() {
				if (req.readyState == 4 && req.status == 200) {
					/* get JSON response as object */ 
					var responseJSON = JSON.parse(req.responseText);

					var commentID = responseJSON.commentID;
					var content = responseJSON.content;
					var date = responseJSON.date;
					var fullName = responseJSON.fullName;
					var postID = responseJSON.postID;
					var userID = responseJSON.userID;
					
					
					var template = document.getElementsByTagName("template")[0];
			        var clone = template.content.cloneNode(true);
					var newComment = clone.getElementById("comment");
					
			        /* copy comment template */
					/* var commentLayout = 
						document.getElementsByClassName('agileits_three_comment_grid')[0]; */
					
						/* create a new div element and copy the comment
					template into it */
					
					/* var newComment = document.createElement("div");
					newComment.innerHTML = commentLayout.innerHTML; */
					
					newComment.id = "comment"+ commentID;
					newComment.childNodes[1].childNodes[1].href = "profile?id="+userID;
					newComment.childNodes[1].childNodes[1].childNodes[1].src = "getProfilePic?id="+ userID;
					
					var firstElement = newComment.childNodes[1];
					var secondElement = firstElement.nextSibling.nextSibling;
					var thirdElement = secondElement.nextSibling.nextSibling;
					
					secondElement.childNodes[1].childNodes[1].href = "profile?id="+ userID;
					secondElement.childNodes[1].childNodes[1].firstChild.innerHTML = fullName;
					secondElement.childNodes[1].childNodes[3].innerHTML = date.year+ "-"+ date.monthValue+ "-"+ date.dayOfMonth;
					secondElement.childNodes[5].innerHTML = content;
					thirdElement.childNodes[1].onclick = function() {deleteComment(commentID, postID)};
					
					/* attach new comment to comments section */
					var comments = document.getElementById("comments");
					comments.appendChild(newComment);
				}
			}
		}
		 
		 function deleteComment(commentID, postID) {
				var req = new XMLHttpRequest();
				req.open("Delete", "comment/"+ commentID);
				req.send();
				
				req.onreadystatechange = function() {
					if (req.readyState == 4 && req.status == 200) {
						var element = document.getElementById("comment"+ commentID);
						element.parentNode.removeChild(element);
					}
				}
			}
	 </script>

	<script type="text/javascript">
		function rate(rating, postID){
			var req = new XMLHttpRequest();
			
			req.open("Post", "rate");
			/* req.setRequestHeader("Content-Type", "application/json"); */
			
			var formData = new FormData();
			formData.append("rating", rating);
			formData.append("postID", postID);
			
			req.send(formData);
			
			req.onreadystatechange = function() {
				if (req.readyState == 4 && req.status == 200) {
					var stars = document.getElementsByClassName("fa-star");
					var ratedStar = document.getElementById("rate"+rating); 
					ratedStar.style = "color:green";
					for (star of stars) {
						if(star.id == ("rate"+rating)) continue;
						star.style = "";
					}
					getRating(postID);
				}
			}
		}
		
		function getRating(postID) {
			var req = new XMLHttpRequest();
			req.open("Get", "getRating?id="+ postID);
			req.send();
			
			req.onreadystatechange = function() {
				if (req.readyState == 4 && req.status == 200) {
					req.responseText;
					console.log();
					document.getElementById("postRating").innerHTML = req.responseText+ "/5 Rating";
				}
			} 
		}
				
	</script>
</body>
</html>