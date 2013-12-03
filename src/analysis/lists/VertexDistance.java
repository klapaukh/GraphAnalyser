package analysis.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Graph;

public class VertexDistance extends ListAnalysis {

	private final boolean reportFull;

	public VertexDistance(boolean reportFull){
		this.reportFull = reportFull;
	}

	public String toString() {
		if(reportFull){
			return "Vertex Distance";
		}
		return "Mean Vertex Distance,Median Vertex Distance,Max Vertex Distance,Min Vertex Distance";
	}

	@Override
	public String value(Graph g) {
		List<Double> lengths = new ArrayList<>();
		for (int i = 0; i < g.numNodes(); i++) {
			for (int j = i + 1; j < g.numNodes(); j++) {
				lengths.add(g.distanceBetween(i, j));
			}
		}
		Collections.sort(lengths);
		if(reportFull){
			return String.format("%.4f",toRVector(lengths));
		}
		return String.format("%.4f,%.4f,%.4f,%.4f",mean(lengths),median(lengths),lengths.get(lengths.size()-1),lengths.get(0));
	}

}
