package analysis;


public abstract class Symmetry implements Analysis {
	private final double sigma_scale;
	private final boolean distanceBound;
	private final double sigma_distance;
	
	public Symmetry(double sigma_scale, boolean distanceBound, double sigma_distance){
		this.sigma_scale = sigma_scale;
		this.distanceBound = distanceBound;
		this.sigma_distance = sigma_distance;
	}
	
	protected double scaleFactor(SIFTFeature f1, SIFTFeature f2) {
		double top = -Math.abs(f1.scale - f2.scale);
		double bottom = sigma_scale * (f1.scale + f2.scale);
		double exponent = top / bottom;
		double val = Math.exp(exponent);
		return val * val;
	}

	protected double distanceFactor(SIFTFeature f1, SIFTFeature f2) {
		if (!distanceBound) {
			return 1;
		}
		double d = f1.distanceTo(f2);
		double exp = -(d * d) / (2 * sigma_distance * sigma_distance);
		return Math.exp(exp);
	}
}
