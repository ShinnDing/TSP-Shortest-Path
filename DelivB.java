import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

// Class DelivB does the work for deliverable DelivB of the Prog340

public class DelivB {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	ArrayList<Node> sC;

	public DelivB(File in, Graph gr) {
		inputFile = in;
		g = gr;
		sC = sortedCities();

		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring(0, inputFileName.length() - 4); // Strip off ".txt"
		String outputFileName = baseFileName.concat("_out.txt");
		outputFile = new File(outputFileName);
		if (outputFile.exists()) { // For retests
			outputFile.delete();
		}

		try {
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}
		ArrayList<Edge> finalPath = totalDistance();
		ArrayList<Node> trail = createTrail(finalPath);

		System.out.println("Shortest bitonic tour has distance " + distanceOfEdges(finalPath));
		output.println("Shortest bitonic tour has distance " + distanceOfEdges(finalPath));
		printTrail(trail);
		System.out.println(printPredecessor(trail));
		output.println(printPredecessor(trail));
	}

	// copy of g.NodeList
	public ArrayList<Node> copyNodeList() {
		ArrayList<Node> cNL = new ArrayList<Node>();
		for (Node n : g.nodeList) {
			cNL.add(n);
		}
		return cNL;
	}

	// array of float values
	public float[] fValArray() {
		ArrayList<Node> cNL = copyNodeList();
		float[] flArr = new float[cNL.size()];
		int index = 0;

		for (Node n : cNL) {
			flArr[index] = n.fVal();
			index++;
		}
		return flArr;
	}

	// returns list of cities in descending order by val
	public ArrayList<Node> sortedCities() {

		float[] flArr = fValArray();
		// Arrays.sort() was found at
		// https://www.tutorialspoint.com/java/util/arrays_sort_float.htm
		Arrays.sort(flArr);
		int index = 0;
		ArrayList<Node> cNL = copyNodeList();
		ArrayList<Node> sorted = new ArrayList<Node>();
		for (int i = cNL.size() - 1; i > -1; i--) {
			for (int j = 0; j < cNL.size(); j++) {
				if (cNL.get(j).fVal() == flArr[i] && index < cNL.size()) {
					sorted.add(cNL.get(j));
					index++;
				}
			}
		}
		return sorted;
	}
	 
	// returns list of bitonic tour edges
	public ArrayList<Edge> getDistance(int i, int j) {

		ArrayList<Edge> path = new ArrayList<Edge>();

		if (i >= j) {
			return new ArrayList<Edge>();
		}
		if (i == 0 && j == 1) {
			path.add(sC.get(i).findEdge(sC.get(j)));
		} else if (i < j - 1) {
			path = getDistance(i, j - 1);
			path.add(sC.get(j - 1).findEdge(sC.get(j)));
		} else if (i == j - 1) {
			int minDistance = Integer.MAX_VALUE;
			ArrayList<Edge> minPath = new ArrayList<Edge>();
			for (int k = 0; k < j - 1; k++) {
				ArrayList<Edge> tempPath = getDistance(k, j - 1);
				tempPath.add(sC.get(k).findEdge(sC.get(j)));
				int tempDistance = distanceOfEdges(tempPath);
				if (tempDistance < minDistance) {
					minDistance = tempDistance;
					minPath = tempPath;
				}
			}
			path = minPath;
		}
		return path;
	}

	// returns total distance of bitonic tour edges
	public int distanceOfEdges(ArrayList<Edge> edges) {

		int total = 0;
		for (Edge e : edges) {
			total += e.getDist();
		}
		return total;
	}

	// returns list of edges that make up total distance
	public ArrayList<Edge> totalDistance() {

		ArrayList<Edge> edges = getDistance(sC.size() - 2, sC.size() - 1);
		edges.add(sC.get(sC.size() - 2).findEdge(sC.get(sC.size() - 1)));

		return edges;
	}

	// returns bitonic tour path
	public ArrayList<Node> createTrail(ArrayList<Edge> edgeList) {
		ArrayList<Edge> edgeCopy = new ArrayList<Edge>();

		for (Edge e : edgeList) {
			edgeCopy.add(e);
		}

		ArrayList<Node> pathOne = new ArrayList<Node>();
		ArrayList<Node> pathTwo = new ArrayList<Node>();

		pathOne.add(sC.get(0));
		pathTwo.add(sC.get(0));

		while (!edgeCopy.isEmpty()) {
			Edge e = edgeCopy.remove(0);
			Node one = pathOne.get(pathOne.size() - 1);
			Node two = pathTwo.get(pathTwo.size() - 1);

			Node head = e.getHead();
			Node tail = e.getTail();
			if (head.equals(one)) {
				pathOne.add(tail);
			} else if (tail.equals(one)) {
				pathOne.add(head);
			} else if (head.equals(two)) {
				pathTwo.add(tail);
			} else {
				pathTwo.add(head);
			}
		}
		pathTwo.remove(pathTwo.size() - 1);
		while (!pathTwo.isEmpty()) {
			pathOne.add(pathTwo.remove(pathTwo.size() - 1));
		}
		return pathOne;
	}

	// prints bitonic tour path
	public void printTrail(ArrayList<Node> trail) {
		System.out.print("\nTour is ");
		output.print("\nTour is ");
		for (Node n : trail) {
			System.out.print(n.getAbbrev());
			output.print(n.getAbbrev());
		}
	}

	// returns String of predecessor cities
	public String printPredecessor(ArrayList<Node> trail) {
		
		String s = "\n\n   City on Path\t   Predecessor(s)\n";
		s += "   ------------    --------------\n";

		s += "\t" + trail.get(0).getAbbrev() + "\t     null";
		for (int i = 0; i < trail.size()-1; i++) {
			s += "\n\t" + trail.get(i + 1).getAbbrev() + "\t     ";
			for (int j = i; j >= 0; j--) {
				s += trail.get(j).getAbbrev() + " ";
			}
		}
		return s;
	}
}