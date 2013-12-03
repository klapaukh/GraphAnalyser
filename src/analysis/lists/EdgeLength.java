package analysis.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Graph;
import main.Pair;
import main.Point;

public class EdgeLength extends ListAnalysis {
	private final boolean reportFull;

	public EdgeLength(boolean reportFull) {
		this.reportFull = reportFull;
	}

	public String toString() {
		if (reportFull) {
			return "Edge Length";
		}
		return "Mean Edge Length,Median Edge Length,Max Edge Length,Min Edge Length";
	}

	@Override
	public String value(Graph g) {
		List<Double> lengths = new ArrayList<>();

		for (Pair<Point, Point> e : g.edgeIterator()) {
			lengths.add(e.x.distanceTo(e.y));
		}

		if (reportFull) {
			return toRVector(lengths);
		}

		Collections.sort(lengths);
		return String.format("%.4f,%.4f,%.4f,%.4f",mean(lengths),median(lengths),lengths.get(lengths.size()-1),lengths.get(0));
	}

}
