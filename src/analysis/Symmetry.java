package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Graph;

/**
 * This class is the commmon computations needed to implement the different
 * symmetry algorithms. Pretty much takes care of the Hough space voting stuff.
 *
 * @author Roma Klapaukh
 *
 */
public abstract class Symmetry implements Analysis {
	private final double sigma_scale;
	private final boolean distanceBound;
	private final double sigma_distance;

	private final int numFeatures;

	private final int xMerge;
	private final int yMerge;
	protected final int xMin;
	protected final int yMin;

	/**
	 * Constructor for Symmerty
	 *
	 * @param sigma_scale Parameter for scale similarity
	 * @param distanceBound Should distance similarity be used
	 * @param sigma_distance Parameter for distance similarity
	 * @param numFeatures Maximum number of features to return
	 * @param xMerge X cells to merge to allow for more error
	 * @param yMerge Y cells to merge to allow for more error
	 * @param xMin Min x distance to be different
	 * @param yMin Min y distance to be different
	 */
	public Symmetry(double sigma_scale, boolean distanceBound, double sigma_distance, int numFeatures, int xMerge, int yMerge, int xMin, int yMin){
		this.sigma_scale = sigma_scale;
		this.distanceBound = distanceBound;
		this.sigma_distance = sigma_distance;
		this.numFeatures = numFeatures;
		this.xMerge = xMerge;
		this.yMerge = yMerge;
		this.xMin = xMin;
		this.yMin = yMin;
	}

	/**
	 * This computes the similarity in scale between two SIFTFeatures (edges).
	 *
	 * @param f1 Edge 1
	 * @param f2 Edge 2
	 * @return How similar they are in [0,1]
	 */
	protected double scaleFactor(SIFTFeature f1, SIFTFeature f2) {
		double top = -Math.abs(f1.scale - f2.scale);
		double bottom = sigma_scale * (f1.scale + f2.scale);
		double exponent = top / bottom;
		double val = Math.exp(exponent);
		return val * val;
	}

	/**
	 * Returns a measure of how close the edges are, or 1 if distanceBound
	 * was set to false.
	 *
	 * @param f1 Edge 1
	 * @param f2 Edge 2
	 * @return Distance measure in [0,1] or 1 if !distanceBound
	 */
	protected double distanceFactor(SIFTFeature f1, SIFTFeature f2) {
		if (!distanceBound) {
			return 1;
		}
		double d = f1.distanceTo(f2);
		double exp = -(d * d) / (2 * sigma_distance * sigma_distance);
		return Math.exp(exp);
	}

	/**
	 * Turn each edge in the graph into a single SIFTFeature
	 *
	 * @param g Graph to get edges from
	 * @return List of all edges in the graph
	 */
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
					double theta = n1.angleToOtherFromXForward(n2);
					edges.add(new SIFTFeature(x, y, theta, length, 1, i, j));
				}
			}
		}

		return edges;
	}

	/**
	 * Find the best votes. Uses the settings from the constructor to determine tolerances
	 *
	 * @param votes List of all the votes
	 * @return The maximal points
	 */
	protected List<Point> findMaxima( List<Vote> votes){
		if(votes.isEmpty()){
			return new ArrayList<Point>();
		}


		//Need to make the structure sparse!
		Map<Pair<Integer,Integer>, Double> voteSpace = new HashMap<>();
		for (Vote v : votes) {
			int x = (int) (v.x/xMerge);
			int y = (int) (v.y/yMerge);

			x*=xMerge;
			y*=yMerge;

			Pair<Integer,Integer> point = new Pair<>(x,y);
			Double soFar = voteSpace.get(point);
			if(soFar== null){
				voteSpace.put(point, v.vote);
			}else{
				voteSpace.put(point, soFar + v.vote);
			}
		}
		return Image.findMax(voteSpace, xMin, yMin, numFeatures);
	}

	/**
	 * Given a set of best points, the graph, and the votes find the score.
	 * This is given as the sum of matching edges for each feature / (total edges * featuresGiven)
	 *
	 * @param g The graph
	 * @param features List of features to score
	 * @param votes The original votes
	 * @return A goodness value in [0,1]
	 */
	protected double computeScore(Graph g, List<Point> features, List<Vote> votes){
		if(features.isEmpty()){
			return 1;
		}
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
							for (Point p : features) {
								if (Math.abs(p.x() - v.x) < xMin
										&& Math.abs(p.y() - v.y) < yMin) {
									numEdgesMatched++;
								}
							}
						}
					}
				}
			}
		}
		return numEdgesMatched / (double) (features.size() * numEdges);
	}
}
