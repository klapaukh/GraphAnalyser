import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
			o.println( image.length + " " + image[0].length);
			o.println("255");
			for (int i = 0; i < image.length; i++) {
				for (int j = 0; j < image[i].length; j++) {
					int c = (int) (((image[i][j] - voteMin) / (voteMax - voteMin)) * 255);
//					UI.setColor(new Color(c, c, c));
//					UI.drawRect(i, j, 1, 1, false);
					o.print(c+ " " );
				}
				o.println();
			}
			o.close();
//			UI.repaintGraphics();


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static List<Point> findMax(double[][] image, double minAngle, double minDistance){
		List<Point> p = new ArrayList<>();

		int maxi = 0, maxj = 0;

		for(int i = 0; i < image.length; i++){
			for(int j = 0; j < image[i].length; j++){
				if(image[i][j] > image[maxi][maxj]){
					maxi = i;
					maxj = j;
				}
			}
		}

		double angle = maxi + minAngle;
		double rad = maxj + minDistance;

		p.add(new Point(angle,rad));

		return p;
	}


}
