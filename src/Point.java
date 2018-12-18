import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Matt Kent
 */
public class Point {

	private final double x;
	private final double y;
	private final int vert;

	public Point(final double x, final double y, final int vert) {
		this.x = x;
		this.y = y;
		this.vert = vert;
	}

	public int getVert() {
		return vert;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Point) {
			final Point that = (Point) o;
			return this.x == that.x && this.y == that.y && this.vert == that.vert;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
