import java.util.*;

/*
 Shape data for ShapeClient:
 "0 0  0 1  1 1  1 0"
 "10 10  10 11  11 11  11 10"
 "0.5 0.5  0.5 -10  1.5 0"
 "0.5 0.5  0.75 0.75  0.75 0.2"
*/

public class Shape {
	private ArrayList<Point> data = new ArrayList<>();
	private Point center;
	// radius of center of the circle defined by shape
	private double radius = Double.MAX_VALUE;	
	
	/**
	 * constructor
	 * create a shape from a text String which lists the points that 
	 * make up the shape, as an even number of double values "x y", 
	 * all separated by one or more whitespace chars. Here is the 
	 * String for a square with its lower left corner at (0, 0):
	 * "0 0 0 1 1 1 1.0 0.0"
	 * @param str
	 */
	public Shape (String str) {
		String[] coordinates = str.split("\\s+");
		if (coordinates.length == 0) {
			System.out.println("Illegal input given!");
			System.exit(1);
		}
		if (coordinates.length % 2 == 1) {
			System.out.println("Odd number of coordinates given!");
			System.exit(1);
		}
		double x, y;
		double sumX = 0.0, sumY = 0.0;
		for (int i = 1; i < coordinates.length; i += 2) {
			x = Double.parseDouble(coordinates[i-1]);
			y = Double.parseDouble(coordinates[i]);
			sumX += x;
			sumY += y;
			data.add(new Point(x, y));
		}
		
		double centerX = sumX / (coordinates.length / 2);
		double centerY = sumY / (coordinates.length / 2);
		center = new Point(centerX, centerY);
		
		for (Point p: data) radius = Math.min(radius, center.distance(p));
	}
	
	/**
	 * A "crosses" B if A has a line which crosses the B circle -- 
	 * one endpoint of the line is within the circle and the other 
	 * endpoint is not. A point which is on the edge of the circle 
	 * is counted as being in the circle. For this case, ignore the 
	 * case where the line crosses the circle, but both points are outside of it 
	 * @param other: another Shape object
	 * @return true if this shape crosses the other
	 */
	public boolean crosses(Shape other) {
		Point otherCenter = other.getCenter();
		double otherRadius = other.getRadius();
		for (int i = 0; i < getPointCount()-1; i++) {
			Point a1 = data.get(i);
			Point a2 = data.get(i+1);
			boolean a1InOther = a1.distance(otherCenter) <= otherRadius;
			boolean a2InOther = a2.distance(otherCenter) <= otherRadius;
			if (a1InOther ^ a2InOther) {
				return true;	// XOR operation
			}
		}
		return false;
	}
	
	/**
	 * compute how A "encircles" B in one of three ways, encoded as an int
	 * @param other: another Shape object
	 * @return 2 if the center of B's circle is within A's circle
	 * 		   1 if otherwise, B's circle intersects A's circle
	 * 		   0 if otherwise, the two circles have no intersection 
	 */
	public int encircles(Shape other) {
		Point otherCenter = other.getCenter();
		double otherRadius = other.getRadius();
		if (center.distance(otherCenter) <= radius) {
			return 2;
		} else if (center.distance(otherCenter) <= radius + otherRadius) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public Point getCenter() {
		return center;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public int getPointCount() {
		return data.size();
	}
}

