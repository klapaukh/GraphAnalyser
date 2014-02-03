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

public class TranslationalSymmetry extends Symmetry {

	public TranslationalSymmetry(double sigma_scale, double sigma_distance,
			boolean useDistanceWeighting, int numShifts, int angleMerge,
			int pixelMerge, int xMin, int yMin) {
		super(sigma_scale, useDistanceWeighting, sigma_distance, numShifts,
				angleMerge, pixelMerge, xMin, yMin);
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
					double phi = rotFactor(f1, f2);
					double s = scaleFactor(f1, f2);
					double d = distanceFactor(f1, f2);

					//Is this right?
					double dx = f1.p.x() - f2.p.x();
					double dy = f1.p.y() - f2.p.y();

					if(dy < 0){
						dx *= -1;
						dy *= -1;
					}
					double vote = phi * s * d;

					votes.add(new Vote(dx, dy, vote,
							f1.node1, f1.node2, f2.node1, f2.node2));
				}
			}
		}

		List<Point> axis = findMaxima(votes);

		//drawing would be nice, but how?

		double score = computeScore(g, axis, votes);
		return String.format("%.4f", score);
	}

	private double rotFactor(SIFTFeature f1, SIFTFeature f2) {
		return Math.abs(Math.cos(f1.theta - f2.theta));
	}

	public String toString() {
		return "Translational Symmetry";
	}
}
