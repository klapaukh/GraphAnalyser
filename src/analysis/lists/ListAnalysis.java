package analysis.lists;

import java.util.Collections;
import java.util.List;

import analysis.Analysis;

public abstract class ListAnalysis extends Analysis {

	public String toRVector(List<? extends Object> data) {
		StringBuilder temp = new StringBuilder();
		temp.append("\"c(");
		boolean first = true;
		for (Object d : data) {
			if (!first) {
				temp.append(',');
			}
			temp.append(d.toString());
			first = false;
		}
		temp.append(')');
		temp.append('"');
		return temp.toString();
	}

	public double mean(List<? extends Number> data) {
		double total = 0;
		for (Number n : data) {
			total += n.doubleValue();
		}
		total /= data.size();
		return total;
	}

	/**
	 * Find the median of a sorted list
	 * @param data Sorted list
	 * @return Median value
	 */
	public double median(List<? extends Number> data) {
		int length = data.size();
		if (length % 2 == 0) {
			double upper = data.get(length / 2).doubleValue();
			double lower = data.get(length / 2 - 1).doubleValue();
			return (lower + upper) / 2.0;
		} else {
			return data.get(length / 2).doubleValue();
		}
	}

	public double range(List<Double> data) {
		return Collections.max(data) - Collections.min(data);
	}

}
