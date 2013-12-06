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

public class UsedHeight extends Analysis {

	public String toString(){
		return "Used Height";
	}

	@Override
	public String value(Graph g) {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for(Point p : g){
			if(p.y() < min){
				min = p.y();
			}
			if(p.y() > max){
				max = p.y();
			}
		}
		return String.format("%.4f", max - min);
	}

}
