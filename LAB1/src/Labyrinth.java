
// Import required java libraries
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import pathfinding.Node;
import pathfinding.Path;

// Extend HttpServlet class
public class Labyrinth extends HttpServlet {

	private int m_iDimX, m_iDimY;
	private Node m_StartNode, m_EndNode;
	private Path m_Path;
	private String m_strPath;
	private int m_iPathSize;
	private List < Node > m_aPath;

	/**
	 * Initialize the Servlet.
	 */
	public void init( ) throws ServletException {
		// Do required initialization
		List < Node > blockedNodes = new ArrayList < Node >( );
		try {
			blockedNodes = getBlockedNodes( );
		} catch ( Exception ex ) {
			// Error
			destroy( );
		}
		try {
			initVars( blockedNodes );
		} catch ( Exception ex ) {
			// Error
			destroy( );
		}
	}

	/**
	 * Handles the typical servlet functions.
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		// Set response content type.
		response.setContentType( "text/html" );
		PrintWriter out = response.getWriter( );

		boolean bShowLab = false;
		if ( request.getParameter( "bShowLab" ) != null ) {
			bShowLab = true;
		}

		if ( !bShowLab ) {
			initFrontPage( request, response, out );
		} else {
			initLabyrinth( request, response, out );
		}
	}

	/**
	 * Handles everything when there is posted something to the Servlet. 1.
	 * Writes everything to the local variables. 2. Puts them into the database.
	 * 3. Redirects to the normal page.
	 */
	public void doPost( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException {
		// Set response content type.
		response.setContentType( "text/html" );
		PrintWriter out = response.getWriter( );

		if ( request.getParameter( "start" ) != null ) {
			String strValue = request.getParameter( "start" );
			m_StartNode.setX( Integer.parseInt( strValue.substring( 5, 6 ) ) );
			m_StartNode.setY( Integer.parseInt( strValue.substring( 6, 7 ) ) );
		}
		if ( request.getParameter( "end" ) != null ) {
			String strValue = request.getParameter( "end" );
			m_EndNode.setX( Integer.parseInt( strValue.substring( 3, 4 ) ) );
			m_EndNode.setY( Integer.parseInt( strValue.substring( 4, 5 ) ) );
		}

		try {
			clearTable( "blockednodes" );
		} catch ( Exception ex ) {
			out.println( "Fehler bei clearTable: " + ex );
		}

		List < Node > blockedNodes = new ArrayList < Node >( );
		for ( int i = 0; i < 10; i++ ) {
			for ( int z = 0; z < 10; z++ ) {
				if ( request.getParameter( "block" + i + z ) == null )
					continue;
				Node n = new Node( i, z );
				blockedNodes.add( n );
			}
		}
		try {
			setBlockedNode( blockedNodes );
		} catch ( Exception ex ) {
			out.println( "Fehler bei setBlockedNode: " + ex );
		}

		out.println( "m_iDimX: " + m_iDimX + " m_iDimY: " + m_iDimY + " m_StartNode: " + m_StartNode.toString( )
				+ " m_EndNode: " + m_EndNode.toString( ) + " blockedNodes: " );
		for ( int i = 0; i < blockedNodes.size( ); i++ )
			out.println( blockedNodes.get( i ).toString( ) );

		m_Path = new Path( m_iDimX, m_iDimY, m_StartNode, m_EndNode, blockedNodes );
		m_strPath = m_Path.getPath( );
		m_iPathSize = m_Path.getPathSize( );
		m_aPath.clear( );
		m_aPath = m_Path.getPathArray( );

		try {
			clearTable( "ui" );
		} catch ( Exception ex ) {
			out.println( "Fehler bei doMysql: " + ex );
		}

		try {
			setMysql( m_iDimX, m_iDimY, m_StartNode, m_EndNode, m_iPathSize, m_strPath );
		} catch ( Exception ex ) {
			out.println( "Fehler bei setMySql: " + ex );
		}

		response.sendRedirect( "http://127.0.0.1:8080/mywebapp/lab?bShowLab=true" );
	}

	/**
	 * Handles everything with the Frontpage. 1. Prints out the Navbar. 2. Prints out the Information about the Application.
	 * @param request
	 * @param response
	 * @param out
	 */
	private void initFrontPage( HttpServletRequest request, HttpServletResponse response, PrintWriter out ) {
		// Styling Navbar.
		out.print(
				"<style>body {background-color:#202020;}ul {list-style-type: none;margin: 0;padding: 0;overflow: hidden;background-color: #333;}li {float: left;}li a {display: block;color: white;text-align: center;padding: 14px 16px;text-decoration: none;}li a:hover:not(.active) {background-color: #111;}.active {background-color: #4CAF50;}</style>" );
		// Navbar
		out.print( "<ul>" );
		out.print( "<li><a class='active' href='http://127.0.0.1:8080/mywebapp/lab'>Startseite</a></li>" );
		out.print( "<li><a href='http://127.0.0.1:8080/mywebapp/lab?bShowLab=true'>Labyrinth</a></li>" );
		out.print( "</ul>" );
		
		out.print( "<div class='core' style='margin-left:auto;margin-right:auto;'>" );
		
		// Title
		out.print( "<h1 style='color:white;text-align:center;font-size:46px;'>Labyrinth</h1>" );
		out.print( "<p style='color:white;text-align:center;font-size:32px;'>Von Tobias Macha, Jörg Eckholdt und Daniel Martin</p>" );
		out.print( "<p style='color:white;text-align:left;font-size:26px;'>Beschreibung:</p>" );
		out.print( "<p style='color:white;text-align:left;font-size:24px;'>Die Anwendung \"Labyrinth\" ist eine Servlet Applikation, welche den Nutzer erlaubt ein in HTML erstelltes Labyrinth zu bauen, dabei Start- und Endpunkt zu definieren und sich den dazugehörigen Weg errechnen zu lassen.</p>" );
		out.print( "<p style='color:white;text-align:left;font-size:26px;'>Lizensvereinbarung:</p>" );
		out.print( "<p style='color:white;text-align:left;font-size:24px;'>Alle Autoren eines Entwicklungsprojektes besitzen uneingeschränktes Urheberrecht am Entwicklungsprojekt und am entwickelten Softwareprodukt. Die Autoren weisen sich in allen erstellten Produkten/Dokumenten mit ihrem Namen und als Studenten der HTW Dresden aus. Urheberrechte für nachgenutzte Dokumente bleiben unberührt. Literatur ist nach den üblichen Regeln zu zitieren. Zur Entwicklung wird ausschließlich \"Open Source\"-Software eingesetzt. Das entwickelte Produkt ist ebenfalls \"Open Source\", eine kommerzielle Nutzung ist ausgeschlossen. Es ist zu dokumentieren, welchen Lizenzbedingungen die für die Entwicklung genutzten Werkzeuge und Artefakte besitzen. Es ist ebenfalls zu dokumentieren, welchen Lizenzbedingungen die für das Betreiben des entwickelten Produktes nachgenutzten Komponenten unterliegen. Die weitere Nutzung des Entwicklungsprojektes und des entwickelten Produktes für Forschung und Lehre an der HTW Dresden wird von den Urhebern gewährt.</p>" );
		
		out.print( "</div>" );
	}

	/**
	 * Handles everything with the Labyrinth. 1. Prints the Labyrinth. 2. Set
	 * the Labyrinth with the values of the database. 3. Allow the user to
	 * change the field and calculate a new Path through the Labyrinth.
	 * @param request
	 * @param response
	 * @param out
	 */
	private void initLabyrinth( HttpServletRequest request, HttpServletResponse response, PrintWriter out ) {
		// Styling Navbar.
		out.print(
				"<style>body {background-color:#202020;}ul {list-style-type: none;margin: 0;padding: 0;overflow: hidden;background-color: #333;}li {float: left;}li a {display: block;color: white;text-align: center;padding: 14px 16px;text-decoration: none;}li a:hover:not(.active) {background-color: #111;}.active {background-color: #4CAF50;}</style>" );
		// Navbar
		out.print( "<ul>" );
		out.print( "<li><a href='http://127.0.0.1:8080/mywebapp/lab'>Startseite</a></li>" );
		out.print( "<li><a class='active' href='http://127.0.0.1:8080/mywebapp/lab?bShowLab=true'>Labyrinth</a></li>" );
		out.print( "</ul>" );

		List < Node > blockedNodes = new ArrayList < Node >( );

		try {
			blockedNodes = getBlockedNodes( );
		} catch ( Exception ex ) {
			out.println( "Fehler bei getBlockedNodes: " + ex );
		}

		try {
			initVars( blockedNodes );
		} catch ( Exception ex ) {
			// Error
			out.println( "Fehler bei initVars: " + ex );
		}

		// Print out the Labyrinth.
		out.print( "<div class='core' style='margin-left:auto;margin-right:auto;width:600px;height:640px;'>" );
		// Form Input
		out.print( "<form action='http://127.0.0.1:8080/mywebapp/lab' method='POST'>" );
		
		// Styling Radiobuttons.
		out.print(
				"<style>input[type=radio] {display:none;}input[type=radio] + label {display:inline-block;border:1px solid blue;background-color: #f0f0f0;border-color: #ddd;width: 36px; height: 36px;}input[type=radio]:checked + label {background-image: none;background-color:#008000;}</style>" );
		

		// Left
		out.print( "<div class'leftfromborder' style='margin-top:82px;float:left;'>" );
		for ( int i = 0; i < 10; i++ )
			if ( m_StartNode.getX( ) == 0 && m_StartNode.getY( ) == i )
				out.print(
						"<div class='radiobuttons'><input style='margin-top:15px;' type=\"radio\" name='start' id='start0"
								+ i + "' value='start0"
								+ i + "' checked><label style='border:1px solid black;' for='start0"
								+ i + "'> </label></div>" );
			else
				out.print(
						"<div class='radiobuttons'><input style='margin-top:15px;' type=\"radio\" name='start' id='start0"
								+ i + "' value='start0"
								+ i + "'><label style='border:1px solid black;' for='start0"
								+ i + "'> </label></div>" );

		out.print( "</div><div class'leftfromborder' style='margin-top:82px;float:left'>" );

		for ( int i = 0; i < 10; i++ )
			if ( m_EndNode.getX( ) == 0 && m_EndNode.getY( ) == i )
				out.print(
						"<div class='radiobuttons'><input style='margin-top:7px;' type=\"radio\" name='end' id='end0"
								+ i + "' value='end0"
								+ i + "' checked><label style='border:1px solid black;' for='end0"
								+ i + "'> </label></div>" );
			else
				out.print(
						"<div class='radiobuttons'><input style='margin-top:7px;' type=\"radio\" name='end' id='end0"
								+ i + "' value='end0"
								+ i + "'><label style='border:1px solid black;' for='end0"
								+ i + "'> </label></div>" );
		out.print( "</div>" );

		// Top
		out.print(
				"<div class='damitrechtsnochgeht' style='width:387px;float:left;'><div class='topfromborder' style='margin-left:5px;'>" );

		for ( int i = 0; i < 10; i++ )
			if ( m_StartNode.getX( ) == i && m_StartNode.getY( ) == 0 )
				out.print( "<input style='margin-left:4px;' type=\"radio\" name='start' id='start" + i
						+ "0' value='start" + i
						+ "0' checked><label style='border:1px solid black;' for='start" + i
						+ "0'> </label>" );
			else
				out.print( "<input style='margin-left:4px;' type=\"radio\" name='start' id='start" + i + "0' value='start" + i + "0'><label style='border:1px solid black;' for='start" + i + "0'> </label>" );
		out.print( "</div><div class='topfromborder' style='margin-left:5px;'>" );
		for ( int i = 0; i < 10; i++ )
			if ( m_EndNode.getX( ) == i && m_EndNode.getY( ) == 0 )
				out.print( "<input style='margin-left:4px;' type=\"radio\" name='end' id='end" + i + "0' value='end" + i + "0' checked><label style='border:1px solid black;' for='end" + i + "0'> </label>" );
			else
				out.print( "<input style='margin-left:4px;' type=\"radio\" name='end' id='end" + i + "0' value='end" + i + "0'><label style='border:1px solid black;' for='end" + i + "0'> </label>" );

		out.print( "</div>" );

		// Mid
		out.print(
				"<div class='labyrinth' style='margin-top:5px;margin-left:5px;border-width:150px;border:1px solid black;background:#ffa500;'>" );

		// Styling Checkboxes.
		out.print(
				"<style>input[type=checkbox] {display:none;}input[type=checkbox] + label {display:inline-block;border:1px solid blue;background-color: #ffa500;border-color: #ddd;width: 30px; height: 30px;}input[type=checkbox]:checked + label {background-image: none;background-color:#000080;}</style>" );
		m_aPath = m_Path.getPathArray( );
		for ( int i = 0; i < 10; i++ ) {
			out.print( "<div class='checkboxes' style='margin-top:0px;'>" );
			for ( int z = 0; z < 10; z++ ) {
				boolean bExists = false;
				if ( blockedNodes.contains( new Node( z, i ) ) ) {
					out.print( "<input type=\"checkbox\" name=\"block" + z + i + "\" id='block" + z + i
							+ "' checked><label style='border:4px solid black;' for='block" + z + i + "'> </label>" );
				} else if ( m_aPath.contains( new Node( z, i ) ) ) {
					out.print( "<input type=\"checkbox\" name=\"block" + z + i + "\" id='block" + z + i
							+ "'><label style='border:4px solid lime;' for='block" + z + i + "'> </label>" );
				} else {
					out.print( "<input type=\"checkbox\" name=\"block" + z + i + "\" id='block" + z + i
							+ "'><label style='border:4px solid black;' for='block" + z + i + "'> </label>" );
				}
			}
			out.print( "</div>" );
		}
		out.print( "</div>" );

		// Bottom
		out.print( "<div class='botfromborder' style='margin-left:5px;margin-top:5px;'>" );
		for ( int i = 0; i < 10; i++ )
			if ( m_EndNode.getX( ) == i && m_EndNode.getY( ) == 9 )
				out.print( "<input style='margin-left:4px;' type=\"radio\" name='end' id='end" + i + "9' value='end" + i + "9' checked><label style='border:1px solid black;' for='end" + i + "9'> </label>" );
			else
				out.print( "<input style='margin-left:4px;' type=\"radio\" name='end' id='end" + i + "9' value='end" + i + "9'><label style='border:1px solid black;' for='end" + i + "9'> </label>" );
		out.print( "</div><div class='botfromborder' style='margin-left:5px;'>" );
		for ( int i = 0; i < 10; i++ )
			if ( m_StartNode.getX( ) == i && m_StartNode.getY( ) == 9 )
				out.print(
						"<input style='margin-left:4px;' type=\"radio\" name='start' id='start" + i + "9' value='start" + i + "9' checked><label style='border:1px solid black;' for='start" + i + "9'> </label>" );
			else
				out.print( "<input style='margin-left:4px;' type=\"radio\" name='start' id='start" + i + "9' value='start" + i + "9'><label style='border:1px solid black;' for='start" + i + "9'> </label>" );
		out.print( "</div></div>" );

		// Right
		out.print( "<div class'rightfromborder' style='margin-top:82px;float:left;margin-left:5px;'>" );
		for ( int i = 0; i < 10; i++ )
			if ( m_EndNode.getX( ) == 9 && m_EndNode.getY( ) == i )
				out.print(
						"<div class='radiobuttons'><input style='margin-top:7px;' type=\"radio\" name='end' id='end9"
								+ i + "' value='end9"
								+ i + "' checked><label style='border:1px solid black;' for='end9"
								+ i + "'> </label></div>" );
			else
				out.print(
						"<div class='radiobuttons'><input style='margin-top:7px;' type=\"radio\" name='end' id='end9"
								+ i + "' value='end9"
								+ i + "'><label style='border:1px solid black;' for='end9"
								+ i + "'> </label></div>" );

		out.print( "</div><div class'rightfromborder' style='margin-top:82px;float:left;'>" );

		for ( int i = 0; i < 10; i++ )
			if ( m_StartNode.getX( ) == 9 && m_StartNode.getY( ) == i )
				out.print(
						"<div class='radiobuttons'><input style='margin-top:7px;' type=\"radio\" name='start' id='start9"
								+ i + "' value='start9"
								+ i + "' checked><label style='border:1px solid black;' for='start9"
								+ i + "'> </label></div>" );
			else
				out.print(
						"<div class='radiobuttons'><input style='margin-top:7px;' type=\"radio\" name='start' id='start9"
								+ i + "' value='start9"
								+ i + "'><label style='border:1px solid black;' for='start9"
								+ i + "'> </label></div>" );
		out.print( "</div>" );

		out.print( "<input style='font-size:36px;margin-top:20px;margin-right:55px;float:right;' type='submit' name='submit' value='Weg berechnen'>" );
		out.print( "</form>" );

		out.print( "</div>" );

		// ******************************************************************

		try {
			initVars( blockedNodes );
		} catch ( Exception ex ) {
			// Error
			out.println( "Fehler bei initVars: " + ex );
		}

		// Print out the table with all important variables.
		try {
			printTable( out );
		} catch ( Exception ex ) {
			out.println( "Fehler bei doMysql: " + ex );
		}
	}

	/**
	 * Clears a table with the passed Name in the database.
	 * 
	 * @param strTable
	 *            Defines the table which should be cleared.
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected static void clearTable( String strTable )
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName( "com.mysql.jdbc.Driver" ).newInstance( );
		Connection con = null;
		con = DriverManager.getConnection( "jdbc:mysql://localhost:7188/lab1", "root", "" );
		Statement stmt = con.createStatement( );
		String strStatement = "DELETE FROM " + strTable;
		stmt.executeUpdate( strStatement );
		con.close( );
	}

	/**
	 * Writes every passed Node into the blocked nodes database.
	 * 
	 * @param blockedNodes
	 *            Defines a List of all the blocked Nodes.
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected static void setBlockedNode( List < Node > blockedNodes )
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName( "com.mysql.jdbc.Driver" ).newInstance( );
		Connection con = null;
		con = DriverManager.getConnection( "jdbc:mysql://localhost:7188/lab1", "root", "" );
		Statement stmt = con.createStatement( );
		String strStatement = "";
		for ( int i = 0; i < blockedNodes.size( ); i++ ) {
			strStatement = "INSERT INTO blockednodes (x, y) VALUES (" + blockedNodes.get( i ).getX( ) + ", "
					+ blockedNodes.get( i ).getY( ) + ")";
			stmt.executeUpdate( strStatement );
		}
		con.close( );
	}

	/**
	 * Reads every blocked node out of the blocked node database.
	 * 
	 * @return blockedNodes Defines every node which is blocked.
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected static List < Node > getBlockedNodes( )
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName( "com.mysql.jdbc.Driver" ).newInstance( );
		Connection con = null;
		con = DriverManager.getConnection( "jdbc:mysql://localhost:7188/lab1", "root", "" );
		Statement stmt = con.createStatement( );
		ResultSet rs = stmt.executeQuery( "SELECT * FROM blockednodes" );
		List < Node > blockedNodes = new ArrayList < Node >( );
		while ( rs.next( ) ) {
			Node n = new Node( rs.getInt( 1 ), rs.getInt( 2 ) );
			blockedNodes.add( n );
		}
		con.close( );
		return blockedNodes;
	}

	/**
	 * Writes every important variable which has to be saved in the ui database.
	 * 
	 * @param dimX
	 *            Defines the width of the field.
	 * @param dimY
	 *            Defines the height of the field.
	 * @param startNode
	 *            Defines the starting Node.
	 * @param endNode
	 *            Defines the ending Node.
	 * @param pathSize
	 *            Defines the size of the Path.
	 * @param path
	 *            Defines the Path as String.
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected static void setMysql( int dimX, int dimY, Node startNode, Node endNode, int pathSize, String path )
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName( "com.mysql.jdbc.Driver" ).newInstance( );
		Connection con = null;
		con = DriverManager.getConnection( "jdbc:mysql://localhost:7188/lab1", "root", "" );
		Statement stmt = con.createStatement( );
		String strStatement = "";
		strStatement = "INSERT INTO ui ( id, dimX, dimY, startX, startY, endX, endY, pathSize, path) VALUES ( 1, "
				+ dimX + ", " + dimY + ", " + startNode.getX( ) + ", " + startNode.getY( ) + ", " + endNode.getX( )
				+ ", " + endNode.getY( ) + ", " + pathSize + ", '" + path + "')";
		stmt.executeUpdate( strStatement );
		con.close( );
	}

	/**
	 * Initializes every member variable of the Servlet and reads them out of
	 * the ui database.
	 * 
	 * @param blockedNodes
	 *            Defines the blocked Nodes to initialize the labyrinth.
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected void initVars( List < Node > blockedNodes )
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName( "com.mysql.jdbc.Driver" ).newInstance( );
		Connection con = null;
		con = DriverManager.getConnection( "jdbc:mysql://localhost:7188/lab1", "root", "" );
		Statement stmt = con.createStatement( );
		ResultSet rs = stmt.executeQuery( "SELECT * FROM ui" );
		boolean bExists = false;
		while ( rs.next( ) ) {
			bExists = true;
			m_iDimX = rs.getInt( 2 );
			m_iDimY = rs.getInt( 3 );
			m_StartNode = new Node( rs.getInt( 4 ), rs.getInt( 5 ) );
			m_EndNode = new Node( rs.getInt( 6 ), rs.getInt( 7 ) );
			m_Path = new Path( m_iDimX, m_iDimY, m_StartNode, m_EndNode, blockedNodes );
			m_strPath = m_Path.getPath( );
			m_iPathSize = m_Path.getPathSize( );
			m_aPath = m_Path.getPathArray( );
		}
		if ( !bExists ) {
			m_iDimX = 10;
			m_iDimY = 10;
			m_StartNode = new Node( 0, 0 );
			m_EndNode = new Node( 0, 0 );
			m_Path = new Path( m_iDimX, m_iDimY, m_StartNode, m_EndNode, blockedNodes );
			m_strPath = m_Path.getPath( );
			m_iPathSize = m_Path.getPathSize( );
			m_aPath = m_Path.getPathArray( );
		}
		con.close( );
	}

	/**
	 * Prints the ui database styled as a table.
	 * 
	 * @param out
	 *            Defines the response.PrintWriter.
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected static void printTable( PrintWriter out )
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName( "com.mysql.jdbc.Driver" ).newInstance( );
		Connection con = null;
		con = DriverManager.getConnection( "jdbc:mysql://localhost:7188/lab1", "root", "" );
		Statement stmt = con.createStatement( );
		ResultSet rs = stmt.executeQuery( "SELECT * FROM ui" );
		out.println( "" );
		out.println(
				"<table style=\"font-size: 14pt;margin-left:auto;margin-right:auto;\"><tr><td style=\"background-color:#C9C8C8\">ID</td><td style=\"background-color:#C9C8C8\">Height</td><td style=\"background-color:#C9C8C8\">Width</td><td style=\"background-color:#C9C8C8\">StartX</td><td style=\"background-color:#C9C8C8\">StartY</td><td style=\"background-color:#C9C8C8\">EndX</td><td style=\"background-color:#C9C8C8\">EndY</td><td style=\"background-color:#C9C8C8\">PathSize</td><td style=\"background-color:#C9C8C8\">Path</td></tr>" );
		int iIndex = 0;
		while ( rs.next( ) ) {
			String strColor = ( iIndex % 2 == 0 ) ? "#E1E1E1" : "#F4f3f3";
			out.println( "<tr style=\"background-color: " + strColor + ";\">" );
			out.println( "<td>" + rs.getString( 1 ) + "</td>" );
			out.println( "<td>" + rs.getString( 2 ) + "</td>" );
			out.println( "<td>" + rs.getString( 3 ) + "</td>" );
			out.println( "<td>" + rs.getString( 4 ) + "</td>" );
			out.println( "<td>" + rs.getString( 5 ) + "</td>" );
			out.println( "<td>" + rs.getString( 6 ) + "</td>" );
			out.println( "<td>" + rs.getString( 7 ) + "</td>" );
			out.println( "<td>" + rs.getString( 8 ) + "</td>" );
			out.println( "<td>" + rs.getString( 9 ) + "</td>" );
			out.println( "</tr>" );
			iIndex++;
		}
		con.close( );
	}

	/**
	 * Destroys (exit) the Servlet.
	 */
	public void destroy( ) {
		// do nothing.
	}
}