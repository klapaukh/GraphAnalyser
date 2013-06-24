package analysis;

import java.util.ArrayList;
import java.util.List;

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

	public abstract String value(Graph g);

	public double computeMean(List<Double> list){
		if(list.isEmpty()) {
			return 0;
		}
		double total = 0;
		for(Double v : list){
			total += v;
		}
		total /= list.size();
		return total;
	}

	public double computeCorrectedStandardDeviation(List<Double> list){
		if(list.isEmpty()){
			return 0;
		}
		double total = 0;
		double mean = computeMean(list);
		for (Double val: list) {
					total += Math.pow(val - mean, 2);
		}
		total = Math.sqrt(total / (double) (list.size()-1));
		return total;
	}

	public List<Double> computeEdgeLengths(Graph g) {
		List<Double> lengths = new ArrayList<>();
		for (int i = 0; i < g.numNodes(); i++) {
			for (int j = i; j < g.numNodes(); j++) {
				if (g.isEdge(i, j)) {
					lengths.add(g.distanceBetween(i, j));
				}
			}
		}

		//TODO This needs to replace the above once it has been tested
		List<Double> tests = new ArrayList<>();
		for(Pair<Point,Point> e : g.edgeIterator()){
			tests.add(e.x.distanceTo(e.y));
		}

		if(tests.size() != lengths.size()){
			throw new RuntimeException("Edge iterator is broken");
		}
		for(int i = 0 ; i < tests.size();i++){
			if(Double.compare(tests.get(i), lengths.get(i)) != 0){
				throw new RuntimeException("Edge iterator is broken");
			}
		}
		return lengths;
	}

	public List<Double> computeVertexDistances(Graph g) {
		List<Double> lengths = new ArrayList<>();
		for (int i = 0; i < g.numNodes(); i++) {
			for (int j = i + 1; j < g.numNodes(); j++) {
				lengths.add(g.distanceBetween(i, j));
			}
		}
		return lengths;
	}

}
