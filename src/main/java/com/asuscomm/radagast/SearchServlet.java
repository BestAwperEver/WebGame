package com.asuscomm.radagast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import static java.lang.Class.forName;

import java.io.IOException;

//import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
//import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.jdbc.OrientJdbcDriver;
import com.orientechnologies.orient.jdbc.OrientJdbcConnection;
//import static com.orientechnologies.orient.jdbc.OrientDbCreationHelper.createSchemaDB; 
//import static com.orientechnologies.orient.jdbc.OrientDbCreationHelper.loadDB; 

//import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
    public static Connection conn;
    
	public void init(ServletConfig config) throws ServletException {
		try {
			//OrientJdbcDriver ojd = new OrientJdbcDriver(); 
			//forName("com.orientechnologies.orient.jdbc.OrientJdbcDriver");
			forName(OrientJdbcDriver.class.getName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		String dbUrl = "remote:radagast.asuscomm.com:2424/testdb";
		String username = "testuser";
		String password = "test";
	       
	    //Create Connection
	    Properties info = new Properties();
	    info.put("user", username);
	    info.put("password", password);
	    try {
			conn = (OrientJdbcConnection) DriverManager.getConnection("jdbc:orient:" + dbUrl, info);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		//Close Connection 
		try {
			if (conn != null && !conn.isClosed()) 
			   conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @throws SQLException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public String getRecordByName(String part) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select from PhoneBookRecord where FullName containsText \"" + part + "\"");
		return rs.getString("FullName") + ", " + rs.getString("Adress") + ", PN: " + rs.getString("PhoneNumber"); 
	}
	public String getRecordByPhone(String part) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select from PhoneBookRecord where PhoneNumber containsText \"" + part + "\"");
		return rs.getString("FullName") + ", " + rs.getString("Adress") + ", PN: " + rs.getString("PhoneNumber"); 
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//c:/Users/admin/workspace/servlet/WebContent
		//RequestDispatcher rd = getServletContext()
		RequestDispatcher rd = request.getRequestDispatcher("/jsp0.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String name_part = request.getParameter("name_part");
		String phone_part = request.getParameter("phone_part");
		String result = new String();
		
		try {
			if (name_part != null && name_part.length() != 0) {
				result = getRecordByName(name_part);
			} else if (phone_part != null && phone_part.length() != 0) {
				result = getRecordByPhone(phone_part);
			} else {
				result = "Record is not found!";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("record", result);
		//RequestDispatcher rd = getServletContext().
		RequestDispatcher rd = request.getRequestDispatcher("/jsp1.jsp");
		rd.forward(request, response);
	}

}