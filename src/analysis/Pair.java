package analysis;

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

}
