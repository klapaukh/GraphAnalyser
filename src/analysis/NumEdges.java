package analysis;

import main.Graph;

public class NumEdges extends Analysis {

	public String toString(){
		return "Num Edges";
	}

	@Override
	public String value(Graph g) {
		int numEdges = 0;

		for(@SuppressWarnings("unused") Pair<Point,Point> p : g.edgeIterator()){
			numEdges += 1;
		}

		//TODO This needs to be removed once it has been tested more!!
		int total = 0;
		for(int i = 0 ; i < g.numNodes() ; i++){
			for(int j = i; j < g.numNodes(); j++){
				total += g.isEdge(i, j)? 1 : 0;
			}
		}

		if(numEdges != total){
			throw new RuntimeException("Edge counting failed " + numEdges + " -- " + total);
		}
		return String.format("%d", numEdges);
	}

}
