package analysis.lists;

import java.util.ArrayList;
import java.util.List;

import main.Graph;
import main.Pair;
import main.Point;

public class EdgeLength extends ListAnalysis {

	public String toString() {
		return "Edge Length";
	}

	@Override
	public String value(Graph g) {
		List<Double> lengths = new ArrayList<>();

		for (Pair<Point, Point> e : g.edgeIterator()) {
			lengths.add(e.x.distanceTo(e.y));
		}
		return toRVector(lengths);
	}

}
