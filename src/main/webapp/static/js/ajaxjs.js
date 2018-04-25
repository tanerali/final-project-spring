<script>
  jQuery(document).ready(
	function($) {

		$("#btn-save").click(function(event) {

			var data = {}
			data["title"] = $("#title").val();
			data["description"] = $("#description").val();
			data["type"] = $("#type").val();
			data["price"] = $("#price").val();
			$("#upload").prop("disabled", true);

			$.ajax({
		             type: "POST",
		             contentType: "application/json",
		             url: "/upload",
		             data: JSON.stringify(data),
		             dataType: 'json',
		             timeout: 600000,
		             success: function (data) {
		                 $("#upload").prop("disabled", false);
		                 //...
                     alert("yes");
		             },
		             error: function (e) {
		                 $("#upload").prop("disabled", false);
		                 //...
                     alert("nooo");
		             }
			});


		});

	});
</script>
