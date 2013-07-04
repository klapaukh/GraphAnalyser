package analysis.lists;

import java.util.ArrayList;
import java.util.List;

import main.Graph;

public class VertexDistance extends ListAnalysis {

	public String toString() {
		return "Vertex Distance";
	}

	@Override
	public String value(Graph g) {
		List<Double> lengths = new ArrayList<>();
		for (int i = 0; i < g.numNodes(); i++) {
			for (int j = i + 1; j < g.numNodes(); j++) {
				lengths.add(g.distanceBetween(i, j));
			}
		}
		return toRVector(lengths);
	}

}
