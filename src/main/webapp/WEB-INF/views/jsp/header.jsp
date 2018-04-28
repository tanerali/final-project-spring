<%@page import="airbnb.model.Notification"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="header">
	<div class="container">
		<div class="header-left">
			<div class="w3layouts-logo">
				<h1>
					<a href="index"><span>re </span> AIRBNB</a>
				</h1>
			</div>
		</div>
		<div class="header-right">
			<div class="w3-header-bottom">
				<div class="top-nav">
					<nav class="navbar navbar-default">
						<div class="navbar-header">
							<button type="button" class="navbar-toggle collapsed"
								data-toggle="collapse"
								data-target="#bs-example-navbar-collapse-1">

								<span class="sr-only">Toggle navigation</span> <span
									class="icon-bar"></span> <span class="icon-bar"></span> <span
									class="icon-bar"></span>
							</button>
						</div>
						<!-- Collect the nav links, forms, and other content for toggling -->
						<div class="collapse navbar-collapse"
							id="bs-example-navbar-collapse-1">
							<ul class="nav navbar-nav">
								<li>
									<form action="search">
										<div id="myOverlay" class="overlay">
											<span class="closebtn" onclick="closeSearch()"
												title="Close Overlay">X</span>
											<div class="overlay-content">
												<input type="text" id="search" placeholder="Search.."
													name="search">
												<button type="submit" id="searchButton" onclick="search()">Search</button>
											</div>
										</div>
									</form>
								</li>

								<li><a href="#" id="openBtn" class="active" onclick="openSearch()">Search</a></li>
								<li><a href="explore" onclick="explore(this);">Explore</a></li>
								<%
									if (session.getAttribute("user") == null) {
								%>
								<li><a href="login">Login</a></li>
								<li><a href="register">Register</a></li>
								<%
									} else {
								%>
								<li><a href="host">Host</a></li>
								
								<script type="text/javascript">
									function rejectBookingRequest(notificationID) {
										var req = new XMLHttpRequest();
										req.open("Delete", "notification/"+ notificationID);
										req.send();
										
										req.onreadystatechange = function() {
											if (req.readyState == 4 && req.status == 200) {
												var element = document.getElementById("notification"+ notificationID);
												element.parentNode.removeChild(element);
											}
										}
									}
									
									function acceptBookingRequest(notificationID) {
										var req = new XMLHttpRequest();
										req.open("Post", "notification/"+ notificationID);
										req.send();
										
										req.onreadystatechange = function() {
											if (req.readyState == 4 && req.status == 200) {
												var element = document.getElementById("notification"+ notificationID);
												element.parentNode.removeChild(element);
											}
										}
									}
								</script>
								
								<li class=""><a href="#" class="dropdown-toggle hvr-bounce-to-bottom" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Notifications<span class="caret"></span></a>
									<ul class="dropdown-menu" style="width:270px;">
										<c:forEach var="notification" items="${bookingRequests}">
											<li id="notification${notification.notificationID }">
												<a href="post?id=${ notification.postID }">
													<img class="img-responsive" src="getThumbnail?id=${ notification.postID }">
														<h4>Booking request for <em>${ notification.title }</em></h4>
												</a>
												<p>
													Customer name: ${ notification.fullName }<br>
													Profile: <a href="profile?id=${ notification.customerID }">${ notification.email }</a>
												</p>
												<button
													onclick="rejectBookingRequest(${notification.notificationID})"
													style="float: left; margin-bottom: 5px; background-color: red; 
													border: none; color: white; padding: 15px 32px;">REJECT
												</button>
												<button
													onclick="acceptBookingRequest(${notification.notificationID})"
													style="float: right; margin-bottom: 5px; background-color: #4CAF50; 
													border: none; color: white; padding: 15px 32px;">ACCEPT
												</button>
											</li>
														
										</c:forEach>
									
									</ul>
								</li>
								<li><a href="personalProfile">Profile</a></li>
								<li><a href="logout">Logout</a></li>
								<%
									}
								%>
							</ul>
							<div class="clearfix"></div>
						</div>
					</nav>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
</div>