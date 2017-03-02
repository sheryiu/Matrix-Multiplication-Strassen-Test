import java.util.Random;

public class Strassen {

	public static void main(String[] args) {
		test(256);
		test(512);
		test(1024);
	}
	
	public static void test(int size) {
		long time = System.currentTimeMillis();
		Matrix a = genRandomMatrix(size);
		//a.print();
		//System.out.println();
		Matrix b = genRandomMatrix(size);
		//b.print();
		//System.out.println();
		System.out.printf("Time for generating two %dx%d matrices: %d\n\n", size, size, System.currentTimeMillis()-time);
		time = System.currentTimeMillis();
		Matrix.multiplication(a, b);
		System.out.printf("Time for normal: %d\n\n", System.currentTimeMillis()-time);
		time = System.currentTimeMillis();
		strassenMultiplication(size, a, b);
		System.out.printf("Time for strassen: %d\n\n\n", System.currentTimeMillis()-time);
	}
	
	public static Matrix strassenMultiplication(int size, Matrix a, Matrix b) {
		if (size == 128) {
			return Matrix.multiplication(a, b);
		} else {
			Matrix[] m = prepareCommonProcedures(size, a.doubleBisection(), b.doubleBisection());
			Matrix[] c = new Matrix[4];
			c[0] = asa(m[0], m[3], m[4], m[6]);
			c[1] = Matrix.addition(m[2], m[4]);
			c[2] = Matrix.addition(m[1], m[3]);
			c[3] = ssa(m[0], m[1], m[2], m[5]);
			return Matrix.mergeBisection(size, c);
		}
	}
	
	public static Matrix[] prepareCommonProcedures(int size, Matrix[] a, Matrix[] b) {
		Matrix[] m = new Matrix[7];
		m[0] = strassenMultiplication(size/2, Matrix.addition(a[0], a[3]), Matrix.addition(b[0], b[3]));
		m[1] = strassenMultiplication(size/2, Matrix.addition(a[2], a[3]), b[0]);
		m[2] = strassenMultiplication(size/2, a[0], Matrix.subtraction(b[1], b[3]));
		m[3] = strassenMultiplication(size/2, a[3], Matrix.subtraction(b[2], b[0]));
		m[4] = strassenMultiplication(size/2, Matrix.addition(a[0], a[1]), b[3]);
		m[5] = strassenMultiplication(size/2, Matrix.subtraction(a[2], a[0]), Matrix.addition(b[0], b[1]));
		m[6] = strassenMultiplication(size/2, Matrix.subtraction(a[1], a[3]), Matrix.addition(b[2], b[3]));
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
