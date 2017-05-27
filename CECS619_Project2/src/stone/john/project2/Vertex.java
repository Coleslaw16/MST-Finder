package stone.john.project2;

import java.util.ArrayList;
import java.util.LinkedList;

public class Vertex {
	private int x;
	private int y;
	private char name;
	public LinkedList<Edge> EdgeList;
	
	public Vertex(char name, int x, int y)
	{
		EdgeList = new LinkedList<Edge>();
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	public char getName()
	{
		return name;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
}
