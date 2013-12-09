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
import main.Point;

public class EdgeCrossings extends Analysis {

	public String toString() {
		return "Edge Crossings";
	}

	public String value(Graph g) {
//		System.out.println();
		int count = 0;
		for (int i = 0; i < g.numNodes(); i++) {
			for (int j = i + 1; j < g.numNodes(); j++) {
				if (g.isEdge(i, j)) {
					for (int k = i + 1; k < g.numNodes(); k++) {
						for (int l = k + 1; l < g.numNodes(); l++) {
							if (g.isEdge(k, l)) {
								if (k == i || l == i || k == j || l == j) {
									// Two edges going out from i
									// Change the names so the graph looks like |a| -- |b| -- |c|
									int a, b, c;
									if (k == i) {
										a = j;
										b = i;
										c = l;
									} else if (k == j) {
										a = i;
										b = j;
										c = l;
									} else if (l == i) {
										a = j;
										b = i;
										c = k;
									} else if (l == j) {
										a = i;
										b = j;
										c = k;
									} else {
										throw new RuntimeException("Doubled up node not found");
									}

									// They overlap if they have the same gradient going out of b!
									Point pa = g.getNode(a);
									Point pb = g.getNode(b);
									Point pc = g.getNode(c);

									double gradba = (pa.y() - pb.y()) / (pa.x() - pb.x());
									double gradbc = (pc.y() - pb.y()) / (pc.x() - pb.x());

									if (Double.compare(gradba, gradbc) == 0) {
//										System.out.printf("%d -- %d -- %d", a,b,c);
										count += 1;
									}
								} else {
									// Disconnected
									Point p1 = g.getNode(i);
									Point p2 = g.getNode(j);

									Point p3 = g.getNode(k);
									Point p4 = g.getNode(l);

									/*
									 * [p4-3.x p1-2.x][t] = [p1-3.x] [p4-3.y p1-2.y][s] = [p1-3.y]
									 */
									// Check if matrix is solvable
									double a = p4.x() - p3.x();
									double b = p1.x() - p2.x();
									double c = p4.y() - p3.y();
									double d = p1.y() - p2.y();

									double e = p1.x() - p3.x();
									double f = p1.y() - p3.y();

									double det = a * d - b * c;

									if (Double.compare(det, 0) != 0) {
										double s = (d * e - b * f) / det;
										double t = (-c * e + a * f) / det;

										if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
											count += 1;
//											System.out.printf("%d -- %d && %d -- %d\n", i,j,k,l);
										}
									} else {
										// Det (Matrix) = 0
										// Their are either infinite solutions or none

										// check for none -- one line never becomes the other
										double gx = a;
										double gy = c;

										// /gx and gy can only be zero if the edge has zero length
										if (Double.compare(gx, 0) == 0 && Double.compare(gy, 0) == 0) {
											// There can't really be an edge crossing, as this edge has 0 length
											continue;
										} else if (Double.compare(p1.x() / gx, p1.y() / gy) == 0) {
											// They might overlap
											double tStart = p1.x() / gx;
											double tEnd = p2.x() / gx;

											if (tStart >= 0 && tStart <= 1) {
												count += 1;
												System.out.printf("%d -- %d && %d -- %d\n", i,j,k,l);
											} else if (tEnd >= 0 && tEnd <= 1) {
												count += 1;
												System.out.printf("%d -- %d && %d -- %d\n", i,j,k,l);
											} else if (Math.min(tEnd, tStart) <= 0 && Math.max(tEnd, tStart) >= 1) {
											}
										}// else { They never overlap }
									}
								}
							}
						}
					}
				}
			}
		}
		return String.format("%d", count);
	}
}
