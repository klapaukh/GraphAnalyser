package analysis;

import main.Graph;

public class AverageVertexDistance extends Analysis {

	public String toString() {
		return "Average Vertex Distance";
	}


	public String value(Graph g) {
		return String.format("%.4f", computeMean(computeVertexDistances(g)));
	}
}