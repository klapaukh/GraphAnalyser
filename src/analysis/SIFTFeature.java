package analysis;


public class SIFTFeature {

	public final Point p;
	public final double theta, scale;
	public final int type;
	public final int node1, node2;

	public SIFTFeature(double x, double y, double theta, double scale, int type, int node1, int node2) {
		this.p = new Point(x, y);
		this.theta = theta;
		this.scale = scale;
		this.type = type;
		this.node1 = node1;
		this.node2 = node2;
	}

	public double angleToForwards(SIFTFeature other) {
		return this.p.angleToOtherFromXForward(other.p);
	}

	public double angleTo(SIFTFeature other) {
		return this.p.angleToOtherFromX(other.p);
	}
	
	public double distanceTo(SIFTFeature other) {
		return this.p.distanceTo(other.p);
	}

	public Point midPointTo(SIFTFeature other) {
		return this.p.midPointTo(other.p);
	}


}
