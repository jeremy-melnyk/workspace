package spiral_matrix;

public class driver {

	public static void main(String[] args) {
		int count = recursiveCount(4);
		System.out.println(count);
	}

	public static int recursiveCount(int timesToCount){
		if(timesToCount == 0){
			return 1;
		}
		int count = 0;
		for(int i = 0; i < timesToCount; ++i){
			count += recursiveCount(timesToCount-1);
		}
		return count;
	}
}
