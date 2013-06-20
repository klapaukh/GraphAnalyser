package analysis;

import main.Graph;

public class CorrectedStandardDeviationOfVertexDistance extends Analysis {
	public String toString() {
		return "Corrected Standard Deviation of Vertex Distance";
	}

	public String value(Graph g) {
		return String.format("%.4f", computeCorrectedStandardDeviation(computeVertexDistances(g)));
	}
}