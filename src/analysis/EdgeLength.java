package analysis;

import java.util.ArrayList;
import java.util.List;

import main.Graph;

public class EdgeLength extends Analysis {

	public String toString() {
		return "Edge Length";
	}

	@Override
	public String value(Graph g) {
		List<Double> lengths = new ArrayList<>();

		for (Pair<Point, Point> e : g.edgeIterator()) {
			lengths.add(e.x.distanceTo(e.y));
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
