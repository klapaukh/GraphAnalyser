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

public class MirrorSymmetry extends Symmetry {

	public MirrorSymmetry(double sigma_scale, double sigma_distance, boolean useDistanceWeighting, int numMirrors, int angleMerge, int pixelMerge,
			int xMin, int yMin) {
		super(sigma_scale, useDistanceWeighting, sigma_distance, numMirrors, angleMerge, pixelMerge, xMin, yMin);
	}

	public String toString() {
		return "Mirror Symmetry";
	}

	public String value(Graph g) {
		List<SIFTFeature> edges = createFeatures(g);

		List<Vote> votes = new ArrayList<>();

		// Now that we have the features, we need to find axes
		// between pairs of edges
		for (int i = 0; i < edges.size(); i++) {
			SIFTFeature f1 = edges.get(i);
			for (int j = i + 1; j < edges.size(); j++) {
				SIFTFeature f2 = edges.get(j);
				if (f2.type == f1.type) {
					double phi = rotFactor(f1, f2);
					double s = scaleFactor(f1, f2);
					double d = distanceFactor(f1, f2);

					double thetaij = f1.angleToForwards(f2);
					Point mid = f1.midPointTo(f2);

					double rij = mid.x() * Math.cos(thetaij) + mid.y() * Math.sin(thetaij);

					double vote = phi * s * d;

					votes.add(cannonicalVote(Math.toDegrees(thetaij), rij, vote, f1.node1, f1.node2, f2.node1, f2.node2));
				}
			}

			Point mid = g.getNode(f1.node1).midPointTo(g.getNode(f1.node2));
			double vote = 1;
			double thetaij = g.getNode(f1.node1).angleToOtherFromXForward(g.getNode(f1.node2));

			// And perpendular bisector

			double rij = mid.x() * Math.cos(thetaij + Math.PI /2 ) + mid.y() * Math.sin(thetaij+ Math.PI /2);
			votes.add(cannonicalVote(Math.toDegrees(thetaij+ Math.PI / 2), rij, vote, f1.node1, f1.node2, f1.node1, f1.node2));


			//parallel to the edge
			rij = mid.x() * Math.cos(thetaij) + mid.y() * Math.sin(thetaij);
			votes.add(cannonicalVote(Math.toDegrees(thetaij), rij, vote, f1.node1, f1.node2, f1.node1, f1.node2));

		}

		List<Point> axis = findMaxima(votes);

//		 try {
//		 Image.drawSVG(g, axis, votes, "mirror.svg", xMin, yMin, Image.MIRROR);
//		 } catch (FileNotFoundException e) {
//		 // Don't really care too much if this fails
//		 e.printStackTrace();
//		 }

		double score = computeScore(g, axis, votes);
		return String.format("%.4f", score);
	}

	private double rotFactor(SIFTFeature f1, SIFTFeature f2) {
		double thetaij = f1.angleToForwards(f2);
		return Math.abs(Math.cos(f1.theta + f2.theta - 2 * thetaij));
	}

	private Vote cannonicalVote(double degrees, double radius, double vote, int n1, int n2, int n3, int n4){
		while(degrees < 0){
			degrees += 360;
		}
		if(radius < 0){
			radius *=-1;
			degrees += 180;
			while(degrees >= 360){
				degrees -= 360;
			}
		}
		return new Vote(degrees,radius, vote,n1,n2,n3,n4);
	}

}
