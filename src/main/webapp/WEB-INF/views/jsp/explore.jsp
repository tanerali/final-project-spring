<%@page import="java.util.Map"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="airbnb.model.Post"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Airbnb</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<!-- bootstrap-css -->
<link href="css/bootstrap.css" rel="stylesheet" type="text/css"
	media="all" />
<!--// bootstrap-css -->

<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
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

<style>
.filterDiv {
	display: none;
}

.show {
	display: block;
}
</style>
<body>


	<%@ include file="header.jsp"%>

	<div class="container">
		<h2>Explore All The Great Places You Can Stay</h2>

		<div class="form-group" id="types" style="display: inline">
			<label for="typeSelector" style="display: inline">Select
				type:</label> <select class="form-control" id="typeSelector">
				<option>Show all</option>
				<option>HOUSE</option>
				<option>APARTMENT</option>
				<option>HOTEL</option>
				<option>COTTAGE</option>
			</select>
		</div>

		<div class="form-group" id="countries" style="display: inline">
			<label for="countrySelector" style="display: inline">Select
				country:</label> <select class="form-control" id="countrySelector">
				<option>Show all</option>
			</select>
		</div>

		<div class="form-group" id="cities" style="display: none">
			<label for="citySelector" style="display: inline">Select
				city:</label> <select class="form-control" id="citySelector">
				<option>Show all</option>
			</select>
		</div>

		<!-- POSTS -->
		<c:forEach var="post" items="${ posts }" varStatus="loop">
			<c:if test="${loop.index % 3 == 0 || loop.index == 0}">
				<div class="row" id="posts" style="margin-top: 2%;">
			</c:if>
			<div class="col-md-4 filterDiv ${post.type} show"
				id="post${post.postID }">
				<div class="thumbnail">
					<a href="post?id=${post.postID}"> 
						<img src="getThumbnail?id=${post.postID}" alt="" style="height: 200px">
						<div class="row">
							<div class="col-md-6">
								<div class="caption">
									<p>${post.title}</p>
									<p>${post.type}</p>
								</div>
								<div class="caption">
									<p>Price: ${post.price}</p>
								</div>
							</div>
							<div class="col-md-6">
								<div class="caption">
									<p>
										<span id="cityOfPost${post.postID}">${post.city}</span>,
										${post.country}
									</p>
								</div>
							</div>
						</div>
					</a>
				</div>
			</div>
			<c:if test="${(loop.index+1) % 3 == 0}">
	</div>
	</c:if>
	
	</c:forEach>
	</div>
	<c:if test="${empty posts}">
		<script>				
		swal("Sorry!", "No results found", "info");
		</script>
	</c:if>
	<%@ include file="footer.jsp"%>

	<script type="text/javascript">
	var posts = document.getElementsByClassName("filterDiv");
	
	$('#countrySelector').click(function() {
		$('#typeSelector').val("Show all");
	});
	
	$('#typeSelector').click(function() {
		$('#citySelector').val("Show all");
		document.getElementById('cities').style = 'display: none';
		$('#countrySelector').val("Show all");
	});
	
	$('#citySelector').change(function() {
		for (div of posts) {
			if($(this).val() == "Show all") {
				div.classList.add("show");
			} else {
				div.classList.remove("show");
				
				var cityOfPost = div.firstChild.nextSibling.firstChild.nextSibling.childNodes[2].
				nextSibling.childNodes[2].nextSibling.firstChild.nextSibling.firstChild.nextSibling.
				firstChild.nextSibling.innerHTML;
				
			    if (cityOfPost == $(this).val() ) {
			    	div.classList.add("show");
			    }
			}
		}
	});
	
	$('#typeSelector').change(function() {
		
		for (div of posts) {
			if($(this).val() == "Show all") {
				div.classList.add("show");
			} else {
				div.classList.remove("show");
				
				var postType = div.firstChild.nextSibling.firstChild.nextSibling.childNodes[2].
				nextSibling.childNodes[1].firstChild.nextSibling.childNodes[2].nextSibling.innerHTML;
				
			    if (postType == $(this).val() ) {
			    	div.classList.add("show");
			    }
			}
		}
	});
	</script>

	<script type="text/javascript">
		var req = new XMLHttpRequest();
		var responseJSON;
		
		$(document).ready(function() {
			//get all locations
			req.open("Get", "locations");
			req.send();
			req.onreadystatechange = function() {
				if (req.readyState == 4 && req.status == 200) {
					/* get JSON response as object */ 
					responseJSON = JSON.parse(req.responseText);
					
					var select = document.getElementById('countrySelector');
					
					//load all countries into countries selector
					for (var key in responseJSON) {
						var opt = document.createElement("option");
						opt.value = key;
					    opt.innerHTML = key;
					    select.appendChild(opt);
					}    			
				}
			}
		});
		
		
		$('#countrySelector').change(function() {
			if($(this).val() == "Show all") {
				document.getElementById('cities').style = 'display: none';
				
				var posts = document.getElementsByClassName("filterDiv");
				
				for (div of posts) {
					div.classList.add("show");
				}
				
			} else {
				var cities;
				
				var select = document.getElementById('citySelector');
				select.innerHTML = null;
				select.appendChild(document.createElement("option"));
				
				for (var key in responseJSON) {
				    if (!responseJSON.hasOwnProperty(key)) continue;
				    
				    if (key == $(this).val()) {
				    	cities = responseJSON[key];
				    	break;
				    }
				}
				/* console.log(cities); */
				
				for (var city of cities) {
					var opt = document.createElement("option");
					opt.value = city;
				    opt.innerHTML = city;
				    select.appendChild(opt);
				}
				document.getElementById('cities').style = 'display: inline';
			}

		    // if you want to do stuff based on the OPTION element:
		    //var opt = $(this).find('option:selected')[0];
		    //console.log(opt);
		});
	</script>
</body>
</html>
