import java.util.Arrays;

public class MinInt {


    public static void main(String[] args){
	MinInt m = new MinInt();
	int[] arr = {24,52,74,9,34,23,64,34};
	System.out.println("Minimum is :" + m.findMin(arr));
	
    }

    /*
     * each iteration takes the first element off and compares it with the result of previous iterations.
     * finishes when copy of array is empty
     */
    public int findMin(int[] array){
        	   if (array.length <= 1)
        		   return array[0];
        	   int newSmall = findMin(Arrays.copyOfRange(array, 1, array.length));
        	   if (newSmall > array[0])
        		   return array[0];
        	   else
        		   return newSmall;
           
    }

	
}
