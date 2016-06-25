package pathfinding;

import java.util.*;

/**
 * Class Path is for calculating purposes.
 * 
 * @author s72724, s72589, sXXXXXX
 *
 */
public class Path {
	public static final int m_iDiagonalCost = 14;
	public static final int m_iHVCost = 10;

	private static Node[ ][ ] m_aGrid = new Node[ 10 ][ 10 ];
	private static PriorityQueue < Node > m_Openlist;
	private static boolean m_Closedlist[][];
	private static Node m_Start;
	private static Node m_End;
	private static int m_iPathSize = 0, m_iDimX = 0, m_iDimY = 0;
	private static String m_strInitField = "", m_strCostField = "", m_strPath = "";
	private static List < Node > m_aPath;

	/**
	 * Returns the X-Dimension of the Grid.
	 * 
	 * @return m_iDimX Represents the X-Dimension of the Grid.
	 */
	public int getDimX( ) {
		return m_iDimX;
	}

	/**
	 * Returns the Y-Dimension of the Grid.
	 * 
	 * @return m_iDimY Represents the Y-Dimension of the Grid.
	 */
	public int getDimY( ) {
		return m_iDimY;
	}

	/**
	 * Returns the starting Node.
	 * 
	 * @return m_Start Represents the starting Node of the Path.
	 */
	public Node getStartNode( ) {
		return m_Start;
	}

	/**
	 * Returns the ending Node.
	 * 
	 * @return m_End Represents the ending Node of the Path.
	 */
	public Node getEndNode( ) {
		return m_End;
	}

	/**
	 * Returns the length of the Path.
	 * 
	 * @return m_iPathSize Represents the length of the Path.
	 */
	public int getPathSize( ) {
		return m_iPathSize;
	}

	/**
	 * Returns the initial Field as String.
	 * 
	 * @return m_strInitField Represents the initial field as String.
	 */
	public String getInitialLab( ) {
		return m_strInitField;
	}

	/**
	 * Returns the cost Field as String.
	 * 
	 * @return m_strCostField Represents the Field with the Costs of the Nodes
	 *         as String.
	 */
	public String getCostLab( ) {
		return m_strCostField;
	}

	/**
	 * Returns the Path as String.
	 * 
	 * @return m_strPath Represents the Path as String.
	 */
	public String getPath( ) {
		return m_strPath;
	}

	/**
	 * Returns the Path as Array of Nodes.
	 * 
	 * @return m_aPath Represents the Path as an Array of Nodes.
	 */
	public List < Node > getPathArray( ) {
		return m_aPath;
	}

	/**
	 * Set the cost of the passed Node (testNode) to the passed cost and the
	 * parentNode to the passed Node (currentNode).
	 * 
	 * @param currentNode
	 *            Is passed to set the parentNode of the testNode
	 * @param testNode
	 *            The currently tested Node
	 * @param cost
	 *            The cost of the currently tested Node
	 */
	private static void getCost( Node currentNode, Node testNode, int cost ) {
		// Handle errors and returns if the cost of the Node was already
		// calculated.
		if ( testNode == null || m_Closedlist[ testNode.getX( ) ][ testNode.getY( ) ] )
			return;

		// Initiate an Integer with the cost of the current Node.
		int testNodeCost = testNode.getCost( ) + cost;

		// If the Node has currently no cost or the cost is lower than before,
		// the current Node will be set.
		boolean isOpened = m_Openlist.contains( testNode );
		if ( !isOpened || testNodeCost < testNode.getFinalCost( ) ) {
			testNode.setFinalCost( testNodeCost );
			testNode.setParentNode( currentNode );
			// Add the testNode to the mOpenList, so the Algorithm will continue
			// calculating the Path.
			if ( !isOpened )
				m_Openlist.add( testNode );
		}
	}

