$(document).ready(function()
{
	if ($("#alertSuccess").text().trim() == "")
	 {
	 	$("#alertSuccess").hide();
	 }
	 $("#alertError").hide();
});

//SAVE --------------------------------------------
$(document).on("click", "#btnSave", function(event)
{
 	// Clear alerts---------------------
	 $("#alertSuccess").text("");
	 $("#alertSuccess").hide();
	 $("#alertError").text("");
	 $("#alertError").hide();
	 
	 // Form validation-------------------
	var status = validateBreakdownForm();
	if (status != true)
	 {
		 $("#alertError").text(status);
		 $("#alertError").show();
		 return;
	 }
	 
	 // If valid------------------------
 	var type = ($("#hidBreakdownIDSave").val() == "") ? "POST" : "PUT";
 	
 	$.ajax(
	 {
		 url : "BreakdownsAPI",
		 type : type,
		 data : $("#formBreakdown").serialize(),
		 dataType : "text",
		 complete : function(response, status)
		 {
		 	onBreakdownSaveComplete(response.responseText, status);
	 	 }
	 }); 

});

//UPDATE---------------------------------------------
$(document).on("click", ".btnUpdate", function(event)
{
	$("#hidBreakdownIDSave").val($(this).data("breakdownid")); 	
	$("#breakdownSector").val($(this).closest("tr").find('td:eq(0)').text());
	$("#breakdownDate").val($(this).closest("tr").find('td:eq(1)').text());
	$("#breakdownSTime").val((decodeURI($(this).closest("tr").find('td:eq(2)').text())));
	$("#breakdownETime").val((decodeURI($(this).closest("tr").find('td:eq(3)').text())));
	$("#breakdownType").val((decodeURI($(this).closest("tr").find('td:eq(4)').text()))); 
});

//DELETE----------------------------------------------
$(document).on("click", ".btnRemove", function(event)
{
	 $.ajax(
	 {
		 url : "BreakdownsAPI",
		 type : "DELETE",
		 data : "breakdownID=" + $(this).data("breakdownid"),
		 dataType : "text",
		 complete : function(response, status)
		 {
		 	onBreakdownDeleteComplete(response.responseText, status);
		 }
	 });
});


// CLIENT-MODEL================================================================
function validateBreakdownForm()
{
	// SECTOR
	if ($("#breakdownSector").val().trim() == "")
	 {
	 	return "Insert Breakdown Sector.";
	 }
	// DATE
	if ($("#breakdownDate").val().trim() == "")
	 {
	 	return "Insert Breakdown Date.";
	 }
	// START TIME-------------------------------
	if ($("#breakdownSTime").val().trim() == "")
	 {
	 	return "Insert Breakdown Start Time.";
	 }
	// END TIME------------------------
	if ($("#breakdownETime").val().trim() == "")
	 {
	 	return "Insert Breakdown End Time.";
	 }
	 // TYPE------------------------
	if ($("#breakdownType").val().trim() == "")
	 {
	 	return "Insert Breakdown Type.";
	 }
	return true;
}

function onBreakdownSaveComplete(response, status)
{	
	if (status == "success") 
	{
		var resultSet = JSON.parse(response); 

		if (resultSet.status.trim() == "success")
		{
			 $("#alertSuccess").text("Successfully saved.");
			 $("#alertSuccess").show();
			 
			 $("#divBreakdownsGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error")
		{
			 $("#alertError").text(resultSet.data);
			 $("#alertError").show();
		}
	} else if (status == "error")
	{
		 $("#alertError").text("Error while saving.");
		 $("#alertError").show();
	} else
	{
		 $("#alertError").text("Unknown error while saving..");
		 $("#alertError").show();
	}
	
	$("#hidBreakdownIDSave").val("");
	$("#formBreakdown")[0].reset();
}

function onBreakdownDeleteComplete(response, status)
{
	if (status == "success")
	{
		 var resultSet = JSON.parse(response);
		 if (resultSet.status.trim() == "success")
		 {
			 $("#alertSuccess").text("Successfully deleted.");
			 $("#alertSuccess").show();
			 
			 $("#divBreakdownsGrid").html(resultSet.data);
		 } else if (resultSet.status.trim() == "error")
		 {
			 $("#alertError").text(resultSet.data);
			 $("#alertError").show();
		 }
	 } else if (status == "error")
	 {
		 $("#alertError").text("Error while deleting.");
		 $("#alertError").show();
	 } else
	 {
		 $("#alertError").text("Unknown error while deleting..");
		 $("#alertError").show();
 	 }
 }



