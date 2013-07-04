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
