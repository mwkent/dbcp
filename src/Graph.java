import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Represents an undirected graph data structure with weighted nodes
 * 
 * @author Matt Kent
 *
 */
public class Graph {

	private final int numVerts;
	private final List<List<Integer>> adjListArray;
	private final Set<Edge> edges;
	private final Map<Integer, Integer> nodesToWeights;

	private static Graph twoConnected = new Graph(4);
	static {
		twoConnected.addEdge(0, 1);
		twoConnected.addEdge(1, 2);
		twoConnected.addEdge(2, 3);
		twoConnected.addEdge(0, 3);
		twoConnected.addWeight(0, 1);
		twoConnected.addWeight(1, 1);
		twoConnected.addWeight(2, -1);
		twoConnected.addWeight(3, -1);
	};

	static Graph threeConnected = new Graph(6);
	static {
		threeConnected.addEdge(0, 1);
		threeConnected.addEdge(0, 2);
		threeConnected.addEdge(0, 3);
		threeConnected.addEdge(1, 2);
		threeConnected.addEdge(1, 4);
		threeConnected.addEdge(2, 5);
		threeConnected.addEdge(3, 4);
		threeConnected.addEdge(3, 5);
		threeConnected.addEdge(4, 5);
		threeConnected.addWeight(0, 1);
		threeConnected.addWeight(1, -1);
		threeConnected.addWeight(2, 1);
		threeConnected.addWeight(3, -1);
		threeConnected.addWeight(4, -1);
		threeConnected.addWeight(5, 1);
	};

	Graph(final int numVerts) {
		this.numVerts = numVerts;
		adjListArray = new ArrayList<>(numVerts);
		for (int i = 0; i < numVerts; i++) {
			adjListArray.add(new ArrayList<>());
		}
		edges = new HashSet<>();
		nodesToWeights = new HashMap<>(numVerts);
	}

	static class Edge {
		private final Set<Integer> edge;
		private final int first;
		private final int second;

		Edge(final int first, final int second) {
			this.edge = new HashSet<>();
			edge.add(first);
			edge.add(second);
			this.first = first;
			this.second = second;
		}

		int getFirst() {
			return first;
		}

		int getSecond() {
			return second;
		}

		@Override
		public boolean equals(final Object o) {
			if (o instanceof Edge) {
				final Edge that = (Edge) o;
				return this.edge.equals(that.edge);
			}
			return false;
		}

		// 1 and 2 should hash to the same value as 2 and 1
		@Override
		public int hashCode() {
			return Objects.hash(edge);
		}
	}

	Set<Edge> getEdges() {
		return edges;
	}

	int getNumVerts() {
		return numVerts;
	}

	List<Integer> getNeighbors(final int vert) {
		return adjListArray.get(vert);
	}

	boolean containsEdge(final int src, final int dest) {
		return adjListArray.get(src).contains(dest);
	}

	void addEdge(final int src, final int dest) {
		adjListArray.get(src).add(dest);
		adjListArray.get(dest).add(src);
		edges.add(new Edge(src, dest));
	}

	void addWeight(final int node, final int weight) {
		nodesToWeights.put(node, weight);
	}

	int getWeight(final int node) {
		return nodesToWeights.get(node);
	}

	// does not guarantee the graph will be 3-connected
	void make3Connected() {
		final int minNumNeighbors = 3;
		final Random random = new Random();
		for (int vert = 0; vert < numVerts; vert++) {
			final List<Integer> neighbors = getNeighbors(vert);
			while (neighbors.size() < minNumNeighbors) {
				final int neighbor = random.nextInt(numVerts);
				if (!neighbors.contains(neighbor)) {
					addEdge(vert, neighbor);
				}
			}
		}
	}

	@Override
	public String toString() {
		return numVerts + " vertices\n" + edges.size() + " edges\n" + "adjListArray: " + adjListArray
				+ "\nnodesToWeights: " + nodesToWeights;
	}

}
