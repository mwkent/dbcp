import java.util.Random;

/**
 * @author Matt Kent
 */
public class GraphGenerator {

	static Graph getComplete(final int numVerts) {
		final Graph graph = new Graph(numVerts);
		for (int i = 0; i < numVerts; i++) {
			for (int j = i + 1; j < numVerts; j++) {
				graph.addEdge(i, j);
			}
		}
		setWeights(graph);
		return graph;
	}

	static Graph get3Connected(final int numVerts) {
		final int k = 3;
		final int minSize = 3;
		if (numVerts < minSize) {
			throw new IllegalArgumentException("numVerts must be greater than " + minSize);
		}
		final Graph graph = new Graph(numVerts);
		for (int i = 0; i < minSize; i++) {
			for (int j = i + 1; j < minSize; j++) {
				graph.addEdge(i, j);
			}
		}
		final Random random = new Random();
		for (int i = minSize; i < numVerts; i++) {
			for (int j = 0; j < k; j++) {
				boolean edgeAdded = false;
				while (!edgeAdded) {
					final int vert = random.nextInt(i);
					if (!graph.containsEdge(i, vert)) {
						graph.addEdge(i, vert);
						edgeAdded = true;
					}
				}
			}
		}
		setWeights(graph);
		return graph;
	}

	static void setWeights(final Graph graph) {
		final int numVerts = graph.getNumVerts();
		for (int i = 0; i < numVerts / 2; i++) {
			graph.addWeight(i, 1);
		}
		for (int i = numVerts / 2; i < numVerts; i++) {
			graph.addWeight(i, -1);
		}
	}

}
