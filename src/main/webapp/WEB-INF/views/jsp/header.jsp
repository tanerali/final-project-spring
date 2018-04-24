<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="header">
	<div class="container">
		<div class="header-left">
			<div class="w3layouts-logo">
				<h1>
					<a href="index.jsp"><span>re </span> AIRBNB</a>
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

								<form action="search">
									<div id="myOverlay" class="overlay">
										<span class="closebtn" onclick="closeSearch()"
											title="Close Overlay">X</span>
										<div class="overlay-content">
											<input type="text" id="search" placeholder="Search.."
												name="search">
											<button type="submit" id="searchButton" onclick="search()"></button>
										</div>
									</div>
								</form>

								<li><a id="openBtn" class="active" onclick="openSearch()">Search</a></li>
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