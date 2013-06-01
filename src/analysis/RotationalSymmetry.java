package analysis;

import java.util.ArrayList;
import java.util.List;

import main.Graph;

public class RotationalSymmetry extends Symmetry {

	public RotationalSymmetry(double sigma_scale, double sigma_distance,
			boolean useDistanceWeighting, int numCenters, int angleMerge,
			int pixelMerge, int xMin, int yMin) {
		super(sigma_scale, useDistanceWeighting, sigma_distance, numCenters,
				angleMerge, pixelMerge, xMin, yMin);
	}

	@Override
	public String value(Graph g) {
		List<SIFTFeature> edges = createFeatures(g);
		// Now that we have the features, we need to find axes
		List<Vote> votes = new ArrayList<>();
		for (int i = 0; i < edges.size(); i++) {
			SIFTFeature f1 = edges.get(i);
			for (int j = i + 1; j < edges.size(); j++) {
				SIFTFeature f2 = edges.get(j);
				if (f2.type == f1.type) {
					double s = scaleFactor(f1, f2);
					double d = distanceFactor(f1, f2);

					//Not sure this is quite right
					double gamma = f1.angleTo(f2);
					double dist = f1.distanceTo(f2)/2;
					double beta = (f1.theta - f2.theta + Math.PI)/2.0;
					double r = dist/Math.cos(beta);

					double x = f1.p.x() + r * Math.cos(beta + gamma);
					double y = f1.p.y() + r * Math.sin(beta + gamma);
					
					double vote = s * d;

					votes.add(new Vote(x, y, vote, f1.node1, f1.node2, f2.node1, f2.node2));
				}
			}
		}

		List<Point> axis = findMaxima(votes);

		// Drawing would be good here

		double score = computeScore(g, axis, votes);
		return String.format("%.4f", score);
	}

	public String toString() {
		return "Rotational Symmetry";
	}
}
