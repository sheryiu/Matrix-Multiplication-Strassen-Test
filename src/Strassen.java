import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Strassen {
	
	private static int strassenStop;
	
	private static Map<Integer, Long[][]> timeTotal;

	public static void main(String[] args) {
		timeTotal = new HashMap<>();
		strassenStop = 8;
		test(128);
		test(256);
		test(512);
		test(1024);
		strassenStop = 16;
		test(128);
		test(256);
		test(512);
		test(1024);
		strassenStop = 32;
		test(128);
		test(256);
		test(512);
		test(1024);
		strassenStop = 64;
		test(128);
		test(256);
		test(512);
		test(1024);
		strassenStop = 128;
		test(256);
		test(512);
		test(1024);
		for (int i=128; i<=1024; i=i*2) {
			for (int strassen=8; (i>=256&&strassen<=128)||(i==128&&strassen<=64); strassen*=2) {
				System.out.printf("\"%d %d normal\", ", i, strassen);
				Long[][] temp = timeTotal.get(i*1000+strassen);
				for (int j=0; j<temp[0].length; j++) {
					System.out.printf("%d, ", temp[0][j]);
				}
				System.out.println();
				System.out.printf("\"%d %d strassen\", ", i, strassen);
				for (int j=0; j<temp[1].length; j++) {
					System.out.printf("%d, ", temp[1][j]);
				}
				System.out.println();
			}
		}
		
	}
	
	public static void test(int size) {
		Long[][] temp = new Long[2][5];
		for (int i=0; i<5; i++) {
			long time = System.currentTimeMillis();
			Matrix a = genRandomMatrix(size);
			Matrix b = genRandomMatrix(size);
			
			time = System.currentTimeMillis();
			Matrix.multiplication(a, b);
			temp[0][i] = System.currentTimeMillis()-time;
			//System.out.printf("Time for normal: %d\n", System.currentTimeMillis()-time);
			
			time = System.currentTimeMillis();
			strassenMultiplication(size, a, b);
			temp[1][i] = System.currentTimeMillis()-time;
			//System.out.printf("Time for strassen (stop at %dx%d): %d\n--------------------------\n", strassenStop, strassenStop, System.currentTimeMillis()-time);
		}
		timeTotal.put(size*1000+strassenStop, temp);
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
				temp[i][j] = ran.nextInt(1000);
			}
		}
		return new Matrix(size, temp);
	}

}
