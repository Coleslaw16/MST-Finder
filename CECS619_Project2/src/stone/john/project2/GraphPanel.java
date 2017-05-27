package stone.john.project2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class GraphPanel extends JPanel {
	private ArrayList<ShapeItem> nodes;
	private ArrayList<Vertex> vertex;
	LinkedList<Edge> mstEdges;
	LinkedList<Edge> oldMST;
	LinkedList<Edge> prevMST;
	private double scaleFactorx = 1;
	private double scaleFactory = 1;
	private static double size = 100;
	private boolean oldMSTSET = false;
	public boolean hasCap = false;
	public int timer = 0;
	int remainingCap = 60;
	public int counter = 0;
	LinkedList<ShapeItem> listOfChangedShapes = new LinkedList<>();
	private ShapeItem selectedShape;

	public GraphPanel() {
		mouseAdapter handler = new mouseAdapter();
		addMouseListener(handler);
		addMouseMotionListener(handler);
	}

	public class mouseAdapter extends MouseInputAdapter {
		public void mousePressed(MouseEvent e) {
			if (nodes != null) {
				counter = 0;
				for (ShapeItem s : nodes) {
					if (s.getShape() instanceof Line2D && getClickedLine(s.getShape(), e.getX(), e.getY())) {
						if (s.getColor() == Color.YELLOW) {
							s.setColor(Color.BLACK);
							s.e.msetDist(s.e.olddist);
							runMST(Color.BLACK);
							prevMST = null;
						} else {
							s.setColor(Color.YELLOW);
							s.e.olddist = s.e.getDist();
							s.e.msetDist(Double.MAX_VALUE);
							remainingCap = 60 - s.e.getCap();
							if (mstEdges.contains(s.e)) {
								selectedShape = s;
							}
							listOfChangedShapes.add(s);
							// System.out.println(listOfChangedShapes.size() + "
							// DOn't confuse me");
							runMST(Color.YELLOW);
							setPrev();
							counter++;
						}
					}
				}
				repaint();
			}
		}
	}

	public boolean getClickedLine(Shape s, int x, int y) {
		int boxX = x - 4 / 2;
		int boxY = y - 4 / 2;
		int width = 4;
		int height = 4;
		if (s.intersects(boxX, boxY, width, height))
			return true;
		else
			return false;
	}

	public void readNodes(ArrayList<Vertex> a) {
		// System.out.println("Got Here");
		double sx = 5000;
		double sy = 5000;
		vertex = a;
		nodes = new ArrayList<>();
		for (Vertex b : a) {
			double e = (this.getWidth() - size) / b.getX();
			// System.out.println(e);
			double f = (this.getHeight() - size) / b.getY();
			if (e < sx) {
				sx = e;
			}
			if (f < sy) {
				sy = f;
			}
		}
		scaleFactorx = sx;
		scaleFactory = sy;
		for (Vertex b : a) {
			Shape c = new Ellipse2D.Double(b.getX() * scaleFactorx - size / 2, b.getY() * scaleFactory - size / 2, size,
					size);
			ShapeItem si = new ShapeItem(Color.BLACK, c, b.getName());
			nodes.add(si);
		}

		for (Vertex b : a) {
			for (Edge c : b.EdgeList) {
				Shape e = new Line2D.Double(c.getV1().getX() * scaleFactorx, c.getV1().getY() * scaleFactory,
						c.getV2().getX() * scaleFactorx, c.getV2().getY() * scaleFactory);
				ShapeItem si = new ShapeItem(Color.BLACK, e,
						String.valueOf(c.getV1().getName()) + String.valueOf((c.getV2().getName())));
				si.e = c;
				nodes.add(si);
			}
		}
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.WHITE);
		if (mstEdges != null) {
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).getColor() == Color.GREEN) {
					nodes.get(i).setColor(Color.BLACK);
				}
			}
			for (int i = 0; i < nodes.size(); i++) {
				for (Edge t : mstEdges) {
					// System.out.println(nodes.get(i).name + " " + t.name);
					if (nodes.get(i).e == t) {
						if (!oldMST.contains(nodes.get(i).e)) {
							nodes.get(i).setColor(Color.GREEN);
						} else {
							nodes.get(i).setColor(Color.RED);
						}
						// System.out.println(nodes.get(i).getColor());
						ShapeItem temp = nodes.remove(i);
						nodes.add(temp);
					}
				}
			}
		}
		Graphics2D g2d = (Graphics2D) g.create();
		// g2d.scale(WIDTH, HEIGHT);
		if (nodes != null) {
			for (ShapeItem s : nodes) {
				// System.out.println(s.getColor());
				g2d.setColor(s.getColor());
				// System.out.println(g2d.getColor());
				g2d.draw(s.getShape());
				if (s.getShape() instanceof Ellipse2D) {
					g2d.drawString(String.valueOf(s.cname), (int) s.getShape().getBounds().getX(),
							(int) s.getShape().getBounds().getY());
				}
				if (s.getShape() instanceof Line2D) {
					Line2D line = (Line2D) s.getShape();
					g2d.drawString(String.valueOf((int) s.e.getDist()), (int) ((line.getX1() + line.getX2()) / 2) + 5,
							(int) ((line.getY1() + line.getY2()) / 2) - 5);
					if (hasCap) {
						g2d.drawString(String.valueOf((int) s.e.getDist() + " " + s.e.getCap()),
								(int) ((line.getX1() + line.getX2()) / 2) + 5,
								(int) ((line.getY1() + line.getY2()) / 2) - 5);
					}
				}
			}
		}
	}

	public void runMST(Color c) {
		if (vertex != null) {
			if (!hasCap) {
				MSTRunner();
			} else {
				runMST1(c);
			}
		}
		timer = 0;
		repaint();
	}

	public void fillQueue(PriorityQueue<Edge> q, Vertex n1, LinkedList<Edge> mstEdges) {
		// System.out.println("We got here too");
		if (mstEdges.size() == 0) {
			for (int i = 0; i < n1.EdgeList.size(); i++) {
				Edge e = n1.EdgeList.get(i);
				e.timestamp = ++timer;
				q.add(e);
			}
		} else {
			for (int i = 0; i < n1.EdgeList.size(); i++) {
				Edge e = n1.EdgeList.get(i);
				e.timestamp = ++timer;
				if (!mstEdges.contains(e)) {
					q.add(e);
				}
			}
		}
	}

	public void MSTRunner() {
		Comparator<Edge> e = new EdgeComparator();
		PriorityQueue<Edge> q = new PriorityQueue<Edge>(vertex.size() * vertex.size(), e);
		LinkedList<Vertex> visited = new LinkedList<>();
		mstEdges = new LinkedList<>();
		Vertex n1 = vertex.get(0);
		visited.add(n1);
		do {
			// System.out.println("Here");
			fillQueue(q, n1, mstEdges);
			Edge n2 = q.remove();
			while (visited.contains(n2.getV2())) {
				n2 = q.remove();
			}
			mstEdges.add(n2);
			for (Edge ed : n2.getV2().EdgeList) {
				if (ed.getV2() == n2.getV1())
					mstEdges.add(ed);
			}
			n1 = n2.getV2();
			visited.add(n1);
		} while (visited.size() < vertex.size());

		if (!oldMSTSET) {
			oldMST = new LinkedList<>();
			for (Edge m : mstEdges) {
				oldMST.add(m);
			}
			oldMSTSET = true;
		}

		for (Edge m : mstEdges) {
			System.out.println(m.getV1().getName() + " " + m.getV2().getName());
		}
		System.out.println();
	}

	public void runMST1(Color c) {
		Comparator<Edge> e = new EdgeComparator();
		PriorityQueue<Edge> q = new PriorityQueue<Edge>(vertex.size() * vertex.size(), e);
		LinkedList<Vertex> visited = new LinkedList<>();
		if (!oldMSTSET || c.equals(Color.BLACK)) {
			for (ShapeItem s : listOfChangedShapes) {
				// s.setColor(Color.BLACK);
				s.e.msetDist(s.e.olddist);
			}
			listOfChangedShapes.clear();
			mstEdges = new LinkedList<>();
			Vertex n1 = vertex.get(0);
			visited.add(n1);
			do {
				// System.out.println("Here");
				fillQueue(q, n1, mstEdges);
				Edge n2;
				do {
					n2 = q.remove();
				} while (n2.getCap() < 30);
				while (visited.contains(n2.getV2())) {
					n2 = q.remove();
				}
				while (n2.getCap() < 30) {
					n2 = q.remove();
				}
				mstEdges.add(n2);
				for (Edge ed : n2.getV2().EdgeList) {
					if (ed.getV2() == n2.getV1())
						mstEdges.add(ed);
				}
				n1 = n2.getV2();
				visited.add(n1);
			} while (visited.size() < vertex.size());

			oldMST = new LinkedList<>();
			for (Edge m : mstEdges) {
				oldMST.add(m);
			}
			oldMSTSET = true;
			prevMST = null;

		} else {
			if (true) {
				mstEdges = new LinkedList<>();
				if (prevMST == null) {
					for (int i = 0; i < oldMST.size(); i++) {
						mstEdges.add(oldMST.get(i));
					}
				} else {
					for (int i = 0; i < prevMST.size(); i++) {
						mstEdges.add(prevMST.get(i));
					}
				}
				Edge z = new Edge();
				for (int i = 0; i < mstEdges.size(); i++) {
					Edge t = mstEdges.get(i);
					if (t.name.equals(new StringBuilder(selectedShape.name).reverse().toString())) {
						z = t;
						mstEdges.remove(t);
					}
				}
				mstEdges.remove(selectedShape.e);
				for (Vertex n1 : vertex) {
					fillQueueCapacity(q, n1, mstEdges);
				}
				boolean notLeave = true;
				// System.out.println("ello there");
				do {

//					System.out.println(q.size());
					Edge n2;
					do {
						n2 = q.remove();
						System.out.println("We Here");
						// System.out.println("Culprit");
					} while (n2.getCap() + selectedShape.e.getCap() > 60 || n2 == selectedShape.e || n2 == z);

					mstEdges.add(n2);
					for (Edge ed : n2.getV2().EdgeList) {
						if (ed.getV2() == n2.getV1())
							mstEdges.add(ed);
					}
					
					visited = new LinkedList<>();
					for(int i=0;i<mstEdges.size();i++)
					{
						if(!visited.contains(mstEdges.get(i).getV1()))
						{
							visited.add(mstEdges.get(i).getV1());
						}
					}
					
					for(Vertex v : visited)
					{
						System.out.println(v.getName());
					}
					
					System.out.println(mstEdges.size() + " " + vertex.size());
					if(visited.size() == vertex.size())
					{
						notLeave = false;
					}
					else
					{
						mstEdges.removeLast();
					}
				} while (notLeave);

				for (Edge m : mstEdges) {
					System.out.println(m.getV1().getName() + " " + m.getV2().getName());
				}
				System.out.println();
				for (Edge m : oldMST) {
					System.out.println(m.getV1().getName() + " " + m.getV2().getName());
				}
				System.out.println();
			}
		}
	}

	public void setPrev() {
		if (counter == 1) {
			prevMST = new LinkedList<>();
			for (Edge m : mstEdges) {
				prevMST.add(m);
			}
		}
	}

	public void fillQueueCapacity(PriorityQueue<Edge> q, Vertex n1, LinkedList<Edge> mstEdges) {
		// System.out.println("We got here too");
		for (int i = 0; i < n1.EdgeList.size(); i++) {
			Edge e = n1.EdgeList.get(i);
			e.timestamp = ++timer;
			if (!mstEdges.contains(e) && !oldMST.contains(e)) {
				q.add(e);
			}
		}
	}

}
