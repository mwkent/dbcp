import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Matt Kent
 */
public class DbcpPerformanceTest {

	private static Graph large3ConnectedGraph;
	private static Graph largeCompleteGraph;

	@BeforeAll
	public static void setup() throws InterruptedException {
		large3ConnectedGraph = GraphGenerator.get3Connected(500);
		largeCompleteGraph = GraphGenerator.getComplete(200);
	}

	@Test
	public void testLarge3Connected() {
		Dbcp.solve(large3ConnectedGraph);
	}

	@Test
	public void testLargeComplete() {
		Dbcp.solve(largeCompleteGraph);
	}

}
