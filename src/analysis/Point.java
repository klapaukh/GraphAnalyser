package analysis;

/**
 * This class represents a simple immutable 2D point.
 * It contains some basic geometric methods needed for the various kinds
 * of graph analysis that are preformed
 *
 * @author Roma Klapaukh
 *
 */
public class Point {
	private final double x;
	private final double y;

	/**
	 * Make a point from 2 doubles
	 * @param x The x value
	 * @param y The y value
	 */
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Make a new point pointing in a given direction with unit length
	 *
	 * @param theta Angle from x axis
	 */
	public Point(double theta){
		x = Math.cos(theta);
		y = Math.sin(theta);
	}

	/**
	 * Make a point that has the same value as another point
	 *
	 * @param o The point to copy
	 */
	public Point(Point o){
		this(o.x,o.y);
	}

	/**
	 * Computes the euclidean distance between this point and ther other
	 * @param o Point to compute distance to
	 * @return Euclidean distance to point O from this
	 */
	public double distanceTo(Point o){
		final double dx = o.x - this.x;
		final double dy = o.y - this.y;
		return Math.sqrt(dx * dx + dy*dy);
	}

	/**
	 * Makes this point represent a vector going up. If it is going backwards
	 * it reverses it. This doesn't make sense for most points, but only does
	 * when the point represents a direction that is the same forwards and
	 * backwards.
	 *
	 * @return Either the same point, or this -this point.
	 */
	public Point forceForwards(){
		if(Double.compare(y,0) > 0 || (Double.compare(y, 0) == 0 && Double.compare(x,0) >= 0)){
			return this;
		}
		return new Point(-x,-y);
	}

	/**
	 * Get the x value
	 * @return x co-ordinate
	 */
	public double x(){
		return this.x;
	}

	/**
	 * Get the y value
	 * @return y co-ordinate
	 */
	public double y(){
		return this.y;
	}

	/**
	 * Vector subtraction. Returns a new point representing this - o.
	 *
	 * @param o Point to subtract from this.
	 * @return
	 */
	public Point minus(Point o){
		return new Point(this.x - o.x, this.y - o.y);
	}

	/**
	 * Computes the angle from this point to another.
	 * The angle is measured from the x axis.
	 * It returns a value in the range [0, Math.PI].
	 * If this value from this to the other point is
	 * outside that range, it will return the angle from
	 * it to this point.
	 *
	 * @param o The other point
	 * @return The angle between points from the x axis
	 */
	public double angleToOtherFromXForward(Point o){
		return this.minus(o).forceForwards().angleToX();
	}

	public double angleToOtherFromX(Point o){
		return this.minus(o).angleToX();
	}

	/**
	 * Returns the angle between this point (treated as
	 * a vector direction) and the x axis.
	 *
	 * @return Angle to x axis
	 */
	public double angleToX(){
		//use the dot product with [1,0]
		double dot = x;
		double length = length();
		if(Double.compare(length, 0) == 0){
			return 0;
		}
		double norm = dot / length;
		return Math.acos(norm);
	}

	/**
	 * Returns the norm of this vector. It is the distance
	 * from the origin to here.
	 *
	 * @return Vector norm
	 */
	public double length(){
		return Math.sqrt(x*x + y*y);
	}

	/**
	 * Find the point exactly in this middle between this and o.
	 *
	 * @param o Other point to find center between
	 * @return A point that is the center from this to o
	 */
	public Point midPointTo(Point o){
		return new Point((this.x + o.x)/2, (this.y + o.y)/2);
	}

	/**
	 * Returns the same vector as this point but with unit length.
	 *
	 * @return The same direction with unit length
	 */
	public Point normalise(){
		double length = this.length();
		return new Point(this.x/ length, this.y / length);
	}

	public Point rotate(double theta){
		double nx = x*Math.cos(theta) - y * Math.sin(theta);
		double ny = x*Math.sin(theta) + y * Math.cos(theta);

		return new Point(nx, ny);
	}

	/**
	 * Add two points
	 * @param o The point to add
	 * @return New point giving the vector sum
	 */
	public Point plus(Point o){
		return new Point (this.x + o.x , this.y + o.y);
	}

	public String toString(){
		return String.format("(%.2f, %.2f)", x, y);
	}
}
