package analysis.lists;

import java.util.List;

import analysis.Analysis;

public abstract class ListAnalysis extends Analysis{

	public String toRVector(List<? extends Object> data){
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
}
