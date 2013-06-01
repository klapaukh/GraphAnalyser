package analysis;

public class Vote {
	public final double y, x, vote;
	public final int i, j, i2, j2; // voting edges

	public Vote(double x, double y, double vote, int i, int j, int i2, int j2) {
		this.x = x;
		this.y = y;
		this.vote = vote;
		this.i = i;
		this.j = j;
		this.i2 = i2;
		this.j2 = j2;
	}

}