package de.embots.touchflow.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

public class JGraphTest implements GraphSelectionListener{

	private static int id=1;
	
	public static void main(String[] args) {
	           GraphModel model = new DefaultGraphModel();
	           GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
	           JGraph graph = new JGraph(model, view);

	           graph.setPortsVisible(true);

	           DefaultGraphCell[] cells = new DefaultGraphCell[5];
	           cells[4]=new DefaultGraphCell(new String("zwei"));
	           DefaultPort port4 = new DefaultPort();
	           	  cells[4].addPort(new Point(GraphConstants.PERMILLE,0),port4);
	              cells[4].add(port4);
	              port4.setParent(cells[4]);

	           cells[0] = new DefaultGraphCell(new String("Hello"));
	           GraphConstants.setBounds(cells[0].getAttributes(), new
	                Rectangle2D.Double(20,20,40,20));
	           GraphConstants.setGradientColor(
	cells[0].getAttributes(),
	                                                 Color.orange);
	           GraphConstants.setOpaque(cells[0].getAttributes(), true);
	           DefaultPort port0 = new DefaultPort();
	           cells[0].add(port0);
	           createNode(cells);
	        		              DefaultEdge edge = new DefaultEdge();
	        		              edge.setSource(cells[0].getChildAt(0));
	        		              edge.setTarget(cells[1].getChildAt(0));
	        		              cells[2] = edge;
	        		              GraphConstants.setLineStyle(cells[2].getAttributes(), GraphConstants.STYLE_ORTHOGONAL);		              
	        		              GraphConstants.setRouting(cells[2].getAttributes(), GraphConstants.ROUTING_SIMPLE);
	        		              DefaultEdge edge2=new DefaultEdge();
	        		              edge2.setSource(cells[4].getChildAt(0));
	        		              edge2.setTarget(cells[0].getChildAt(0));
	        		     
	        		              cells[3]=edge2;
	        		              GraphConstants.setLineStyle(cells[3].getAttributes(), GraphConstants.STYLE_ORTHOGONAL);		              
	        		              GraphConstants.setRouting(cells[3].getAttributes(), GraphConstants.ROUTING_SIMPLE);
	        		         
	        		              //int arrow = GraphConstants.ARROW_CLASSIC;
	        		              //GraphConstants.setLineEnd(edge.getAttributes(), arrow);
	        		              //GraphConstants.setEndFill(edge.getAttributes(), true);
	        		              graph.getGraphLayoutCache().insert(cells);
	        		              JFrame frame = new JFrame();
	        		              frame.getContentPane().add(new JScrollPane(graph));
	        		              frame.pack();
	        		              frame.setVisible(true);
	        		        }

		private static void createNode(DefaultGraphCell[] cells) {

			cells[id] = new DefaultGraphCell(new String("World"));
	           GraphConstants.setBounds(cells[id].getAttributes(), new
	                Rectangle2D.Double(140,140,40,20));
				GraphConstants.setAutoSize(cells[id].getAttributes(), true);
				
	           GraphConstants.setIcon(cells[id].getAttributes(), new ImageIcon(("mouse.jpg")));//new ImageIcon(("nod.gif")));
	           /*GraphConstants.setGradientColor(

	        		   cells[id].getAttributes(),
	        		                         Color.red);
	        		              GraphConstants.setOpaque(cells[id].getAttributes(), true);*/
	        		              DefaultPort port1 = new DefaultPort();
	        		              cells[id].add(port1);
	        		              GraphConstants.setOffset(port1.getAttributes(), new Point2D.Double(0, GraphConstants.PERMILLE));
	        		              id++;
	        	
		}

		@Override
		public void valueChanged(GraphSelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

}
