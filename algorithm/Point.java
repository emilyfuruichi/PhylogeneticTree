package algorithm;

public class Point 
{
	private int row,col;
	private Double value;
	private Point parent;
	private String cluster;
	protected boolean isHeader;
	
	public Point()
	{
//		row = Integer.MAX_VALUE;
//		col = Integer.MAX_VALUE;
//		value = 0.0;
//		cluster = null;
//		isHeader = true;
	}
	
	public Point(int colIn, int rowIn, String clusterIn, boolean header)
	{
		row = rowIn;
		col = colIn;
		value = 0.0;
		cluster = clusterIn;
		isHeader = header;
	}
	
	public Point(int colIn, int rowIn, Double valueIn, String clusterIn, boolean header)
	{
		row = rowIn;
		col = colIn;
		value = valueIn;
		cluster = clusterIn;
		isHeader = header;
	}
	
	public void setCluster(String clusterIn)
	{
		cluster = clusterIn;
	}

	public Point getParent() 
	{
		return parent;
	}

	public int getRow() 
	{
		return row;
	}

	public int getCol() 
	{
		return col;
	}
	
	public Double getValue() 
	{
		return value;
	}

	public void setRow(int row) 
	{
		this.row = row;
	}

	public void setCol(int col) 
	{
		this.col = col;
	}

	public void setValue(Double value) 
	{
		this.value = value;
	}
	
	public String getCluster()
	{
		return cluster;
	}

	public String toString()
	{
		return "["+cluster+"(r_"+row+"c_"+col+"): "+value+"]";
	}
}
