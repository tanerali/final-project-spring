<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="images/icon.png">

<title>Register</title>

<!-- Bootstrap core CSS -->
<link href="css/bootstrap.css" rel="stylesheet">

<!--  jQuery and bootstrap js -->
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.js"></script>

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

<!-- common for whole website css -->
<link rel="stylesheet" href="css/style.css" type="text/css" media="all" />
<!--// css -->



<!-- Custom styles for this page -->
<link href="css/signin.css" rel="stylesheet">


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link href="../../assets/css/ie10-viewport-bug-workaround.css"
	rel="stylesheet">


<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

	<%@ include file="header.jsp"%>

	<div class="container">
		<!-- enctype = "multipart/form-data" -->
		<form class="form-signin" action="register" method="post"
			enctype="multipart/form-data">
			<h2 class="form-signin-heading">Please sign up</h2>

			<%
				Exception e = (Exception) request.getAttribute("error");
				if (e != null) {
			%>
			<h4 class="form-signin-heading" style="color: red"><%=e.getMessage()%></h4>
			<%
				}
			%>

			First Name<input type="text" class="form-control" name="firstName">
			Last Name<input type="text" class="form-control" name="lastName">
			Email<input type="email" class="form-control" name="email" required>
			Password<input type="password" class="form-control" name="pass1">
			Confirm Password<input type="password" class="form-control" name="pass2"> 
			
			Gender<select class="form-control" name="gender" required>
				<option>Male</option>
				<option>Female</option>
			</select>
			 
			<div class="form-group" id="countries" style="display: inline">
			<label for="countrySelector" style="display: inline">Select country:</label> 
			<select class="form-control" id="countrySelector" name="country">
				<option>Show all</option>
			</select>
			</div>
	
			<div class="form-group" id="cities" style="display: none">
				<label for="citySelector" style="display: inline">Select city:</label> 
				<select class="form-control" id="citySelector" name="city">
					<option>Show all</option>
				</select>
			</div>

			Photo<input type="file" class="form-control" name="photo" accept="image/*" size="50" required /> 
			Description<textarea class="form-control" name="description"></textarea> 
			Birth date<input type="date" class="form-control" name="birthDate" value="1990-01-01">
			Telephone Number<input type="tel" class="form-control" name="telNumber"> <br>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign up</button>
		</form>


	</div>
	<!-- /container -->
	<%@ include file="footer.jsp"%>
	
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
		
		$('#citySelector').change(function() {
			var posts = document.getElementsByClassName("filterDiv");
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
	</script>
</body>
</html>
