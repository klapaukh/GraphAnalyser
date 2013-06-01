package analysis;

public class Vote {
	public final double rad, theta, vote;
	public final int i, j, i2, j2; // voting edges

	public Vote(double radius, double theta, double vote, int i, int j, int i2, int j2) {
		this.rad = radius;
		this.theta = theta;
		this.vote = vote;
		this.i = i;
		this.j = j;
		this.i2 = i2;
		this.j2 = j2;
	}

}