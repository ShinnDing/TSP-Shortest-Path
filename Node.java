import java.util.ArrayList;

// A node of a graph for the Spring 2018 ICS 340 program
public class Node {
	String name;
	String val; // The value of the Node, if String
	String abbrev; // The abbreviation for the Node
	ArrayList<Edge> outgoingEdges;
	ArrayList<Edge> incomingEdges;
	Graph g;

	public Node(String theAbbrev) {
		setAbbrev(theAbbrev);
		val = null;
		name = null;
		outgoingEdges = new ArrayList<Edge>();
		incomingEdges = new ArrayList<Edge>();
	}

	public String getAbbrev() {
		return abbrev;
	}
	public String getName() {
		return name;
	}
	public String getVal() {
		return val;
	}

	public ArrayList<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}

	public ArrayList<Edge> getIncomingEdges() {
		return incomingEdges;
	}

	public void setAbbrev(String theAbbrev) {
		abbrev = theAbbrev;
	}

	public void setName(String theName) {
		name = theName;
	}

	public void setVal(String theVal) {
		val = theVal;
	}

	public void addOutgoingEdge(Edge e) {
		outgoingEdges.add(e);
	}

	public void addIncomingEdge(Edge e) {
		incomingEdges.add(e);
	}

	// equals method to compare Nodes - they are the same if they have the same name
	public boolean equals(Node other) {
		return this.getName().equalsIgnoreCase(other.getName());
	}

	// returns distance from current city to the next city
	public int findDistance(Node other) {
		// find distance between node we're at and ends at the other node
		for (Edge e : outgoingEdges) {
			if (e.getHead().equals(other)) {
				return e.getDist();
			}
		}
		return -1;
	}

	// converts String val to float fVal
	public float fVal() {
		// found at https://www.javatpoint.com/java-string-to-float
		float fVal = Float.parseFloat(getVal());
		return fVal;
	}

	// returns edge between current node and other
	public Edge findEdge(Node other) {
		for (Edge e : outgoingEdges) {
			if (e.getHead().equals(other)) {
				return e;
			}
		}
		return null;
	}
	
	//helper method
	public boolean isStart() {
		return val.equalsIgnoreCase("S");
	}
	
	//helper method
	public boolean isGoal() {
		return val.equalsIgnoreCase("G");
	}
	
	public String toString() {
		return getAbbrev();
	}
	
    public boolean visited() {
        return true;
    }
}
