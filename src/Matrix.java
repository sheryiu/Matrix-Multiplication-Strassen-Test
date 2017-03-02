
public class Matrix {
	
	public int[][] data;
	public int size;

	public Matrix(int size) {
		data = new int[size][size];
		this.size = size;
	}
	
	public Matrix(int size, int[][] matrix) {
		this(size);
		data = matrix;
	}
	
	public void setEntry(int value, int i, int j) {
		data[i][j] = value;
	}
	
	public Matrix[] doubleBisection() {
		if (size >= 2) {
			int[][][] temp = new int[4][size/2][size/2];
			for (int i=0; i<size; i++) {
				for (int j=0; j<size; j++) {
					if (i < size/2 && j < size/2) {
						// top left submatrix --> [0]
						temp[0][i][j] = data[i][j];
					} else if (i >= size/2 && j < size/2) {
						// bottom left submatrix --> [2]
						temp[2][i-size/2][j] = data[i][j];
					} else if (i < size/2 && j >= size/2) {
						// top right submatrix --> [1]
						temp[1][i][j-size/2] = data[i][j];
					} else if (i >= size/2 && j >= size/2) {
						// bottom right submatrix --> [3]
						temp[3][i-size/2][j-size/2] = data[i][j];
					}
				}
			}
			return new Matrix[] {new Matrix(size/2, temp[0]), new Matrix(size/2, temp[1]), new Matrix(size/2, temp[2]), new Matrix(size/2, temp[3])};
		} else {
			return null;
		}
	}
	
	public void print() {
		// i is rows/vertical
		// j is columns
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				System.out.printf("%4d ", data[i][j]);
			}
			System.out.println();
		}
	}
	
	public static Matrix addition(Matrix a, Matrix b) {
		int[][] temp = new int[a.size][a.size];
		for (int i=0; i<a.size; i++) {
			for (int j=0; j<a.size; j++) {
				temp[i][j] = a.data[i][j]+b.data[i][j];
			}
		}
		return new Matrix(a.size, temp);
	}
	
	public static Matrix subtraction(Matrix a, Matrix b) {
		int[][] temp = new int[a.size][a.size];
		for (int i=0; i<a.size; i++) {
			for (int j=0; j<a.size; j++) {
				temp[i][j] = a.data[i][j]-b.data[i][j];
			}
		}
		return new Matrix(a.size, temp);
	}
	
	public static Matrix multiplication(Matrix a, Matrix b) {
		int[][] temp = new int[a.size][a.size];
		for (int i=0; i<a.size; i++) {
			for (int j=0; j<a.size; j++) {
				for (int k=0; k<a.size; k++) {
					temp[i][j] += a.data[i][k]*b.data[k][j];
				}
			}
		}
		return new Matrix(a.size, temp);
	}
	
	public static Matrix mergeBisection(int size, Matrix[] c) {
		int[][] temp = new int[size][size];
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				if (i < size/2 && j < size/2) {
					// top left submatrix --> [0]
					temp[i][j] = c[0].data[i][j];
				} else if (i >= size/2 && j < size/2) {
					// bottom left submatrix --> [2]
					temp[i][j] = c[2].data[i-size/2][j];
				} else if (i < size/2 && j >= size/2) {
					// top right submatrix --> [1]
					temp[i][j] = c[1].data[i][j-size/2];
				} else if (i >= size/2 && j >= size/2) {
					// bottom right submatrix --> [3]
					temp[i][j] = c[3].data[i-size/2][j-size/2];
				}
			}
		}
		return new Matrix(size, temp);
	}
}
