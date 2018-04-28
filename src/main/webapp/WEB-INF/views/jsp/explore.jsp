<%@page import="java.util.Map"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="airbnb.model.Post"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Airbnb</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<!-- bootstrap-css -->
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
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

<style>
.filterDiv {
	display: none;
}

.show {
	display: block;
}

/* Style the buttons */
.btn {
	border: none;
	outline: none;
	padding: 12px 16px;
	background-color: #f1f1f1;
	cursor: pointer;
}

.btn:hover {
	background-color: #ddd;
}

.btn.active {
	background-color: #666;
	color: white;
}

.dropdown-toggle {
	background-color: #4fce18;
}

.dropdown-toggle:hover {
	background-color: #3b9912;
}
</style>
<body>


	<%@ include file="header.jsp"%>

	<% ArrayList<Post> posts = (ArrayList) request.getAttribute("posts"); %>

	<div class="container">
		<h2>Explore All The Great Places You Can Stay</h2>

		<!-- TYPE dropdown menu -->
		<div class="dropdown" style="display: inline;">
			<button class="btn btn-primary dropdown-toggle" type="button"
				data-toggle="dropdown">
				Type <span class="caret"><br></span>
			</button>
			<ul class="dropdown-menu" id="myBtnContainer">
				<button class="btn active" onclick="filterSelection('all')">
					Show all</button>
				<br>
				<button class="btn" onclick="filterSelection('HOUSE')">
					House</button>
				<button class="btn" onclick="filterSelection('APARTMENT')">
					Apartment</button>
				<button class="btn" onclick="filterSelection('HOTEL')">
					Hotel</button>
				<button class="btn" onclick="filterSelection('COTTAGE')">
					Cottage</button>
			</ul>
			
		</div>

		<%-- <div class="form-group" style="display: inline">
			<label for="sel1" style="display:inline">Select list:</label> 
			<select class="form-control" id="sel1">
				<c:forEach var="country" items="countries">
					<option>${ country }</option>
				</c:forEach>
			</select>
		</div> --%>
		
<%-- <!-- cities dropdown menu -->
		<div class="dropdown" style="display: inline;">
			<button class="btn btn-primary dropdown-toggle" type="button"
				data-toggle="dropdown">
				Countries <span class="caret"><br></span>
			</button>
			
			<%			
			Map<String, TreeSet<String>> locations = (Map)application.getAttribute("locations");
			%>
			<ul class="dropdown-menu">
				<button class="btn active" onclick="filterSelection('all')">
					Show all</button>
				<br>
				<% for (String country: locations.keySet()) { %>
					<button class="btn" onclick="filterSelection('<%= country %>')">
						<%= country %></button>
				<% } %>
			</ul>
		</div> --%>

		<!-- POSTS -->
		<div class="row" id="posts">
			<% if (posts != null) {
					for (Post post : posts) { %>

			<div
				class="col-md-4 filterDiv <%=post.getType()%> <%-- <%=post.getCity()%> --%>">
				<div class="thumbnail">
					<a href="post?id=<%=post.getPostID()%>"> <img
						src="getThumbnail?id=<%=post.getPostID()%>" alt=""
						style="width: 100%">
						<div class="caption">
							<p><%=post.getTitle()%></p><%=post.getType()%>
						</div>
						<div class="caption">
							<p>
								Price:
								<%=post.getPrice()%></p>
						</div>
					</a>
				</div>
			</div>


			<% } %>
		<% } %>

		</div>

	</div>
	<script>
		var req = new XMLHttpRequest();
		function openSearch() {
			document.getElementById("myOverlay").style.display = "block";
		}

		function closeSearch() {
			document.getElementById("myOverlay").style.display = "none";
		}
		function search() {
			//true means - async requests
			req.open("Get", "search?search="
					+ document.getElementById("search").value, true);
			req.onreadystatechange = function() {
				if (req.readyState == 4 && req.status == 200) {
					document.getElementById("body").innerHTML = req.responseText;
				}
			};

			req.send(null);
		}
		function proccesSearch() {
			if (req.readyState == 4 && req.status == 200
					&& req.responseText != "[]") {
				closeSearch();
				var jsonSearch = eval('(' + req.responseText + ')');
				document.getElementById("top").className = "n";
				var table = document.getElementById("search-table");
				table.innerHTML = "";
				var headRow = table.insertRow(0);
				var headCell = headRow.insertCell(0);
				var results = jsonSearch;
				var i = 0;
				while (i < results.length) {
					row = table.insertRow(i + 1);
					cell = row.insertCell(0);
					cell.innerHTML = results[i++].title;
				}
			}

		}

		filterSelection("all")
		function filterSelection(c) {
			var x, i;
			x = document.getElementsByClassName("filterDiv");
			if (c == "all")
				c = "";
			for (i = 0; i < x.length; i++) {
				w3RemoveClass(x[i], "show");
				if (x[i].className.indexOf(c) > -1)
					w3AddClass(x[i], "show");
			}
		}

		function w3AddClass(element, name) {
			var i, arr1, arr2;
			arr1 = element.className.split(" ");
			arr2 = name.split(" ");
			for (i = 0; i < arr2.length; i++) {
				if (arr1.indexOf(arr2[i]) == -1) {
					element.className += " " + arr2[i];
				}
			}
		}

		function w3RemoveClass(element, name) {
			var i, arr1, arr2;
			arr1 = element.className.split(" ");
			arr2 = name.split(" ");
			for (i = 0; i < arr2.length; i++) {
				while (arr1.indexOf(arr2[i]) > -1) {
					arr1.splice(arr1.indexOf(arr2[i]), 1);
				}
			}
			element.className = arr1.join(" ");
		}

		// Add active class to the current button (highlight it)
		var btnContainer = document.getElementById("myBtnContainer");
		var btns = btnContainer.getElementsByClassName("btn");
		for (var i = 0; i < btns.length; i++) {
			btns[i].addEventListener("click", function() {
				var current = document.getElementsByClassName("active");
				for (var j = 0; j < current.length; j++) {
					current[j].className = current[j].className.replace(
							" active", "");
				}
				this.className += " active";
			});
		}
	</script>

</body>
</html>
