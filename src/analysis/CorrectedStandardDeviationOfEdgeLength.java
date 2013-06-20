package analysis;

import main.Graph;

public class CorrectedStandardDeviationOfEdgeLength extends Analysis {
	public String toString() {
		return "Corrected Standard Deviation of Edge Length";
	}

	public String value(Graph g) {
		return String.format("%.4f", computeCorrectedStandardDeviation(computeEdgeLengths(g)));
	}
}