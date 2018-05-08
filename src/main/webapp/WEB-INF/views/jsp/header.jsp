<%@page import="airbnb.model.Notification"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="css/loginRegister.css">
<!-- <script src="js/artyom.window.js"></script> -->
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

								<li><a href="#" id="openBtn" class="active"
									onclick="openSearch()">Search</a></li>
								<li><a href="explore" onclick="explore(this);">Explore</a></li>

								<c:if test="${sessionScope.user == null }">
									<li class="nav-item"><a class="nav-link"
										data-toggle="modal" href="javascript:void(0)"
										onclick="openLoginModal();">Log in</a></li>
									<li class="nav-item"><a href="register">Register</a></li>
								</c:if>


								<c:if test="${sessionScope.user != null }">
									<li><a href="host">Host</a></li>

									<li class=""><a href="#"
										class="dropdown-toggle hvr-bounce-to-bottom"
										data-toggle="dropdown" role="button" aria-haspopup="true"
										aria-expanded="false"> Notifications<span
											id="notificationsCounter" class="badge badge-light">${bookingRequests.size()}</span><span
											class="caret"></span>
									</a>
										<ul class="dropdown-menu"
											style="border: solid; border-radius: 20px;">
											<c:if test="${bookingRequests.size() == 0 }">
												<div style="width: 250px">
													<h1 class="bg-primary" style="text-align: center;">No
														notifications</h1>
												</div>
											</c:if>
											<c:forEach var="notification" items="${bookingRequests}">

												<li style="padding-top: 5px"
													id="notification${notification.notificationID }">
													<div style="width: 250px">
														<a href="post?id=${ notification.postID }"> <img
															style="width: 60%; height: 60%;" class="img-responsive"
															src="getThumbnail?id=${ notification.postID }">
															<h4>
																Booking request for <em>${ notification.title }</em>
															</h4>
														</a>
														<p>
															From: ${ notification.dateFrom }<br> To: ${ notification.dateTo }<br>
															Customer name: ${ notification.fullName }<br>
															Profile: <a
																href="profile?id=${ notification.customerID }">${ notification.email }</a>
														</p>
														<button
															onclick="rejectBookingRequest(${notification.notificationID})"
															style="float: left; margin-bottom: 5px; background-color: red; border: none; color: white; padding: 15px 32px;">REJECT
														</button>
														<button
															onclick="acceptBookingRequest(${notification.notificationID})"
															style="float: right; margin-bottom: 5px; background-color: #4CAF50; border: none; color: white; padding: 15px 32px;">ACCEPT
														</button>
													</div>
												</li>
											</c:forEach>
										</ul></li>


									<li><a href="personalProfile">Profile</a></li>
									<li><a href="logout">Logout</a></li>
								</c:if>
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
<!-- LOGIN & REGISTER HIDDEN -->
<div class="modal fade login" id="loginModal">
	<div class="modal-dialog login animated">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title">Login</h4>
			</div>
			<div class="modal-body">
				<div class="box">
					<div class="content">
						<div class="error"></div>
						<div class="form loginBox">
							<form class="form-signin" action="login" method="post"
								autocomplete="off">
								<h2 class="form-signin-heading">Please sign in</h2>

								<input type="email" required name="email" id="inputEmail"
									class="form-control" placeholder="Email address" autofocus>
								<input type="password" required name="password"
									id="inputPassword" class="form-control" placeholder="Password">
								<div class="checkbox">
									<label> <input type="checkbox" value="remember-me">
										Remember me
									</label>
								</div>
								<button
									style="background-color: #82b62d; border-color: #82b62d;"
									class="btn btn-lg btn-primary btn-block" type="submit">Sign
									in</button>
							</form>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<div class="forgot login-footer">
					<span>Looking to <a href="register">create an account</a> ?
					</span>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
function rejectBookingRequest(notificationID) {
	var req = new XMLHttpRequest();
	req.open("Delete", "notification/"+ notificationID);
	req.send();
	
	req.onreadystatechange = function() {
		if (req.readyState == 4 && req.status == 200) {
			var element = document.getElementById("notification"+ notificationID);
			element.parentNode.removeChild(element);
			var numNotifications = parseInt(document.getElementById('notificationsCounter').innerHTML);
			document.getElementById('notificationsCounter').innerHTML = --numNotifications;
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
			var numNotifications = parseInt(document.getElementById('notificationsCounter').innerHTML);
			document.getElementById('notificationsCounter').innerHTML = --numNotifications;
		}
	}
}
</script>

