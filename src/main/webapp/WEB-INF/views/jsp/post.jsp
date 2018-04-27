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
%>
<title><%=currPost.getTitle()%></title>
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
<!-- //font -->
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.js"></script>
 
<!-- Include Date Range Picker -->

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.pt.min.js"></script>

<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/css/bootstrap-datepicker3.css">
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/js/bootstrap-datepicker.js"></script>

</head>
<body>

	<%@ include file="header.jsp"%>

	<!-- single -->
	<div class="single">
		<div class="container">
			<div class="agileits-single-img">

				<!-- POST'S PICTURE -->
				<img id="placeImage"
					src="getThumbnail?id=<%=currPost.getPostID()%>">
				<h4><%=currPost.getTitle()%></h4>
				<div class="agileinfo-single-icons">
					<ul>
						<li><a href="profile?id=<%=postUser.getUserID()%>"><i
								class="fa fa-user" aria-hidden="true"></i> <span>Host: <%=postUser.getFirstName()%>
									<%=postUser.getLastName()%></span></a></li>
						<li><i class="fa fa-calendar" aria-hidden="true"></i><span>Date
								of posting: <%=currPost.getDateOfPosting().toString()%></span></li>
						<li><a href="#"><i class="fa fa-heart" aria-hidden="true"></i><span><%=currPost.getRating()%>/10
									Rating</span></a></li>
					</ul>

					<!-- BOOK -->
					<% if (session.getAttribute("user") != null) { %>
					
					<!-- BOOK BUTTON -->
					<form action="book" method="post">
						From<input type="date" name="dateFrom" required="required"><br>
						To<input type="date" name="dateTo" required="required"><br>
						
						<input type="hidden" name="postID" value="<%=currPost.getPostID()%>"> 
						<input type="submit" value="Request Booking" style="background-color: #4CAF50; 
								border: none; color: white; padding: 15px 32px;">
					</form>

					<% } %>

				</div>
				<h3>Description</h3>
				<p><%=currPost.getDescription()%></p>
				<p>
					Price: <b><%=currPost.getPrice()%></b>
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
				
				<% for (Comment comment : comments) { 
					int commentID = comment.getCommentID();
					int userID = comment.getUserID();
					String fullName = comment.getFullName();
					LocalDate date = comment.getDate();
					String content = comment.getContent();
					int postID = comment.getPostID();
				%>

				<div class="agileits_three_comment_grid" id="comment<%= commentID %>">
					<div class="agileits_tom" id="profilePic">
						<a href="profile?id=<%= userID  %>"> <img
							src="getProfilePic?id=<%= userID %>" class="img-responsive"></a>
					</div>
					<div class="agileits_tom_right">
						<div class="hardy" id="commenterAndDate">
							<a href="profile?id=<%= userID %>"><h4><%= fullName %></h4></a>
							<p><%= date %></p>
						</div>
						<div class="clearfix"></div>
						<p class="lorem"><%= content %></p>
					</div>
					<div class="clearfix">
						
						<% if (session.getAttribute("user") != null) { %>
							<button
								onclick="deleteComment(<%= commentID %>, <%= postID %>)"
								style="float: right; background-color: #4CAF50; border: none; color: white; padding: 15px 32px;">DELETE
								COMMENT</button>
						<% } %>
					</div>
				</div>

				<% } %>
			</div>
			<!-- //comments -->


			<!-- leave-comments -->
			<% if (session.getAttribute("user") != null) { %>
				<div class="w3_leave_comment">
					<h3>Leave your comment</h3>
					<%-- <form action="comment" method="post">
						<textarea placeholder="Comment" name="comment" required></textarea>
						<input type="hidden" name="postID" value="<%=currPost.getPostID()%>"> 
						<input  type="submit" style="background-color: #4CAF50; border: none; 
							color: white; padding: 15px 32px;">
						<!-- <button class="form-control" onclick="postComment()">SUBMIT</button> -->
					</form> --%>
					
					<!-- AJAX -->
					
						<textarea placeholder="Comment" id="comment" required></textarea>
						<input type="hidden" id="postID" value="<%=currPost.getPostID()%>"> 
						
						<button class="form-control" id="leaveComment" onclick="postComment()">SUBMIT</button>
				</div>
			<% } %>
			<!-- //leave-comments -->
		</div>
	</div>

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
						
						/* copy comment template */
						var commentLayout = 
							document.getElementsByClassName('agileits_three_comment_grid')[0];
						/* create a new div element and copy the comment
						template into it */
						var newComment = document.createElement("div");
						newComment.innerHTML = commentLayout.innerHTML;
						
						newComment.id = "comment"+ commentID;
						newComment.childNodes[1].childNodes[1].href = "profile?id="+userID;
						newComment.childNodes[1].childNodes[1].childNodes[1].src = "getProfilePic?id="+ userID;
						
						var firstElement = newComment.childNodes[1];
						var secondElement = firstElement.nextSibling.nextSibling;
						var thirdElement = secondElement.nextSibling.nextSibling;
						
						secondElement.childNodes[1].childNodes[1].href = "profile?id"+ userID;
						secondElement.childNodes[1].childNodes[1].firstChild.innerHTML = fullName;
						secondElement.childNodes[1].childNodes[3].innerHTML = date;
						secondElement.childNodes[5].innerHTML = content;
						thirdElement.childNodes[1].onclick = function() {deleteComment(commentID, postID)};
						
						/* attach new comment to comments section */
						var comments = document.getElementById("comments");
						comments.appendChild(newComment);
					}
				}
			}
	 </script>

	<script type="text/javascript">
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
</body>
</html>