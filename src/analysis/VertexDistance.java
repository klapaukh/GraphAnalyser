package analysis;

import java.util.ArrayList;
import java.util.List;

import main.Graph;

public class VertexDistance extends Analysis {

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
		StringBuilder temp = new StringBuilder();
		temp.append("\"c(");
		boolean first = true;
		for (Double d : lengths) {
			if (!first) {
				temp.append(',');
			}
			temp.append(d);
			first = false;
		}
		temp.append(')');
		temp.append('"');
		return temp.toString();
	}

}
