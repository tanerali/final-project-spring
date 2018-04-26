<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Airbnb</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<link href="css/bootstrap.css" rel="stylesheet" type="text/css"
	media="all" />

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
<body>
	<!-- banner -->
	<div id="top" class="banner">

		<%@ include file="header.jsp"%>
		
		<div class="container">
			<div class="slider">
				<div class="callbacks_container">
					<div class="inputPost">
						<form id="postForm">
							<table align="center"
								style="border-collapse: separate; border-spacing: 0.5em; margin-top: 4%;">
								<tr>
									<td><input class="form-control" type="text" name="title"
										id="title" placeholder="Title"></td>
								</tr>
								<tr>
									<td><textarea rows="10" cols="135" class="form-control"
											name="description" id="description" placeholder="description"></textarea></td>
								</tr>

								<tr>
									<td><input class="form-control" type="number" name="price"
										id="price" placeholder="Price"></td>
								</tr>

								<tr>
									<td><select class="form-control" name="type" id="type">
											<option id="opt1" value="Hotel">Hotel</option>
											<option id="opt2" value="Hotel">Apartment</option>
											<option id="opt3" value="Hotel">House</option>
											<option id="opt4" value="Hotel">Cottage</option>
									</select></td>
								</tr>
								<tr>
									<td>
										<!--<form method="post" action="upload"
											enctype="multipart/form-data">
											<input style="display: none;" class="form-control"
												type="file" id="myFileField" accept="image/*" name="file"><br>
											<div id="fc">ADD A PICTURE</div>
											<div id="name"></div>
										</form>!-->
									</td>
								</tr>
								<tr>
									<td>
										<button id="upload" class="form-control" type="button"">Upload</button>
									</td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
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
		$('#fc').click(function() {
			$('#myFileField').click();
		});

		$('#myFileField').change(
				function() {
					$('#name').html(
							function() {
								var fakePath = $('#myFileField').attr('value')
										.toString().split('\\');
								return fakePath[fakePath.length - 1];
							});
				});
	</script>

	<script>
		jQuery(document).ready(function($) {

			$("#upload").click(function(event) {

				var data = {}
				data["title"] = $("#title").val();
				data["description"] = $("#description").val();
				data["type"] = $("#type").val();
				data["price"] = $("#price").val();
				$("#upload").prop("disabled", true);

				$.ajax({
					type : "POST",
					contentType : "application/json",
					url : "upload",
					data : JSON.stringify(data),
					dataType : 'json',
					timeout : 600000,
					success : function(response) {
						$("#upload").prop("disabled", false);
						//...
						var obj = JSON.parse(response);

						window.location.href = 'post?id=' + obj;

					},
					error : function(e) {
						$("#upload").prop("disabled", false);
						window.location.href = 'host';
					}
				});

			});

		});
	</script>
</body>
</html>
