package analysis.lists;

import java.util.ArrayList;
import java.util.List;

import main.Graph;

public class NodeDegree extends ListAnalysis {

	public String toString() {
		return "Node Degree";
	}

	@Override
	public String value(Graph g) {
		List<Integer> degree = new ArrayList<>();
		for (int i = 0; i < g.numNodes(); i++) {
			int count = 0;
			for (int j = 0; j < g.numNodes(); j++) {
				count += g.isEdge(i, j) ? 1 : 0;
			}
			degree.add(count);
		}

		return toRVector(degree);
	}

}
