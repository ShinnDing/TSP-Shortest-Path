import java.io.*;
import java.util.ArrayList;

// Class DelivA does the work for deliverable DelivA of the Prog340

public class DelivA {

	File inputFile;
	File outputFile; // can print to screen or print to output file
	PrintWriter output;
	Graph g;

	public DelivA(File in, Graph gr) {
		inputFile = in;
		g = gr;

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
		System.out.println("Path " + printItinerary() + " has distance " + totalDistance());
		output.println("Path " + printItinerary() + " has distance " + totalDistance());
		output.flush();
	}

	// returns start city
	public Node findStart() {

		for (Node n : g.nodeList) {
			if (n.getVal().equals("S")) {
				return n;
			}
		}
		return null;
	}

	// returns index of start city
	public int indexOfStart() {

		int index = 0;
		for (Node n : g.nodeList) {
			if (n.getVal().equals("S")) {
				return index;
			}
			index++;
		}
		return -1;
	}

	// returns number of nodes in nodeList
	public int length() {

		int count = 0;
		for (Node n : g.nodeList) {
			if (n != null) {
				count++;
			}
		}
		return count;
	}

	// returns an itinerary of cities
	public ArrayList<Node> findItinerary() {

		ArrayList<Node> nL = new ArrayList<Node>();
		int index = 0;

		for (Node n : g.nodeList) {
			if (index >= indexOfStart() && index < length()) {
				nL.add(n);
			}
			index++;
		}
		index = 0;
		for (Node n : g.nodeList) {
			if (index < indexOfStart()) {
				nL.add(n);
			}
			index++;
		}
		nL.add(findStart());
		return nL;
	}

	// returns a String of the ordered itinerary() abbreviations
	public String printItinerary() {

		String s = "";
		for (Node n : findItinerary()) {
			s += n.getAbbrev();
		}
		return s;
	}

	// Calculates total distance of all cities.
	public int totalDistance() {

		int total = 0;
		ArrayList<Node> nL = findItinerary();

		Node current = nL.remove(0);
		while (!nL.isEmpty()) {
			Node next = nL.remove(0);
			total += current.findDistance(next);
			current = next;
		}
		return total;
	}
}