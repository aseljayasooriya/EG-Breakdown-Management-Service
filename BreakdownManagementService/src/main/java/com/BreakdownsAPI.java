package com;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Servlet implementation class BreakdownsAPI
 */
@WebServlet("/BreakdownsAPI")
public class BreakdownsAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	BreakDown breakdownObj = new BreakDown();

    public BreakdownsAPI() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String output = breakdownObj.insertBreakdown(request.getParameter("breakdownSector"),
				request.getParameter("breakdownDate"),
				request.getParameter("breakdownSTime"),
				request.getParameter("breakdownETime"),
				request.getParameter("breakdownType"));

		response.getWriter().write(output);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map paras = getParasMap(request);
		
		String output = breakdownObj.updateBreakdown(paras.get("hidBreakdownIDSave").toString(),
				 paras.get("breakdownSector").toString(),
				 paras.get("breakdownDate").toString(),
				 paras.get("breakdownSTime").toString(),
				 paras.get("breakdownETime").toString(),
				 paras.get("breakdownType").toString());
		
		response.getWriter().write(output);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map paras = getParasMap(request);
		
		String output = breakdownObj.deleteBreakdown(paras.get("breakdownID").toString());
		
		response.getWriter().write(output);
	}
	
	// Convert request parameters to a Map
	private static Map getParasMap(HttpServletRequest request)
	{
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			Scanner scanner = new Scanner(request.getInputStream(), "UTF-8");
			String queryString = scanner.hasNext() ? scanner.useDelimiter("\\A").next() : "";
			scanner.close();
			String[] params = queryString.split("&");
			for (String param : params)
			{ 
				String[] p = param.split("=");
				map.put(p[0], p[1]);
			}
		}
		catch (Exception e)
		{
		}
		return map;
	}	

}
