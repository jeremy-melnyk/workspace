package longest_path;

public class LongestPath
{
	public static void main(String[] args)
	{		
		Node<Integer> root = new Node<Integer>(-5);
		root.left = new Node<Integer>(-4);
		root.right = new Node<Integer>(-4);
		
		root.left.left = new Node<Integer>(-2);
		root.left.right = new Node<Integer>(-3);
		
		root.right.left = new Node<Integer>(-3);
		root.right.right = new Node<Integer>(-3);
		
		root.right.left.left = new Node<Integer>(-2);
		root.right.left.right = new Node<Integer>(1);
		
		// Max should be 4
		LongestPath lp = new LongestPath();
		
		Integer max = lp.longestPath(root);
		System.out.println(max);
	}
	
	public LongestPath() {	
	}

	public Integer longestPath(Node<Integer> root){
		Integer max = 0;
		int localMax = longestPath(root, max);
		return Math.max(max, localMax);
	}
	
	private Integer longestPath(Node<Integer> root, Integer max){
		if (root == null){
			return 0;
		}
		Integer leftPath = longestPath(root.left, max);
		Integer rightPath = longestPath(root.right, max);
		boolean leftConsecutive = isLeftConsecutive(root);
		boolean rightConsecutive = isRightConsecutive(root);
		if (leftConsecutive){
			leftPath += 1;
		} else {
			// Reset the left chain
			max = Math.max(max, leftPath);
			leftPath = 1;
		}
		if (rightConsecutive){
			rightPath += 1;
		} else {
			// Reset the right chain
			max = Math.max(max, rightPath);
			rightPath = 1;
		}
		return Math.max(leftPath, rightPath);
	}
	
	private boolean isLeftConsecutive(Node<Integer> root){
		return root.left != null && root.value + 1 == root.left.value;
	}
	
	private boolean isRightConsecutive(Node<Integer> root){
		return root.right != null && root.value + 1 == root.right.value;
	}
}
