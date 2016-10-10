package spiral_matrix;

public class SpiralMatrixPrinter {
	
	public static void main(String[] args) {
		// Test SpiralMatrixPrinter
		SpiralMatrixPrinter spiralMatrixPrinter= new SpiralMatrixPrinter();
		
		// Test case inputs
		char[][] oddMatrix = new char[][] {
			{'a','b','c'},
			{'h','i','d'},
			{'g','f','e'}
		};
		char[][] evenMatrix = new char[][] {
			{'a','b','c','d'},
			{'l','m','n','e'},
			{'k','p','o','f'},
			{'j','i','h','g'}
		};
		char[][] moreColMatrix = new char[][] {
			{'a','b','c','d','e'},
			{'n','o','p','q','f'},
			{'m','t','s','r','g'},
			{'l','k','j','i','h'}
		};
		char[][] moreRowMatrix = new char[][] {
			{'a','b','c','d'},
			{'n','o','p','e'},
			{'m','t','q','f'},
			{'l','s','r','g'},
			{'k','j','i','h'}
		};
		char[][] singleRowMatrix = new char[][]{
			{'a','b','c'}
		};
		char[][] singleColMatrix = new char[][]{
			{'a'},
			{'b'},
			{'c'}
		};
		char[][] oneElementMatrix = new char[][] {
			{'a'}
		};
		char[][] emptyMatrix = new char [][]{
			{}
		};
		char[][] nullMatrix = null;
				
		// Test cases
		System.out.println("---- Printing Odd Matrix ----");
		spiralMatrixPrinter.printSpiralMatrix(oddMatrix);
		System.out.println("---- Printing Even Matrix ----");
		spiralMatrixPrinter.printSpiralMatrix(evenMatrix);
		System.out.println("---- Printing More Rows Matrix ----");
		spiralMatrixPrinter.printSpiralMatrix(moreRowMatrix);
		System.out.println("---- Printing More Columns Matrix ----");
		spiralMatrixPrinter.printSpiralMatrix(moreColMatrix);
		System.out.println("---- Printing Single Row Matrix ----");
		spiralMatrixPrinter.printSpiralMatrix(singleRowMatrix);
		System.out.println("---- Printing Single Column Matrix ----");
		spiralMatrixPrinter.printSpiralMatrix(singleColMatrix);
		System.out.println("---- Printing One Element Matrix ----");
		spiralMatrixPrinter.printSpiralMatrix(oneElementMatrix);
		System.out.println("---- Printing Empty Matrix ----");
		spiralMatrixPrinter.printSpiralMatrix(emptyMatrix);
		System.out.println("---- Printing Null Matrix ----");
		spiralMatrixPrinter.printSpiralMatrix(nullMatrix);
	}
	
	public SpiralMatrixPrinter() {
		super();
	}
	
	// Time: O(n), Space: O(1)
	public void printSpiralMatrix(char[][] matrix){
		if (isNullOrEmpty(matrix)){
			return;
		}
		
		// Initialize size of matrix
		int numOfRows = matrix.length;	
		int numOfColumns = matrix[0].length;
		
		// Initialize the iteration indices
		int rowStartIndex = 0;
		int rowEndIndex = numOfRows - 1;
		int colStartIndex = 0;
		int colEndIndex = numOfColumns - 1;
		
		// While the rows and columns have not met or overlapped
		while (rowStartIndex < rowEndIndex && colStartIndex < colEndIndex){
			
			// Print the edges of the matrix in spiral order
			printTopRow(colStartIndex, colEndIndex, rowStartIndex, matrix);
			printRightColumn(rowStartIndex, rowEndIndex, colEndIndex, matrix);
			printBottomRow(colStartIndex, colEndIndex, rowEndIndex, matrix);
			printLeftColumn(rowStartIndex, rowEndIndex, colStartIndex, matrix);	
			
			// Decrement iteration indices
			++colStartIndex;
			--colEndIndex;
			++rowStartIndex;
			--rowEndIndex;
		}
		
		// One row left
		if(colStartIndex < colEndIndex){
			printSingleRow(colStartIndex, colEndIndex, rowStartIndex, matrix);
		}
		
		// One column left
		if(rowStartIndex < rowEndIndex){
			printSingleColumn(rowStartIndex, rowEndIndex, colEndIndex, matrix);
		}
		
		// NxN matrix
		if (numOfRows == numOfColumns){
			// If odd
			if(numOfRows % 2 == 1){
				printMiddle(rowStartIndex, matrix);
			}
		}
	}
	
	private void printTopRow(int startIndex, int endIndex, int rowIndex, char[][] matrix) {
		for (int i = startIndex; i < endIndex; ++i){
			System.out.println(matrix[rowIndex][i]);
		}
	}
	
	private void printRightColumn(int startIndex, int endIndex, int colIndex, char[][] matrix) {
		for (int i = startIndex; i < endIndex; ++i){
			System.out.println(matrix[i][colIndex]);
		}
	}
	
	private void printBottomRow(int startIndex, int endIndex, int rowIndex, char[][] matrix) {
		for (int i = endIndex; i > startIndex; --i){
			System.out.println(matrix[rowIndex][i]);
		}
	}
	
	private void printLeftColumn(int startIndex, int endIndex, int colIndex, char[][] matrix) {
		for (int i = endIndex; i > startIndex; --i){
			System.out.println(matrix[i][colIndex]);
		}
	}
	
	private void printSingleRow(int startIndex, int endIndex, int rowIndex, char[][] matrix) {
		for (int i = startIndex; i <= endIndex; ++i){
			System.out.println(matrix[rowIndex][i]);
		}
	}
	
	private void printSingleColumn(int startIndex, int endIndex, int colIndex, char[][] matrix) {
		for (int i = startIndex; i <= endIndex; ++i){
			System.out.println(matrix[i][colIndex]);
		}
	}
	
	private void printMiddle(int middleIndex, char[][] matrix){
		System.out.println(matrix[middleIndex][middleIndex]);
	}
	
	private boolean isNullOrEmpty(char[][] matrix){
		if(matrix == null){
			return true;
		}
		int numOfRows = matrix.length;
		if(numOfRows< 1){
			return true;
		}
		int numOfColumns = matrix[0].length;
		if(numOfColumns < 1){
			return true;
		}
		return false;
	}
}