	/**
	 * Calculate the best Path from the start Node to the end Node.
	 */
	public static void getAStar( ) {
		// Add starting Node to the mOpenList.
		m_Openlist.add( m_aGrid[ m_Start.getX( ) ][ m_Start.getY( ) ] );
		Node currentNode;

		while ( true ) {
			// If mOpenList is null break and continue with the next one.
			currentNode = m_Openlist.poll( );
			if ( currentNode == null )
				break;

			// Add currentNode to the mClosedList so it costs will not get
			// calculated again.
			m_Closedlist[ currentNode.getX( ) ][ currentNode.getY( ) ] = true;

			// If we reached the end Node we are done.
			if ( currentNode.equals( m_aGrid[ m_End.getX( ) ][ m_End.getY( ) ] ) ) {
				return;
			}

			Node testNode;
			// Calculate the cost of the Node left next to it.
			if ( currentNode.getX( ) - 1 >= 0 ) {
				testNode = m_aGrid[ currentNode.getX( ) - 1 ][ currentNode.getY( ) ];
				getCost( currentNode, testNode, currentNode.getFinalCost( ) + m_iHVCost );

				// Diagonal Costs disabled, because it is not needed in the HTML
				// Labyrinth.

				/*
				 * if ( currentNode.getY( ) - 1 >= 0 ) { testNode = m_aGrid[
				 * currentNode.getX( ) - 1 ][ currentNode.getY( ) - 1 ];
				 * getCost( currentNode, testNode, currentNode.getFinalCost( ) +
				 * m_iDiagonalCost ); }
				 * 
				 * if ( currentNode.getY( ) + 1 < m_aGrid[ 0 ].length ) {
				 * testNode = m_aGrid[ currentNode.getX( ) - 1 ][
				 * currentNode.getY( ) + 1 ]; getCost( currentNode, testNode,
				 * currentNode.getFinalCost( ) + m_iDiagonalCost ); }
				 */

			}

			// Calculate the cost of the Node over it.
			if ( currentNode.getY( ) - 1 >= 0 ) {
				testNode = m_aGrid[ currentNode.getX( ) ][ currentNode.getY( ) - 1 ];
				getCost( currentNode, testNode, currentNode.getFinalCost( ) + m_iHVCost );
			}

			// Calculate the cost of the Node under it.
			if ( currentNode.getY( ) + 1 < m_aGrid[ 0 ].length ) {
				testNode = m_aGrid[ currentNode.getX( ) ][ currentNode.getY( ) + 1 ];
				getCost( currentNode, testNode, currentNode.getFinalCost( ) + m_iHVCost );
			}

			// Calculate the cost of the Node right next to it.
			if ( currentNode.getX( ) + 1 < m_aGrid.length ) {
				testNode = m_aGrid[ currentNode.getX( ) + 1 ][ currentNode.getY( ) ];
				getCost( currentNode, testNode, currentNode.getFinalCost( ) + m_iHVCost );

				// Diagonal Costs disabled, because it is not needed in the HTML
				// Labyrinth.

				/*
				 * if ( currentNode.getY( ) - 1 >= 0 ) { testNode = m_aGrid[
				 * currentNode.getX( ) + 1 ][ currentNode.getY( ) - 1 ];
				 * getCost( currentNode, testNode, currentNode.getFinalCost( ) +
				 * m_iDiagonalCost ); }
				 * 
				 * if ( currentNode.getY( ) + 1 < m_aGrid[ 0 ].length ) {
				 * testNode = m_aGrid[ currentNode.getX( ) + 1 ][
				 * currentNode.getY( ) + 1 ]; getCost( currentNode, testNode,
				 * currentNode.getFinalCost( ) + m_iDiagonalCost ); }
				 */

			}
		}
	}

