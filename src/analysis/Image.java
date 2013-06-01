package analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import main.Graph;


public class Image {

	private static final int X = 0;
	private static final int Y = 1;

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

		double[][] temp2 = image;
		image = temp;
		temp = temp2;
		// convolve y
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				temp[i][j] = convolve(i, j, image, gaussKernel, (int) mid, Y);
			}
		}

		return temp;
	}

	private static double gaussian(double x, double sigma) {
		double ss = sigma * sigma;
		double factor = 1.0 / (Math.sqrt(2 * Math.PI * ss));
		double exp = (x * x) / (2 * ss);
		return factor * Math.exp(-exp);
	}

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

	public static void draw(double[][] image) {

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
			PrintStream o = new PrintStream(new File("ex.pbm"));
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

	public static List<Point> findMax(double[][] image, double minAngle, double minDistance, double tooCloseDist, double tooCloseAngle, int numPoints, int numDeg, int numPix) {
		List<Point> p = new ArrayList<>();

		List<Pair<Integer, Integer>> best = new ArrayList<>();
		PeakComparator comp = new PeakComparator(image, tooCloseDist, tooCloseAngle);

		best.add(new Pair<>(0, 0));

		for (int i = 0; i < image.length; i++) {
			loopy: for (int j = 0; j < image[i].length; j++) {
				Pair<Integer, Integer> thisPoint = new Pair<>(i, j);
				boolean shouldBeAdded = best.size() < numPoints;
				for (int k = 0; k < best.size(); k++) {
					if (comp.compare(best.get(k), thisPoint) < 0) {
						if (comp.tooClose(best.get(k), thisPoint)) {
							shouldBeAdded = false;
							best.set(k, thisPoint);
							continue loopy;
						}
						shouldBeAdded=true;
					}else if(comp.tooClose(best.get(k), thisPoint)){
						shouldBeAdded = false;
						continue loopy;
					}
				}
				if (shouldBeAdded) {
					best.add(thisPoint);
				}
			}
			Collections.sort(best, comp);
			Collections.reverse(best);
			while (best.size() > 0 && (best.size() > numPoints || image[best.get(best.size()-1).x][best.get(best.size()-1).y] == 0)) {
				best.remove(best.size() - 1);
			}
		}

//		boolean bad = false;
//		for(int i = 0; i < best.size(); i++){
//			for(int j = i+1; j < best.size(); j++){
//				if(comp.tooClose(best.get(i), best.get(j))){
//					bad = true;
//				}
//			}
//		}
//		System.err.println(bad);


//		System.err.println();
//		for(Pair<Integer,Integer> cp: best){
//			System.err.println("(" + (cp.x + minAngle) + ", "+ (cp.y + minDistance)  + ") -- " + image[(int) (cp.x)][(int) (cp.y)]);
//		}

		for (Pair<Integer, Integer> b : best) {
			double angle = b.x * numDeg + (numDeg/2) + minAngle;
			double rad = b.y * numPix + (numPix/2) + minDistance;

			p.add(new Point(angle, rad));
		}


		return p;
	}

	public static double[][] sampleDown(double[][] image, int numDeg, int numPix){
		int width = image.length/ numDeg + Math.min(1, image.length % numDeg);
		int height = image[0].length/ numPix + Math.min(1, image[0].length % numPix);
		double[][] newImage = new double[width][height];


		for(int i = 0; i < newImage.length ; i++){
			for(int j = 0; j < newImage[i].length; j++){
				newImage[i][j] = 0;
				for(int k = 0; k < numDeg; k++){
					for(int l = 0; l < numPix; l++){
						int x = i * numDeg + k;
						int y = j * numPix + l;
						if( x < image.length && y < image[x].length){
							newImage[i][j] += image[x][y];
						}
					}
				}
			}
		}

		return newImage;
	}

	public static void drawWithMirrorLines(Graph g, List<Point> axis, List<Analysis.Vote> votes) throws FileNotFoundException {

		PrintStream out = new PrintStream(new File("temp.svg"));

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

					//Am I mirrored

					//What was my vote
					out: for(Analysis.Vote v : votes){
						if((v.i == i && v.j == j) || (v.i2 ==i && v.j2 == j)){
							for(Point p : axis){
								if(Math.abs(p.x() - v.theta) < 10 && Math.abs(p.y() - v.rad) < 15){
									color = "rgb(0,0,0)";
									break out;
								}
							}
						}
					}


					//Draw me
					Point n1 = g.getNode(i);
					Point n2 = g.getNode(j);
					int x1 = (int) n1.x();
					int x2 = (int) n2.x();
					int y1 = (int) n1.y();
					int y2 = (int) n2.y();
					out.printf("<line x1=\"%d\" x2=\"%d\" y1=\"%d\" y2=\"%d\" stroke=\"%s\" fill=\"%s\" opacity=\"%.2f\"/>\n", x1, x2, y1, y2,
							color, color, 1.0f);
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

		out.printf("<rect x=\"0\" y=\"0\" width=\"%d\" height=\"%d\" stroke-width=\"10\" stroke=\"rgb(0,255,0)\" fill-opacity=\"0\"/>\n", screenWidth, screenHeight);

		for (Point p : axis) {

			double angle = Math.toRadians(p.x());
			double radius = p.y();

			double xint =  radius / Math.cos(angle);
			double yint = radius / Math.sin(angle);

			int x1 = 0, x2 = (int) xint, y1 = (int) yint, y2 = 0;

			if (xint == Double.NaN) {
				//Horizontal Line
				x1 = 0;
				x2 = 1920;
				y2 = y1;
			} else if (yint == Double.NaN) {
				//Vertical line
				x1 = x2;
				y1 = 0;
				y2 = 1080;
			} else if (Double.compare(yint, miny) > 0 && Double.compare(xint, minx) > 0) {
				// The line will be visible trivially, so we are fine

			} else {
				// So we remake the yint, to be the y position at the right edge
				x1=1920;
				y1 = (int) ((1920 - xint) * Math.tan(angle - (Math.PI / 2)));

			}
			out.printf("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\""
					+ " style=\"stroke: rgb(255,255,0); stroke-width: 10; stroke-dasharray: 9, 5;\"/>\n", x1, y1,x2,y2);
		}
		out.printf("</svg>");
		out.close();

	}



	private static class PeakComparator implements Comparator<Pair<Integer, Integer>> {

		private final double[][] image;
		private final double limitDist, limitAngle;

		public PeakComparator(double[][] image, double limitDist, double limitAngle) {
			this.image = image;
			this.limitDist = limitDist;
			this.limitAngle = limitAngle;
		}

		@Override
		public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
			return Double.compare(image[o1.x][o1.y], image[o2.x][o2.y]);
		}

		public boolean tooClose(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
			return Math.abs(o1.x - o2.x) < limitAngle &&  Math.abs(o1.y - o2.y) < limitDist;
		}
	}
}