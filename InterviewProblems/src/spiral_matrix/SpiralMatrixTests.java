package spiral_matrix;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SpiralMatrixTests {
	
	private SpiralMatrixPrinter spiralMatrix;
	private char nextCharInOrder;
	
	@Before
	public void setUp() throws Exception {
		this.spiralMatrix = new SpiralMatrixPrinter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEmptyMatrixPrintNothing() {
		int size = 0;
		char[][] matrix = new char[0][0];
		spiralMatrix.printSpiralMatrix(matrix);
	}
	
	@Test
	public void testNullInputPrintsNothing() {
		int size = 0;
		char[][] matrix = null;
		spiralMatrix.printSpiralMatrix(matrix);
	}
	
	@Test
	public void testPrintsSpiralOdd() {
		int oddSize = 3;
		char[][] oddMatrix = new char[][] {
			{'a','b','c'},
			{'h','i','d'},
			{'g','f','e'}
		};
		spiralMatrix.printSpiralMatrix(oddMatrix);
	}
	
	@Test
	public void testPrintsSpiralEven() {
		int evenSize = 4;
		char[][] evenMatrix = new char[][] {
			{'a','b','c','d'},
			{'l','m','n','e'},
			{'k','p','o','f'},
			{'j','i','h','g'}
		};
		spiralMatrix.printSpiralMatrix(evenMatrix);
	}
	
	private boolean validatePrintOrder(char[] printOrder, int size, char nextCharInOrder){
		for(int i = 0; i < size; ++i){
			if (printOrder[i] != nextCharInOrder++){
				return false;
			}
		}
		return true;
	}
	
	private boolean printOrderIsEmpty(char[] printOrder){
		return printOrder.length < 1;
	}
}
