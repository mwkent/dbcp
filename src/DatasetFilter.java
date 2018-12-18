import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getFirst(final String line) {
		return line.substring(0, line.indexOf(' '));
	}

	private static String getSecond(final String line) {
		return line.substring(line.indexOf(' ') + 1);
	}

	private static int getOneIndexed(final int vert) {
		return vert - 1;
	}

	public static void main(final String[] args) {
		filterOpsahlFile(4000, "data/opsahl-powergrid-filtered-4000.txt");
	}

}
