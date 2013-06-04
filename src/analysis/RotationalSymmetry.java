package analysis;

import java.io.FileNotFoundException;
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

					double beta = (Math.PI - (f2.theta - f1.theta))/2.0;

					double r = dist/Math.cos(beta);
//					double r = (dist*Math.sqrt(1+ Math.pow(Math.tan(beta),2)));

					double epsilon = 2*Math.PI - (beta + gamma + Math.PI/2);
					double x = f1.p.x() - r * Math.sin(epsilon);
					double y = f1.p.y() - r * Math.cos(epsilon);

					double vote = s * d;

					votes.add(new Vote(x, y, vote, f1.node1, f1.node2, f2.node1, f2.node2));
				}
			}
		}

		List<Point> axis = findMaxima(votes);

		try {
			Image.drawSVG(g, axis, votes, "rotation.svg", xMin, yMin, Image.ROTATION);
		} catch (FileNotFoundException e) {
			// Don't really care too much if this fails
			e.printStackTrace();
		}

		double score = computeScore(g, axis, votes);
		return String.format("%.4f", score);
	}

	public String toString() {
		return "Rotational Symmetry";
	}
}
