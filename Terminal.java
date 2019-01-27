/*
*Terminal is a data structure that represents one of the endpoints of Cluster.
*See Cluster class for more information.
*/

import java.util.ArrayList;

public class Terminal extends Entity{
	private String identifier = "";
	public Terminal (String identifier) {
		this.identifier = identifier;
	}
	public String getIdentifier() {
		return identifier;
	}
	public String toString() {
		return identifier;
	}
	public ArrayList<Terminal> returnTerminals() {
		ArrayList<Terminal> result = new ArrayList<Terminal>();
		result.add(this);
		return result;
	}
	public double totalDifferences() {
		return 0;
	}
}
