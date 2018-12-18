import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import Jama.Matrix;

/**
 * @author Matt Kent
 * 
 *         Doubly Balanced Connected Graph Partitioning
 */
public class Dbcp {

	public static Pair<List<Integer>, List<Integer>> solve(final Graph graph) {
		assert (MathUtil.isEven(graph.getNumVerts()));
		final List<Integer> triangle = findTriangle(graph);
		if (triangle != null) {
			final Map<Graph.Edge, Double> c = getC(graph);
			final List<Point> embedding = findPointEmbedding(graph, triangle, c);
			// System.out.println("embedding = " + embedding);
			return getPartitioningFrom(graph, embedding);
		}
		throw new IllegalArgumentException("Graph does not contain triangle");
	}

	private static Pair<List<Integer>, List<Integer>> getPartitioningFrom(final Graph graph,
			final List<Point> embedding) {
		for (final Pair<Point, Point> pointPair : getAllPairs(embedding)) {
			// System.out.println("Point pair = " + pointPair);
			final LineEquation lineEquation = new LineEquation(pointPair.getLeft(), pointPair.getRight());
			// System.out.println("lineEquation = " + lineEquation);
			final List<Point> greaterPartition = new ArrayList<>();
			final List<Point> lessPartition = new ArrayList<>();
			for (final Point point : embedding) {
				if (!point.equals(pointPair.getLeft()) && !point.equals(pointPair.getRight())) {
					final double lineY = lineEquation.getY(point.getX());
					// System.out.println("point = " + point);
					// System.out.println("lineY = " + lineY);
					if (Double.isNaN(lineY)) {
						assert (pointPair.getLeft().getX() == pointPair.getRight().getX());
						final double lineX = pointPair.getLeft().getX();
						if (point.getX() > lineX) {
							greaterPartition.add(point);
						} else if (point.getX() < lineX) {
							lessPartition.add(point);
						} else {
							final String pointsString = String.join(", ", point.toString(),
									pointPair.getLeft().toString(), pointPair.getRight().toString());
							throw new IllegalStateException("The following three points are colinear: " + pointsString);
						}
					} else {
						if (point.getY() > lineY) {
							greaterPartition.add(point);
						} else if (point.getY() < lineY) {
							lessPartition.add(point);
						} else {
							final String pointsString = String.join(", ", point.toString(),
									pointPair.getLeft().toString(), pointPair.getRight().toString());
							throw new IllegalStateException("The following three points are colinear: " + pointsString);
						}
					}
				}
			}
			if (lessPartition.size() == greaterPartition.size()) {
				final List<Integer> lessPartitionVertices = lessPartition.stream().map(p -> p.getVert())
						.collect(Collectors.toList());
				final int lessPartitionWeightSum = getSumOfWeights(graph, lessPartitionVertices);
				final List<Integer> greaterPartitionVertices = greaterPartition.stream().map(p -> p.getVert())
						.collect(Collectors.toList());
				final int greaterPartitionWeightSum = getSumOfWeights(graph, greaterPartitionVertices);
				if (Math.abs((lessPartitionWeightSum + graph.getWeight(pointPair.getLeft().getVert()))
						- (greaterPartitionWeightSum + +graph.getWeight(pointPair.getRight().getVert()))) <= 2) {
					lessPartitionVertices.add(pointPair.getLeft().getVert());
					greaterPartitionVertices.add(pointPair.getRight().getVert());
					return Pair.of(lessPartitionVertices, greaterPartitionVertices);
				} else if (Math.abs((lessPartitionWeightSum + graph.getWeight(pointPair.getRight().getVert()))
						- (greaterPartitionWeightSum + +graph.getWeight(pointPair.getLeft().getVert()))) <= 2) {
					lessPartitionVertices.add(pointPair.getRight().getVert());
					greaterPartitionVertices.add(pointPair.getLeft().getVert());
					return Pair.of(lessPartitionVertices, greaterPartitionVertices);
				}
			}
		}
		throw new IllegalArgumentException("Could not find solution for graph");
	}

	static int getSumOfWeights(final Graph graph, final List<Integer> vertices) {
		int sum = 0;
		for (final int v : vertices) {
			sum += graph.getWeight(v);
		}
		return sum;
	}

