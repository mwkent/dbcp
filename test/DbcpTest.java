import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import Jama.Matrix;

/**
 * @author Matt Kent
 */
public class DbcpTest {

	private static Graph triangleGraph = new Graph(3);
	static {
		triangleGraph.addEdge(0, 1);
		triangleGraph.addEdge(1, 2);
		triangleGraph.addEdge(2, 0);
	};

	private static Graph noTriangleGraph = new Graph(3);
	static {
		triangleGraph.addEdge(0, 1);
		triangleGraph.addEdge(1, 2);
	};

	@Test
	public void testFindTriangle() {
		final List<Integer> triangle = new ArrayList<>(Arrays.asList(0, 1, 2));
		assertEquals(triangle, Dbcp.findTriangle(triangleGraph));
	}

	@Test
	public void testFindTriangleNoTriangle() {
		assertEquals(null, Dbcp.findTriangle(noTriangleGraph));
	}

	@Test
	public void testThreeConnected() {
		runAndAssert(Graph.threeConnected);
	}

	@Test
	public void testOpsahlPowergrid() {
		final String filepath = "data/opsahl-powergrid-filtered.txt";
		final Graph graph = DatasetFilter.getGraphFrom(filepath, 500);
		System.out.println("Graph = " + graph);
		runAndAssert(graph);
	}

	@Test
	@Disabled
	public void testOpsahlPowergrid4000() {
		final String filepath = "data/opsahl-powergrid-filtered-4000.txt";
		final Graph graph = DatasetFilter.getGraphFrom(filepath, 4000);
		System.out.println("Graph = " + graph);
		runAndAssert(graph);
	}

	@Test
	@Disabled
	public void testOpsahlPowergridOriginal() {
		final String filepath = "data/opsahl-powergrid-original.txt";
		final Graph graph = DatasetFilter.getGraphFrom(filepath, 4941);
		System.out.println("Graph = " + graph);
		runAndAssert(graph);
	}

	@Test
	public void testAirTrafficControl() {
		final String filepath = "data/air-traffic-control.txt";
		final Graph graph = DatasetFilter.getGraphFrom(filepath, 1226);
		System.out.println("Graph = " + graph);
		runAndAssert(graph);
	}

	@Test
	public void testJazzMusicians() {
		final String filepath = "data/jazz-musicians.txt";
		final Graph graph = DatasetFilter.getGraphFrom(filepath, 198);
		System.out.println("Graph = " + graph);
		runAndAssert(graph);
	}

	@Test
	public void test() {
		final double[][] lhsArray = { //
				{ 1, 0, 0, 0, 0 }, //
				{ 0, 1, 0, 0, 0 }, //
				{ 0, 0, 1, 0, 0 }, //
				{ 1.0 / 3, 1.0 / 3, 0, -1, 1.0 / 3 }, //
				{ 1.0 / 3, 0, 1.0 / 3, 1.0 / 3, -1 } }; //
		final double[] rhsArray = { 0, 1, 0, 0, 0 };
		final Matrix matrix = Dbcp.findEmbedding(lhsArray, rhsArray);
		MatrixUtil.print(matrix);
	}

	@Test
	public void test2() {
		System.out.println(1.0 / 3);
		final double[][] vals = { { 1., 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
		final Matrix A = new Matrix(vals);
		final double[] rhsArray = { 1, 2, 3 };
		final Matrix rhs = new Matrix(rhsArray, 3);
		final Matrix x = A.solve(rhs);
		MatrixUtil.print(x);
	}

	@Test
	public void testLarge3Connected() {
		final Graph graph = GraphGenerator.get3Connected(500);
		System.out.println("Graph = " + graph);
		runAndAssert(graph);
	}

	@Test
	public void testLarger3Connected() {
		final Graph graph = GraphGenerator.get3Connected(750);
		System.out.println("Graph = " + graph);
		runAndAssert(graph);
	}

	@Test
	public void testLargeCompleteGraph() {
		System.out.println("testLargeCompleteGraph");
		final Graph graph = GraphGenerator.getComplete(200);
		System.out.println("Graph = " + graph);
		runAndAssert(graph);
	}

	private void runAndAssert(final Graph graph) {
		final Pair<List<Integer>, List<Integer>> solution = Dbcp.solve(graph);
		final int leftSize = solution.getLeft().size();
		assertTrue(isAboutHalf(leftSize, graph.getNumVerts()));
		final int rightSize = solution.getRight().size();
		assertTrue(isAboutHalf(rightSize, graph.getNumVerts()));
		System.out.println("First partition = " + solution.getLeft());
		System.out.println("Second partition = " + solution.getRight());
		final boolean weightSumIsZero = isWeightSumZero(graph, solution);
		assertTrue(weightSumIsZero);
	}

	private boolean isWeightSumZero(final Graph graph, final Pair<List<Integer>, List<Integer>> partitions) {
		final List<Integer> leftPartition = partitions.getLeft();
		final boolean isLeftPartitionWeightSumZero = isWeightSumZero(graph, leftPartition);
		final List<Integer> rightPartition = partitions.getRight();
		final boolean isRightPartitionWeightSumZero = isWeightSumZero(graph, rightPartition);
		return isLeftPartitionWeightSumZero && isRightPartitionWeightSumZero;
	}

	private boolean isWeightSumZero(final Graph graph, final List<Integer> partition) {
		final int partitionWeightSum = getWeightSum(graph, partition);
		if (MathUtil.isEven(partition.size())) {
			return partitionWeightSum == 0;
		}
		return Math.abs(partitionWeightSum) == 1;
	}

	private int getWeightSum(final Graph graph, final List<Integer> vertices) {
		return vertices.stream().mapToInt(vert -> graph.getWeight(vert)).sum();
	}

	private boolean isAboutHalf(final int computedSize, final int expectedTotal) {
		return computedSize == expectedTotal / 2 || computedSize == expectedTotal / 2 + 1;
	}

}
