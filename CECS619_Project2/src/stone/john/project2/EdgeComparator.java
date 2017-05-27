package stone.john.project2;

import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge> {
	public int compare(Edge x, Edge y)
	{
		if(x.getDist() > y.getDist())
		{
			return  1;
		}
		if(x.getDist() < y.getDist())
		{
			return -1;
		}
		if(x.timestamp < y.timestamp)
		{
			return -1;
		}
		
		if(x.timestamp > y.timestamp)
		{
			return 1;
		}
		return 0;
	}
}
