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

public abstract class Analysis {

	public static final String ELAPSED_TIME = "elapsed";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String ITERATIONS = "iterations";
	public static final String FORCEMODE = "forcemode";
	public static final String KE = "ke";
	public static final String KH = "kh";
	public static final String KL = "kl";
	public static final String KW = "kw";
	public static final String MASS = "mass";
	public static final String TIME = "time";
	public static final String COEFFICIENTOFRESTITUTION = "coefficientOfRestitution";
	public static final String MUS = "mus";
	public static final String MUK = "muk";
	public static final String KG = "kg";
	public static final String WELLMASS = "wellMass";
	public static final String EDGECHARGE = "edgeCharge";
	public static final String FINALKINETICENERGY = "finalKineticEnergy";
	public static final String NODEWIDTH = "nodeWidth";
	public static final String NODEHEIGHT = "nodeHeight";
	public static final String NODECHARGE = "nodeCharge";

	public abstract String value(Graph g);


}