	/**
	 * Initiates and calculates the Path.
	 * 
	 * @param dimX
	 *            X-Dimension
	 * @param dimY
	 *            Y-Dimension
	 * @param startNode
	 *            Starting Node
	 * @param endNode
	 *            Ending Node
	 * @param blockedNodes
	 *            Blocked Nodes
	 */
	public Path( int dimX, int dimY, Node startNode, Node endNode, List < Node > blockedNodes ) {
		// Clear everything.
		m_aGrid = new Node[ dimX ][ dimY ];
		m_Closedlist = new boolean[ dimX ][ dimY ];
		m_Openlist = new PriorityQueue<>( ( Node n1, Node n2 ) -> {
			// Returns -1 if the costs of Node1 are lower than the costs of
			// Node2
			// 0 if the costs of Node1 are higher than the costs of Node2
			// 1 if the costs of Node1 are equal as the costs of Node2.
			return n1.getFinalCost( ) < n2.getFinalCost( ) ? -1 : n1.getFinalCost( ) > n2.getFinalCost( ) ? 1 : 0;
		} );
		m_aPath = new ArrayList < Node >( );
		m_aPath.clear( );
		m_iPathSize = 0;
		m_strPath = "";
		m_strInitField = "";
		m_strCostField = "";

		m_iDimX = dimX;
		m_iDimY = dimY;

		// Set start position.
		m_Start = startNode;

		// Set End Location.
		m_End = endNode;

		// Initiate the mGrid (our 2D Labyrinth).
		for ( int i = 0; i < dimX; ++i ) {
			for ( int j = 0; j < dimY; ++j ) {
				m_aGrid[ i ][ j ] = new Node( i, j );
				// The cost of each Node is the distance from it to the end
				// Node, because it is the lowest possible cost.
				m_aGrid[ i ][ j ].setCost( Math.abs( i - m_End.getX( ) ) + Math.abs( j - m_End.getY( ) ) );
			}
		}

		// Override the cost of the start Node to 0.
		m_aGrid[ m_Start.getX( ) ][ m_Start.getY( ) ].setFinalCost( 0 );
		
		// If start/end node is blocked do not calculate a Path and exit.
		if (blockedNodes.contains( m_Start ) || blockedNodes.contains( m_End )) {
			m_strPath = "No possible Path found.";
			return;
		}

		// Set blocked cells to null.
		for ( int i = 0; i < blockedNodes.size( ); ++i ) {
			m_aGrid[ blockedNodes.get( i ).getX( ) ][ blockedNodes.get( i ).getY( ) ] = null;
		}

		// Display initial map.
		m_strInitField += "Grid: \n";
		for ( int i = 0; i < dimX; ++i ) {
			for ( int j = 0; j < dimY; ++j ) {
				if ( i == m_Start.getX( ) && j == m_Start.getY( ) )
					m_strInitField += "S  "; // Source
				else if ( i == m_End.getX( ) && j == m_End.getY( ) )
					m_strInitField += "E  "; // End
				else if ( m_aGrid[ i ][ j ] != null )
					m_strInitField += "0  ";
				else
					m_strInitField += "B  "; // Block
			}
			m_strInitField += "\n";
		}
		m_strInitField += "\n";

		// Calculate Path with the A* - Algorithm
		getAStar( );

		// Display finished map.
		m_strCostField += "\nCosts for nodes: \n";
		for ( int i = 0; i < dimX; ++i ) {
			for ( int j = 0; j < dimY; ++j ) {
				if ( i == m_Start.getX( ) && j == m_Start.getY( ) )
					m_strCostField += "S  "; // Source
				else if ( i == m_End.getX( ) && j == m_End.getY( ) )
					m_strCostField += "E  "; // End
				else if ( m_aGrid[ i ][ j ] != null )
					m_strCostField += m_aGrid[ i ][ j ].getCost( ) + "  "; // Cost
				else
					m_strCostField += "B  "; // Block
			}
			m_strCostField += "\n";
		}
		m_strCostField += "\n";

		// Display complete path started from the End.
		if ( m_Closedlist[ m_End.getX( ) ][ m_End.getY( ) ] ) {
			Node currentNode = m_aGrid[ m_End.getX( ) ][ m_End.getY( ) ];
			m_strPath += currentNode.toString( );
			m_aPath.add( currentNode );
			m_iPathSize++;
			while ( currentNode.getParentNode( ) != null ) {
				m_strPath += " -> " + currentNode.getParentNode( );
				m_aPath.add( currentNode.getParentNode( ) );
				m_iPathSize++;
				currentNode = currentNode.getParentNode( );
			}
			m_strPath += "\n";
		} else {
			m_strPath += "No possible path found.\n";
		}
	}
}