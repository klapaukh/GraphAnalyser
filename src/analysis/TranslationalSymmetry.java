package analysis;

import java.util.List;

import main.Graph;

public class TranslationalSymmetry extends Symmetry {

	public TranslationalSymmetry(double sigma_scale, double sigma_distance,
			boolean useDistanceWeighting, int numShifts, int angleMerge,
			int pixelMerge, int xMin, int yMin) {
		super(sigma_scale, useDistanceWeighting, sigma_distance,numShifts,angleMerge,pixelMerge, xMin, yMin);
	}
		
	@Override
	public String value(Graph g) {
		List<SIFTFeature> edges = createFeatures(g);
		return "x";
	}
	
	public String toString() {
		return "Translational Symmetry";
	}
}
