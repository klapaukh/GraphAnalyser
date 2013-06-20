package analysis;

import main.Graph;

public class GraphName extends Analysis {
	public String toString() {
		return "Input File";
	}

	public String value(Graph g) {
		return g.graphName();
	}
}