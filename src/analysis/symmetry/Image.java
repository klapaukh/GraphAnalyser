package analysis.symmetry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import analysis.Analysis;

import main.Graph;
import main.Pair;
import main.Point;

/**
 * This class contains methods for manipulating 2D arrays of doubles as if they were grey scale images. It contains much of the common code needed by
 * the symmetry detection algorithms.
 *
 * @author Roma Klapaukh
 *
 */
public class Image {

	/** Private field indicating to do Gaussian blur in X */
	private static final int X = 0;

	/**
	 * Private field indicating to do Gaussian blur in Y
	 */
	private static final int Y = 1;

	public static final int MIRROR = 0;
	public static final int TRANSLATION = 1;
	public static final int ROTATION = 2;

	/**
	 * Gaussian blur an array. This returns the resulting image without affecting the original.
	 *
	 * @param image
	 *            The image to blur
	 * @param sigma_gauss
	 *            The sigma for the gaussian
	 * @return The Gaussian blured image.
	 */
	public static double[][] gaussianBlur(double[][] image, double sigma_gauss) {
		int length = (int) Math.ceil(6 * sigma_gauss);
		double mid = Math.ceil(length / 2.0);
		double[] gaussKernel = new double[length];

		// Initialise the gaussKernel
		for (int i = 0; i < gaussKernel.length; i++) {
			gaussKernel[i] = gaussian(mid - i, sigma_gauss);
		}

		// normalise
		double sum = 0;
		for (double d : gaussKernel) {
			sum += d;
		}
		for (int i = 0; i < gaussKernel.length; i++) {
			gaussKernel[i] /= sum;
		}

		double[][] temp = new double[image.length][image[0].length];

		// Convolve x
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				temp[i][j] = convolve(i, j, image, gaussKernel, (int) mid, X);
			}
		}

		double[][] temp2 = new double[image.length][image[0].length];

		// convolve y
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				temp2[i][j] = convolve(i, j, temp, gaussKernel, (int) mid, Y);
			}
		}

		return temp2;
	}

	/**
	 * Calculate the value for the gaussian at a given x with a given sigma. Mean is 0.
	 *
	 * @param x
	 *            X value to find gaussian at
	 * @param sigma
	 *            Standard deviation of gaussian
	 * @return The value of the gaussian
	 */
	private static double gaussian(double x, double sigma) {
		double ss = sigma * sigma;
		double factor = 1.0 / (Math.sqrt(2 * Math.PI * ss));
		double exp = (x * x) / (2 * ss);
		return factor * Math.exp(-exp);
	}

	/**
	 * Apply a 1 dimentional convolution matrix to a given matrix. mid gives the index of the middle of the filter
	 *
	 * @param x
	 *            The x co-ordinate in the image to find the value for
	 * @param y
	 *            The y coordinate in the image to find the value for
	 * @param image
	 *            The original image
	 * @param kernel
	 *            The 1D kernel being used
	 * @param mid
	 *            The center of the kernel (ie. the one which is (x,y))
	 * @param axis
	 *            X or Y representing the axis the 1D kernel should be applied to
	 * @return The value for (x,y) in the convolved image
	 */
	private static double convolve(int x, int y, double[][] image, double[] kernel, int mid, int axis) {
		if (axis != X && axis != Y) {
			throw new RuntimeException("Illegal axis to convolve: " + axis);
		}
		double result = 0;

		int tx = x, ty = y;
		for (int i = 0; i < kernel.length; i++) {

			// Move away from the given pixel
			int dp = i - mid;
			if (axis == X) {
				tx = x + dp;
			} else {
				ty = y + dp;
			}

			// Check the bounds!!!
			tx = Math.max(tx, 0);
			ty = Math.max(ty, 0);
			tx = Math.min(tx, image.length - 1);
			ty = Math.min(ty, image[tx].length - 1);

			// Read the value

			result += image[tx][ty] * kernel[i];
		}

		return result;
	}

	/**
	 * Draw a 2D double array as a gray scale image. It creates the image using the netpbm format.
	 *
	 * @param image
	 *            The image to draw
	 * @param filename
	 *            The filename to use
	 */
	public static void draw(double[][] image, String filename) {

		double voteMin = Double.MAX_VALUE;
		double voteMax = Double.MIN_VALUE;

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				voteMin = Math.min(voteMin, image[i][j]);
				voteMax = Math.max(voteMax, image[i][j]);
			}
		}

		// System.out.println("voteMin: " + voteMin +"\nMax: " + voteMax);
		try {
			PrintStream o = new PrintStream(new File(filename));
			o.println("P2");
			o.println(image.length + " " + image[0].length);
			o.println("255");
			for (int i = 0; i < image.length; i++) {
				for (int j = 0; j < image[i].length; j++) {
					int c = (int) (((image[i][j] - voteMin) / (voteMax - voteMin)) * 255);
					// UI.setColor(new Color(c, c, c));
					// UI.drawRect(i, j, 1, 1, false);
					o.print(c + " ");
				}
				o.println();
			}
			o.close();
			// UI.repaintGraphics();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reduce the size of the image. This merges cells from the top left. Each cell is combined as a linear sum. This preserves the original image.
	 *
	 * @param image
	 *            The original image
	 * @param numDeg
	 *            The number of cells to merge in the first array dimension
	 * @param numPix
	 *            The number of cells to merge in the second array dimension
	 * @return A smaller version of the original image
	 */
	public static double[][] sampleDown(double[][] image, int numDeg, int numPix) {
		int width = image.length / numDeg + Math.min(1, image.length % numDeg);
		int height = image[0].length / numPix + Math.min(1, image[0].length % numPix);
		double[][] newImage = new double[width][height];

		for (int i = 0; i < newImage.length; i++) {
			for (int j = 0; j < newImage[i].length; j++) {
				newImage[i][j] = 0;
				for (int k = 0; k < numDeg; k++) {
					for (int l = 0; l < numPix; l++) {
						int x = i * numDeg + k;
						int y = j * numPix + l;
						if (x < image.length && y < image[x].length) {
							newImage[i][j] += image[x][y];
						}
					}
				}
			}
		}

		return newImage;
	}

	/**
	 * Draw the graph with the mirror lines described in axis. Note that the mirror lines are specied by their closest point to the origin. The file
	 * is an SVG file. Unmirrored edges are red, and mirrored edges are black. Mirrors are drawn as dotted yellow lines. A green bounding box is drawn
	 * around 1920x1080.
	 *
	 * @param g
	 *            Graph to draw
	 * @param axis
	 *            List of mirror lines
	 * @param votes
	 *            All the votes used to find the mirror lines
	 * @param filename
	 *            The filename to write to
	 * @throws FileNotFoundException
	 *             If it can't file the file in filename
	 */
	public static void drawSVG(Graph g, List<Point> axis, List<Vote> votes, String filename, int xMin, int yMin, int type)
			throws FileNotFoundException {

		PrintStream out = new PrintStream(new File(filename));

		long screenWidth = g.getProperty(Analysis.WIDTH).longValue();
		long screenHeight = g.getProperty(Analysis.HEIGHT).longValue();
		int width = 20, height = 10;

		out.printf("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n");
		out.printf("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\"\n");
		out.printf("\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n");
		out.printf("<svg xmlns=\"http://www.w3.org/2000/svg\"\n");
		out.printf("xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\"\n");
		out.printf("width=\"%dpx\" height=\"%dpx\"\n", screenWidth, screenHeight);

		double minx = Double.MAX_VALUE, maxx = Double.MIN_VALUE, miny = Double.MAX_VALUE, maxy = Double.MIN_VALUE;
		for (int i = 0; i < g.numNodes(); i++) {
			Point n = g.getNode(i);
			if (n.x() - width / 2 < minx) {
				minx = n.x() - width / 2;
			}
			if (n.x() + width / 2 > maxx) {
				maxx = n.x() + width / 2;
			}
			if (n.y() - height / 2 < miny) {
				miny = n.y() - height / 2;
			}
			if (n.y() + height / 2 > maxy) {
				maxy = n.y() + height / 2;
			}
		}

		minx = Math.min(minx, 0);
		miny = Math.min(miny, 0);
		maxx = Math.max(1920, maxx);
		maxy = Math.max(1080, maxy);

		out.printf("viewBox=\"%d %d %d %d\"\n", (long) minx, (long) miny, (long) (maxx - minx), (long) (maxy - miny));
		out.printf("zoomAndPan=\"disable\" >\n");

		int i, j;
		/* Draw edges */
		for (i = 0; i < g.numNodes(); i++) {
			for (j = i + 1; j < g.numNodes(); j++) {
				if (g.isEdge(i, j)) {
					String color = "rgb(255,0,0)";

					// Am I mirrored

					// What was my vote
					out: for (Vote v : votes) {
						if ((v.i == i && v.j == j) || (v.i2 == i && v.j2 == j)) {
							for (Point p : axis) {
								if (Math.abs(p.x() - v.x) < xMin && Math.abs(p.y() - v.y) < yMin) {
									color = "rgb(0,0,0)";
									break out;
								}
							}
						}
					}

					// Draw me
					Point n1 = g.getNode(i);
					Point n2 = g.getNode(j);
					int x1 = (int) n1.x();
					int x2 = (int) n2.x();
					int y1 = (int) n1.y();
					int y2 = (int) n2.y();
					out.printf("<line x1=\"%d\" x2=\"%d\" y1=\"%d\" y2=\"%d\" stroke=\"%s\" fill=\"%s\" opacity=\"%.2f\"/>\n", x1, x2, y1, y2, color,
							color, 1.0f);
				}
			}
		}

		/* Draw nodes */
		for (i = 0; i < g.numNodes(); i++) {
			Point n = g.getNode(i);
			int x = (int) (n.x() - width / 2);
			int y = (int) (n.y() - height / 2);
			out.printf("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\" stroke=\"%s\" opacity=\"%.2f\"/>\n", x, y, width, height,
					"rgb(0,0,255)", "rgb(0,0,0)", 1.0f);

		}

		out.printf("<rect x=\"0\" y=\"0\" width=\"%d\" height=\"%d\" stroke-width=\"10\" stroke=\"rgb(0,255,0)\" fill-opacity=\"0\"/>\n",
				screenWidth, screenHeight);

		if (type == MIRROR) {
			drawMirrorLines(out, axis, minx, miny);
		} else if (type == ROTATION) {
			drawRotationCenters(out, axis);
		}

		out.printf("</svg>");
		out.close();

	}

	private static void drawRotationCenters(PrintStream out, List<Point> centers) {
		for (Point p : centers) {
			out.printf("<circle cx=\"%d\" cy=\"%d\" r=\"10\" stroke=\"yellow\" fill=\"yellow\" />\n", (int) p.x(), (int) p.y());
		}
	}

	private static void drawMirrorLines(PrintStream out, List<Point> axis, double minx, double miny) {
		for (Point p : axis) {

			double angle = Math.toRadians(p.x());
			double radius = p.y();

			double xint = radius / Math.cos(angle);
			double yint = radius / Math.sin(angle);

			int x1 = 0, x2 = (int) xint, y1 = (int) yint, y2 = 0;

			if (xint == Double.NaN) {
				// Horizontal Line
				x1 = 0;
				x2 = 1920;
				y2 = y1;
			} else if (yint == Double.NaN) {
				// Vertical line
				x1 = x2;
				y1 = 0;
				y2 = 1080;
			} else if (Double.compare(yint, miny) > 0 && Double.compare(xint, minx) > 0) {
				// The line will be visible trivially, so we are fine

			} else {
				// So we remake the yint, to be the y position at the right edge
				x1 = 1920;
				y1 = (int) ((1920 - xint) * Math.tan(angle - (Math.PI / 2)));

			}
			out.printf("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\""
					+ " style=\"stroke: rgb(255,255,0); stroke-width: 10; stroke-dasharray: 9, 5;\"/>\n", x1, y1, x2, y2);
		}
	}

	/**
	 * This is a comparator used to help find maxima in images. It compares two coordinates to see which is bigger, and also checks if they are too
	 * close.
	 *
	 * @author Roma Klapaukh
	 *
	 */
	private static class PeakComparator implements Comparator<Pair<Integer, Integer>> {

		/**
		 * The image that coordinates are samples from
		 */
		private final Map<Pair<Integer, Integer>, Double> votes;
		/**
		 * Limits of how close points are allowed to be
		 */
		private final double limitX, limitY;

		/**
		 * Create a compartor for a given image with fixed limits
		 *
		 * @param cotes
		 *            The image to compare points from
		 * @param limitDist
		 *            Closest distance in second array dimension
		 * @param limitAngle
		 *            Closest distance in first array dimension
		 */
		public PeakComparator(Map<Pair<Integer, Integer>, Double> votes, double limitX, double limitY) {
			this.votes = votes;
			this.limitX = limitX;
			this.limitY = limitY;
		}

		@Override
		public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
			return Double.compare(votes.get(o1), votes.get(o2));
		}

		/**
		 * Determines if two points are too close together
		 *
		 * @param o1
		 *            Point 1
		 * @param o2
		 *            Point 2
		 * @return True if they are too close
		 */
		public boolean tooClose(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
			return Math.abs(o1.x - o2.x) < limitX && Math.abs(o1.y - o2.y) < limitY;
		}
	}

	public static List<Point> findMax(Map<Pair<Integer, Integer>, Double> voteSpace, int tooCloseX, int tooCloseY, int numFeatures) {
		List<Point> p = new ArrayList<>();

		List<Pair<Integer, Integer>> best = new ArrayList<>();
		PeakComparator comp = new PeakComparator(voteSpace, tooCloseX, tooCloseY);


		for (Pair<Integer, Integer> thisPoint : voteSpace.keySet()) {
			best.add(thisPoint);
		}

		Collections.sort(best, comp);
		Collections.reverse(best);

		for(int i = 0 ; i < numFeatures && i < best.size(); i++){
			for (int j = i+1; j < best.size(); j++) {
				if (comp.tooClose(best.get(i), best.get(j))) {
					best.remove(j);
				}
			}
		}

		while (best.size() > 0 && (best.size() > numFeatures || voteSpace.get(best.get(best.size() - 1)) == 0)) {
			best.remove(best.size() - 1);
		}

		for (Pair<Integer, Integer> b : best) {
			double angle = b.x;
			double rad = b.y;

			p.add(new Point(angle, rad));
		}

		return p;
	}
}
