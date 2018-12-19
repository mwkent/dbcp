import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Used to convert datasets to Graph objects
 * 
 * @author Matt Kent
 */
public class DatasetFilter {

	public static void filterOpsahlFile(final int numVerts, final String filename) {
		final Path originalFilename = Paths.get("data/opsahl-powergrid-original.txt");
		final Path filteredFilepath = Paths.get(filename);
		try (final Stream<String> stream = Files.lines(originalFilename);
				final BufferedWriter writer = Files.newBufferedWriter(filteredFilepath)) {
			stream.filter(line -> getOneIndexed(Integer.valueOf(getFirst(line))) < numVerts
					&& getOneIndexed(Integer.valueOf(getSecond(line))) < numVerts)// .peek(System.out::println)
					.forEach(line -> {
						try {
							System.out.println(line);
							writer.write(line + "\n");
						} catch (final IOException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
					});
		} catch (final IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	static Graph getGraphFrom(final String datasetFilepath, final int numVerts) {
		try (final Stream<String> stream = Files.lines(Paths.get(datasetFilepath))) {
			final Graph graph = new Graph(numVerts);
			stream.forEach(line -> addEdgeFromDataset(graph, line));
			GraphGenerator.setWeights(graph);
			graph.make3Connected();
			return graph;
		} catch (final IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static void addEdgeFromDataset(final Graph graph, final String line) {
		final Pair<Integer, Integer> intPair = DatasetFilter.getNextIntPair(line);
		graph.addEdge(DatasetFilter.getOneIndexed(intPair.getLeft()), DatasetFilter.getOneIndexed(intPair.getRight()));
	}

	static Pair<Integer, Integer> getNextIntPair(final String line) {
		final Matcher matcher = Pattern.compile("\\d+").matcher(line);
		matcher.find();
		final Integer first = Integer.valueOf(matcher.group());
		matcher.find();
		final Integer second = Integer.valueOf(matcher.group());
		return Pair.of(first, second);
	}

	static String getFirst(final String line) {
		return line.substring(0, line.indexOf(' '));
	}

	static String getSecond(final String line) {
		return line.substring(line.indexOf(' ') + 1);
	}

	static int getOneIndexed(final int vert) {
		return vert - 1;
	}

	public static void main(final String[] args) {
		filterOpsahlFile(4000, "data/opsahl-powergrid-filtered-4000.txt");
	}

}
