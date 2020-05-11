/**
 * UPGMA uses the unweighted pair group method using arithmetic averages
 * to create a phylogenetic tree. Each progressive step down in the distance
 * matrix is written to a new file for easy viewing. The final tree is 
 * currently displayed as a non-graphical representation of the tree as we
 * ran out of time to complete the graphical representation.
 */

package algorithm;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UPGMA 
{
	private ArrayList<ArrayList<Point>> array = new ArrayList<ArrayList<Point>>();
	private String lineRepOfTree;
	private FileHandling fh;
	private int count;
	private Point min;
	private static boolean DEBUG = false; //set to true if troubleshooting algorithm
	private String filename;
	
	/**
	 * Creates a tree using UPGMA from the given file
	 * @param fileToUse file containing the distance matrix
	 */
	public UPGMA(String fileToUse)
	{
		try 
		{	
			filename = fileToUse;
			fh = new FileHandling();
			count = 0;
			array = fh.readFromCSV(filename);
			
			printArray();
			runAlgorithm();
			lineRepOfTree = min.getCluster();
			
			System.out.println("\n"+filename+" Tree: "+lineRepOfTree);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Controls how many times the matrix is shrunk
	 * Loops until there is only 1 column of distance values
	 */
	public void runAlgorithm()
	{
		while(array.size() > 2)
		{
			findMin();
			redoMatrix();
		}
	}
	
	/**
	 * Finds the minimum distance in the matrix
	 * @return minimum point
	 */
	public Point findMin()
	{
		if(DEBUG)
			System.out.println("\nfindMin");
		Point temp;
		Point minFound = null;
		
		if(array.size() > 2)
		{
			for(int row = 1; row < array.size(); row++)
			{
				for(int col = row+1; col < array.size(); col++)
				{
					temp = array.get(row).get(col);
					if(minFound == null || temp.getValue() < minFound.getValue())
					{
						minFound = temp;
					}
				}
			}
		}
		
		min = minFound;
		return minFound;
	}

	/**
	 * Takes the distance matrix in array and puts it through another iteration of the UPGMA
	 * computation so it combines another cluster based on the minimum point/distance
	 */
	public void redoMatrix()
	{
		if(DEBUG)
		{
			System.out.println("redoMatrix min: " + min);
		}
		ArrayList<ArrayList<Point>> newArray = new ArrayList<ArrayList<Point>>();
		Double valNew, val1, val2, val3, val4;
		Point tempPoint;
		String newCluster;
		
		for(int i = 0; i < array.size()-1; i++) //create a new array 1 size smaller
		{
			newArray.add(new ArrayList<Point>());
			for(int j = 0; j < array.size()-1; j++)
			{
				newArray.get(i).add(new Point());
			}
		}		
		
		for(int row = 0, newRows = 0; row <= array.size(); row++)
		{
			while(row == min.getRow() || row == min.getCol())
			{
				row++;
			}
			for(int col = 0, newCols = 0; col <= array.size(); col++)
			{
				while(col == min.getCol() || col == min.getRow())
				{
					col++;
				}
				if(DEBUG)
				{
					System.out.println("\narray row: "+row+" col: "+col);
					System.out.println("newArrary row: "+newRows+" col: "+newCols);
				}
				
				if( row == 0) //clusters horizontal
				{
					if(col != min.getCol() && col != min.getRow() && col <= newArray.size()) //copy from parent matrix
					{
						
						newCluster = array.get(row).get(col).getCluster();
						tempPoint = new Point(newCols,newRows,newCluster,true);
						if(DEBUG)
						{
							System.out.println("copy cluster horizontal title");
							System.out.println("         tempPoint in copied cluster "+tempPoint);
						}
						newArray.get(0).set(newCols,tempPoint);
						newCols++;
					}
					else //create new cluster horizontal
					{
						newCluster = "("+array.get(0).get(min.getCol()).getCluster()+" "+array.get(0).get(min.getRow()).getCluster()+")";
						tempPoint = new Point(newCols,newRows,newCluster,true);
						if(DEBUG)
						{	
							System.out.println("create new cluster horizontal header col at end");
							System.out.println("         tempPoint in new cluster "+tempPoint);
						}
						newArray.get(0).set(newCols,tempPoint);
						newCols++;
						newRows++;
					}
				}
				else if(col == 0 && row > 0) //clusters vertical
				{
					newCluster = newArray.get(0).get(newRows).getCluster();
					tempPoint = new Point(newCols,newRows,newCluster,true);
					if(DEBUG)
					{
						System.out.println("cluster title vertical");
						System.out.println("         tempPoint in vertical cluster "+tempPoint);
					}
					newArray.get(newRows).set(0,tempPoint);
					newCols++;
				}
				else if(row >= col) //0s
				{
					tempPoint = new Point (newCols,newRows,0.0,null,false);
					if(DEBUG)
					{
						System.out.println("set cell to 0");
						System.out.println("         tempPoint in 0 cell "+tempPoint);
					}
					newArray.get(newRows).set(newCols,tempPoint);
					newCols++;
				}
				else if(row <= newArray.size() && col <= newArray.size()) //copy
				{
					newCluster = newArray.get(0).get(newCols).getCluster()+newArray.get(0).get(newRows).getCluster();
					tempPoint = new Point(newCols,newRows,array.get(row).get(col).getValue(),newCluster,false);
					newArray.get(newRows).set(newCols,tempPoint);
					if(DEBUG)
					{
						System.out.println("copy cell with same value");
						System.out.println("         copy cell with val " + newArray.get(newRows).get(newCols).getValue());
					}
					newCols++;
				}
				else if(col > newArray.size() && row <= newArray.size() && row != 0) //compute new cluster col
				{
					val1 = array.get(row).get(min.getCol()).getValue();
					val2 = array.get(min.getRow()).get(row).getValue();
					val3 = array.get(min.getCol()).get(row).getValue();
					val4 = array.get(row).get(min.getRow()).getValue();
					valNew = ( val1+val2+val3+val4 ) / 2;
					newCluster = "("+newArray.get(0).get(newCols).getCluster()+" "+newArray.get(0).get(newRows).getCluster()+")";
					tempPoint = new Point(newArray.size()-1,newRows,valNew,newCluster,false);
					newArray.get(newRows).set(newArray.size()-1,tempPoint);
					if(DEBUG)
					{
						System.out.println("         computed new cluster val:("+val1+"+"+val2+"+"+val3+"+"+val4+")/2="+valNew);
					}
					newCols++;
					newRows++;
				}
			}
			if(row == array.size()) //new cluster
			{
				newCluster = newArray.get(0).get(newArray.size()-1).getCluster();
				tempPoint = new Point(array.size()-2,array.size()-2,0.0,newCluster,false);
				if(DEBUG)
				{
					System.out.println("final row new");
					System.out.println("         tempPoint in new bottom corner cell "+tempPoint);
				}
				newArray.get(array.size()-2).set(array.size()-2,tempPoint);
				newRows++;
			}
		}
		
		array = newArray;
		++count;
		
		if(DEBUG)
		{
			System.out.println("finished "+ count);
			System.out.println("-------------------------------------------");
		}
		
		//printArray();
		
		fh.writeToFile(filename+"_output_"+count, newArray);
	}
	
	/**
	 * Prints the current array to the console for ease of access
	 * (same array is also written to csv file and viewable there)
	 */
	private void printArray()
	{
		System.out.println("\nPrinting current Array");
		for(ArrayList<Point> i : array)
		{
			for(Point j : i)
			{
				if(j.isHeader)
					System.out.print(j.getCluster()+"\t");
				else
					System.out.printf("%.3f,\t",j.getValue());
				//System.out.print(j+",\t");
			}
			System.out.println();
		}
	}
}
