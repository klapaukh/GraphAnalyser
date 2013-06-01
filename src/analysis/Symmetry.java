package analysis;

import java.util.ArrayList;
import java.util.List;

import main.Graph;


public abstract class Symmetry implements Analysis {
	private final double sigma_scale;
	private final boolean distanceBound;
	private final double sigma_distance;
	
	private final int numFeatures;
	private final int xMerge;
	private final int yMerge;
	private final int xMin = 10;
	private final int yMin = 10;
	
	public Symmetry(double sigma_scale, boolean distanceBound, double sigma_distance, int numFeatures, int xMerge, int yMerge){
		this.sigma_scale = sigma_scale;
		this.distanceBound = distanceBound;
		this.sigma_distance = sigma_distance;
		this.numFeatures = numFeatures;
		this.xMerge = xMerge;
		this.yMerge = yMerge;
	}
	
	protected double scaleFactor(SIFTFeature f1, SIFTFeature f2) {
		double top = -Math.abs(f1.scale - f2.scale);
		double bottom = sigma_scale * (f1.scale + f2.scale);
		double exponent = top / bottom;
		double val = Math.exp(exponent);
		return val * val;
	}

	protected double distanceFactor(SIFTFeature f1, SIFTFeature f2) {
		if (!distanceBound) {
			return 1;
		}
		double d = f1.distanceTo(f2);
		double exp = -(d * d) / (2 * sigma_distance * sigma_distance);
		return Math.exp(exp);
	}
	
	protected List<SIFTFeature> createFeatures(Graph g){
		List<SIFTFeature> edges = new ArrayList<>();

		// Make all the edges into features
		// Each feature is the center of the edge
		for (int i = 0; i < g.numNodes(); i++) {
			for (int j = i + 1; j < g.numNodes(); j++) {
				if (g.isEdge(i, j)) {
					Point n1 = g.getNode(i);
					Point n2 = g.getNode(j);
					double x = (n1.x() + n2.x()) / 2;
					double y = (n1.y() + n2.y()) / 2;
					double length = n1.distanceTo(n2);
					double theta = n1.angleToOtherFromX(n2);
					edges.add(new SIFTFeature(x, y, theta, length, 1, i, j));
				}
			}
		}
		
		return edges;
	}
	
	protected List<Point> findMaxima( List<Vote> votes){
		double maxx = 0;
		double maxy = 0;
		double minx = Double.MAX_VALUE;
		double miny = Double.MAX_VALUE;

		for (Vote v : votes) {
			maxx = Math.max(maxx, v.theta);
			maxy = Math.max(maxy, v.rad);
			miny = Math.min(miny, v.rad);
			minx = Math.min(minx, v.theta);
		}
		
		double[][] voteSpace = new double[(int) (maxx - minx) + 1][(int) (maxy - miny) + 1];
		for (Vote v : votes) {
			voteSpace[(int) (v.theta - minx)][(int) (v.rad - miny)] += v.vote;
		}
		
		double[][] voteRedSpace = Image.sampleDown(voteSpace, xMerge, yMerge);
		
		Image.draw(voteRedSpace, "ex.pbm");
		
		return Image.findMax(voteRedSpace, minx, miny, yMin/yMerge, xMin/xMerge, numFeatures, xMerge, yMerge);
	}
	
	protected double computeScore(Graph g, List<Point> axis, List<Vote> votes){
		// compute score
		
		// \sigma numEdges matched
		// ----------------------
		// numEdges * numFeatures
		
		int numEdgesMatched = 0;
		int numEdges = 0;
		for (int i = 0; i < g.numNodes(); i++) {
			for (int j = i + 1; j < g.numNodes(); j++) {
				if (g.isEdge(i, j)) {
					numEdges++;
					// What was my vote
					for (Vote v : votes) {
						if ((v.i == i && v.j == j) || (v.i2 == i && v.j2 == j)) {
							for (Point p : axis) {
								if (Math.abs(p.x() - v.theta) < xMin
										&& Math.abs(p.y() - v.rad) < yMin) {
									numEdgesMatched++;
								}
							}
						}
					}
				}
			}
		}
		return numEdgesMatched / (double) (axis.size() * numEdges);
	}
}
