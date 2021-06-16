import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

// Class DelivC does the work for deliverable DelivC of the Prog340
public class DelivC {
	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	boolean hitDepthBound;
	int bound;

	public DelivC(File in, Graph gr) {
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
		System.out.println();
		output.println();
		output.flush();
		iterativeDS();
	}

	// copy of g.NodeList
	public ArrayList<Node> copyNodeList() {
		ArrayList<Node> cNL = new ArrayList<Node>();
		for (Node n : g.nodeList) {
			cNL.add(n);
		}
		return cNL;
	}

	// returns node of start city
	public Node startCity() {
		ArrayList<Node> cNL = copyNodeList();
		for (Node n : cNL) {
			if (n.isStart()) {
				return n;
			}
		}
		return null;
	}

	// returns node of start city
	public Node goalCity() {
		ArrayList<Node> cNL = copyNodeList();
		for (Node n : cNL) {
			if (n.isGoal()) {
				return n;
			}
		}
		return null;
	}

	// Adapted from the Iterative Deepening algorithm in Figure 3.8 of
	// "Artificial Intelligence, Foundations of Computational
	// Agents, 2nd Edition" ~ David L. Poole & Alan K. Mackworth
	public ArrayList<Node> iterativeDS() {
		bound = 0;
		// infinite loop until hitDepthBound boolean is created
		// outside of method.
		do {
			hitDepthBound = false;
			ArrayList<Node> temp = new ArrayList<Node>();
			temp.add(startCity());
			ArrayList<Node> res = depthBS(temp, bound);

			if (res != null) {
				if (res.get(res.size() - 1) == goalCity()) {
					System.out.print("Path found: ");
					output.print("Path found: ");
					for (Node n : res) {
						System.out.print(n.getAbbrev());
						output.print(n.getAbbrev());
					}
					bound = bound - 1;
					System.out.print(", length = " + bound + "\n\n");
					output.print(", length = " + bound + "\n\n");
				}
				return res;
			}
			bound++;
		} while (hitDepthBound);
		return null;
	}

	// Adapted from the Iterative Deepening algorithm in Figure 3.8 of
	// "Artificial Intelligence, Foundations of Computational
	// Agents, 2nd Edition" ~ David L. Poole & Alan K. Mackworth
	public ArrayList<Node> depthBS(ArrayList<Node> path, int b) {
		Node lastCity = path.get(path.size() - 1);
		if (b == 0) {
			System.out.print("Depth " + bound + ":\n");
			output.print("Depth " + bound + ":\n");
			for (Node n : path) {
				System.out.print(n.getAbbrev());
				output.print(n.getAbbrev());
			}
			System.out.print("\n\n");
			output.print("\n\n");
		}
		if (lastCity.isGoal() && b > 0) {
			return path;
		} else if (b >= 0) {
			ArrayList<Edge> edges = sortEdges(lastCity.getOutgoingEdges());
			for (Edge e : edges) {
				ArrayList<Node> copyPath = copyPath(path);
				if (!copyPath.contains(e.getHead())) {
					copyPath.add(e.getHead());
					int dist = b - e.getTail().findDistance(e.getHead());
					ArrayList<Node> res = depthBS(copyPath, dist);

					if (res != null) {
						return res;
					}
				}
			}
		} else if (lastCity.getOutgoingEdges() != null) {
			hitDepthBound = true;
		}
		return null;
	}

	public ArrayList<Node> copyPath(ArrayList<Node> path) {
		ArrayList<Node> copy = new ArrayList<Node>();
		for (int i = 0; i < path.size(); i++) {
			copy.add(path.get(i));
		}
		return copy;
	}

	public ArrayList<Edge> sortEdges(ArrayList<Edge> edges) {
		ArrayList<Edge> list = new ArrayList<Edge>();
		int manyItems = 0;
		int current = 1;
		for (Edge e : edges) {
			if (manyItems == 0) {
				list.add(e);
				manyItems++;
			} else {
				current = manyItems;
				for (int i = manyItems - 1; i >= 0; i--) {
					if (e.getDist() < list.get(i).getDist()) {
						current = i;
					}
				}
				manyItems++;
				list.add(current, e);
			}
		}
		return list;
	}
}
