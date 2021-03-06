/**
 * Here we re-factor Paint1 to isolate the model as a separate 
 * class---GridModel.  Also we factor out PaintPanel and call it
 * GridPanel and place it in a separate top-level class.
 * 
 * @author Andrew Vardy
 */
package assign3;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;

public class Paint2 extends MouseAdapter implements ActionListener {

	GridModel model = new GridModel(100, 100);
	JFrame frame = new JFrame("Paint2");
	GridPanel gridPanel = new GridPanel(400, 400, model);
	JButton clearButton = new JButton("Clear");
        JToggleButton redButton = new JToggleButton("Red");
        JToggleButton greenButton = new JToggleButton("Green");
        JToggleButton blueButton = new JToggleButton("Blue");
        JToggleButton eraseButton = new JToggleButton("Erase");
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        CellColor color = CellColor.White;
        Stack<PaintCommand> undoStack = new Stack();
        Stack<PaintCommand> redoStack = new Stack();
	
	public Paint2() {
		// Initialize frame and add the paintPanel in the center
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(gridPanel);
                
		// Create a panel on the left for buttons and add
		// the buttons to it
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));	
		buttonPanel.add(clearButton);
                buttonPanel.add(redButton);
                buttonPanel.add(greenButton);
                buttonPanel.add(blueButton);
                buttonPanel.add(eraseButton);
                buttonPanel.add(undoButton);
                buttonPanel.add(redoButton);
		frame.add(buttonPanel, BorderLayout.WEST);
                
                // Can't undo or redo before anything is done
                undoButton.setEnabled(false);
                redoButton.setEnabled(false);
                          	
		// Setup event listeners.  In this case, Paint2 is the
		// listener for all component events.
		gridPanel.addMouseListener(this);
		gridPanel.addMouseMotionListener(this);
		
		frame.pack();
		frame.setResizable(false); // Must not be resizable because we aren't
		frame.setVisible(true);	   // handling changes in size.
                
                clearButton.addActionListener((ActionEvent e) -> {
                    PaintCommand com = new ClearCommand(model);
                    undoStack.push(com);
                    com.execute();
                    gridPanel.repaint();
                    
                    undoButton.setEnabled(true);
                    // Can't redo after making new change
                    redoStack.clear();
                    redoButton.setEnabled(false);
                });
                
                redButton.addActionListener((ActionEvent e) -> {
                    color = CellColor.Red;
                    eraseButton.setSelected(false);
                    redButton.setSelected(true);
                    greenButton.setSelected(false);
                    blueButton.setSelected(false);
                });
                
                greenButton.addActionListener((ActionEvent e) -> {
                    color = CellColor.Green;
                    eraseButton.setSelected(false);
                    redButton.setSelected(false);
                    greenButton.setSelected(true);
                    blueButton.setSelected(false);
                });
                
                blueButton.addActionListener((ActionEvent e) -> {
                    color = CellColor.Blue;
                    eraseButton.setSelected(false);
                    redButton.setSelected(false);
                    greenButton.setSelected(false);
                    blueButton.setSelected(true);
                });
                
                eraseButton.addActionListener((ActionEvent e) -> {
                    color = CellColor.White;
                    eraseButton.setSelected(true);
                    redButton.setSelected(false);
                    greenButton.setSelected(false);
                    blueButton.setSelected(false);
                });
                
                undoButton.addActionListener((ActionEvent e) -> {
                    PaintCommand com = undoStack.pop();
                    com.undo();
                    redoStack.push(com);
                    gridPanel.repaint();
                    
                    redoButton.setEnabled(true);
                    if (undoStack.isEmpty()) {
                        undoButton.setEnabled(false);
                    }
                        
                });
                
                redoButton.addActionListener((ActionEvent e) -> {
                    PaintCommand com = redoStack.pop();
                    com.undo();
                    undoStack.push(com);
                    gridPanel.repaint();
                    
                    undoButton.setEnabled(true);
                    if (redoStack.isEmpty()) {
                        redoButton.setEnabled(false);
                    }
                });
                
                // Set color to Red by default
                redButton.doClick();
	}
        
        @Override
	public void mousePressed(MouseEvent e) {
		int cellX = e.getX() / gridPanel.getCellWidth();
		int cellY = e.getY() / gridPanel.getCellHeight();
		
		if (cellX >= 0 && cellX < model.getWidth() && 
			cellY >= 0 && cellY < model.getHeight()) {
                    
                        ClickCommand com = new ClickCommand(cellX, cellY, color, model);
                        undoStack.push(com);
                        com.execute();
			gridPanel.repaint();
                        
                        undoButton.setEnabled(true);
                        // Can't redo after making new change
                        redoStack.clear();
                        redoButton.setEnabled(false);
		}
	}
	        
        @Override
	public void mouseDragged(MouseEvent e) {
		mousePressed(e);
	}
	
	public static void main(String[] args) throws Exception {		
		SwingUtilities.invokeLater(() -> {
                    new Paint2();
                });
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

