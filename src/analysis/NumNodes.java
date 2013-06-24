package analysis;

import main.Graph;

public class NumNodes extends Analysis {

	public String toString(){
		return "Num Nodes";
	}

	@Override
	public String value(Graph g) {
		return String.format("%d", g.numNodes());
	}

}
