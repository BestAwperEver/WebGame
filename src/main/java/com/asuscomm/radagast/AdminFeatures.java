package com.asuscomm.radagast;

import static java.lang.Class.forName;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orientechnologies.orient.jdbc.OrientJdbcConnection;
import com.orientechnologies.orient.jdbc.OrientJdbcDriver;

/**
 * Servlet implementation class SendBullets
 */
public class AdminFeatures extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminFeatures() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static Connection conn;
    public static Connection mysql_conn;
    
	public void init(ServletConfig config) throws ServletException {
		try {
			forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		String username = "web_game";
		String password = "webgamepassword";
	       
	    //Create Connection
	    Properties info = new Properties();
	    info.put("user", username);
	    info.put("password", password);
	    try {
			mysql_conn = DriverManager.getConnection("jdbc:mysql://radagast.asuscomm.com:3306/web_game", info);
		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		//Close Connection 
		try {
			if (mysql_conn != null && !mysql_conn.isClosed()) 
				   mysql_conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("/admin");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void resurrectMinotaurs(String map_name) {
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("player");
		String bullets_s = request.getParameter("bullets");
		
		if (username == null || bullets_s == null || bullets_s.length() == 0) {
			doGet(request, response);
			return;
		}
		
		int bullets = Integer.parseUnsignedInt(bullets_s);
		if (bullets == 0 || bullets > 20) {
			request.setAttribute("error", "wrong number of bullets");
			request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
			return;
		}
		
		Statement stmt2;
		try {
			stmt2 = mysql_conn.createStatement();
			stmt2.executeUpdate("update Players set bullets ="
					+ " bullets + " + bullets
					+ " where username = '" + username + "'");
		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    request.setAttribute("error", "SQL error, see stack trace");
		}
		
		if (request.getAttribute("error") == null) {
			request.setAttribute("success", "You've sent " + bullets + " bullets to " + username + ".");
		}
		request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
	}

}
