<%@page import="airbnb.model.Notification"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
									<li><a href="login">Login</a></li>
									<li><a href="register">Register</a></li>
								</c:if>
								
								
								<c:if test="${sessionScope.user != null }">
									<li><a href="host">Host</a></li>

									<li class=""><a href="#" class="dropdown-toggle hvr-bounce-to-bottom" data-toggle="dropdown" role="button" 
													aria-haspopup="true" aria-expanded="false">Notifications<span class="caret"></span></a>									
										<ul class="dropdown-menu" style="border: solid; border-radius:20px;">
											<c:forEach var="notification" items="${bookingRequests}">
											
												<li style="padding-top:5px" id="notification${notification.notificationID }">
													<div style="width: 250px">
													<a href="post?id=${ notification.postID }">
														<img style="width: 40%; height: 40%;" class="img-responsive" src="getThumbnail?id=${ notification.postID }">
															<h4>Booking request for <em>${ notification.title }</em></h4>
													</a>
													<p>
														From: ${ notification.dateFrom }<br> To: ${ notification.dateTo }<br>
														Customer name: ${ notification.fullName }<br> Profile:
														<a href="profile?id=${ notification.customerID }">${ notification.email }</a>
													</p>
													<button
														onclick="rejectBookingRequest(${notification.notificationID})"
														style="float: left; margin-bottom: 5px; background-color: red; border: none; color: white; padding: 15px 32px;">REJECT
													</button>
													<button
														onclick="acceptBookingRequest(${notification.notificationID})"
														style="float: right; margin-bottom: 5px; background-color: #4CAF50; 
														border: none; color: white; padding: 15px 32px;">ACCEPT
													</button>
													</div>
												</li>
											</c:forEach>
										</ul>
									</li>
									

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
