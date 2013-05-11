
public class Point {
	private final double x;
	private final double y;

	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}

	public Point(Point o){
		this(o.x,o.y);
	}

	public double distanceTo(Point o){
		final double dx = o.x - this.x;
		final double dy = o.y - this.y;
		return Math.sqrt(dx * dx + dy*dy);
	}

}
