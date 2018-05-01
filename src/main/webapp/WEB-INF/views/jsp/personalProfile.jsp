<%@page import="airbnb.model.Review"%>
<%@page import="java.util.ArrayList"%>
<%@page import="airbnb.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Airbnb</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<script type="application/x-javascript">
	
		
	 addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } 


</script>

<!-- bootstrap-css -->
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
<script type="text/javascript">
	jQuery(document).ready(function($) {
		$(".scroll").click(function(event) {
			event.preventDefault();
			$('html,body').animate({
				scrollTop : $(this.hash).offset().top
			}, 1000);
		});
	});
</script>
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript">
	function editUser() {
		document.getElementById("user").style.display = "none";
		document.getElementById("editUser").style.display = "block";
	}
	function cancelEdit() {
		document.getElementById("user").style.display = "block";
		document.getElementById("editUser").style.display = "none";
	}
	function saveEdit() {

	}
</script>

</head>


<body>


	<%@ include file="header.jsp"%>

	<div class="container">
		<div class="row">

			<%
				User user = (User) session.getAttribute("user");
			%>

			<div class="col-sm-3">
				<h1>User details</h1>
				<div style="width: 240px; height: 240px">
					<img class="img-responsive" alt=""
						src="getProfilePic?id=<%=user.getUserID()%>">
				</div>

				<h2>Listings</h2>
				<div class="list-group">
					<c:forEach var="listing" items="${ hostedPosts }">
						<a href="post?id=${ listing.postID }" class="list-group-item">
							<img class="img-responsive"
							src="getThumbnail?id=${ listing.postID }">
							<p>${ listing.title }</p>
						</a>
					</c:forEach>
				</div>
			</div>

			<div class="col-sm-9">
				<div id="user" style="display: block;">

					<h1 class="page-header"><%=user.getFirstName() + " " + user.getLastName()%></h1>
					<div class="panel panel-default">
						<div class="panel-heading">Verified info</div>
						<div class="panel-body">
							<table class="table table-hover">
								<tr>
									<td>Email</td>
									<td><%=user.getEmail()%></td>
								</tr>
								<tr>
									<td>Gender</td>
									<td><%=user.getGender()%></td>
								</tr>
								<tr>
									<td>Country</td>
									<td><%=user.getCountry()%></td>
								</tr>
								<tr>
									<td>City</td>
									<td><%=user.getCity()%></td>
								</tr>
								<tr>
									<td>Description</td>
									<td><%=user.getDescription()%></td>
								</tr>
								<tr>
									<td>Birth Date</td>
									<td><%=user.getBirthDate()%></td>
								</tr>
								<tr>
									<td>Telephone Number</td>
									<td><%=user.getTelNumber()%></td>
								</tr>
							</table>
							<button
								style="float: right; background-color: #4CAF50; border: none; color: white; padding: 15px 32px;"
								onclick="editUser()">Edit</button>
						</div>
					</div>
				</div>

				<%
				Exception e = null;
				if (request.getAttribute("exception") != null) {
					e = (Exception) request.getAttribute("exception"); %>
					<p style="color: red"><%=e.getMessage()%></p>
				<% } %>

				<div id="editUser" style="display: none;">

					<div class="panel panel-default">
						<div class="panel-heading">Edit user details</div>
						<div class="panel-body">
							<form action="personalProfile" method="post">
								<table class="table table-hover">
									<tr>
										<td>First Name</td>
										<td><input type="text" name="firstName"
											value="<%=user.getFirstName()%>"></td>
									</tr>
									<tr>
										<td>Last Name</td>
										<td><input type="text" name="lastName"
											value="<%=user.getLastName()%>"></td>
									</tr>
									<tr>
										<td>Email</td>
										<td><input type="email" name="email"
											value="<%=user.getEmail()%>"></td>
									</tr>
									<tr>
										<td>Gender</td>
										<td><input type="text" name="gender"
											value="<%=user.getGender()%>"></td>
									</tr>
									<tr>
										<td>Country</td>
										<td><input type="text" name="country"
											value="<%=user.getCountry()%>"></td>
									</tr>
									<tr>
										<td>City</td>
										<td><input type="text" name="city"
											value="<%=user.getCity()%>"></td>
									</tr>
									<tr>
										<td>Description</td>
										<td><input type="text" name="description"
											value="<%=user.getDescription()%>"></td>
									</tr>
									<tr>
										<td>Birth Date</td>
										<td><input type="date" name="birthDate"
											value="<%=user.getBirthDate()%>"></td>
									</tr>
									<tr>
										<td>Telephone Number</td>
										<td><input type="tel" name="telNumber"
											value="<%=user.getTelNumber()%>"></td>
									</tr>
								</table>
								<input
									style="float: left; background-color: #4CAF50; border: none; color: white; padding: 15px 32px;"
									type="submit" value="Save">
							</form>
							<button
								style="float: right; background-color: red; border: none; color: white; padding: 15px 32px;"
								onclick="cancelEdit()">Cancel</button>
						</div>
					</div>

					<!-- <button style=
						"float: right; background-color: red; border: none; color: white; padding: 15px 32px;"
						onclick="saveEdit()"
					>Save</button> -->

				</div>

				<h1 class="page-header">Reviews from Hosts</h1>
				
				<c:forEach var="reviewFromHost" items="${reviewsFromHosts}">
					<div class="panel panel-default">
						<div class="panel-heading">${reviewFromHost.reviewerName}
							<span style="float: right">${reviewFromHost.date }</span>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-md-6">
									<div class="panel-body">
										${reviewFromHost.review}
									</div>
								</div>
								<div class="col-md-6"></div>
							</div>
						</div>
					</div>
				</c:forEach>

				<h1 class="page-header">Reviews from Guests of
					<%=user.getFirstName() + " " + user.getLastName()%></h1>
								
				<c:forEach var="reviewFromGuest" items="${reviewsFromGuests}">
					<div class="panel panel-default">
						<div class="panel-heading">${reviewFromGuest.reviewerName }
							<span style="float: right">${reviewFromGuest.date }</span>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="panel-body">
									${reviewFromGuest.review }
								</div>
							</div>
							<div class="col-md-6">
								<div class="panel-body">
									Reviewed Property: <i>${reviewFromGuest.reviewedName }</i> <a
										href="post?id=${reviewFromGuest.reviewedPropertyID }"> <img
										class="img-responsive" style="width: 400px"
										src="getThumbnail?id=${reviewFromGuest.reviewedPropertyID }">
									</a>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
				
			</div>
		</div>
	</div>
	<%@ include file="footer.jsp"%>
</body>
</html>