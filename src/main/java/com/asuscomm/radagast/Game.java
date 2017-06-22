package com.asuscomm.radagast;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import static java.lang.Class.forName;

import java.io.IOException;

//import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
//import com.orientechnologies.orient.core.record.impl.ODocument;

//import static com.orientechnologies.orient.jdbc.OrientDbCreationHelper.createSchemaDB; 
//import static com.orientechnologies.orient.jdbc.OrientDbCreationHelper.loadDB; 
//import java.io.IOException;

import com.orientechnologies.orient.jdbc.OrientJdbcDriver;
import com.orientechnologies.orient.jdbc.OrientJdbcConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
/**
 * Servlet implementation class Game
 */
@WebServlet("/Game")
public final class Game extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Game() {
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
	protected String getRandomMapName() throws SQLException {
		Statement stmt = conn.createStatement();
		// get random map name
		ResultSet rs = stmt.executeQuery("select map_name from Maps");
		Vector<String> map_names = new Vector<String>();
		while (rs.next()) {
			map_names.add(rs.getString("map_name"));
		}
		int randomMapIndex = ThreadLocalRandom.current().nextInt(0, map_names.size() + 1);
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
			int randomInt = ThreadLocalRandom.current().nextInt(0, max_number + 1);
			coord = String.valueOf(randomLetter) + randomInt;
			info = getCellInfo(map_name, coord);
		}
		return coord;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		Subject currentUser = SecurityUtils.getSubject();
//		String username = (String) currentUser.getPrincipal();
//		
//		String coord = null;
//		String map_name = null;
//		try {
//			map_name = getRandomMapName();
//			coord = getRandomCoords(map_name);
//		} catch (SQLException ex) {
//			System.out.println("SQLException: " + ex.getMessage());
//		    System.out.println("SQLState: " + ex.getSQLState());
//		    System.out.println("VendorError: " + ex.getErrorCode());
//		}
//		Answer answer = new Answer();
//		pi.info = "You are somewhere in the darkness of Labyrinth.";
//		answer.number_of_bullets = ThreadLocalRandom.current().nextInt(0, 7);
//		PlayerInfo pi = new PlayerInfo(map_name, coord, answer.number_of_bullets);
//		try {
//			updatePlayerInfo(username, pi);
//		} catch (SQLException ex) {
//			System.out.println("SQLException: " + ex.getMessage());
//		    System.out.println("SQLState: " + ex.getSQLState());
//		    System.out.println("VendorError: " + ex.getErrorCode());
//		}
		//request.setAttribute("number_of_bullets", answer.number_of_bullets);
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String url = "/game";
		if (session.getAttribute("init") == null
				|| session.getAttribute("init").equals(false)) {
			url = "/Lobby";
		}
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}
	protected class Answer {
		int number_of_bullets;
		String info;
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
	protected PlayerInfo getInfoByUsername(String username) throws SQLException {
		Statement stmt = mysql_conn.createStatement();
		ResultSet rs = null;
		if (stmt.execute("select map_name, coords, bullets, info from Players where username = \"" + username + "\"")) {
			rs = stmt.getResultSet();
			rs.next();
		}
//		ResultSet rs = stmt.executeQuery("select 1");
		return new PlayerInfo(rs.getString("map_name"), rs.getString("coords"),
				rs.getInt("bullets"), rs.getString("info")); 
	}
	protected void updatePlayerInfo(String username, PlayerInfo pi) throws SQLException {
//		Statement stmt = conn.createStatement();
//		stmt.executeQuery("update Players set map_name = " + pi.map_name
//						+ ", coords = '" + pi.coord +
//						"', bullets = " + pi.number_of_bullets +
//						" where username = '" + username + "'");
		Statement stmt2 = mysql_conn.createStatement();
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
	}
	protected void updatePlayerVictories(String username) throws SQLException {
		Statement stmt2 = mysql_conn.createStatement();
		stmt2.executeUpdate("update Players set victories = victories + 1 "
				+ "where username = '" + username + "'");
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
	protected String getCellToCellInfo(String map_name, String coord1, String coord2) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select max_letter, max_number " +
						"from Maps where map_name = '" + map_name + "'");
		//Object obj = rs.getObject("max_letter");
		String max_letter = rs.getString("max_letter");
		int max_number = rs.getInt("max_number");
		ResultSet rs2 = stmt.executeQuery("select " + coord1 + "_" + coord2 +
				", " + coord2 + "_" + coord1 +
				" from Maps where map_name = '" + map_name + "'");
		//Object obj2 = rs2.getObject(coord1 + "_" + coord2);
		if (rs2.getObject(coord1 + "_" + coord2) != null) {
			System.out.println(rs2.getString(coord1 + "_" + coord2));
			return rs2.getString(coord1 + "_" + coord2);
		}
		if (rs2.getObject(coord2 + "_" + coord1) != null) {
			System.out.println(rs2.getString(coord2 + "_" + coord1));
			return rs2.getString(coord2 + "_" + coord1);
		}
		else if (coord2.charAt(0) == 'z' || coord2.charAt(0) > max_letter.charAt(0)) {
			return "wall";
		} else if (Integer.parseInt(coord2.substring(1,coord2.length())) == 0
				|| Integer.parseInt(coord2.substring(1,coord2.length())) > max_number) {
			return "wall";
		} else return "clear";
	}
	protected String getHospitalCoord(String map_name) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select hospital_coord " +
				"from Maps where map_name = '" + map_name + "'");
		return rs.getString("hospital_coord");		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected PlayerInfo processTurn(PlayerInfo pi, String direction) throws SQLException {
		//Answer ans = new Answer();
		String coord2 = pi.coord;
		switch (direction) {
			case "right": {
				coord2 = (char)(coord2.charAt(0) + 1) + coord2.substring(1);
			} break;
			case "up": {
				coord2 = coord2.substring(0, 1) +
						(Integer.parseInt(coord2.substring(1,coord2.length()))+1);
			} break;
			case "left": {
				char c = coord2.charAt(0);
				if (c == 'a') c = 'z';
				else --c;
				coord2 = c + coord2.substring(1);
			} break;
			case "down": {
				coord2 = coord2.substring(0, 1) +
						(Integer.parseInt(coord2.substring(1,coord2.length()))-1);			
			} break;
		}
		String info = getCellToCellInfo(pi.map_name, pi.coord, coord2);
		if (info.equals("wall")) {
			pi.info = "There is a wall.";
			pi.number_of_bullets = pi.number_of_bullets;
			return pi;
		}
		if (info.equals("exit")) {
			pi.info = "Victory! You are out of the Labyrinth!";
			pi.number_of_bullets = pi.number_of_bullets;
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession();
			session.setAttribute("init", false);
			return pi;
			// TO DO
		}
		info = getCellInfo(pi.map_name, coord2);
		if (info.equals("minotaur")) {
			pi.info = "You have been slayed by a minotaur and lost all your bullets. " +
						"You are in hospital.";
			pi.was_slayed_by_minotaur = true;
			pi.number_of_bullets = 0;
			pi.coord = getHospitalCoord(pi.map_name);
		}
		if (info.substring(0, 5).equals("river")) {
			pi.info = "You are in river. You have been carried away by the flow.";
			String river_direction = info.substring(6);
			switch (river_direction) {
				case "right": {
					pi.coord = (pi.coord.charAt(0) + 1) + pi.coord.substring(1);
				} break;
				case "up": {
					pi.coord = pi.coord.substring(0, 1) +
							(Integer.parseInt(pi.coord.substring(1,pi.coord.length()))+1);
				} break;
				case "left": {
					char c = pi.coord.charAt(0);
					if (c == 'a') c = 'z';
					else --c;
				} break;
				case "down": {
					pi.coord = pi.coord.substring(0, 1) +
							(Integer.parseInt(pi.coord.substring(1,coord2.length()))-1);			
				} break;
				case "stop": {
					pi.info = "You are in the river. It ends here, so you are not carried away.";
				} break;
			}
		}
		if (info.equals("clear")) {
			pi.info = "You stepped " + direction + ".";
			pi.coord = coord2;
		}
		if (info.equals("hospital")) {
			pi.info = "You stepped " + direction + ". You are in hospital.";
			pi.coord = coord2;
		}
		pi.number_of_bullets = pi.number_of_bullets;
		return pi;
	}
	protected PlayerInfo processAction(String pa) throws SQLException {
		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		PlayerInfo pi = null;
		pi = getInfoByUsername(username);
		//System.out.println(pa.substring(0, 4));
		if (pa.substring(0, 4).equals("turn")) {
			pi = processTurn(pi, pa.substring(5));
		} else if (pa.equals("shoot")) {
			//answer = processShoot(pi, pa.substring(6));
		} else {
			//answer = processKnife(pi);
		}
		updatePlayerInfo(username, pi);
		if (pi.info.substring(0, "Victory".length()).equals("Victory")) {
			updatePlayerVictories(username);
		}
		return pi;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
        String action = request.getParameter("action");

        PlayerInfo pi = null;
		try {
			pi = processAction(action);
		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		String url = "/game/game.jsp";
		
		if (pi.info.substring(0, "Victory".length()).equals("Victory")) {
			url = "/game/victory.jsp";
		}
		
//		request.setAttribute("number_of_bullets", pi.number_of_bullets);
//		request.setAttribute("in_hospital", true);
//		request.setAttribute("info", pi.info);

		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}

}