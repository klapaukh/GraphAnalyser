package analysis.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Graph;

public class NodeDegree extends ListAnalysis {
	private final boolean reportFull;

	public NodeDegree(boolean reportFull) {
		this.reportFull = reportFull;
	}

	public String toString() {
		if (reportFull) {
			return "Node Degree";
		}
		return "Mean Node Degree,Median Node Degree,Max Node Degree,Min Node Degree";
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
		Collections.sort(degree);
		if(reportFull){
			return toRVector(degree);
		}
		return String.format("%.4f,%.4f,%d,%d",mean(degree),median(degree), degree.get(degree.size()-1),degree.get(0));
	}

}
