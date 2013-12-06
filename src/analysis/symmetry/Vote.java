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

public class Vote {
	public final double y, x, vote;
	public final int i, j, i2, j2; // voting edges

	public Vote(double x, double y, double vote, int i, int j, int i2, int j2) {
		this.x = x;
		this.y = y;
		this.vote = vote;
		this.i = i;
		this.j = j;
		this.i2 = i2;
		this.j2 = j2;
	}

}