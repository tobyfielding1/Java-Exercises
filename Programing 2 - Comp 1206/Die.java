import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Die extends JPanel {
	
	Object[] values = new Object[6];// array of values, where each value is an array of points
	Integer[] value = new Integer[9];// array of integers representing each number (1 = point, 0 = no point)
	final int SIZE = 40;
	
	public void init(){
		JFrame window = new JFrame("ui");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		window.setContentPane(this);
		this.setLayout(null);
		this.initialiseValues();
		
		this.setSize(new Dimension(400,400));
		window.setSize(this.getSize());
		window.setVisible(true);
	}
	
	public void initialiseValues(){
		values[0] = new Integer[]{
				0,0,0,
				0,1,0,
				0,0,0};
		values[1] = new Integer[]{
				0,0,1,
				0,0,0,
				1,0,0};
		values[2] = new Integer[]{
				0,0,1,
				0,1,0,
				1,0,0};
		values[3] = new Integer[]{
				1,0,1,
				0,0,0,
				1,0,1};
		values[4] = new Integer[]{
				1,0,1,
				0,1,0,
				1,0,1};
		values[5] = new Integer[]{
				1,0,1,
				1,0,1,
				1,0,1};
	}
	
	public void updateVal(int i){
		value = (Integer[]) values[i-1];
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.setColor(Color.BLACK);
	    
	    /*
	     * i corresponds to rows for each value (0-2)
	     * j corresponds to columns (0-2)
	     */
	    for(int i =0;i<3;i++){
	    	for (int j =0;j<3;j++){
	    		if (value[j+i*3] == 1){
	    			int x = (j+1)*100;
	    			int y = (i+1)*100;
	    			g.fillOval(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
	    		}
	    	}
	    }
	}
	
	public static void main(String[] args) {
		Die panel = new Die();
		panel.init();
		
		while(true){
			for (int i = 1 ; i<=6; i++){
				panel.updateVal(i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

}
