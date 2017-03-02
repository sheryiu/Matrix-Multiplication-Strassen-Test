import java.util.Random;

public class Strassen {

	public static void main(String[] args) {
		test(256, 8);
		test(512, 8);
		test(1024, 8);
		test(256, 16);
		test(512, 16);
		test(1024, 16);
		test(256, 32);
		test(512, 32);
		test(1024, 32);
		test(256, 64);
		test(512, 64);
		test(1024, 64);
		test(256, 128);
		test(512, 128);
		test(1024, 128);
	}
	
	public static void test(int size, int strassenStop) {
		long time = System.currentTimeMillis();
		Matrix a = genRandomMatrix(size);
		//a.print();
		//System.out.println();
		Matrix b = genRandomMatrix(size);
		//b.print();
		//System.out.println();
		System.out.printf("Time for generating two %dx%d matrices: %d\n", size, size, System.currentTimeMillis()-time);
		time = System.currentTimeMillis();
		Matrix.multiplication(a, b);
		System.out.printf("Time for normal: %d\n", System.currentTimeMillis()-time);
		time = System.currentTimeMillis();
		strassenMultiplication(size, strassenStop, a, b);
		System.out.printf("Time for strassen (stop at %dx%d): %d\n--------------------------\n", strassenStop, strassenStop, System.currentTimeMillis()-time);
	}
	
	public static Matrix strassenMultiplication(int size, int strassenStop, Matrix a, Matrix b) {
		if (size == strassenStop) {
			return Matrix.multiplication(a, b);
		} else {
			Matrix[] m = prepareCommonProcedures(size, strassenStop, a.doubleBisection(), b.doubleBisection());
			Matrix[] c = new Matrix[4];
			c[0] = asa(m[0], m[3], m[4], m[6]);
			c[1] = Matrix.addition(m[2], m[4]);
			c[2] = Matrix.addition(m[1], m[3]);
			c[3] = ssa(m[0], m[1], m[2], m[5]);
			return Matrix.mergeBisection(size, c);
		}
	}
	
	public static Matrix[] prepareCommonProcedures(int size, int strassenStop, Matrix[] a, Matrix[] b) {
		Matrix[] m = new Matrix[7];
		m[0] = strassenMultiplication(size/2, strassenStop, Matrix.addition(a[0], a[3]), Matrix.addition(b[0], b[3]));
		m[1] = strassenMultiplication(size/2, strassenStop, Matrix.addition(a[2], a[3]), b[0]);
		m[2] = strassenMultiplication(size/2, strassenStop, a[0], Matrix.subtraction(b[1], b[3]));
		m[3] = strassenMultiplication(size/2, strassenStop, a[3], Matrix.subtraction(b[2], b[0]));
		m[4] = strassenMultiplication(size/2, strassenStop, Matrix.addition(a[0], a[1]), b[3]);
		m[5] = strassenMultiplication(size/2, strassenStop, Matrix.subtraction(a[2], a[0]), Matrix.addition(b[0], b[1]));
		m[6] = strassenMultiplication(size/2, strassenStop, Matrix.subtraction(a[1], a[3]), Matrix.addition(b[2], b[3]));
		return m;
	}
	
	public static Matrix asa(Matrix a, Matrix b, Matrix c, Matrix d) {
		int[][] temp = new int[a.size][a.size];
		for (int i=0; i<a.size; i++) {
			for (int j=0; j<a.size; j++) {
				temp[i][j] = a.data[i][j]+b.data[i][j]-c.data[i][j]+d.data[i][j];
			}
		}
		return new Matrix(a.size, temp);
	}
	
	public static Matrix ssa(Matrix a, Matrix b, Matrix c, Matrix d) {
		int[][] temp = new int[a.size][a.size];
		for (int i=0; i<a.size; i++) {
			for (int j=0; j<a.size; j++) {
				temp[i][j] = a.data[i][j]-b.data[i][j]+c.data[i][j]+d.data[i][j];
			}
		}
		return new Matrix(a.size, temp);
	}
	
	
	
	
	public static Matrix genRandomMatrix(int size) {
		int[][] temp = new int[size][size];
		Random ran = new Random();
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				temp[i][j] = ran.nextInt(1000);
			}
		}
		return new Matrix(size, temp);
	}

}
