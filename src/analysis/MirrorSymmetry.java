package analysis;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import main.Graph;

public class MirrorSymmetry extends Symmetry {


	private final int numMirrors;
	private final int angleMerge;
	private final int pixelsMerge;

	public MirrorSymmetry(double sigma_scale, double sigma_distance,
			boolean useDistanceWeighting, int numMirrors, int angleMerge,
			int pixelMerge) {
		super(sigma_scale, useDistanceWeighting, sigma_distance);
		
		this.numMirrors = numMirrors;
		this.angleMerge = angleMerge;
		this.pixelsMerge = pixelMerge;
	}

	public String toString() {
		return "Mirror Symmetry";
	}

	public String value(Graph g) {
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

		// Now that we have the features, we need to find axes
		List<Vote> votes = new ArrayList<>();
		for (int i = 0; i < edges.size(); i++) {
			SIFTFeature f1 = edges.get(i);
			for (int j = i + 1; j < edges.size(); j++) {
				SIFTFeature f2 = edges.get(j);
				if (f2.type == f1.type) {
					double phi = rotFactor(f1, f2);
					double s = scaleFactor(f1, f2);
					double d = distanceFactor(f1, f2);

					double thetaij = f1.angleTo(f2);
					Point mid = f1.midPointTo(f2);

					double rij = mid.x() * Math.cos(thetaij) + mid.y()
							* Math.sin(thetaij);

					double vote = phi * s * d;
					if (vote > 2) {
						System.out.println(vote);
					}

					votes.add(new Vote(rij, Math.toDegrees(thetaij), vote,
							f1.node1, f1.node2, f2.node1, f2.node2));
				}
			}
		}

		double maxang = 0;
		double maxrad = 0;
		double minang = Double.MAX_VALUE;
		double minrad = Double.MAX_VALUE;

		for (Vote v : votes) {
			maxang = Math.max(maxang, v.theta);
			maxrad = Math.max(maxrad, v.rad);
			minrad = Math.min(minrad, v.rad);
			minang = Math.min(minang, v.theta);
		}

		// System.out.println("\n ("+minang + "," + maxang+") deg : (" +
		// minrad+","+minrad+")");

		double[][] voteSpace = new double[(int) (maxang - minang) + 1][(int) (maxrad - minrad) + 1];
		for (Vote v : votes) {
			voteSpace[(int) (v.theta - minang)][(int) (v.rad - minrad)] += v.vote;
		}

		// voteSpace = Image.gaussianBlur(voteSpace, sigma_gauss);
		int numDeg = angleMerge, numPix = pixelsMerge;
		double[][] redVoteSpace = Image.sampleDown(voteSpace, numDeg, numPix);

		Image.draw(redVoteSpace, "ex.pbm");

		// System.out.println("\n ("+minang + "," + maxang+") deg : (" +
		// minrad+","+maxrad+")");
		List<Point> axis = Image.findMax(redVoteSpace, minang, minrad,
				10 / numDeg, 10 / numPix, numMirrors, numDeg, numPix);

		try {
			Image.drawWithMirrorLines(g, axis, votes, "temp.svg");
		} catch (FileNotFoundException e) {
			// Don't really care too much if this fails
			e.printStackTrace();
		}

		// compute score
		// \sigma numEdges mirrored
		// ----------------------
		// numEdges * numMirrors
		int numEdgesMirorred = 0;
		int numEdges = 0;
		for (int i = 0; i < g.numNodes(); i++) {
			for (int j = i + 1; j < g.numNodes(); j++) {
				if (g.isEdge(i, j)) {
					numEdges++;
					// What was my vote
					for (Vote v : votes) {
						if ((v.i == i && v.j == j) || (v.i2 == i && v.j2 == j)) {
							for (Point p : axis) {
								if (Math.abs(p.x() - v.theta) < 10
										&& Math.abs(p.y() - v.rad) < 15) {
									numEdgesMirorred++;
								}
							}
						}
					}
				}
			}
		}
		double score = numEdgesMirorred / (double) (axis.size() * numEdges);
		return String.format("%.4f", score);
	}

	private double rotFactor(SIFTFeature f1, SIFTFeature f2) {
		double thetaij = f1.angleTo(f2);
		return Math.abs(Math.cos(f1.theta + f2.theta - 2 * thetaij));
	}



}
