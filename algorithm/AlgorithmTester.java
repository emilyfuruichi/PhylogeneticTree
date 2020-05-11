/**
 * Tester for our program. It calls the UPGMA algorithm while
 * giving it the data files. All the work is done internally.
 */

package algorithm;

public class AlgorithmTester 
{
	public static void main(String[] args)
	{
		UPGMA test = new UPGMA("lectureData");
		
		UPGMA test2 = new UPGMA("HW4Data");
	}
}
