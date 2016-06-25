package testpackage;

import static org.junit.Assert.*;
import org.junit.Test;
import pathfinding.Node;

public class NodeTests {
	@Test
	public void testSetCost( ) {
		// Initializations.
		Node n1 = new Node( 0, 0 );
		int erg = 1;

		// Methods.
		n1.setCost( 1 );

		// Tests.
		assertEquals( erg, n1.getCost( ) );
	}

	@Test
	public void testSetFinalCost( ) {
		// Initializations.
		Node n1 = new Node( 0, 0 );
		int erg = 1;

		// Methods.
		n1.setFinalCost( 1 );

		// Tests.
		assertEquals( erg, n1.getFinalCost( ) );
	}

	@Test
	public void testSetX( ) {
		// Initializations.
		Node n1 = new Node( 0, 0 );
		int erg = 1;

		// Methods.
		n1.setX( 1 );

		// Tests.
		assertEquals( erg, n1.getX( ) );
	}

	@Test
	public void testSetY( ) {
		// Initializations.
		Node n1 = new Node( 0, 0 );
		int erg = 1;

		// Methods.
		n1.setY( 1 );

		// Tests.
		assertEquals( erg, n1.getY( ) );
	}

	@Test
	public void testSetParentNode( ) {
		// Initializations.
		Node n1 = new Node( 0, 0 );
		Node n2 = new Node( 1, 1 );
		Node erg = n2;

		// Methods.
		n1.setParentNode( n2 );

		// Tests.
		assertEquals( erg, n1.getParentNode( ) );
	}
	
	@Test
	public void testToStringOperator( ) {
		// Initializations.
		Node n1 = new Node( 0, 0 );
		String erg = "[0, 0]";

		// Methods.

		// Tests.
		assertEquals( erg, n1.toString( ) );
	}

	@Test
	public void testEqualsOperator( ) {
		// Initializations.
		Node n1 = new Node( 0, 0 );
		Node n2 = new Node( 0, 0 );

		// Methods.

		// Tests.
		assertEquals( n1, n2 );
	}
}
