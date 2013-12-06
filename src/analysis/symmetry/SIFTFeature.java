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

import main.Point;


public class SIFTFeature {

	public final Point p;
	public final double theta, scale;
	public final int type;
	public final int node1, node2;

	public SIFTFeature(double x, double y, double theta, double scale, int type, int node1, int node2) {
		this.p = new Point(x, y);
		this.theta = theta;
		this.scale = scale;
		this.type = type;
		this.node1 = node1;
		this.node2 = node2;
	}

	public double angleToForwards(SIFTFeature other) {
		return this.p.angleToOtherFromXForward(other.p);
	}

	public double angleTo(SIFTFeature other) {
		return this.p.angleToOtherFromX(other.p);
	}

	public double distanceTo(SIFTFeature other) {
		return this.p.distanceTo(other.p);
	}

	public Point midPointTo(SIFTFeature other) {
		return this.p.midPointTo(other.p);
	}


}
