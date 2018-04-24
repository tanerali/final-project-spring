<%@page import="airbnb.model.Comment"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="airbnb.model.Post"%>
<%@page import="airbnb.model.User"%>
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
					<%
						if (session.getAttribute("user") != null) {
					%>
					<!-- BOOK BUTTON -->
					<form action="book" method="post">
						From<input type="date" name="dateFrom" required="required"><br>
						To<input type="date" name="dateTo" required="required"><br>

						<input type="hidden" name="postID"
							value="<%=currPost.getPostID()%>"> <input type="submit"
							value="Request Booking"
							style="background-color: #4CAF50; border: none; color: white; padding: 15px 32px;">
					</form>

					<%
						}
					%>

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
			<div class="agileits_three_comments">
				<h3>Comments</h3>
				
				<% for (Comment comment : comments) { %>

					<div class="agileits_three_comment_grid"
						id="comment<%=comment.getCommentID()%>">
						<div class="agileits_tom">
							<a href="profile?id=<%=comment.getUserID()%>"> <img
								src="getProfilePic?id=<%=comment.getUserID()%>" class="img-responsive"></a>
							<div class=""></div>
						</div>
						<div class="agileits_tom_right">
							<div class="hardy">
								<a href="profile?id=<%=comment.getUserID()%>"><h4><%=comment.getFullName()%></h4></a>
								<p><%=comment.getDate()%></p>
							</div>
							<div class="clearfix"></div>
							<p class="lorem"><%=comment.getContent()%></p>
						</div>
						<div class="clearfix">
							
							<% if (session.getAttribute("user") != null) { %>
								<button
									onclick="deleteComment(<%=comment.getCommentID()%>, <%=currPost.getPostID()%>)"
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
					<form action="comment" method="post">
						<textarea placeholder="Comment" id="comment" required></textarea>
						<input type="hidden" id="postID" value="<%=currPost.getPostID()%>"> 
						<input  type="submit" style="background-color: #4CAF50; border: none; 
							color: white; padding: 15px 32px;">
						<!-- <button class="form-control" onclick="postComment()">SUBMIT</button> -->
					</form>
				</div>
			<% } %>

			<!-- //leave-comments -->


		</div>
	</div>

	<script type="text/javascript">

			function postComment() {
				var req = new XMLHttpRequest();
				
				req.open("Post", "comment");
				/* req.setRequestHeader("Content-Type", "application/json"); */
				
				/* req.send(JSON.stringify(
						{
							"postID": parseInt(document.getElementById("postID").value),
							"content": document.getElementById("comment").value
						}
						)); */
						
				req.send("comment="+ document.getElementById("comment").value+ 
						"&postID="+ parseInt(document.getElementById("postID").value));
				
				req.onreadystatechange = function() {
					if (req.readyState == 4 && req.status == 200) {
						
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
</body>
</html>