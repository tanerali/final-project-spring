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
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<body>
	<!-- banner -->
	<div id="top" class="banner">

		<%@ include file="header.jsp"%>

		<div class="container">
			<div class="slider">
				<div class="callbacks_container">
					<div class="inputPost">
						<form id="postForm" enctype='multipart/form-data'>
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
											<option id="opt2" value="Apartment">Apartment</option>
											<option id="opt3" value="House">House</option>
											<option id="opt4" value="Cottage">Cottage</option>
									</select></td>
								</tr>
								<tr>
									<td><input style="display: none;" type="file"
										id="myFileField" accept="image/*" name="file"><br>
										<div
											style="border: 2px; color: palevioletred; border-style: outset; text-align: center;"
											id="fc">CLICK TO ADD PICTURE</div>
										<div id="name"></div></td>
								</tr>
								<tr>
									<td>
										<div class="form-group" id="countries" style="display: inline">
											<label for="country" style="display: inline">Select
												country:</label> <select class="form-control" id="country">
												<option></option>
											</select>
										</div>

										<div class="form-group" id="cities" style="display: none">
											<label for="city" style="display: inline">Select
												city:</label> <select class="form-control" id="city">
												<option></option>
											</select>
										</div>
									</td>
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

	<%@ include file="footer.jsp"%>
	<script>
		var req = new XMLHttpRequest();
		$('#fc').click(function() {
			$('#myFileField').click();
		});

		$('#myFileField').change(function() {
			$('#name').html(function() {
				var fakePath = ($('#myFileField').val() + "").split('\\');
				return fakePath[fakePath.length - 1];
			});
		});
	</script>

	<script type="text/javascript">
		var responseJSON;
		
		$(document).ready(function() {
			req.open("Get", "locations");
			req.send();
			req.onreadystatechange = function() {
				if (req.readyState == 4 && req.status == 200) {
					/* get JSON response as object */ 
					responseJSON = JSON.parse(req.responseText);
					console.log(responseJSON);
					var select = document.getElementById('country');
					
					for (var key in responseJSON) {
						var opt = document.createElement("option");
						opt.value = key;
					    opt.innerHTML = key;
					    select.appendChild(opt);
					}    			
				}
			}
		});
		
		$('#country').change(function() {
			var cities;
			
			for (var key in responseJSON) {
			    if (!responseJSON.hasOwnProperty(key)) continue;
			    
			    if (key == $(this).val()) {
			    	cities = responseJSON[key];
			    	break;
			    }
			}
			/* console.log(cities); */
			
			var select = document.getElementById('city');
			select.innerHTML = null;
			
			for (var city of cities) {
				var opt = document.createElement("option");
				opt.value = city;
			    opt.innerHTML = city;
			    select.appendChild(opt);
			}
			document.getElementById('cities').style = 'display: inline';

		    // if you want to do stuff based on the OPTION element:
		    //var opt = $(this).find('option:selected')[0];
		    //console.log(opt);
		});
	</script>

	<script>
		jQuery(document).ready(function($) {

			$("#upload").click(function(event) {

				var fd = new FormData();
				fd.append('file', $('#myFileField')[0].files[0]);

// 				var data = {}
// 				data["title"] = $("#title").val();
// 				data["description"] = $("#description").val();
// 				data["type"] = $("#type").val();
// 				data["price"] = $("#price").val();
// 				data["image"] = fd;
				var formData = new FormData();
					formData.append("file",document.getElementById("myFileField").files[0]);
					formData.append("title", document.getElementById("title").value);
					formData.append("description", document.getElementById("description").value);
					formData.append("price", document.getElementById("price").value);
					formData.append("type", document.getElementById("type").value);
					formData.append("country", document.getElementById("country").value);
					formData.append("city", document.getElementById("city").value);

				/* $("#upload").prop("disabled", true); */

				$.ajax({
					type : "POST",
					url : "upload",
					data : formData,
					dataType: 'text',
				    processData: false,
				    contentType: false,
					success : function(response) {
						$("#upload").prop("disabled", false);
						//...
						var obj = JSON.parse(response);

						window.location.href = 'post?id=' + obj;

					},
					error : function(e) {
						$("#upload").prop("disabled", false);
						swal("Error", "Could not create your listing. Please make sure you enter "+
								"all details correctly.", "error", {
							button : "OK",
						});
					}
				});

			});

		});
	</script>
</body>
</html>