	// order is not considered, and a pair consisting of the same point is not
	// returned
	static List<Pair<Point, Point>> getAllPairs(final List<Point> points) {
		final List<Pair<Point, Point>> pointPairs = new ArrayList<>();
		for (int i = 0; i < points.size(); i++) {
			for (int j = i + 1; j < points.size(); j++) {
				pointPairs.add(Pair.of(points.get(i), points.get(j)));
			}
		}
		return pointPairs;
	}

	public static List<Integer> findTriangle(final Graph graph) {
		for (final Graph.Edge edge : graph.getEdges()) {
			for (int node = 0; node < graph.getNumVerts(); node++) {
				if (graph.containsEdge(node, edge.getFirst()) && graph.containsEdge(node, edge.getSecond())) {
					return new ArrayList<>(Arrays.asList(edge.getFirst(), edge.getSecond(), node));
				}
			}
		}
		return null;
	}

	static Map<Graph.Edge, Double> getC(final Graph graph) {
		final Random random = new Random();
		final Set<Graph.Edge> edges = graph.getEdges();
		final Map<Graph.Edge, Double> result = new HashMap<>(edges.size());
		for (final Graph.Edge edge : edges) {
			double cuv = 0.0;
			boolean isZero = true;
			while (isZero) {
				cuv = random.nextDouble();
				isZero = cuv == 0.0;
			}
			result.put(edge, cuv);
		}
		return result;
	}

	static List<Point> findPointEmbedding(final Graph graph, final List<Integer> triangle,
			final Map<Graph.Edge, Double> c) {
		final int numVerts = graph.getNumVerts();
		final double[][] xLeftSideArray = new double[numVerts][numVerts];
		final double[] xRightSideArray = new double[numVerts];
		final double[][] yLeftSideArray = new double[numVerts][numVerts];
		final double[] yRightSideArray = new double[numVerts];
		assert (triangle.size() == 3);
		for (int i = 0; i < triangle.size(); i++) {
			final int triangleVert = triangle.get(i);
			xLeftSideArray[triangleVert][triangleVert] = 1.0;
			yLeftSideArray[triangleVert][triangleVert] = 1.0;
		}
		int triangleVert = triangle.get(0);
		xRightSideArray[triangleVert] = 0;
		yRightSideArray[triangleVert] = 0;
		triangleVert = triangle.get(1);
		xRightSideArray[triangleVert] = 1;
		yRightSideArray[triangleVert] = 0;
		triangleVert = triangle.get(2);
		xRightSideArray[triangleVert] = 0;
		yRightSideArray[triangleVert] = 1;
		for (int vert = 0; vert < numVerts; vert++) {
			if (!triangle.contains(vert)) {
				xLeftSideArray[vert][vert] = -1;
				yLeftSideArray[vert][vert] = -1;
				final List<Integer> neighbors = graph.getNeighbors(vert);
				final int finalVert = vert;
				final double cv = neighbors.stream().mapToDouble(neighbor -> c.get(new Graph.Edge(finalVert, neighbor)))
						.sum();
				for (final int neighbor : neighbors) {
					final double cuv = c.get(new Graph.Edge(vert, neighbor));
					xLeftSideArray[vert][neighbor] = cuv / cv;
					yLeftSideArray[vert][neighbor] = cuv / cv;
				}
			}
		}
		return findPointEmbedding(xLeftSideArray, xRightSideArray, yLeftSideArray, yRightSideArray);
	}

	public static List<Point> findPointEmbedding(final double[][] xLeftSideArray, final double[] xRightSideArray,
			final double[][] yLeftSideArray, final double[] yRightSideArray) {
		final Matrix xValues = findEmbedding(xLeftSideArray, xRightSideArray);
		final Matrix yValues = findEmbedding(yLeftSideArray, yRightSideArray);
		final List<Point> result = new ArrayList<>(xValues.getRowDimension());
		assert (xValues.getRowDimension() == yValues.getRowDimension());
		for (int i = 0; i < xValues.getRowDimension(); i++) {
			final Point p = new Point(xValues.get(i, 0), yValues.get(i, 0), i);
			result.add(p);
		}
		return result;
	}

	public static Matrix findEmbedding(final double[][] lhsArray, final double[] rhsArray) {
		final Matrix leftSide = new Matrix(lhsArray);
		final Matrix rightSide = new Matrix(rhsArray, rhsArray.length);
		final Matrix result = leftSide.solve(rightSide);
		return result;
	}

}
