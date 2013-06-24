package analysis;

import main.Graph;

public class UsedWidth extends Analysis {

	public String toString(){
		return "Used Width";
	}

	@Override
	public String value(Graph g) {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for(Point p : g){
			if(p.x() < min){
				min = p.x();
			}
			if(p.x() > max){
				max = p.x();
			}
		}
		return String.format("%.4f", max - min);
	}

}
