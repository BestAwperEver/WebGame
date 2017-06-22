package com.asuscomm.radagast;

import static java.lang.Class.forName;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.orientechnologies.orient.jdbc.OrientJdbcConnection;
import com.orientechnologies.orient.jdbc.OrientJdbcDriver;

/**
 * Servlet implementation class Lobby
 */
public final class Lobby extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Lobby() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
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
//		String dbUrl = "remote:radagast.asuscomm.com:2424/testdb";
//		String username = "testuser";
//		String password = "test";
		
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
		// TODO Auto-generated method stub
		//Close Connection 
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
	protected class PlayerInfo {
		public PlayerInfo(String map_name, String coord, int number_of_bullets, String info) {
			this.map_name = map_name;
			this.coord = coord;
			this.number_of_bullets = number_of_bullets;
			this.info = info;
		}
		String map_name;
		String coord;
		String info;
		int number_of_bullets;
		boolean was_slayed_by_minotaur = false;
		boolean was_slayed_by_player = false;
	}
	protected String getRandomMapName() throws SQLException {
		Statement stmt = conn.createStatement();
		// get random map name
		ResultSet rs = stmt.executeQuery("select map_name from Maps");
		Vector<String> map_names = new Vector<String>();
		while (rs.next()) {
			map_names.add(rs.getString("map_name"));
		}
		int randomMapIndex = ThreadLocalRandom.current().nextInt(0, map_names.size());
		return map_names.get(randomMapIndex);
	}
	protected String getRandomCoords(String map_name) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select max_letter, max_number " +
				"from Maps where map_name = '" + map_name + "'");
		String max_letter = rs.getString("max_letter");
		int max_number = rs.getInt("max_number");
		String coord = "a1";
		String info = "wall";
		while (info.equals("clear") == false) {
			Random r = new Random();
			char randomLetter =  (char) ('a' + r.nextInt((int)(max_letter.charAt(0) - 'a' + 1)));
			//char randomLetter = (char) ThreadLocalRandom.current().nextInt(0, (int) max_letter.charAt(0) + 1);
			int randomInt = ThreadLocalRandom.current().nextInt(1, max_number + 1);
			coord = String.valueOf(randomLetter) + randomInt;
			info = getCellInfo(map_name, coord);
		}
		return coord;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		Session session = currentUser.getSession();
	
		String coord = null;
		String map_name = null;
		try {
			map_name = getRandomMapName();
			coord = getRandomCoords(map_name);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		int number_of_bullets = ThreadLocalRandom.current().nextInt(0, 7);
		PlayerInfo pi = new PlayerInfo(map_name, coord, number_of_bullets,
				"You are somewhere in the darkness of Labyrinth.");
		try {
			updatePlayerInfo(username, pi);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
//		request.setAttribute("number_of_bullets", number_of_bullets);
//		request.setAttribute("info", pi.info);
		session.setAttribute("init", true);
		RequestDispatcher rd = request.getRequestDispatcher("/Game");
		rd.forward(request, response);
	}
	protected PlayerInfo getInfoByUsername(String username) throws SQLException {
		Statement stmt = mysql_conn.createStatement();
		ResultSet rs = null;
		if (stmt.execute("select map_name, coords, bullets, info from Players where username = \"" + username + "\"")) {
			rs = stmt.getResultSet();
			rs.next();
		}
		return new PlayerInfo(rs.getString("map_name"), rs.getString("coords"),
				rs.getInt("bullets"), rs.getString("info")); 
	}
	protected void updatePlayerInfo(String username, PlayerInfo pi) throws SQLException {
		Statement stmt = mysql_conn.createStatement();
		ResultSet rs = stmt.executeQuery("select username from Players where username ='" + username + "'");
		Statement stmt2 = mysql_conn.createStatement();
		if (rs.next()) {
			String slayed = "";
			if (pi.was_slayed_by_minotaur) {
				slayed = ", slayed_by_minotaurs = slayed_by_minotaurs + 1";
				pi.was_slayed_by_minotaur = false;
			}
			if (pi.was_slayed_by_player) {
				slayed = ", slayed_by_players = slayed_by_players + 1";
				pi.was_slayed_by_player = false;
			}
			stmt2.executeUpdate("update Players set map_name = '" + pi.map_name
							+ "', coords = '" + pi.coord
							+ "', info = '" + pi.info
							+ "', bullets = " + pi.number_of_bullets
							+ slayed
							+ " where username = '" + username + "'");
		} else {
			stmt2.executeUpdate("insert into Players (username, map_name, coords, info, bullets) values "
					+ "('" + username + "','" + pi.map_name + "','" + pi.coord + "','"
					+ pi.info + "'," + pi.number_of_bullets + ")");
		}
	}
	protected String getCellInfo(String map_name, String coord) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select " + coord + " from Maps where map_name = '" + map_name + "'");
		if (rs.getObject(coord) != null) {
			System.out.println(rs.getString(1));
			return rs.getString(1);
		}
		return "clear";
	}

}
