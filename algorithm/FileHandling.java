/**
 * FileHandling class can read csv files.
 * Read data is stored in an array list of array lists of Strings
 * for the algorithm to use. This way a multitude of sequences of 
 * various sizes can be read without specifying length or number
 */

package algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandling
{
	private ArrayList<ArrayList<Point>> array; //to hold distance matrix
	
	public FileHandling()
	{
		array = new ArrayList<ArrayList<Point>>();
	}
	
	/**
	 * Given a csv file with the distance matrix, we want to read it into
	 * an arraylist of arraylists of Strings for the algorithm
	 * @param fileName the name of the data file
	 * @throws FileNotFoundException file does not exist
	 */
	public ArrayList<ArrayList<Point>> readFromCSV(String fileName) throws FileNotFoundException
	{
		File inputFile = new File(fileName);
		int rowCount = 0;
		
		try (Scanner sc = new Scanner(inputFile, "UTF-8")) //"data.csv"
		{
			while (sc.hasNextLine()) 
		    {
		        String next = sc.nextLine();
		    	array.add(readNextRow(next,rowCount));
		    	rowCount++;
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return array;
	}
	
	/**
	 * Helper method splits up the given string into an arraylist of Strings
	 * @param nextline the string to split up by comma
	 * @return the arraylist of Strings in the row
	 */
	private ArrayList<Point> readNextRow(String nextline, int rowCount) 
	{
	    ArrayList<Point> colVals = new ArrayList<Point>();
	    int colCount = 0;
	    Point newPoint;
	    
	    try (Scanner rowScanner = new Scanner(nextline)) 
	    {
	        rowScanner.useDelimiter(",");
	        
	        while (rowScanner.hasNext()) 
	        {
	        	String next = rowScanner.next();
	        	if(colCount == 0 || rowCount == 0)
	        	{
	        		newPoint = new Point(rowCount,colCount,next,true);
	        	}
	        	else
	        	{
	        		String clusterName = array.get(0).get(colCount).getCluster()+array.get(0).get(rowCount).getCluster();
	        		newPoint = new Point(rowCount,colCount,Double.parseDouble(next),clusterName,false);
	        	}
	        	colVals.add(newPoint);
	        	colCount++;
	        }
	    }
	    
	    return colVals;
	}
	
	/**
	 * Creates a csv file populated with the given array
	 * placing each array on a new line
	 * @param name name to use for the created csv file
	 * @param array to write to the created file
	 */
	public void writeToFile(String name, ArrayList<ArrayList<Point>> arrayIn)
	{
		try(PrintWriter out = new PrintWriter(new File(name + ".csv"), "UTF-8"))
		{
			for(ArrayList<Point> i : arrayIn)
			{
				for(Point j : i)
				{
					if(j.isHeader)
						out.print(j.getCluster()+",");
					else
						out.print(j.getValue()+",");
				}
				out.println();
			}
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
