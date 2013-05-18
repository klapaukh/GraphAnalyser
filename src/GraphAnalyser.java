import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class GraphAnalyser {

	public static final long HOOKES_LAW_SPRING = 1 << 0;
	public static final long LOG_SPRING = 1 << 1;
	public static final long FRICTION = 1 << 2;
	public static final long DRAG = 1 << 3;
	public static final long BOUNCY_WALLS = 1 << 4;
	public static final long CHARGED_WALLS = 1 << 5;
	public static final long GRAVITY_WELL = 1 << 6;
	public static final long COULOMBS_LAW = 1 << 7;
	public static final long DEGREE_BASED_CHARGE = 1 << 8;
	public static final long CHARGED_EDGE_CENTERS = 1 << 9;
	public static final long WRAP_AROUND_FORCES = 1 << 10;

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Incorrect usage.");
			System.out.println("java GraphAnalyser file.svg");
			System.exit(-1);
		}

		List<Analysis> tests = new ArrayList<>();
		tests.add(new Analysis.AverageEdgeLength());
		tests.add(new Analysis.CorrectedStandardDeviationOfEdgeLength());
		tests.add(new Analysis.GraphName());
		tests.add(new Analysis.LayoutProperty(Analysis.ELAPSED_TIME));
		tests.add(new Analysis.LayoutProperty(Analysis.WIDTH));
		tests.add(new Analysis.LayoutProperty(Analysis.HEIGHT));
		tests.add(new Analysis.LayoutProperty(Analysis.ITERATIONS));
		tests.add(new Analysis.LayoutProperty(Analysis.FORCEMODE));
		tests.add(new Analysis.LayoutProperty(Analysis.KE));
		tests.add(new Analysis.LayoutProperty(Analysis.KH));
		tests.add(new Analysis.LayoutProperty(Analysis.KL));
		tests.add(new Analysis.LayoutProperty(Analysis.KW));
		tests.add(new Analysis.LayoutProperty(Analysis.MASS));
		tests.add(new Analysis.LayoutProperty(Analysis.TIME));
		tests.add(new Analysis.LayoutProperty(Analysis.COEFFICIENTOFRESTITUTION));
		tests.add(new Analysis.LayoutProperty(Analysis.MUS));
		tests.add(new Analysis.LayoutProperty(Analysis.MUK));
		tests.add(new Analysis.LayoutProperty(Analysis.KG));
		tests.add(new Analysis.LayoutProperty(Analysis.WELLMASS));
		tests.add(new Analysis.LayoutProperty(Analysis.EDGECHARGE));
		tests.add(new Analysis.MirrorSymmetry(0.1, 10, 2, false));

		List<Graph> graphs = new ArrayList<>();
		try {
			Graph g = new Graph(args[0]);
			graphs.add(g);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem loading file - Terminating");
			System.exit(-1);
		}

		PrintStream out = System.out;
		// Print header line
		for (int i = 0; i < tests.size(); i++) {
			out.print(tests.get(i));
			if (i < tests.size() - 1) {
				out.print(",");
			}
		}
		out.println();

		// Print out a line per graph
		for (Graph g : graphs) {
			for (int i = 0; i < tests.size(); i++) {
				out.print(tests.get(i).value(g));
				if (i < tests.size() - 1) {
					out.print(",");
				}
			}
			out.println();
		}
	}
}
