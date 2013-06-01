package analysis;

import java.util.List;

import main.Graph;

public class RotationalSymmetry extends Symmetry {

	public RotationalSymmetry(double sigma_scale, double sigma_distance,
			boolean useDistanceWeighting, int numCenters, int angleMerge,
			int pixelMerge, int xMin, int yMin) {
		super(sigma_scale, useDistanceWeighting, sigma_distance,numCenters,angleMerge,pixelMerge, xMin, yMin);
	}
	
	@Override
	public String value(Graph g) {
		List<SIFTFeature> edges = createFeatures(g);
		return "x";
	}

	public String toString() {
		return "Rotational Symmetry";
	}
}
