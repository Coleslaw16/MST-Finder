package stone.john.project2;

import java.awt.Color;
import java.awt.Shape;

public class ShapeItem {
	private Shape shape;
	private Color color;
	public String name = "";
	public char cname;
	public Edge e;
	
	public ShapeItem(Color c, Shape s, String name)
	{
		shape = s;
		color = c;
		this.name = name;
	}
	
	public ShapeItem(Color c, Shape s, char cn)
	{
		shape = s;
		color = c;
		cname = cn;
	}	
	
	public Color getColor()
	{
		return color;
	}
	
	public Shape getShape()
	{
		return shape;
	}
	
	public void setColor(Color c)
	{
		color = c;
	}
}
