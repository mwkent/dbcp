import java.util.Arrays;

import Jama.Matrix;

/**
 * @author Matt Kent
 */
public class MatrixUtil {
	public static void print(final Matrix matrix) {
		final double[][] matrixArray = matrix.getArray();
		for (int rowNumber = 0; rowNumber < matrixArray.length; rowNumber++) {
			final double[] row = matrixArray[rowNumber];
			System.out.println(Arrays.toString(row));
		}
	}

}
