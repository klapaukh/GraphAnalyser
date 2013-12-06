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

package main;

/**
 * This class represents a generic immutable pair type.
 *
 * @author Roma Klapaukh
 *
 * @param <E> Type of x
 * @param <F> Type of y
 */
public class Pair<E,F> {

	/**
	 * First value
	 */
	public final E x;

	/**
	 * Second value
	 */
	public final F y;

	/**
	 * Create a new pair with the given values
	 * @param x The first value
	 * @param y The second value
	 */
	public Pair(E x, F y){
		this.x=x;
		this.y=y;
	}

	public String toString(){
		return "(" + this.x + ", " + this.y + ")";
	}

	public int hashCode(){
		return x.hashCode() ^ y.hashCode();
	}

	public boolean equals(Object o){
		if(! (o instanceof Pair) ) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		Pair p = (Pair) o;

		return this.x.equals(p.x) && this.y.equals(p.y);
	}

}
