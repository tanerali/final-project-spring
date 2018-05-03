<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Edit ${post.getTitle()}</title>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<link href="css/bootstrap.css" rel="stylesheet" type="text/css"
	media="all" />
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
										id="title" value="${post.title} "></td>
								</tr>
								<tr>
									<td><textarea rows="10" cols="135" class="form-control"
											name="description" id="description">${post.description}</textarea></td>
								</tr>

								<tr>
									<td><input class="form-control" type="number" name="price"
										id="price" value="${post.price}"></td>
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
										<div class="container">
											<input type="file" name="file" id="file"
												style="display: none">

											<!-- Drag and Drop container-->
											<div class="upload-area" id="uploadfile"
												style="width: 70%; height: 200px; border: 2px solid lightgray; border-radius: 3px; margin: 0 auto; margin-top: 0px; text-align: center; overflow: auto;">
												<h2
													style="text-align: center; font-weight: normal; font-family: sans-serif; line-height: 50px; color: darkslategray;">
													Drag and Drop images here<br />Or<br />Click to select
												</h2>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<button id="upload" class="form-control" type="button"">Upload</button>
									<td><input id="postid" type="hidden" name="ID"
										value="${post.postID }"></td>
									<td><input id="postdate" type="hidden" name="date"
										value="${post.dateOfPosting}"></td>
									<td><input id="posthostid" type="hidden" name="userID"
										value="${post.hostID}"></td>


								</tr>
								<tr>
									<td><a href="post?id= ${post.postID}">or return to
											post</a></td>
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

		$('#myFileField').change(function() {
			$('#name').html(function() {
				var fakePath = ($('#myFileField').val() + "").split('\\');
				return fakePath[fakePath.length - 1];
			});
		});
	</script>


	<script>
		jQuery(document).ready(
				function($) {

					$("#upload").click(
							function(event) {

								var formData = new FormData();
								formData.append("title", document
										.getElementById("title").value);
								formData.append("description", document
										.getElementById("description").value);
								formData.append("price", document
										.getElementById("price").value);
								formData.append("type", document
										.getElementById("type").value);

								formData.append("ID", document
										.getElementById("postid").value);
								formData.append("userID", document
										.getElementById("posthostid").value);
								formData.append("date", document
										.getElementById("postdate").value);
								$("#upload").prop("disabled", true);

								$.ajax({
									type : "POST",
									url : "editPost",
									data : formData,
									dataType : 'text',
									processData : false,
									contentType : false,
									success : function(response) {
										$("#upload").prop("disabled", false);
										//...

									},
									error : function(e) {
										$("#upload").prop("disabled", false);
										window.location.href = 'host';
									}
								});

							});

				});
	</script>
	<script>
		$(function() {

			// preventing page from redirecting
			$("html").on("dragover", function(e) {
				e.preventDefault();
				e.stopPropagation();
				$("h2").text("Drag here");
			});

			$("html").on("drop", function(e) {
				e.preventDefault();
				e.stopPropagation();
			});

			// Drag enter
			$('.upload-area').on('dragenter', function(e) {
				e.stopPropagation();
				e.preventDefault();
				$("h2").text("Drop");
			});

			// Drag over
			$('.upload-area').on('dragover', function(e) {
				e.stopPropagation();
				e.preventDefault();
				$("h2").text("Drop");
			});

			// Drop
			$('.upload-area').on('drop', function(e) {
				e.stopPropagation();
				e.preventDefault();

				$("h2").text("Upload");

				var file = e.originalEvent.dataTransfer.files;
				var fd = new FormData();

				fd.append('file', file[0]);

				fd.append('ID', document.getElementById("postid").value);
				swal("Successfully uploaded!", file[0].name, "success");
				uploadData(fd);
			});

			// Open file selector on div click
			$("#uploadfile").click(function() {
				$("#file").click();
			});

			// file selected
			$("#file").change(function() {
				var fd = new FormData();

				var files = $('#file')[0].files[0];

				fd.append('file', files);
				fd.append('ID', document.getElementById("postid").value);
				uploadData(fd);
			});
		});

		// Sending AJAX request and upload file
		function uploadData(formdata) {

			$.ajax({
				url : 'multipleUpload',
				type : 'post',
				data : formdata,
				contentType : false,
				processData : false,
				dataType : 'json',
				success : function(response) {
					alert("Uploaded " + reponse);
				}
			});
		}
	</script>
</body>
</html>
