
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
	
	public Point forceForwards(){
		if(Double.compare(y,0) > 0 || (Double.compare(y, 0) == 0 && Double.compare(x,0) >= 0)){
			return this;
		}
		return new Point(-x,-y);
	}
	
	public double x(){
		return this.x;
	}
	
	public double y(){
		return this.y;
	}
	
	public Point minus(Point o){
		return new Point(this.x - o.x, this.y - o.y);
	}
	
	public double angleToOtherFromX(Point o){
		return this.minus(o).forceForwards().angleToX();
	}
	
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
	
	public double length(){
		return Math.sqrt(x*x + y*y);
	}

}
