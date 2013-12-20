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

import java.util.ArrayList;
import java.util.List;

import main.Graph;
import main.Point;

public class DeviationFromIdealAngle extends Analysis {

	public String toString() {
		return "Angle Deviation From Ideal";
	}

	public String value(Graph g) {
		double d = 0;
		double count = 0;
		for (int i = 0; i < g.numNodes(); i++) {
			Point p = g.getNode(i);
			List<Point> destinations = new ArrayList<>();
			for (int j = 0; j < g.numNodes(); j++) {
				if (i != j && g.isEdge(i, j)) {
					destinations.add(new Point(g.getNode(j)));
				}
			}
			count += destinations.size();
			if (destinations.size() > 1) {
				double idealAngle = 2.0 * Math.PI / destinations.size();
				// So now we need to find the angles between all *adjacent* pairs!
				for (int x = 0; x < destinations.size(); x++) {
					// For each angle,
					// Find the lowest angle going right (that is your next partner)
					double minTheta = Double.MAX_VALUE;
					for (int y = 0; y < destinations.size(); y++) {
						if (x == y) {
							// It can't be you!
							continue;
						}
						Point p2 = destinations.get(x);
						Point p3 = destinations.get(y);

						Point a = p2.minus(p);
						Point b = p3.minus(p);

						double theta = Math.abs(a.angleToOtherFromX(b));
						minTheta = Math.min(theta, minTheta);
					}
					d += Math.abs((idealAngle - minTheta) / idealAngle);
				}
			} else {
				d += 0;
			}
		}
		if (count != 0) {
			d /= count;
			d = 1 - d;
		} else {
			d = 1;
		}

		return String.format("%.4f", d);
	}

}