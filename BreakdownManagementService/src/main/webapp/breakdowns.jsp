<%@ page import="com.BreakDown"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Breakdown Management</title>
<link rel="stylesheet" href="Views/bootstrap.min.css">
<script src="Components/jquery-3.6.0.min.js"></script>
<script src="Components/breakdowns.js"></script>
</head>
<body>

<div class="container">
	<div class="row">
		<div class="col-6">
		
			<h3 style="text-align:center" >Breakdown Management</h3>
			
			<form id="formBreakdown" name="formBreakdown">
				Breakdown Sector: 
				<input id="breakdownSector" name="breakdownSector" type="text" class="form-control form-control-sm">
				<br>
				
				Breakdown Date: 
				<input id="breakdownDate" name="breakdownDate" type="date" class="form-control form-control-sm">
				<br>
				
				Breakdown Start Time: 
				<input id="breakdownSTime" name="breakdownSTime" type="time" class="form-control form-control-sm">
				<br>
				
				Breakdown End Time: 
				<input id="breakdownETime" name="breakdownETime" type="time" class="form-control form-control-sm">
				<br>
				
				Breakdown Type: 
				<input id="breakdownType" name="breakdownType" type="text" class="form-control form-control-sm">
				<br>
						
				<input id="btnSave" name="btnSave" type="button" value="Insert" class="btn btn-warning btn-block">
						
				<input type="hidden" id="hidBreakdownIDSave" name="hidBreakdownIDSave" value="">				
			</form>
		</div>
	 </div>
	 
	 <br>
	 
	 <div class="row">
	 	<div class="col-8">			
			<div id="alertSuccess" class="alert alert-success"></div>
			<div id="alertError" class="alert alert-danger"></div>
		</div>	
	 </div>
	 
	 <br>
	 
	 <div class="row">
	 	<div class="col-12">	
			<div id="divBreakdownsGrid">
				<%
					BreakDown breakdownObj = new BreakDown();
					out.print(breakdownObj.readBreakdowns());
				%>
			</div>					
		</div>
	</div>
		
</div>

</body>
</html>