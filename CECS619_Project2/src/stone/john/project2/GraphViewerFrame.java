package stone.john.project2;

import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class GraphViewerFrame extends JFrame {
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	private File file;
	private boolean haveGraph = false;
	private ArrayList<Vertex> nodes;
	private GraphPanel gPanel;
	 
	
	public GraphViewerFrame()
	{
		gPanel = new GraphPanel();
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		JMenu file = new JMenu("File");
		file.add(createOpen("Open"));
		file.add(runMST("MST"));
		file.add(createExit("Close"));
		menu.add(file);
		
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		add(gPanel);
		
	}
	
	
	public JMenuItem createOpen(final String name)
	{
		class FaceItemListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(GraphViewerFrame.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					file = fc.getSelectedFile();
					readFile(file);
				}
			}
		}
		JMenuItem opener = new JMenuItem(name);
		ActionListener listener = new FaceItemListener();
		opener.addActionListener(listener);
		return opener;
	}
	
	public JMenuItem runMST(final String name)
	{
		class FaceItemListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				MSTRunner();
			}

		}
		JMenuItem MST = new JMenuItem(name);
		ActionListener listener = new FaceItemListener();
		MST.addActionListener(listener);
		return MST;
	}
	
	
    public JMenuItem createExit(final String name)
    {
        class FaceItemListener implements ActionListener
        {
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        }
        JMenuItem closer = new JMenuItem(name);
        ActionListener listener = new FaceItemListener();
        closer.addActionListener(listener);
        return closer;
    }
    
    private void readFile(File file)
    {
    	try(Scanner in = new Scanner(file);)
    	{
    		if(!haveGraph)
    		{
    			getNodes(in);
    			setEdges();
    			printEdges();
    			gPanel.readNodes(nodes);
    			haveGraph = true;
    		}
    		else
    		{
    			getCapacity(in);
    			printCapacity();
    			gPanel.readNodes(nodes);
    			gPanel.hasCap = true;
    		}
    	}
    	catch(IOException e)
    	{
    		JOptionPane.showInputDialog("Error reading file...sorry");
    	}
    	catch(Exception e)
    	{
    		JOptionPane.showInputDialog("Please input the graph first");
    	}
    }
    
    private void setEdges()
    {
		for(Vertex temp : nodes)
		{
			for(Vertex temp1 :nodes)
			{
				if(temp != temp1)
				{
					Edge edge = new Edge(temp, temp1);
					temp.EdgeList.add(edge);
				}
			}
		}    	
    }
    
    private void printEdges()
    {
		for(Vertex temp : nodes)
		{
			System.out.println(temp.getName());
			for(Edge e : temp.EdgeList)
			{
				System.out.println(e.getV1().getName() + " " + e.getV2().getName());
				System.out.println(e.getDist());
			}
		}	
    }
    
    private void printCapacity()
    {
		for(Vertex temp : nodes)
		{
			System.out.println(temp.getName());
			for(Edge e : temp.EdgeList)
			{
				System.out.println(e.getCap());
			}
		}	
    }
    
    private void getNodes(Scanner in)
    {
		nodes = new ArrayList<Vertex>();
		while(in.hasNextLine())
		{
			String temp = in.nextLine();
			String[] tokens = temp.split("[,]");
			
			char name = tokens[0].charAt(0);
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			Vertex vert = new Vertex(name, x, y);
			nodes.add(vert);
		}
    }
    
    public void MSTRunner()
    {
    	gPanel.runMST(Color.BLACK);
    }
    
    public void getCapacity(Scanner in)
    {
    	
    	while(in.hasNextLine())
    	{
    		String temp = in.nextLine();
    		String[] tokens = temp.split("[,]");
    		
    		for(int i=0;i<nodes.size();i++)
    		{
    			if(String.valueOf(nodes.get(i).getName()).equals(tokens[0]))
    			{
    				for(int j=0;j<nodes.get(i).EdgeList.size();j++)
    				{
    					if(String.valueOf(nodes.get(i).EdgeList.get(j).getV2().getName()).equals(tokens[1]))
    					{
    						nodes.get(i).EdgeList.get(j).setCapacity(Integer.parseInt(tokens[2]));
    					}
    				}
    			}
    			
    			if(String.valueOf(nodes.get(i).getName()).equals(tokens[1]))
    			{
    				for(int j=0;j<nodes.get(i).EdgeList.size();j++)
    				{
    					if(String.valueOf(nodes.get(i).EdgeList.get(j).getV2().getName()).equals(tokens[0]))
    					{
    						nodes.get(i).EdgeList.get(j).setCapacity(Integer.parseInt(tokens[2]));
    					}
    				}
    			}
    		}
    	}
    }
}
