/*
 * GBMarqueeHandler.java
 *
 * (c) 2009 Michael Kipp, DFKI, Germany, kipp@dfki.de
 * Created on 18.05.2009, 17:25:09
 */
package de.embots.touchflow.gui;



import de.embots.touchflow.TouchFlow;
import de.embots.touchflow.gui.components.Descriptor;
import de.embots.touchflow.gui.components.GraphModul;
import de.embots.touchflow.gui.components.MyJGraph;
import de.embots.touchflow.gui.components.PinPort;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;



/**
 * @author Michael Kipp, Frederic Raber
 */
public class IXMarqueeHandler extends BasicMarqueeHandler
{

	private NodeStorage storage;

    // Holds the Start and the Current Point
    protected Point2D _start,  _current;
    protected PortView _port;      // current port
    protected PortView _firstPort; // source port
    
    //for Mac-fix
    private Object[] prevSelected;
    /**
     * Component that is used for highlighting cells if
     * the graph does not allow XOR painting.
     */
    protected JComponent _highlight = new JPanel();
    private JGraph _graph;
    private AbstractAction _delete;
    private AbstractAction showOptions;
    private ArrayList<JFrame> openDescriptors=new ArrayList<JFrame>();
    

    public IXMarqueeHandler(JGraph graph, final NodeStorage storage)
    {
        
        // Configures the panel for highlighting ports
        _highlight = createHighlight();
        _graph = graph;
        this.storage=storage;
        
        showOptions = new AbstractAction("Properties...")
        {
        	
            public void actionPerformed(ActionEvent e)
            {
            	openProperties();
            }
        };
        _delete = new AbstractAction("delete")
        {

            public void actionPerformed(ActionEvent e)
            {

            	//if (!_graph.isSelectionEmpty()) {
                    Object[] cells = _graph.getSelectionCells();
                    
                  //restore previously selected cells for mac
                    
                    if (Globals.useMacFix) {
                    	cells=prevSelected;
                    }
                   

                    cells = _graph.getDescendants(cells);
                    storage.removeCells(cells);
                    for (Object cell:cells){
	                    if (cell instanceof GraphModul){
	                    	DefaultEdge[] toRemove=TouchFlow.getConnectedEdges((GraphModul) cells[0]);
		                    
		                    //remove from graph
		                    _graph.getModel().remove(toRemove);
		                    //Remove edges from the storage
		                    for (DefaultEdge edge:toRemove){
		                    	TouchFlow.removeKante(edge);
		                    }
	                    }
                    }
                    
                    
                    _graph.getModel().remove(cells);
                   
                    
               // }
            }
        };
    }
    
    private void openProperties() {
		
    	Object[] cells = _graph.getSelectionCells();
        
    	//restore previously selected cells for mac
    	if (Globals.useMacFix) {
        	cells=prevSelected;
        }
    	for (Object o:cells){
    		if (o instanceof GraphModul){
    			GraphModul g=(GraphModul) o;
    			g.getCorrespondingModule().openOptions();
    		}
    	}
	}

    /**
     * Creates the component that is used for highlighting cells if
     * the graph does not allow XOR painting.
     */
    protected JComponent createHighlight()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        panel.setVisible(false);
        panel.setOpaque(false);

