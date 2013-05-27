
public class Pair<E,F> {

	public final E x;
	public final F y;

	public Pair(E x, F y){
		this.x=x;
		this.y=y;
	}

	public String toString(){
		return "(" + this.x + ", " + this.y + ")";
	}

}
