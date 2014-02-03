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

package analysis.symmetry;

import java.util.ArrayList;
import java.util.List;

import main.Graph;
import main.Point;

public class RotationalSymmetry extends Symmetry {

	public final boolean debug = false;

	public RotationalSymmetry(double sigma_scale, double sigma_distance, boolean useDistanceWeighting, int numCenters, int angleMerge,
			int pixelMerge, int xMin, int yMin) {
		super(sigma_scale, useDistanceWeighting, sigma_distance, numCenters, angleMerge, pixelMerge, xMin, yMin);
	}

	@Override
	public String value(Graph g) {
		List<SIFTFeature> edges = createFeatures(g);
		// Now that we have the features, we need to find axes
		List<Vote> votes = new ArrayList<>();
		for (int i = 0; i < edges.size(); i++) {
			SIFTFeature f1 = edges.get(i);
			for (int j = i + 1; j < edges.size(); j++) {
				SIFTFeature f2 = edges.get(j);
				if (f2.type == f1.type) {

					// Step one: compute the vote magnitude
					double s = scaleFactor(f1, f2);
					double d = distanceFactor(f1, f2);
					double vote = s * d;

					// Find the Line the centers must live on.
					// The line goes through the middle of the two points
					Point offset = f1.midPointTo(f2);

					if (debug) {
						if (Math.abs(offset.distanceTo(f1.p) - offset.distanceTo(f2.p)) > 0.001) {
							throw new RuntimeException("Offset is wrong!!");
						}
					}

					Point a = new Point(1, 0);
					Point b = f2.p.minus(f1.p);
					double angle = Math.atan2(a.x() * b.y() - a.y() * b.x(), a.x() * b.x() + a.y() * b.y()) + Math.PI / 2;

					Point testDir = new Point(angle);

					if (debug) {
						System.out.printf("%s @ %.2f between %.2f -- %.2f\n", testDir.toString(), Math.toDegrees(angle), Math.toDegrees(f1.theta),
								Math.toDegrees(f2.theta));

						// DEBUGGING
						double dx1 = testDir.x();
						double dy1 = testDir.y();
						double dr1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);

						if (Math.abs(1 - dr1) > 0.0001) {
							throw new RuntimeException("Should be a unit vector!!");
						}
					}

					Point testPoint1 = testDir.plus(offset);

					Point direction = testDir;

					if (Math.abs(testPoint1.distanceTo(f1.p) - testPoint1.distanceTo(f2.p)) > 0.001) {
						throw new RuntimeException("Not actually the bisector!");
					}

					// This is always fixed
					double dist = f1.distanceTo(f2) / 2;

					// Pick one of the angles
					double angleDiff = Math.abs(f2.theta - f1.theta);
					double alpha = angleDiff / 2.0;

					// Compute the radius
					double r = dist / Math.tan(alpha);

					if (Double.compare(Math.abs(r), 0.001) < 0 || Double.compare(Math.abs(alpha), 0.5) < 0 || Double.compare(Math.abs(alpha-Math.PI),0.05) < 0) {
						votes.add(new Vote(offset.x(), offset.y(), vote, f1.node1, f1.node2, f2.node1, f2.node2));
						continue;
					}

					if (debug) {
						System.out.printf("Radius: %.2f\n", r);
					}

					// It goes one of two ways -- forward or back
					double x1 = r * direction.x();
					double y1 = r * direction.y();

					double x2 = -r * direction.x();
					double y2 = -r * direction.y();

					// So this gives us two possible centers
					Point center1 = new Point(offset.x() + x1, offset.y() + y1);
					Point center2 = new Point(offset.x() + x2, offset.y() + y2);

					// one of these centers won't work with respect to the actual positions.

					double theta1 = f2.theta - f1.theta;

					if (debug) {
						System.out.printf("%.2f\n", Math.toDegrees(theta1));
					}

					Point test1 = f1.p.minus(center1).rotate(theta1).plus(center1);
					Point test2 = f1.p.minus(center2).rotate(theta1).plus(center2);

					Point c = null;
					int factor = 0;
					if (test1.distanceTo(f2.p) < 0.0001) {
						c = center1;
						factor = -1;
					}
					if (test2.distanceTo(f2.p) < 0.0001) {
						if (c != null) {
							throw new RuntimeException("Both Centers work!");
						}
						c = center2;
						factor = 1;
					}
					if (c == null) {
						throw new RuntimeException("Didn't find a center!!");
					}

					// System.out.printf("vote: (%.2f, %.2f)\n", c.x(), c.y());
					votes.add(new Vote(c.x(), c.y(), vote, f1.node1, f1.node2, f2.node1, f2.node2));

					if (r > 0) {
						double angleDiff2 = Math.PI - angleDiff;
						double alpha2 = angleDiff2 / 2.0;
						double r2 = dist / Math.tan(alpha2);
						// There is a second vote!

						double x = factor * r2 * direction.x();
						double y = factor * r2 * direction.y();

						center1 = new Point(offset.x() + x, offset.y() + y);

						// Point center2 = new Point(offset.x() + x2, offset.y() + y2);
						theta1 = f2.theta - f1.theta - Math.PI;

						test1 = f1.p.minus(center1).rotate(-theta1).plus(center1);
						test2 = f1.p.minus(center1).rotate(theta1).plus(center1);

						if (test1.distanceTo(f2.p) > 0.0001 && test2.distanceTo(f2.p) > 0.0001) {
							throw new RuntimeException("Second center sucks " + center1 + "  " + test1.distanceTo(f2.p) + " "
									+ test2.distanceTo(f2.p));
						}

						// System.out.printf("vote: (%.2f, %.2f)\n", offset.x() + x, offset.y() + y);
						votes.add(new Vote(offset.x() + x, offset.y() + y, vote, f1.node1, f1.node2, f2.node1, f2.node2));
					}
				}
			}

			//Add an extra centre of rotation that is just the centre of the
			//edge allowing it to rotate around itself.
			Point mid = g.getNode(f1.node1).midPointTo(g.getNode(f1.node2));
			votes.add(new Vote(mid.x(), mid.y(), 1,f1.node1, f1.node2,f1.node1,f1.node2));


		}

		List<Point> axis = findMaxima(votes);

//		try {
//			Image.drawSVG(g, axis, votes, "rotation.svg", xMin, yMin, Image.ROTATION);
//		} catch (FileNotFoundException e) {
//			// Don't really care too much if this fails
//			e.printStackTrace();
//		}

		double score = computeScore(g, axis, votes);
		return String.format("%.4f", score);
	}

	public String toString() {
		return "Rotational Symmetry";
	}

	public double sgn(double x) {
		return x < 0 ? -1 : 1;
	}

}
