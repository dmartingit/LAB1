package testpackage;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import pathfinding.Path;
import pathfinding.Node;

public class PathTests {
	@Test
	public void testGetDimX( ) {
		// Initializations.
		Path p1;
		int erg = 5;

		// Methods.
		p1 = new Path( 5, 5, new Node( 0, 0 ), new Node( 1, 0 ), new ArrayList < Node >( ) );

		// Tests.
		assertEquals( erg, p1.getDimX( ) );
	}

	@Test
	public void testGetDimY( ) {
		// Initializations.
		Path p1;
		int erg = 5;

		// Methods.
		p1 = new Path( 5, 5, new Node( 0, 0 ), new Node( 1, 0 ), new ArrayList < Node >( ) );

		// Tests.
		assertEquals( erg, p1.getDimY( ) );
	}

	@Test
	public void testGetStartNode( ) {
		// Initializations.
		Path p1;
		Node erg = new Node( 0, 0 );

		// Methods.
		p1 = new Path( 5, 5, new Node( 0, 0 ), new Node( 1, 0 ), new ArrayList < Node >( ) );

		// Tests.
		assertEquals( erg, p1.getStartNode( ) );
	}

	@Test
	public void testGetEndNode( ) {
		// Initializations.
		Path p1;
		Node erg = new Node( 1, 0 );

		// Methods.
		p1 = new Path( 5, 5, new Node( 0, 0 ), new Node( 1, 0 ), new ArrayList < Node >( ) );

		// Tests.
		assertEquals( erg, p1.getEndNode( ) );
	}

	@Test
	public void testGetPathSize( ) {
		// Initializations.
		Path p1;
		int erg = 1;

		// Methods.
		p1 = new Path( 5, 5, new Node( 0, 0 ), new Node( 1, 0 ), new ArrayList < Node >( ) );

		// Tests.
		assertEquals( erg, p1.getPathSize( ) );
	}

	@Test
	public void testGetInitialLab( ) {
		// Initializations.
		Path p1;
		String erg = "Grid: " + "\nS  0  0  0  0  " + "\nE  0  0  0  0  " + "\n0  0  0  0  0  " + "\n0  0  0  0  0  "
				+ "\n0  0  0  0  0  " + "\n\n";

		// Methods.
		p1 = new Path( 5, 5, new Node( 0, 0 ), new Node( 1, 0 ), new ArrayList < Node >( ) );

		// Tests.
		assertEquals( erg, p1.getInitialLab( ) );
	}

	@Test
	public void testGetCostLab( ) {
		// Initializations.
		Path p1;
		String erg = "\nCosts for nodes: " + "\nS  2  3  4  5  " + "\nE  1  2  3  4  " + "\n1  2  3  4  5  "
				+ "\n2  3  4  5  6  " + "\n3  4  5  6  7  " + "\n\n";

		// Methods.
		p1 = new Path( 5, 5, new Node( 0, 0 ), new Node( 1, 0 ), new ArrayList < Node >( ) );

		// Tests.
		assertEquals( erg, p1.getCostLab( ) );
	}

	@Test
	public void testGetPath( ) {
		// Initializations.
		Path p1;
		String erg = "[1, 0] -> [0, 0]\n";

		// Methods.
		p1 = new Path( 5, 5, new Node( 0, 0 ), new Node( 1, 0 ), new ArrayList < Node >( ) );

		// Tests.
		assertEquals( erg, p1.getPath( ) );
	}

	@Test
	public void testGetPathArray( ) {
		// Initializations.
		Path p1;
		List < Node > erg = new ArrayList < Node >( );
		erg.add( new Node( 1, 0 ) );
		erg.add( new Node( 0, 0 ) );

		// Methods.
		p1 = new Path( 5, 5, new Node( 0, 0 ), new Node( 1, 0 ), new ArrayList < Node >( ) );

		// Tests.
		assertEquals( erg, p1.getPathArray( ) );
	}
}
