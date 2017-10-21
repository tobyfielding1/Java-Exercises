import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Circles extends JPanel {

	public final int HEIGHT= 400;
	public final int WIDTH = 800;
	int minWidth;
	
	public void init(int iter){
		minWidth = WIDTH/(iter*3);
		JFrame window = new JFrame("ui");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		window.setContentPane(this);
		this.setLayout(null);
		
		this.setSize(new Dimension(WIDTH,HEIGHT));
		window.setSize(this.getSize());
		window.setVisible(true);
	}
	/*
	 * array argument is 2 values specifying coordinates of start and end of horizontal line.
	 */
	public void drawCircle(Graphics g, int arr[]){
		float width = (arr[1]-arr[0]);
		if (Math.round(width) <= minWidth){
			return;
		}
		float diameter = width/3;
		g.fillOval(arr[0] + Math.round(width/2) - Math.round(diameter/2), HEIGHT/2 - Math.round(diameter/2), (int)diameter, (int)diameter);
		
		//right
		int[] rightSpace = {arr[0] + Math.round(width/3*2), arr[1]};
		drawCircle(g, rightSpace);
		
		//left
		int[] leftSpace = {arr[0], arr[1] - Math.round(width/3*2)};
		drawCircle(g, leftSpace);
		
		//middle
		int[] midSpace = {arr[0] + Math.round(width/3), arr[1] - Math.round(width/3)};
		drawCircle(g, midSpace);
	}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.setColor(new Color(1, 0, 0, new Float(0.5)));
	    int[] widthArr = {0,WIDTH};
	    drawCircle(g, widthArr);
	}
	
	public static void main(String[] args) {
		Circles panel = new Circles();
		System.out.print("Number of iterations:");
		Scanner in = new Scanner(System.in);
		int i = in.nextInt();
		panel.init(i);
	}

}
