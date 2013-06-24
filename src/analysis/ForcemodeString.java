package analysis;

import main.Graph;

public class ForcemodeString extends Analysis {

	public static final int HOOKES_LAW_SPRING = 1 << 0;
	public static final int LOG_SPRING = 1 << 1;
	public static final int FRICTION = 1 << 2;
	public static final int DRAG = 1 << 3;
	public static final int BOUNCY_WALLS = 1 << 4;
	public static final int CHARGED_WALLS = 1 << 5;
	public static final int GRAVITY_WELL = 1 << 6;
	public static final int COULOMBS_LAW = 1 << 7;
	public static final int DEGREE_BASED_CHARGE = 1 << 8;
	public static final int CHARGED_EDGE_CENTERS = 1 << 9;
	public static final int WRAP_AROUND_FORCES = 1 << 10;

	public String toString(){
		return "Forcemode String";

	}

	@Override
	public String value(Graph g) {
		StringBuilder f = new StringBuilder();
		int fmode = g.getProperty(Analysis.FORCEMODE).intValue();

		if((fmode & COULOMBS_LAW) != 0){
			f.append('C');
		}
		if((fmode & HOOKES_LAW_SPRING) != 0){
			f.append('H');
		}
		if((fmode & LOG_SPRING) != 0){
			f.append('L');
		}
		if((fmode & FRICTION) != 0){
			f.append('F');
		}
		if((fmode & DRAG) != 0){
			f.append('D');
		}
		if((fmode & BOUNCY_WALLS) != 0){
			f.append('B');
		}
		if((fmode & CHARGED_WALLS) != 0){
			f.append('C');
		}
		if((fmode & GRAVITY_WELL) != 0){
			f.append('G');
		}
		if((fmode & DEGREE_BASED_CHARGE) != 0){
			f.append('V');
		}
		if((fmode & CHARGED_EDGE_CENTERS) != 0){
			f.append('E');
		}
		if((fmode & WRAP_AROUND_FORCES) != 0){
			f.append('A');
		}

		return f.toString();
	}

}