        return panel;
    }

    // Override to Gain Control (for PopupMenu and ConnectMode)
    @Override
    public boolean isForceMarqueeEvent(MouseEvent e)
    {
    	if (e.isShiftDown()) {
            return false;
        }
        // If Right Mouse Button we want to Display the PopupMenu
        if (SwingUtilities.isRightMouseButton(e)) // Return Immediately
        {
            return true;
        }
        else{
        	//double-click for open options
        	if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==2) return true;
        }
        // Find and Remember Port
        _port = getSourcePortAt(e.getPoint());
        // If Port Found and in ConnectMode (=Ports Visible)
        if (_port != null && _graph.isPortsVisible()) {
            return true;
        }
        // Else Call Superclass
        return super.isForceMarqueeEvent(e);
    }

    // Display PopupMenu or Remember Start Location and First Port
    @Override
    public void mousePressed(final MouseEvent e)
    {
        
    	
    	
    	//mac-fix
    	
    	prevSelected=_graph.getSelectionCells();
    	
    	//show Descriptor
    	

    	Object[] cells = _graph.getSelectionCells();
        
    	
    	
        if (Globals.autoShowDescriptor && SwingUtilities.isLeftMouseButton(e)){
            
        	//Zellen markiert-> descriptor anzeigen
        	if (cells.length!=0){
            	
	        	for (Object o:cells){
	            	if (o instanceof GraphModul) {
	            		openDescriptors.add(Descriptor.getDescription((GraphModul) o));
	            	}
	            }
            }
        	//keine markiert-> deskriptoren wegmachen
            else{
            	for (JFrame frame:openDescriptors){
            		frame.dispose();
            	}
            }
        }
    	
        if (e.getClickCount()==2){
        	//open properties for doubleclick
        	
        	openProperties();
    	}
    	
    	// If Right Mouse Button
        if (SwingUtilities.isRightMouseButton(e)) {
            // Find Cell in Model Coordinates
            Object cell = _graph.getFirstCellForLocation(e.getX(), e.getY());
            // Create PopupMenu for the Cell
            JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
            // Display PopupMenu
            menu.show(_graph, e.getX(), e.getY());
        // Else if in ConnectMode and Remembered Port is Valid
        } else if (_port != null && _graph.isPortsVisible()) {
            // Remember Start Location
            _start = _graph.toScreen(_port.getLocation());
            // Remember First Port
            _firstPort = _port;
        } else {
            // Call Superclass
            super.mousePressed(e);
        }
    }

    // Find Port under Mouse and Repaint Connector
    @Override
    public void mouseDragged(MouseEvent e)
    {
    	if (MyJGraph.secondMarkedPort!=getSourcePortAt(e.getPoint())){
    		//update markedPort
    		
    		MyJGraph.secondMarkedPort=getSourcePortAt(e.getPoint());
    		TouchFlow.updateGraph();
    	}
    	// If remembered Start Point is Valid
        if (_start != null) {
            // Fetch Graphics from Graph
            Graphics g = _graph.getGraphics();
            // Reset Remembered Port
            PortView newPort = getTargetPortAt(e.getPoint());
            // Do not flicker (repaint only on real changes)
            if (newPort == null || newPort != _port) {
                // Xor-Paint the old Connector (Hide old Connector)
                paintConnector(Color.black, _graph.getBackground(), g);
                // If Port was found then Point to Port Location
                _port =
                        newPort;
                if (_port != null) {
                    _current = _graph.toScreen(_port.getLocation());
                } // Else If no Port was found then Point to Mouse Location
                else {
                    _current = _graph.snap(e.getPoint());
                }
// Xor-Paint the new Connector

                paintConnector(_graph.getBackground(), Color.black, g);
            }

        }
        // Call Superclass
        super.mouseDragged(e);
    }

    public PortView getSourcePortAt(
            Point2D point)
    {
        // Disable jumping
        _graph.setJumpToDefaultPort(false);
        PortView result;

        try {
            // Find a Port View in Model Coordinates and Remember
            result = _graph.getPortViewAt(point.getX(), point.getY());
        } finally {
            _graph.setJumpToDefaultPort(true);
        }

        return result;
    }

// Find a Cell at point and Return its first Port as a PortView
    protected PortView getTargetPortAt(Point2D point)
    {
        // Find a Port View in Model Coordinates and Remember
        return _graph.getPortViewAt(point.getX(), point.getY());
    }

