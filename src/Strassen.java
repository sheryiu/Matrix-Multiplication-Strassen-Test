import java.util.Random;
import java.util.Scanner;

public class Strassen {
	
	private static int strassenStop;
	private static int rounds = 1;

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int mSize = 0;
		while (mSize <= 0) {
			System.out.printf("Please input your matrix size:\n");
			mSize = s.nextInt();
		}
		while (strassenStop <= 0 || (Math.log(strassenStop)/Math.log(2)) % 1 != 0 || strassenStop > mSize) {
			System.out.printf("Please input your Strassen critical size:\n");
			strassenStop = s.nextInt();
		}
		test(mSize, true);
		
		s.close();
	}
	
	public static void test(int size) {
		test(size, false);
	}
	
	public static void test(int size, boolean print) {
		Long[][] temp = new Long[2][rounds];
		for (int i=0; i<rounds; i++) {
			System.out.printf("Matrix size: %d\n", size);
			long time = System.currentTimeMillis();
			Matrix a = genRandomMatrix(size);
			Matrix b = genRandomMatrix(size);
			
			if ((Math.log(size)/Math.log(2)) % 1 != 0) {
				size = (int) Math.pow(2, Math.ceil(Math.log(size)/Math.log(2)));
				a = Matrix.fillZero(a);
				b = Matrix.fillZero(b);
			}
			
			time = System.currentTimeMillis();
			if (print)
				Matrix.multiplication(a, b).print();
			else
				Matrix.multiplication(a, b);
			temp[0][i] = System.currentTimeMillis()-time;
			if (print)
				System.out.printf("Time for trivial: %d\n", temp[0][i]);
			
			time = System.currentTimeMillis();
			if (print)
				strassenMultiplication(size, a, b).print();
			else
				strassenMultiplication(size, a, b);
			temp[1][i] = System.currentTimeMillis()-time;
			if (print)
				System.out.printf("Time for strassen (stop at %dx%d): %d\n--------------------------\n", strassenStop, strassenStop, temp[1][i]);
		}
		
		System.out.printf("%d %d normal, ", size, strassenStop);
		for (int j=0; j<rounds; j++) {
			System.out.printf("%d, ", temp[0][j]);
		}
		System.out.printf("%d %d strassen, ", size, strassenStop);
		for (int j=0; j<rounds; j++) {
			System.out.printf("%d, ", temp[1][j]);
		}
		System.out.println();
	}
	
	public static Matrix strassenMultiplication(int size, Matrix a, Matrix b) {
		if (size == strassenStop) {
			return Matrix.multiplication(a, b);
		} else {
			Matrix[] m = prepareCommonProcedures(size, a.doubleBisection(), b.doubleBisection());
			Matrix[] c = new Matrix[4];
			c[0] = asa(m[0], m[3], m[4], m[6]);
			c[1] = Matrix.addition(m[2], m[4]);
			c[2] = Matrix.addition(m[1], m[3]);
			c[3] = saa(m[0], m[1], m[2], m[5]);
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
	
	public static Matrix saa(Matrix a, Matrix b, Matrix c, Matrix d) {
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
				temp[i][j] = ran.nextInt(100);
			}
		}
		return new Matrix(size, temp);
	}

}
