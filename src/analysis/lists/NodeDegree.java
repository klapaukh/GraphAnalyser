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
