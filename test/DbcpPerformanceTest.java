import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Matt Kent
 */
public class DbcpPerformanceTest {

	private static Graph large3ConnectedGraph;
	private static Graph larger3ConnectedGraph;
	private static Graph largeCompleteGraph;
	private static Graph airTrafficControlGraph;
	private static Graph jazzMusiciansGraph;
	private static Graph powerGridGraph;

	@BeforeAll
	public static void setup() throws InterruptedException {
		large3ConnectedGraph = GraphGenerator.get3Connected(500);
		larger3ConnectedGraph = GraphGenerator.get3Connected(750);
		largeCompleteGraph = GraphGenerator.getComplete(200);
		airTrafficControlGraph = DatasetFilter.getGraphFrom("data/air-traffic-control.txt", 1226);
		jazzMusiciansGraph = DatasetFilter.getGraphFrom("data/jazz-musicians.txt", 198);
		powerGridGraph = DatasetFilter.getGraphFrom("data/opsahl-powergrid-filtered.txt", 500);
	}

	@Test
	public void testLarge3Connected() {
		Dbcp.solve(large3ConnectedGraph);
	}

	@Test
	public void testLarger3Connected() {
		Dbcp.solve(larger3ConnectedGraph);
	}

	@Test
	public void testLargeComplete() {
		Dbcp.solve(largeCompleteGraph);
	}

	@Test
	public void testAirTrafficControl() {
		Dbcp.solve(airTrafficControlGraph);
	}

	@Test
	public void testJazzMusicians() {
		Dbcp.solve(jazzMusiciansGraph);
	}

	@Test
	public void testOpsahlPowergrid() {
		Dbcp.solve(powerGridGraph);
	}

}
