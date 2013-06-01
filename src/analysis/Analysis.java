package analysis;
import main.Graph;

public interface Analysis {

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

	public String value(Graph g);

	public class AverageEdgeLength implements Analysis {
		public String toString() {
			return "Average Edge Length";
		}

		private static double computeMean(Graph g) {
			double total = 0;
			int count = 0;
			for (int i = 0; i < g.numNodes(); i++) {
				for (int j = i + 1; j < g.numNodes(); j++) {
					if (g.isEdge(i, j)) {
						total += g.distanceBetween(i, j);
						count++;
					}
				}
			}
			if (count == 0) {
				return 0;
			}
			return total / (double) count;
		}

		public String value(Graph g) {
			return String.format("%.4f", computeMean(g));
		}
	}

	public class CorrectedStandardDeviationOfEdgeLength implements Analysis {
		public String toString() {
			return "Corrected Standard Deviation of Edge Length";
		}

		public String value(Graph g) {
			double total = 0;
			int count = 0;
			double mean = AverageEdgeLength.computeMean(g);
			for (int i = 0; i < g.numNodes(); i++) {
				for (int j = i + 1; j < g.numNodes(); j++) {
					if (g.isEdge(i, j)) {
						total += Math.pow(g.distanceBetween(i, j) - mean, 2);
						count++;
					}
				}
			}
			if (count == 0) {
				return "0";
			}
			total = Math.sqrt(total / (double) (count - 1));
			return String.format("%.4f", total);
		}
	}

	public class GraphName implements Analysis {
		public String toString() {
			return "Input File";
		}

		public String value(Graph g) {
			return g.graphName();
		}
	}

	public class LayoutProperty implements Analysis {
		private final String name;

		public LayoutProperty(String s) {
			name = s;
		}

		public String toString() {
			return name;
		}

		public String value(Graph g) {
			return g.getProperty(name).toString();
		}
	}



}
