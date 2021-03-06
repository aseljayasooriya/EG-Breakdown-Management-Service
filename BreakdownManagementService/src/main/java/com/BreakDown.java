package com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class BreakDown {

	//A common method to connect to the DB
	private Connection connect()
	{
		Connection con = null;

		try
		{
			Class.forName("com.mysql.jdbc.Driver");

			//Provide the database details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/electrogriddb", "root", "");
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage()); 
		}

		return con;
	}

	public String insertBreakdown(String bsector, String bdate, String sTime, String eTime, String btype) {

		String output = "";

		try {
			Connection con = connect();

			if(con == null) {
				return "Error while connecting to the database for inserting.";
			}

			// get affected Users

			String urlString = "http://localhost:8081/UserManagementService/UserManagementService/Users/sector/" + bsector + "/count";
			URL url = new URL(urlString);

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Accept", "application/json");

			if (urlConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ urlConnection.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(urlConnection.getInputStream())));

			int userCount = Integer.parseInt(br.readLine());

			//creating the prepared statement
			String query = " insert into breakdowninformation (`breakdownID`,`breakdownSector`,`breakdownDate`,`startTime`,`endTime`,`breakdownType`, `affectedUsers`)"
							+ " values (?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			//binding the values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, bsector);
			preparedStmt.setString(3, bdate);
			preparedStmt.setString(4, sTime);
			preparedStmt.setString(5, eTime);
			preparedStmt.setString(6, btype);
			preparedStmt.setInt(7, userCount);

			//execute the statement
			preparedStmt.execute();
			con.close();

			String newBreakdowns = readBreakdowns();
			output = "{\"status\":\"success\", \"data\": \"" + newBreakdowns + "\"}";

		}
		catch (Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error while inserting the breakdown.\"}";
			System.err.println(e.getMessage()); 
		}

		return output;
	}

	public String readBreakdowns() {

		String output = "";

		try {

			Connection con = connect();

			if(con == null) {
				return "Error while connecting to the database for reading.";
			}

			//Prepare the html table to be displayed
			output = "<table class='table table-hover'>" + 
					 "<tr><th>Breakdown Sector</th><th>Breakdown Date</th>" +
					 "<th>Breakdown Start Time</th>" +
					 "<th>Breakdown End Time</th>" +
					 "<th>Breakdown Type</th>" +
					 "<th>No of Affected Users</th>" +
					 "<th>Update</th><th>Remove</th></tr>";

			String query = "SELECT * FROM breakdowninformation";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			//iterate through the rows in the result set
			while(rs.next()) {
				String breakdownID = Integer.toString(rs.getInt("breakdownID"));
				String breakdownSector = rs.getString("breakdownSector");
				String breakdownDate = rs.getString("breakdownDate");
				String startTime = rs.getString("startTime");
				String endTime = rs.getString("endTime");
				String breakdownType = rs.getString("breakdownType");
				int affectedUsers = rs.getInt("affectedUsers");

				//add into html table
				output += "<tr><td>" + breakdownSector + "</td>";
				output += "<td>" + breakdownDate + "</td>";
				output += "<td>" + startTime + "</td>";
				output += "<td>" + endTime + "</td>";
				output += "<td>" + breakdownType + "</td>";
				output += "<td>" + affectedUsers + "</td>";

				//buttons
				output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-warning' data-breakdownid='" + breakdownID + "'></td>"
						 + "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-breakdownid='" + breakdownID + "'></td></tr>";
			}

			con.close();

			//complete the html table
			output += "</table>";
		}
		catch (Exception e) {
			output = "Error while reading the breakdown info.";
			System.err.println(e.getMessage());
		}

		return output;
	}

	public String updateBreakdown(String ID, String sector, String date, String sTime, String eTime, String type) {

		String output = "";
		String decodeSTime = java.net.URLDecoder.decode(sTime);
		String decodeETime = java.net.URLDecoder.decode(eTime);
		String decodeType = java.net.URLDecoder.decode(type);

		try {

			Connection con = connect();

			if(con == null) {
				return "Error while connecting to the database for updating.";
			}

			//create the prepared statement
			String query = "UPDATE breakdowninformation SET breakdownSector=?,breakdownDate=?,startTime=?,endTime=?,breakdownType=? WHERE breakdownID=?";
			
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			//binding values
			preparedStmt.setString(1, sector);
			preparedStmt.setString(2, date);
			preparedStmt.setString(3, decodeSTime);
			preparedStmt.setString(4, decodeETime);
			preparedStmt.setString(5, decodeType);
			preparedStmt.setInt(6, Integer.parseInt(ID));

			//execute the statement
			preparedStmt.execute();
			con.close();

			String newBreakdowns = readBreakdowns();
			output = "{\"status\":\"success\", \"data\": \"" + newBreakdowns + "\"}"; 
		}
		catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while updating the breakdown.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

	public String deleteBreakdown(String breakdownID) {

		String output = "";

		try {
			Connection con = connect();

			if(con == null) {
				return "Error while connecting to the database for deleting.";
			}

			//create a prepared statement
			String query = "delete from breakdowninformation where breakdownID=?";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			//binding values
			preparedStmt.setInt(1, Integer.parseInt(breakdownID));

			//execute the statement
			preparedStmt.execute();
			con.close();

			String newBreakdowns = readBreakdowns();
			output = "{\"status\":\"success\", \"data\": \"" + newBreakdowns + "\"}";

		}
		catch(Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while deleting the breakdown.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

public String readSectorBreakdowns(String bSector) {

		String output = "";

		try {

			Connection con = connect();

			if(con == null) {
				return "Error while connecting to the database for reading.";
			}


			output = "<table border='1'><tr><th>Breakdown Sector</th><th>Breakdown Date</th>" +
					 "<th>Breakdown Start Time</th>" +
					 "<th>Breakdown End Time</th>" +
					 "<th>Breakdown Type</th>" +
					 "<th>Affected Users</th>" +
					 "<th>Update</th><th>Remove</th></tr>";

			//query for extracting the values
			String query = "SELECT * FROM breakdowninformation WHERE breakdownSector=?";

			//creating the prepared statement
			PreparedStatement preparedStmt = con.prepareStatement(query);

			//binding values
			preparedStmt.setString(1, bSector);

			//Obtaining the result set
			ResultSet rs = preparedStmt.executeQuery();


			while(rs.next()) {
				String breakdownID = Integer.toString(rs.getInt("breakdownID"));
				String breakdownSector = rs.getString("breakdownSector");
				String breakdownDate = rs.getString("breakdownDate");
				String startTime = rs.getString("startTime");
				String endTime = rs.getString("endTime");
				String breakdownType = rs.getString("breakdownType");
				int affectedUsers = rs.getInt("affectedUsers");

				//add into html table
				output += "<tr><td>" + breakdownSector + "</td>";
				output += "<td>" + breakdownDate + "</td>";
				output += "<td>" + startTime + "</td>";
				output += "<td>" + endTime + "</td>";
				output += "<td>" + breakdownType + "</td>";
				output += "<td>" + affectedUsers + "</td>";

				//buttons
				output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary' data-itemid='" + breakdownID + "'></td>"
						 + "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-itemid='" + breakdownID + "'></td></tr>";
			}

			con.close();

			//complete the html table
			output += "</table>";
		}
		catch (Exception e) {
			output = "Error while reading the breakdown info.";
			System.err.println(e.getMessage());
		}

		return output;
	}

}
