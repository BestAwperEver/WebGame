package com.asuscomm.radagast;

import static java.lang.Class.forName;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
			forName(OrientJdbcDriver.class.getName());
			forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		String dbUrl = "radagast.asuscomm.com:2424/web_game";
		String username = "web_game";
		String password = "webgamepassword";
	       
	    //Create Connection
	    Properties info = new Properties();
	    info.put("user", username);
	    info.put("password", password);
	    try {
			conn = (OrientJdbcConnection) DriverManager.getConnection("jdbc:orient:remote:" + dbUrl, info);
			mysql_conn = DriverManager.getConnection("jdbc:mysql://radagast.asuscomm.com:3306/web_game", info);
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		try {
			if (mysql_conn != null && !mysql_conn.isClosed()) 
				   mysql_conn.close();
			if (conn != null && !conn.isClosed()) 
			   conn.close();
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

	protected void resurrectMinotaurs(String map_name) throws SQLException {
		Statement stmt = conn.createStatement();
		if (map_name != null) {
			ResultSet rs = stmt.executeQuery("select from Maps where map_name = '" + map_name + "'");
			while (rs.next()) {
				int columnCount = rs.getMetaData().getColumnCount();
				for (int i = 1; i <= columnCount; ++i) {
					String value = rs.getString(i);
					if (value != null && value.equals("minotaur_corpse")) {
						String column = rs.getMetaData().getColumnName(i);
						stmt.executeUpdate("update Maps set " + column
								+ " = 'minotaur' where map_name = '"
								+ map_name + "'");
					}
				}
			}
		} else {
			ResultSet rs = stmt.executeQuery("select map_name from Maps");
			while (rs.next()) {
				resurrectMinotaurs(rs.getString("map_name"));
			}
		}

	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("player");
		String bullets_s = request.getParameter("bullets");
		String map_name = request.getParameter("map_name");
		
		if (map_name != null) {
			try {
				resurrectMinotaurs(map_name.equals("all_maps") ? null : map_name);
			} catch (SQLException ex) {
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			    request.setAttribute("error", "SQL error, see stack trace");
			}
		}
		
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
