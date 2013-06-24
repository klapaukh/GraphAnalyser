package analysis;

import main.Graph;

public class NumEdges extends Analysis {

	public String toString() {
		return "Num Edges";
	}

	@Override
	public String value(Graph g) {
		int numEdges = 0;

		for (@SuppressWarnings("unused")
		Pair<Point, Point> p : g.edgeIterator()) {
			numEdges += 1;
		}

		return String.format("%d", numEdges);
	}

}
