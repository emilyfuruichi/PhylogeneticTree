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
	
	public UPGMA()
	{
		try 
		{	
			//test filehandling
			fh = new FileHandling();
			count = 0;
			array = fh.readFromCSV("lectureData.csv");
			lineRepOfTree = "(";
			//fh.writeToFile("test", array);
			//findMin();
			
			runAlgorithm();
			
			//fh.writeToFile("output", array);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void runAlgorithm()
	{
		while(array.size() > 2 && count < 2)
		{
			findMin();
			redoMatrix();
		}
	}
	
	public Point findMin()
	{
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
		//System.out.println("minFound: "+minFound+"\n");
		min = minFound;
		return minFound;
	}

	public void redoMatrix()
	{
		System.out.println("redoMatrix min: " + min);
		ArrayList<ArrayList<Point>> newArray = new ArrayList<ArrayList<Point>>();
		Double valNew, val1, val2, val3, val4; //points to sum and average goes in pNew
		Point tempPoint;
		
		for(int i = 0; i < array.size(); i++) //create a new array 1 size smaller
		{
			newArray.add(new ArrayList<Point>());
			for(int j = 0; j < array.size(); j++)
			{
				newArray.get(i).add(new Point());
			}
		}		
		
		for(int row = 0, newRows = 0; row <= array.size(); row++)
		{
			if(row != min.getRow() && row != min.getCol())
			{
				for(int col = 0, newCols = 0; col <= array.size(); col++)
				{
					if(col != min.getCol() && col != min.getRow())
					{
						System.out.println("\narray row: "+row+" col: "+col);
						System.out.println("newArrary row: "+newRows+" col: "+newCols);
						
						if( row == 0 ) //cluster title horizontal
						{
							if(col == array.size())//create new cluster
							{
								System.out.println("create new cluster horizontal header col at end");
								String newCluster = array.get(0).get(min.getCol()).getCluster()+""+array.get(0).get(min.getRow()).getCluster();
								tempPoint = new Point(newCols,newRows,newCluster,true);
								System.out.println("         tempPoint in new cluster "+tempPoint);
								newArray.get(0).set(newCols,tempPoint);
								newCols++;
								newRows++;
							}
							else if(col != min.getCol() && col != min.getRow()) //copy from parent matrix
							{
								System.out.println("copy cluster horizontal title");
								String newCluster = array.get(row).get(col).getCluster();
								tempPoint = new Point(newCols,newRows,newCluster,true);
								System.out.println("         tempPoint in copied cluster "+tempPoint);
								newArray.get(0).set(newCols,tempPoint);
								newCols++;
							}
						}
						else if(col == 0 && row > 0) //cluster title vertical
						{
							System.out.println("cluster title vertical");
							String newCluster = newArray.get(0).get(newRows).getCluster();
							tempPoint = new Point(newCols,newRows,newCluster,true);
							System.out.println("         tempPoint in vertical cluster "+tempPoint);
							newArray.get(newRows).set(0,tempPoint);
							newCols++;
						}
						else if(row >= col) //diagonal zeros, identities o
						{
							System.out.println("set cell to 0");
							tempPoint = new Point (newCols,newRows,0.0,null,false);
							System.out.println("         tempPoint in 0 cell "+tempPoint);
							newArray.get(newRows).set(newCols,tempPoint);
							newCols++;
						}
						else if(row != array.size() && col != array.size())
						{
							newArray.get(newRows).set(newCols,array.get(row).get(col));
							System.out.println("         copy cell with val " + newArray.get(newRows).get(newCols).getValue());
							newCols++;
						}
					}
					if(col >= array.size() && row < array.size())
					{
						val1 = array.get(row).get(min.getCol()).getValue();
						val2 = array.get(min.getRow()).get(row).getValue();
						val3 = array.get(min.getCol()).get(row).getValue();
						val4 = array.get(row).get(min.getRow()).getValue();
						valNew = ( val1+val2+val3+val4 ) / 2;
						tempPoint = new Point(newCols,newRows,valNew,newArray.get(0).get(newRows).getCluster(),false);
						newArray.get(newRows).set(newCols,tempPoint);
						System.out.println("         computed new cluster val:("+val1+"+"+val2+"+"+val3+"+"+val4+")/2="+valNew);
						newCols++;
						newRows++;
					}
				}
			}
			if(row == array.size()) //new cluster calc
			{
				System.out.println("final row new");
				String newCluster = array.get(0).get(min.getRow()).getCluster()+array.get(0).get(min.getCol()).getCluster();
				tempPoint = new Point(array.size()-2,array.size()-2,0.0,newCluster,false);
				System.out.println("         tempPoint in new bottom corner cell "+tempPoint);
				newArray.get(array.size()-2).set(array.size()-2,tempPoint);
				newRows++;
			}
			
			
		}
		array = newArray;
		printArray(newArray);
		//fh.writeToFile("output"+count, newArray);
		System.out.println("finished "+ ++count);
		System.out.println("----------------------------------------------");
	}
	
	private void printArray(ArrayList<ArrayList<Point>> arr)
	{
		for(ArrayList<Point> i : arr)
		{
			for(Point j : i)
			{
				if(j.isHeader)
					System.out.print(j.getCluster()+",  ");
				else
					System.out.print(j.getValue()+",");
			}
			System.out.println();
		}
	}
}