// Connect the First Port and the Current Port in the Graph or Repaint
    @Override
    public void mouseReleased(MouseEvent e)
    {
        highlight(_graph, null);

        // If Valid Event, Current and First Port
        if (e != null && _port != null && _firstPort != null && _firstPort != _port && isValidConnection(_firstPort, _port)) {
            // Then Establish Connection
        	
            makeEdge((Port) _firstPort.getCell(), (Port) _port.getCell());
            e.consume();

        // Else Repaint the Graph
        } else {
            _graph.repaint();
        }
// Reset Global Vars

        _firstPort = _port = null;
        _start =
                _current = null;
        // Call Superclass
        super.mouseReleased(e);
    }

    /**
     * Show Special Cursor if Over Port
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
    	MyJGraph.secondMarkedPort=null;
    	if (MyJGraph.markedPort!=getSourcePortAt(e.getPoint())){
    		//update markedPort
    		
    		MyJGraph.markedPort=getSourcePortAt(e.getPoint());
    		TouchFlow.updateGraph();
    	}
  
    	// Check Mode and Find Port
        if (e != null && getSourcePortAt(e.getPoint()) != null && _graph.isPortsVisible()) {
            // Set Cusor on Graph (Automatically Reset)
            _graph.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Consume Event
            // Note: This is to signal the BasicGraphUI's
            // MouseHandle to stop further event processing.
            e.consume();
        } else // Call Superclass
        {
            super.mouseMoved(e);
        }

    }

    // Use Xor-Mode on Graphics to Paint Connector
    protected void paintConnector(Color fg, Color bg, Graphics g)
    {
        if (_graph.isXorEnabled()) {
            // Set Foreground
            g.setColor(fg);
            // Set Xor-Mode Color
            g.setXORMode(bg);
            // Highlight the Current Port
            paintPort(_graph.getGraphics());

            drawConnectorLine(g);
        } else {
            Rectangle dirty = new Rectangle((int) _start.getX(), (int) _start.getY(), 1, 1);

            if (_current != null) {
                dirty.add(_current);
            }

            dirty.grow(1, 1);

            _graph.repaint(dirty);
            highlight(_graph, _port);
        }

    }

    // Overrides parent method to paint connector if
    // XOR painting is disabled in the graph
    @Override
    public void paint(JGraph graph, Graphics g)
    {
        super.paint(graph, g);

        if (!graph.isXorEnabled()) {
            g.setColor(Color.black);
            drawConnectorLine(g);
        }

    }

    protected void drawConnectorLine(Graphics g)
    {
        if (_firstPort != null && _start != null && _current != null) {
            // Then Draw A Line From Start to Current Point
            g.drawLine((int) _start.getX(), (int) _start.getY(),
                    (int) _current.getX(), (int) _current.getY());
        }

    }

    /**
     * Use the Preview Flag to Draw a Highlighted Port
     */
    protected void paintPort(Graphics g)
    {
        // If Current Port is Valid
        if (_port != null) {
            // check for types
            if (isValidConnection(_firstPort, _port)) {
                // If Not Floating Port...
                boolean o = (GraphConstants.getOffset(_port.getAllAttributes()) != null);
                // ...Then use Parent's Bounds
                Rectangle2D r = (o) ? _port.getBounds() : _port.getParentView().getBounds();
                // Scale from Model to Screen
                r =
                        _graph.toScreen((Rectangle2D) r.clone());
                // Add Space For the Highlight Border
                r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r.getHeight() + 6);
                // Paint Port in Preview (=Highlight) Mode
                _graph.getUI().paintCell(g, _port, r, true);
            }

        }
    }

    protected boolean isValidConnection(PortView sourceView, PortView targetView)
    {
    	
        Object source = sourceView.getCell();
        Object target = targetView.getCell();
        
        if (!(source instanceof PinPort || target instanceof PinPort)) return false;
        
    
        // gerichtete Kante
        
        if (!(((PinPort)source).getPin() instanceof OutputPin)){
        	return false;
        }
        if (!(((PinPort)target).getPin() instanceof InputPin)){
        	return false;
        }
        PinPort src=(PinPort) source;
        PinPort tar=(PinPort) target;
        
        return src.isCompatibleWith(tar);
    }

    /**
     * Highlights the given cell view or removes the _highlight if
     * no cell view is specified.ue;
     *
     * @param graph
     * @param cellView
     */
    protected void highlight(JGraph graph, CellView cellView)
    {
        if (cellView != null) {
            _highlight.setBounds(getHighlightBounds(graph, cellView));

            if (_highlight.getParent() == null) {
                graph.add(_highlight);
                _highlight.setVisible(true);
            }

        } else {
            if (_highlight.getParent() != null) {
                _highlight.setVisible(false);
                _highlight.getParent().remove(_highlight);
            }

        }
    }

    /**
     * Returns the bounds to be used to _highlight the given cell view.
     *
     * @param graph
     * @param cellView
     * @return
     */
    protected Rectangle getHighlightBounds(JGraph graph, CellView cellView)
    {
        boolean offset = (GraphConstants.getOffset(cellView.getAllAttributes()) != null);
        Rectangle2D r = (offset) ? cellView.getBounds() : cellView.getParentView().getBounds();
        r =
                graph.toScreen((Rectangle2D) r.clone());
        int s = 3;

        return new Rectangle((int) (r.getX() - s), (int) (r.getY() - s),
                (int) (r.getWidth() + 2 * s), (int) (r.getHeight() + 2 * s));
    }

// Hook for subclassers
    public Map createEdgeAttributes()
    {
        Map map = new Hashtable();
        // Add a Line End Attribute
        GraphConstants.setLineEnd(map, GraphConstants.ARROW_TECHNICAL);
        //GraphConstants.setLineBegin(map, GraphConstants.ARROW_TECHNICAL);
        // Add a label along edge attribute
        GraphConstants.setLabelAlongEdge(map, true);
        GraphConstants.setEditable(map, false);
        return map;
    }

// Insert a new Edge between source and target
    public void makeEdge(Port source, Port target)
    {
        // Construct Edge with no label
        DefaultEdge edge = new DefaultEdge();
        if (_graph.getModel().acceptsSource(edge, source) && _graph.getModel().acceptsTarget(edge, target)) {
        	// Create a Map thath holds the attributes for the edge
        	edge.getAttributes().applyMap(createEdgeAttributes());
            // Insert the Edge and its Attributes
            _graph.getGraphLayoutCache().insertEdge(edge, source, target);
            
            edge.setSource(source);
            edge.setTarget(target);
            storage.addKante(edge);
        }

    }

    public JPopupMenu createPopupMenu(
            final Point pt,
            final Object cell)
    {
        JPopupMenu menu = new JPopupMenu();
        // Remove
        if (!_graph.isSelectionEmpty()) {
            menu.add(new AbstractAction("Delete")
            {

                public void actionPerformed(ActionEvent e)
                {
                    _delete.actionPerformed(e);
                }
            });
        }
        
        //options
        
     // Remove
        if (!_graph.isSelectionEmpty()) {
            menu.add(new AbstractAction("Properties...")
            {

                public void actionPerformed(ActionEvent e)
                {
                    showOptions.actionPerformed(e);
                }
            });
        }

        return menu;
    }
}
