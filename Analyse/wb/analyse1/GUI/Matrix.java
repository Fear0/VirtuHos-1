package wb.analyse1.GUI;

import java.util.Arrays;
import java.util.Random;

/**
 *
 */
public class Matrix {
	private int Matrix_size = new Random().nextInt(4) + 2;
	private int[][] Haufigkeit = new int[Matrix_size][Matrix_size];

	/**
	 * The main Constructor
	 */

	public Matrix() {
		int o = 0;
		while (o < Matrix_size - 1) {
			o = 0;
			for (int i = 0; i < Matrix_size; i++) {
				for (int j = 0; j < i; j++) {
					int value = new Random().nextInt(8);
					Haufigkeit[i][j] = value;
					Haufigkeit[j][i] = value;
					if (value == 0)
						o++;
				}
			}
		}
	}

	/**
	 * Matrix Getter
	 *
	 * @return
	 */
	public int[][] Matrix_getter() {
		return this.Haufigkeit;
	}

	/**
	 * Only for testing and developing
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Matrix tst = new Matrix();
		System.out.println(Arrays.deepToString(tst.Matrix_getter()));
	}
}