<script>
function openSearch() {
	document.getElementById("myOverlay").style.display = "block";
}
function closeSearch() {
	document.getElementById("myOverlay").style.display = "none";
}
function search() { //true means - async requests
	req.open("Get", "search?search="
			+ document.getElementById("search").value, true);
	req.onreadystatechange = proccesSearch;
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
</script>

<script>// With ES6,TypeScript etc

//Create a variable that stores your instance
const artyom = new Artyom();

//Or if you are using it in the browser
//var artyom = new Artyom();// or `new window.Artyom()`

//Add command (Short code artisan way)
artyom.on(['Good morning','Good afternoon']).then((i) => {
 switch (i) {
     case 0:
         artyom.say("Good morning, how are you?");
     break;
     case 1:
         artyom.say("Good afternoon, how are you?");
     break;            
 }
});

//Smart command (Short code artisan way), set the second parameter of .on to true
artyom.on(['Repeat after me *'] , true).then((i,wildcard) => {
 artyom.say("You've said : " + wildcard);
});

//or add some commandsDemostrations in the normal way
artyom.addCommands([
 {
	 indexes: ['go to *'],
	 smart:true,
     action: function(i,wildcard){
         wildcard = wildcard || "";
         
         switch(wildcard.toLowerCase()){
             case "explore":
            	 window.location.href = "explore";
             break;
             case "login":
            	 window.location.href = "login";
             break;
             case "register":
            	 window.location.href = "register";
             break;
             case "index":
            	 window.location.href = "index";
             break;
             case "host":
            	 window.location.href = "host";
             break;
             case "logout":
            	 window.location.href = "logout";
             break;
             case "home":
            	 window.location.href = "index";
             break;
             case "profile":
            	 window.location.href = "personalProfile";
             break;
             case "github":
                 window.location.href = "https://github.com/tanerali/final-project-spring";
             break;
             default:
                 console.warn("Location "+wildcard+" has been not saved.");
             break;
         }
         console.log(i,wildcard);
     }
 },
 {
     indexes: ['Repeat after me *'],
     smart:true,
     action: (i,wildcard) => {
         artyom.say("You've said : "+ wildcard);
     }
 },
 // The smart commands support regular expressions
 {
     indexes: [/Good Morning/i],
     smart:true,
     action: (i,wildcard) => {
         artyom.say("You've said : "+ wildcard);
     }
 },
 {
     indexes: ['shut down yourself'],
     action: (i,wildcard) => {
         artyom.fatality().then(() => {
             console.log("Artyom succesfully stopped");
         });
     }
 },
]);

//Start the commands !
artyom.initialize({
 lang: "en-GB", // GreatBritain english
 continuous: true, // Listen forever
 soundex: true,// Use the soundex algorithm to increase accuracy
 debug: true, // Show messages in the console
 executionKeyword: "and do it now",
 listen: true, // Start to listen commands !

 // If providen, you can only trigger a command if you say its name
 // e.g to trigger Good Morning, you need to say "Jarvis Good Morning"
}).then(() => {
 console.log("Artyom has been succesfully initialized");
}).catch((err) => {
 console.error("Artyom couldn't be initialized: ", err);
});

/**
* To speech text
*/

/* artyom.say("Hello, this is an Airbnb assisstant",{
 onStart: () => {
     console.log("Reading ...");
 },
 onEnd: () => {;
 }
}); */
</script>
<!-- LOGIN & REGISTER JS -->
<script>

function showRegisterForm(){
    $('.loginBox').fadeOut('fast',function(){
        $('.registerBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast',function(){
            $('.register-footer').fadeIn('fast');
        });
        $('.modal-title').html('');
    }); 
    $('.error').removeClass('alert alert-danger').html('');
       
}
function showLoginForm(){
    $('#loginModal .registerBox').fadeOut('fast',function(){
        $('.loginBox').fadeIn('fast');
        $('.register-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');    
        });
        
        $('.modal-title').html('');
    });       
     $('.error').removeClass('alert alert-danger').html(''); 
}

function openLoginModal(){
    showLoginForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230);
    
}
function openRegisterModal(){
    showRegisterForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230);
    
}

function loginAjax(){
    /*   Remove this comments when moving to server
    $.post( "/login", function( data ) {
            if(data == 1){
                window.location.replace("/home");            
            } else {
                 shakeModal(); 
            }
        });
    */

/*   Simulate error message from the server   */
     shakeModal();
}

function shakeModal(){
    $('#loginModal .modal-dialog').addClass('shake');
             $('.error').addClass('alert alert-danger').html("Invalid email/password combination");
             $('input[type="password"]').val('');
             setTimeout( function(){ 
                $('#loginModal .modal-dialog').removeClass('shake'); 
    }, 1000 ); 
}
</script>
<!-- SHOW CITY&COUNTRY -->
<script>

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