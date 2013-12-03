package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import analysis.Analysis;
import analysis.DeviationFromIdealAngle;
import analysis.EdgeCrossings;
import analysis.ForcemodeString;
import analysis.GraphName;
import analysis.LayoutProperty;
import analysis.NumEdges;
import analysis.NumNodes;
import analysis.UsedHeight;
import analysis.UsedWidth;
import analysis.lists.EdgeLength;
import analysis.lists.NodeDegree;
import analysis.lists.VertexDistance;
import analysis.symmetry.MirrorSymmetry;
import analysis.symmetry.RotationalSymmetry;
import analysis.symmetry.TranslationalSymmetry;

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
		if (args.length < 1 || args.length > 2) {
			System.out.println("Incorrect usage.");
			System.out.println("java GraphAnalyser file.svg");
			System.exit(-1);
		}

		List<Analysis> tests = new ArrayList<>();

		tests.add(new GraphName());
		tests.add(new NumNodes());
		tests.add(new NumEdges());
		tests.add(new ForcemodeString());
		tests.add(new LayoutProperty(Analysis.ELAPSED_TIME));
		tests.add(new LayoutProperty(Analysis.WIDTH));
		tests.add(new LayoutProperty(Analysis.HEIGHT));
		tests.add(new LayoutProperty(Analysis.ITERATIONS));
//		tests.add(new LayoutProperty(Analysis.FORCEMODE));
		tests.add(new LayoutProperty(Analysis.KE));
		tests.add(new LayoutProperty(Analysis.KH));
//		tests.add(new LayoutProperty(Analysis.KL));
//		tests.add(new LayoutProperty(Analysis.KW));
		tests.add(new LayoutProperty(Analysis.MASS));
		tests.add(new LayoutProperty(Analysis.TIME));
		tests.add(new LayoutProperty(Analysis.COEFFICIENTOFRESTITUTION));
		tests.add(new LayoutProperty(Analysis.MUS));
		tests.add(new LayoutProperty(Analysis.MUK));
//		tests.add(new LayoutProperty(Analysis.KG));
//		tests.add(new LayoutProperty(Analysis.WELLMASS));
		tests.add(new LayoutProperty(Analysis.EDGECHARGE));
		tests.add(new LayoutProperty(Analysis.FINALKINETICENERGY));
		tests.add(new LayoutProperty(Analysis.NODEWIDTH));
		tests.add(new LayoutProperty(Analysis.NODEHEIGHT));
		tests.add(new LayoutProperty(Analysis.NODECHARGE));
		tests.add(new MirrorSymmetry(0.1, 2, false, 1, 5, 5, 10, 10));
		tests.add(new TranslationalSymmetry(0.1, 2, false, 1, 5, 5, 10, 10));
		tests.add(new RotationalSymmetry(0.1, 2, false, 1, 5, 5, 10, 10));
		tests.add(new EdgeCrossings());
		tests.add(new DeviationFromIdealAngle());
		tests.add(new UsedWidth());
		tests.add(new UsedHeight());
		tests.add(new EdgeLength(false));
		tests.add(new VertexDistance(false));
		tests.add(new NodeDegree(false));

		List<String> graphs = new ArrayList<>();
		if (args.length == 1) {
			graphs.add(args[0]);
		} else {
			File dir = new File(args[0]);
			if (!dir.exists() || !dir.isDirectory()) {
				System.out.println("There is no directory: " + args[0]);
				System.exit(-1);
			}
			String match = args[1];
			addAllFiles(dir, match, graphs);
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
		for (String fname : graphs) {
			Graph g;
			try {
				g = new Graph(fname);
				for (int i = 0; i < tests.size(); i++) {
					out.print(tests.get(i).value(g));
					if (i < tests.size() - 1) {
						out.print(",");
					}
				}
				out.println();
			} catch (IOException e) {
				//Any graph might be a trap and fail
				System.err.println("Failed to read file " + fname);
				e.printStackTrace();
			}
		}
	}

	private static void addAllFiles(File dir, String match, List<String> graphs) {
		File[] filesInDir = dir.listFiles();
		for (File f : filesInDir) {
			if (f.isDirectory()) {
				addAllFiles(f, match, graphs);
			} else if (f.toString().matches(match)) {
				graphs.add(f.toString());
			}
		}
	}
}
