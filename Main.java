import java.io.*;
import java.util.*;

public class Main {
	public static void main (String[] args) {
		Object[] result = readFile();
		int n = (Integer) result[0];
		Entity[] entities = (Entity[]) result[1];
		double[][] differences = (double[][]) result[2];

		Cluster tree = makeTree (n, entities, differences);
		System.out.println("Phylogenetic Tree (Newick format): " + tree + ";");
		System.out.println("To visualize this tree, paste this information in http://phylo.io/.");
	}
	public static Object[] readFile (){
		//these must be declared and initialized outside the try/catch block
		Integer n = null;
		Entity[] entities = null;
		double[][] differences = null;
		try{
			File file = new File(System.getProperty("user.dir") + "/Input.txt"); 
			BufferedReader br = new BufferedReader(new FileReader(file));

			StringTokenizer st = new StringTokenizer(br.readLine());
			ArrayList<Entity> tempEntities = new ArrayList<Entity>();
			while (st.hasMoreTokens()) {tempEntities.add(new Terminal(st.nextToken()));}
			n = tempEntities.size();
			entities = new Entity[n];
			for (int i = 0; i < n; i++) {
				entities[i] = tempEntities.get(i);
			}
			
			differences = new double[n][n];
			for (int i = 0; i < n; i++) {
				st = new StringTokenizer(br.readLine());			
				for (int j = 0; j < n; j++) {
					differences[i][j] = Double.parseDouble(st.nextToken());
				}
			}
		}
		catch (FileNotFoundException e){}
		catch (IOException e){}

		Object[] result = {n, entities, differences};
		return result;
	}

	public static Cluster makeTree(int n, Entity[] entities, double[][] differences) {
		if (n==2) {
			Cluster tree = new Cluster (entities[0], entities[1]);
			double minimum = differences[0][1]; //AKA differences[1][0]
			entities[0].setValue(minimum/2-entities[0].totalDifferences());
			entities[1].setValue(minimum/2-entities[1].totalDifferences());
			return tree;
		}	
		//combine two entities together into a cluster --> modify terminals and differences

		Object[] grouped = group (entities, differences);
		entities = (Entity[]) grouped[0];
		differences = (double[][]) grouped[1];
		return makeTree(n-1, entities, differences);
	}

	private static Object[] group (Entity[] entities, double[][] differences) {
		//1. find minimum difference in differences
		int minimum1 = 0;
		int minimum2 = 1;
		double minimum = differences[minimum1][minimum2];
		for (int i = 0; i < differences.length; i++) {
			for (int j = 0; j < differences[i].length; j++) {
				if (differences[i][j]<minimum && i!=j) {
					minimum1 = i;
					minimum2 = j;
					minimum = differences[i][j];
				}
			}
		}
		//notice that minimum1 is always less than minimum2

		//2. concatenate the two in entities (whichever one comes first)
		Entity[] newEntities = new Entity[entities.length-1];
		Entity newEntity = new Cluster(entities[minimum1], entities[minimum2]);
		entities[minimum1].setValue(minimum/2-entities[minimum1].totalDifferences());
		entities[minimum2].setValue(minimum/2-entities[minimum2].totalDifferences());
		int counter = 0;
		for (int i = 0; i < entities.length; i++) {	
			if (i==minimum1) {newEntities[counter] = newEntity;}
			else if (i==minimum2) {counter--;}	
			else {newEntities[counter] = entities[i];}			
			counter++;
		}
		//3. make new differences
		double[][] newDifferences = new double[differences.length-1][differences.length-1];
		for (int i = 0; i < differences.length-1; i++) {
			for (int j = 0; j < differences.length-1; j++) {
				if (i==j) {newDifferences[i][j] = 0;}
				else if (i!=minimum1 && j!= minimum1) {
					int a, b;
					if (i < minimum2) a = i; else a = i+1;
					if (j < minimum2) b = j; else b = j+1;
					newDifferences[i][j] = differences[a][b];
				}
			}
		}
		for (int i = 0; i < differences.length-1; i++) {
			double value = 0;
			if (i<minimum2) { value = (differences[i][minimum1]+differences[i][minimum2])/2;}
			if (i>=minimum2) { value = (differences[i+1][minimum1]+differences[i+1][minimum2])/2;}
			if (i==minimum1) value = 0;
			newDifferences[i][minimum1] = value;
			newDifferences[minimum1][i] = value;
		}

		//4. return results
		Object[] result = {newEntities, newDifferences};
		result[0] = newEntities;
		result[1] = newDifferences;
		return result;
	}
}
