
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class Shapes extends JFrame {
	
	
	abstract class ShapePanel extends JPanel{
		
		Color myColor;
		final int SIZE = 100;
		Random rand = new Random();
		
		public ShapePanel(){
			myColor = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
			this.addMouseListener(new ClickListener());
			this.setPreferredSize(new Dimension(SIZE,SIZE));
		}
		
		public void changeColor(){
			myColor = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
			this.repaint();
		}
		
		public void paintComponent(Graphics g) {
			g.setColor(myColor);
			super.paintComponent(g);
		}
		
		class ClickListener extends MouseAdapter{
			
			public void mouseClicked(MouseEvent e) {
				((ShapePanel)e.getSource()).changeColor();
			}
		}
	}
	
	class Circle extends ShapePanel{
		public void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    g.fillOval(0, 0, SIZE, SIZE);
		}
	}
	
	class Square extends ShapePanel{
		public void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    g.fillRect(0,0, SIZE,SIZE);
		}
	}
	
	public void init(int numSquares, int numCircles){
		JFrame window = new JFrame("ui");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		window.setContentPane(panel);
		panel.setLayout(new FlowLayout());
		
		for(int i=0;i<numSquares;i++){
			panel.add(new Circle());
		}
		for(int i=0;i<numCircles;i++){
			panel.add(new Square());
		}
		
		window.setSize(600, 400);
		window.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		Shapes myFrame = new Shapes();
		myFrame.init(2,2);
		while(true){}
	}
	
}