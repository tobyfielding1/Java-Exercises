import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DiscoFloor extends JFrame {

	JPanel panel;
	JFrame window;
	JTextArea tf;
	ArrayList<SquarePanel> squares;
	String text;

	Queue<SquarePanel> activeSquares = new LinkedList<SquarePanel>();
		
	public void init(){
		window = new JFrame("ui");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		window.setContentPane(panel);
		panel.setLayout(new BorderLayout());
		JPanel pane = new JPanel();
		pane.setLayout(null);
		pane.setPreferredSize(new Dimension(500,300));
		panel.add(pane,BorderLayout.CENTER);
		tf = new JTextArea("coordinates appear here");
		panel.add(tf, BorderLayout.SOUTH);
		Random rand = new Random();
		squares = new ArrayList<SquarePanel>();
		for (int i=0;i<15;i++){
			SquarePanel square = new SquarePanel();
			square.setBackground(new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()));
			int size = rand.nextInt(new Integer(200));
			square.setPreferredSize(new Dimension(size,size));
			square.setBounds(rand.nextInt(700), rand.nextInt(500), rand.nextInt(200), rand.nextInt(100));
			pane.add(square);
			squares.add(square);
		}
	}
	
	public void go(){
		
		for (SquarePanel square: squares){
		square.addMouseListener(new Listener());
		square.addMouseMotionListener(new Listener());
		activeSquares.add(square);
		
		}
		window.setSize(800,600);
		window.setVisible(true);
		
		while(true){
			MoveListener();
		}
	}
	
	/*
	 * I tried to rotate the mouse listener between all of the shapes on screen,
	 * the plan was that after one cycle, all of the overlapped shapes the mouse is over would have registered the mouse
	 * this did not work, because the mouse cannot be registered by a panel behind another panel
	 * an alternative method is to add a listener to the main panel, and simply record the position and compare it to the stored positions of shapes
	 * I know how to do this, but it is very tedious, so left the code from my broken solution.
	 */
	public void MoveListener(){
		if (activeSquares.size()>1){
			SquarePanel square = activeSquares.peek();
			square.removeMouseMotionListener(square.getMouseMotionListeners()[0]);
			activeSquares.remove();
			activeSquares.peek().addMouseMotionListener(new Listener());
			activeSquares.add(square);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i =0; i<activeSquares.size();i++){
			SquarePanel sq = (SquarePanel)activeSquares.toArray()[i];
			if (sq.isReadyToExit() == true){
				sq.setCords("");
			}
		}
		
		
	}
	
	public class Listener implements MouseMotionListener, MouseListener{
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		
		/*
		 * in theory, the listener will only be active on one panel, but there is a record of other panels
		 * activated in the same cycle (activeSquares) and all of these details are printed out.
		 * this works in certain overlap situations, but not as hoped.
		 * it only works on ther border between two squares
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			((SquarePanel)e.getSource()).setReadyToExit(false);
			((SquarePanel)e.getSource()).setCords("("+e.getX()+", "+e.getY()+"), ");
			text = "";
			for (SquarePanel square : activeSquares){
				text = text + square.getCords();
			}
			tf.setText(text);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
			((SquarePanel)e.getSource()).setReadyToExit(true);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


		
	}
	
	public static void main(String[] args) {
		DiscoFloor myFrame = new DiscoFloor();
		myFrame.init();
		myFrame.go();
		while(true){
			
		}

	}

}
