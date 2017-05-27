package stone.john.project2;

public class Edge {
	private Vertex v1;
	private Vertex v2;
	private double dist;
	public String name;
	private int capacity;
	public Edge backEdge;
	public double olddist;
	public double fDist = Double.MAX_VALUE;
	public int timestamp;
	
	public Edge(Vertex v1, Vertex v2)
	{
		this.v1 = v1;
		this.v2 = v2;
		setDistance();
		name = String.valueOf(v1.getName()) + String.valueOf(v2.getName());
	}
	
	public Edge(Vertex v1, Vertex v2, int cap)
	{
		this.v1 = v1;
		this.v2 = v2;
		capacity = cap;
		setDistance();
		name = String.valueOf(v1.getName()) + String.valueOf(v2.getName());
	}
	
	public Edge() {
		// TODO Auto-generated constructor stub
	}

	public Vertex getV1()
	{
		return v1;
	}
	
	public Vertex getV2()
	{
		return v2;
	}
	
	public double getDist()
	{
		return dist;
	}
	
	public int getCap()
	{
		return capacity;
	}
	
	public void setV1(Vertex v1)
	{
		this.v1 = v1;
	}
	
	public void setV2(Vertex v2)
	{
		this.v2 = v2;
	}
	
	public void setCapacity(int cap)
	{
		capacity = cap;
	}
	
	public void msetDist(double i)
	{
		dist  = i;
	}
	
	private void setDistance()
	{
		dist = Math.sqrt(Math.pow(v2.getX() - v1.getX(), 2) + Math.pow(v2.getY()-v1.getY(), 2));
	}
}
