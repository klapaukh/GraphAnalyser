/*
 * Java Graph Analyser
 *
 * Copyright (C) 2013  Roman Klapaukh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
