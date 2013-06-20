package analysis;

import main.Graph;

public class LayoutProperty extends Analysis {
	private final String name;

	public LayoutProperty(String s) {
		name = s;
	}

	public String toString() {
		return name;
	}

	public String value(Graph g) {
		return g.getProperty(name).toString();
	}
}