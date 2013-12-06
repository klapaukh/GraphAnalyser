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
