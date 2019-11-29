import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4, pyr5;
	private Piece l11, l12, l13, l14, l15;
	private Piece s, sRotated;
	private Piece stick, stickFast;
	private Piece square;

	protected void setUp() throws Exception {
		super.setUp();
		Piece[] pieces = Piece.getPieces();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		pyr5 = pyr4.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		l11 = pieces[1];
		l12 = l11.getNext();
		l13 = l12.getNext();
		l14 = l13.getNext();
		l15 = l14.getNext();
		

		stick = new Piece(Piece.STICK_STR);
		stickFast = pieces[0];
		
		square = pieces[5];
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		assertEquals(1, stick.getWidth());
		assertEquals(4, stick.getHeight());

		assertEquals(1, stickFast.getWidth());
		assertEquals(4, stickFast.getHeight());
		assertEquals(4, stickFast.getNext().getWidth());
		assertEquals(1, stickFast.getNext().getHeight());
		
		assertEquals(2, l11.getWidth());
		assertEquals(3, l11.getHeight());
		assertEquals(3, l12.getWidth());
		assertEquals(2, l12.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}
	
	public void testRotationCircle() {
		assertEquals(l11, l15);
		assertEquals(pyr1, pyr5);
		assertEquals(square, square.getNext());
	}
	
}
