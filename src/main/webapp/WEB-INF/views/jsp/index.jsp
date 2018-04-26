<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

<head>
<title>Airbnb</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
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

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="js/bootstrap.js"></script>
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<![endif]-->
</head>


<body id="body">
	<!-- banner -->

	<%@ include file="header.jsp"%>
	<!-- <hr width="100%" style="border-color: #82b62d;"> !-->

	<!--IMAGE SLIDER STARTS HERE!-->
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div id="myCarousel" class="carousel slide" data-ride="carousel">
					<div class="carousel-inner">
						<div class="item carousel-item active">
							<img src="images/index3.jpg" alt="">
						</div>
						<div class="item carousel-item">
							<img src="images/index2.jpg" alt="">
							<div class="carousel-caption">
								<div class="carousel-action"></div>
							</div>
						</div>
						<div class="item carousel-item">
							<img src="images/index1.jpg" alt="">
							<div class="carousel-caption">
								<div class="carousel-action"></div>
							</div>
						</div>
					</div>
					<!-- Carousel controls -->
					<a class="carousel-control left carousel-control-prev"
						href="#myCarousel" data-slide="prev"> <i
						class="fa fa-angle-left"></i>
					</a> <a class="carousel-control right carousel-control-next"
						href="#myCarousel" data-slide="next"> <i
						class="fa fa-angle-right"></i>
					</a>
				</div>
			</div>
		</div>
	</div>
	<br>
	<!--IMAGE SLIDER ENDS HERE!-->

	<!--Footer-->
	<footer class="page-footer font-small blue-grey lighten-5 pt-0">
	<div style="background-color: #82b62d; margin-top: 2%;">
		<div class="container"
			style="background-color: #82b62d; height: 0.5px;">
			<!--Grid row-->
			<div class="row py-4 d-flex align-items-center">

				<!--Grid column-->
				<div class="col-12 col-md-5 text-left mb-4 mb-md-0"></div>
				<!--Grid column-->

				<!--Grid column-->
				<div class="col-12 col-md-7 text-center text-md-right">
					<!--Facebook-->
					<a class="fb-ic ml-0"> <i
						class="fa fa-facebook white-text mr-lg-4"> </i>
					</a>
					<!--Twitter-->
					<a class="tw-ic"> <i class="fa fa-twitter white-text mr-lg-4">
					</i>
					</a>
					<!--Google +-->
					<a class="gplus-ic"> <i
						class="fa fa-google-plus white-text mr-lg-4"> </i>
					</a>
					<!--Linkedin-->
					<a class="li-ic"> <i class="fa fa-linkedin white-text mr-lg-4">
					</i>
					</a>
					<!--Instagram-->
					<a class="ins-ic"> <i
						class="fa fa-instagram white-text mr-lg-4"> </i>
					</a>
				</div>
				<!--Grid column-->

			</div>
			<!--Grid row-->
		</div>
	</div>

	<!--Footer Links-->
	<div class="container mt-5 mb-4 text-center text-md-left">
		<div class="row mt-3">
			<!--First column-->
			<div class="col-md-3 col-lg-4 col-xl-3 mb-4 dark-grey-text">
				<a style="color: #82b62d;" href="index">Home</a>
			</div>
			<!--/.First column-->
			<!--Second column-->
			<div class="col-md-2 col-lg-2 col-xl-2 mx-auto mb-4 dark-grey-text">
				<a style="color: #82b62d;" href="explore">Explore</a>
			</div>
			<!--/.Second column-->
			<!--Third column-->
			<div class="col-md-3 col-lg-2 col-xl-2 mx-auto mb-4 dark-grey-text">
				<a style="color: #82b62d;" href="login">Login</a>
			</div>
			<!--/.Third column-->
			<!--Fourth column-->
			<div class="col-md-4 col-lg-3 col-xl-3">
				<a style="color: #82b62d;" href="register">Register</a>
			</div>
			<!--/.Fourth column-->

		</div>
	</div>
	<!--/.Footer Links--> <!-- Copyright-->
	<div class="footer-copyright py-3 text-center">
		<br> © 2018 Copyright: <a style="color: #82b62d;" href="index">
			<strong> Airbnb</strong>
		</a>
	</div>
	<!--/.Copyright --> </footer>
	<!--/.Footer-->

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
					window.location = "explore.jsp";
				}
			};

			req.send(null);
		}
	</script>
	<script>
		//footer smooth scroll
		$(window)
				.scroll(
						function(event) {
							function footer() {
								var scroll = $(window).scrollTop();
								if (scroll > 50) {
									$(
											".page-footer font-small blue-grey lighten-5 pt-0")
											.fadeIn("slow").addClass("show");
								} else {
									$(
											".page-footer font-small blue-grey lighten-5 pt-0")
											.fadeOut("slow")
											.removeClass("show");
								}

								clearTimeout($.data(this, 'scrollTimer'));
								$
										.data(
												this,
												'scrollTimer',
												setTimeout(
														function() {
															if ($(
																	'.page-footer font-small blue-grey lighten-5 pt-0')
																	.is(
																			':hover')) {
																footer();
															} else {
																$(
																		".page-footer font-small blue-grey lighten-5 pt-0")
																		.fadeOut(
																				"slow");
															}
														}, 2000));
							}
							footer();
						});
	</script>
</body>
</html>