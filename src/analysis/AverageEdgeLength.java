package analysis;

import main.Graph;

public class AverageEdgeLength extends Analysis {
	public String toString() {
		return "Average Edge Length";
	}

	public String value(Graph g) {
		return String.format("%.4f", computeMean(computeEdgeLengths(g)));
	}
}