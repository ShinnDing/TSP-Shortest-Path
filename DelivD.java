import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.Random;

// Class DelivD does the work for deliverable DelivD of the Prog340

public class DelivD {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	int longEdge = 100000;
	Random rand = new Random();
	// VERBOSE set to true will print all test code.
	final boolean VERBOSE = false;

	public DelivD(File in, Graph gr) {
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
		Node[] sTGE = startToGoal(addLongEdges(copyNodeList()));

		addLongEdges(copyNodeList());
		betterPath(sTGE);
		output.flush();
	}

	// copy of g.NodeList
	public ArrayList<Node> copyNodeList() {
		ArrayList<Node> cNL = new ArrayList<Node>();
		for (Node n : g.nodeList) {
			cNL.add(n);
		}
		return cNL;
	}

	// Replaces null edges with edges of 100000.
	public ArrayList<Node> addLongEdges(ArrayList<Node> cities) {
		int eCountCities = cities.size();
		int eCountEdges = 0;

		for (Node city : cities) {
			eCountEdges = city.getOutgoingEdges().size();
			while (eCountEdges < eCountCities) {
				for (Node nextCity : cities) {
					if (city.findEdge(nextCity) == null) {
						Edge eTemp = new Edge(city, nextCity, longEdge);
						city.addOutgoingEdge(eTemp);
						eCountEdges++;
					}
				}
			}
			if (VERBOSE) {
				System.out.print("\n  ");
				output.print("\n  ");
				for (Edge e : city.getOutgoingEdges()) {
					System.out.print(" " + e.getHead() + " ");
					output.print(" " + e.getHead() + " ");
				}
				System.out.print("\n" + city.getAbbrev() + " " + city.getOutgoingEdges() + "\n\n");
				output.print("\n" + city.getAbbrev() + " " + city.getOutgoingEdges() + "\n\n");
			}
		}
		return cities;
	}

	// Places Start node in first index, Goal node in last index,
	// and places all other nodes in between start and goal.
	// Uses an array to specific index assignments.
	public Node[] startToGoal(ArrayList<Node> path) {

		Node[] sTG = new Node[path.size()];

		int index = 1;
		if (VERBOSE) {
			System.out.print("Path to copy: ");
			output.print("Path to copy: ");
		}
		for (Node n : path) {
			if (VERBOSE) {
				System.out.print(n + " ");
				output.print(n + " ");
			}
			if (!n.isGoal() && !n.isStart()) {
				sTG[index] = n;
				index++;
			} else if (n.isGoal()) {
				sTG[sTG.length - 1] = n;
			} else if (n.isStart()) {
				sTG[0] = n;
			}
		}
		if (VERBOSE) {
			System.out.print("\nPath copied from Start to Goal: ");
			output.print("\nPath copied from Start to Goal: ");
			for (Node c : sTG) {
				System.out.print(c.getAbbrev() + " ");
				output.print(c.getAbbrev() + " ");
			}
			System.out.println(" ");
			output.print(" ");
		}
		return sTG;
	}

	// Calculates distance of all cities on the path.
	public int calculateDistance(Node[] cities) {

		int distance = 0;

		for (int i = 0; i < cities.length - 1; i++) {
			distance += cities[i].findDistance(cities[i + 1]);
		}
		return distance;
	}
	
	// Calculates distance of all cities on the path
	// with edges less than 100000.
	public int calculateShortDistance(Node[] cities) {

		int distance = 0;

		for (int i = 0; i < cities.length - 1; i++) {
			if (cities[i].findDistance(cities[i + 1]) < longEdge) {
				distance += cities[i].findDistance(cities[i + 1]);
			}
		}
		return distance;
	}

	// Returns the maximum edge in the cities list.
	public int findMaxEdge(Node[] cities) {

		int max = cities[0].findDistance(cities[1]);
		int index = 1;

		for (int i = 1; i < cities.length - 3; i++) {

			int currentDist = cities[i].findDistance(cities[i + 1]);
			if (currentDist > max) {
				max = currentDist;
				index = i + 1;
			}
		}
		return index;
	}

	// Swaps cities with a max edge with a city 
	// with a random city between start and goal.
	public void swapTwoCities(Node[] cities) {

		int cityOneIndex = findMaxEdge(cities);
		int cityTwoIndex = rand.nextInt(cities.length - 2);
		
		if (cityTwoIndex == 0) {
			cityTwoIndex = cityTwoIndex + 1;
		}

		Node temp = cities[cityOneIndex];
		cities[cityOneIndex] = cities[cityTwoIndex];
		cities[cityTwoIndex] = temp;
	}

	// Calculates and returns the value of count used
	// in the for-loop for finding a better path.
	public int count(Node[] cities) {

		int count = 0;
		int citySquared = cities.length * cities.length;

		if (cities.length > 100) {
			count = 200 * citySquared;
		}
		if (cities.length > 50) {
			count = 100 * citySquared;
		}
		if (cities.length > 20) {
			count = 50 * citySquared;
		} else {
			count = 2 * citySquared;
		}
		return count;
	}

	// Determines whether a better path is found.
	public void betterPath(Node[] cities) {

		int count = count(cities);
		int distance = calculateDistance(cities);
		int startOver = 0;
		int counter = 0;

		System.out.println("Dist = " + distance + ": " + printCities(cities));
		output.print("Dist = " + distance + ": " + printCities(cities));

		for (int c = startOver; c < count; c++) {
			swapTwoCities(cities);
			int calc = calculateDistance(cities);

			if (distance == calc || distance < calc) {
				if (VERBOSE) {
					System.out.println("Dist = " + distance + ": " + printCities(cities));
					output.print("Dist = " + distance + ": " + printCities(cities));
				}
				swapTwoCities(cities);
				counter++;
			} else if ((calc > 0) && (distance > calc)) {
				distance = calc;
				counter++;
				System.out.println("Dist = " + distance + ": " + printCities(cities));
			}
			if ((c == count / 10) && (findMaxEdge(cities) == longEdge)) {
				distance = calculateShortDistance(cities);
				c = startOver;
				if (VERBOSE) {
				System.out.println("Too many long edges.  Start brute force calculation.");
				}
			}
		}
		System.out.println("Total of " + counter + " paths tried.");
		output.print("Total of " + counter + " paths tried.");
	}

	// Returns a string of abbreviations for cities
	// on the current path.
	public String printCities(Node[] cities) {

		String abbrevs = new String();

		for (Node n : cities) {
			abbrevs += n.getAbbrev() + " ";
		}
		return abbrevs;
	}
}
