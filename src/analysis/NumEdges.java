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

package analysis;

import main.Graph;
import main.Pair;
import main.Point;

public class NumEdges extends Analysis {

	public String toString() {
		return "Num Edges";
	}

	@Override
	public String value(Graph g) {
		int numEdges = 0;

		for (@SuppressWarnings("unused")
		Pair<Point, Point> p : g.edgeIterator()) {
			numEdges += 1;
		}

		return String.format("%d", numEdges);
	}

}
