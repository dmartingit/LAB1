package pathfinding;
/**
 * Managing the Node list and costs.
 * @author s72724, s72589, s72761
 *
 */
public class Node {
	private int m_iCost = 0, m_iFinalCost = 0, m_iX, m_iY;
	private Node m_ParentNode = null;

	/**
	 * Constructor which initiate the Node with the Coordinates x: 0, y: 0.
	 */
	public Node( ) {
		this( 0, 0 );
	}
	
	/**
	 * Constructor which initiate the Node with the Coordinates of the passed parameters.
	 * @param x X-Coordinate
	 * @param y Y-Coordinate
	 */
	public Node( int x, int y ) {
		this.m_iX = x;
		this.m_iY = y;
	}
	
	/**
	 * Set the Node costs to the passed parameter.
	 * @param cost mCost
	 */
	public void setCost( int cost ) {
		this.m_iCost = cost;
	}
	
	/**
	 * Returns the current Node costs.
	 * @return m_iCost
	 */
	public int getCost( ) {
		return this.m_iCost;
	}
	
	/**
	 * Set the FinalCost of the Node to the passed parameter.
	 * @param finalcost mFinalCost
	 */
	public void setFinalCost( int finalcost ) {
		this.m_iFinalCost = finalcost;
	}
	
	/**
	 * Returns the FinalCost of the Node.
	 * @return m_iFinalCost
	 */
	public int getFinalCost( ) {
		return this.m_iFinalCost;
	}

	
	/**
	 * Set the X-Coordinate of the Node.
	 * @param x X-Coordinate
	 */
	public void setX( int x ) {
		this.m_iX = x;
	}

	/**
	 * Returns the X-Coordinate of the Node.
	 * @return m_iX
	 */
	public int getX( ) {
		return this.m_iX;
	}

	/**
	 * Set the Y-Coordinate of the Node.
	 * @param y Y-Coordinate
	 */
	public void setY( int y ) {
		this.m_iY = y;
	}

	/**
	 * Returns the Y-Coordinate of the Node.
	 * @return m_iY
	 */
	public int getY( ) {
		return this.m_iY;
	}
	
	/**
	 * Set parent object of the Node.
	 * @param parentNode m_ParentNode
	 */
	public void setParentNode( Node parentNode ) {
		this.m_ParentNode = parentNode;
	}
	
	/**
	 * Returns the parent object of the Node.
	 * @return m_ParentNode
	 */
	public Node getParentNode( ) {
		return this.m_ParentNode;
	}
	
	/**
	 * Returns the X, Y-Coordinate as String.
	 * @return xyString
	 */
	public String toString( ) {
		return "["+this.m_iX+", "+this.m_iY+"]";
	}
	
	/**
	 * Overrides the equals operator, so that we can later compare Nodes e.g. with List.contains(node).
	 */
	@Override
	public boolean equals(Object object) {
		boolean theSame = false;
		if (object != null && object instanceof Node) {
			theSame = ( this.getX( ) == ((Node) object).getX( ) ) && ( this.getY( ) == ((Node) object).getY( ) );
		}
		return theSame;
	}
}
