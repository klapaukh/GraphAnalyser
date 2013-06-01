package analysis;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import main.Graph;

public class MirrorSymmetry extends Symmetry {

	public MirrorSymmetry(double sigma_scale, double sigma_distance,
			boolean useDistanceWeighting, int numMirrors, int angleMerge,
			int pixelMerge) {
		super(sigma_scale, useDistanceWeighting, sigma_distance, numMirrors,
				angleMerge, pixelMerge);
	}

	public String toString() {
		return "Mirror Symmetry";
	}

	public String value(Graph g) {
		List<SIFTFeature> edges = createFeatures(g);

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

					votes.add(new Vote(rij, Math.toDegrees(thetaij), vote,
							f1.node1, f1.node2, f2.node1, f2.node2));
				}
			}
		}

		List<Point> axis = findMaxima(votes);

		try {
			Image.drawWithMirrorLines(g, axis, votes, "temp.svg");
		} catch (FileNotFoundException e) {
			// Don't really care too much if this fails
			e.printStackTrace();
		}

		double score = computeScore(g, axis, votes);
		return String.format("%.4f", score);
	}

	private double rotFactor(SIFTFeature f1, SIFTFeature f2) {
		double thetaij = f1.angleTo(f2);
		return Math.abs(Math.cos(f1.theta + f2.theta - 2 * thetaij));
	}

}
