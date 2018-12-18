/**
 * @author Matt Kent
 */
public class LineEquation {

	private final double slope;
	private final double b;

	public LineEquation(final Point p1, final Point p2) {
		slope = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
		b = p1.getY() - slope * p1.getX();
	}

	public double getY(final double x) {
		return slope * x + b;
	}

	@Override
	public String toString() {
		return "y = " + slope + "x + " + b;
	}

}
