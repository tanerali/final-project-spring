<%@page import="model.Review"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.User"%>
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

	
	<%@ include file="header.jsp" %>

	<div class="container">
		<div class="row">
		
			<% User user = (User)session.getAttribute("user"); %>
		
			<div class="col-sm-3">
				<h1>User details</h1>
				<div style="width: 240px; height: 240px">
					<img class="img-responsive" alt="" src="getPic?id=<%= user.getUserID() %>">
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">Verified info</div>
					<div class="panel-body">
						<ul>
							<li>Email</li>
						</ul>
					</div>
				</div>
				<h2>Listings</h2>
				<ul>
					<li><img class="img-resposive" src="images/1.jpg" style="width: 320px; height: 100%"></li>
				</ul>
			</div>
			
			
			
			<div class="col-sm-9">
				
				<div id="user" style="display: block;">
					<h1><%=user.getFirstName()+ " "+ user.getLastName()%></h1>
					<button style=
						"float: right; background-color: #4CAF50; border: none; color: white; padding: 15px 32px;"
						onclick="editUser()"
					>Edit</button>
					<ul>
						<li><%=user.getEmail()%></li>
						<li><%=user.getGender()%></li>
						<li><%=user.getCountry()%></li>
						<li><%=user.getCity()%></li>
						<li><%=user.getDescription()%></li>
						<li><%=user.getBirthDate()%></li>
						<li><%=user.getTelNumber()%></li>
					</ul>
				</div>
				
				<%
				Exception e = null; 
				if(request.getAttribute("exception") != null) {
					e = (Exception)request.getAttribute("exception"); %>
					<p style="color: red"><%= e.getMessage() %></p>
				<% } %>
				
				
				<div id="editUser" style="display: none;">
					<form action="profile" method="post">
						First Name<input type="text" name="firstName" value="<%=user.getFirstName()%>"><br>
						Last Name<input type="text" name="lastName" value="<%=user.getLastName()%>"><br>
						Email<input type="email" name="email" value="<%=user.getEmail()%>"><br>
						Gender<input type="text" name="gender" value="<%=user.getGender()%>"><br>
						Country<input type="text" name="country" value="<%=user.getCountry()%>"><br>
						City<input type="text" name="city" value="<%=user.getCity()%>"><br>
						Description<input type="text" name="description" value="<%=user.getDescription()%>"><br>
						Birth Date<input type="date" name="birthDate" value="<%=user.getBirthDate()%>"><br>
						Telephone Number<input type="tel" name="telNumber" value="<%=user.getTelNumber()%>"><br>
						<input type="submit" value="Save">
					</form>
					<button style=
						"float: right; background-color: red; border: none; color: white; padding: 15px 32px;"
						onclick="cancelEdit()"
					>Cancel</button>
					
					<!-- <button style=
						"float: right; background-color: red; border: none; color: white; padding: 15px 32px;"
						onclick="saveEdit()"
					>Save</button> -->
					
				</div>
				
				<h1>Reviews from Hosts</h1>
				
				<%
									if(session.getAttribute("reviewsFromHosts") != null) {
																		ArrayList<Review> reviews = ((ArrayList<Review>)session.getAttribute("reviewsFromHosts")); 
																		for(Review review: reviews) {
								%>
						
						<div class="panel panel-default">
							<div class="panel-heading"><%=review.getReviewerName()%></div>
							<div class="panel-body">
								<%=review.getReview()%>
							</div>
							<div class="panel-body">
								<%=review.getDate()%>
							</div>
						</div>
						
					<%
												}
											%>
				 <%
				 	}
				 %>
					
				<h1>Reviews from Guests of <%=user.getFirstName()+ " "+ user.getLastName()%></h1>
				
				<% 	if(session.getAttribute("reviewsFromGuests") != null) {
						ArrayList<Review> reviews = ((ArrayList<Review>)session.getAttribute("reviewsFromGuests")); 
						for(Review review: reviews) { %>
						
						<div class="panel panel-default">
							<div class="panel-heading"><%= review.getReviewerName() %></div>
							<div class="panel-body">
								<%= review.getReview() %>
							</div>
							<div class="panel-body" style="font-style: italic;">
								
								Reviewed Property: <%= review.getReviewedName() %>
							</div>
							<div class="panel-body">
								<%= review.getDate() %>
							</div>
						</div>
						
					<% } %>
				 <% } %>
				
				
			</div>
		</div>
		
	</div>


</body>
</html>