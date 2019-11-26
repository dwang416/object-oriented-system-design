import junit.framework.TestCase;

public class ShapeTest extends TestCase {
	public void test1 () {
		String a = "0 0 0 1 1 1 1 0";
		String b = "10 10 10 11 11 11 11 10";
		String c = "0.5 0.5 0.5 -10 1.5 0";
		String d = "0.5 0.5 0.75 0.75 0.75 0.2";
		
		Shape shapeA = new Shape(a);
		Shape shapeB = new Shape(b);
		Shape shapeC = new Shape(c);
		Shape shapeD = new Shape(d);
		
		assertEquals(false, shapeA.crosses(shapeB));
		assertEquals(true, shapeA.crosses(shapeC));
		assertEquals(false, shapeA.crosses(shapeD));
		
		assertEquals(0, shapeA.encircles(shapeB));
		assertEquals(1, shapeA.encircles(shapeC));
		assertEquals(2, shapeA.encircles(shapeD));
	}
}
