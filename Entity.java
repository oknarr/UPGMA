import java.util.ArrayList;

public abstract class Entity {
	//the amount that each entity houses (e.g. cluster is difference from junction to terminal)
	protected double value;
	public abstract ArrayList<Terminal> returnTerminals();
	public abstract double totalDifferences();
	public boolean isCluster() {
		return this.getClass().toString().substring(6).equals("Cluster");
	}
	public double getValue() {return value;}
	public void setValue(double value) {this.value = value;}
}
