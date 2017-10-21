import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

/*
 * other listener types:
 * -mouseListener
 * -mouseMotionListener
 * 
 * ***can use MouseAdapter abstract as a shortcut
 * 
 * 
 * anonymous inner classes (name is the name of interface or superclass):
 * ie. new ActionListener() {
 * 		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(reset))
				 reset();
		}	
 * }
 */


public class ALFrame extends JFrame{

	JFrame window;
	JTextArea tf;
	Integer num = new Integer(0);
	
	public ALFrame(){
		window = new JFrame("ui");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		window.setContentPane(panel);
		
		panel.setLayout(new FlowLayout());
		
		JButton btn = new JButton("count 1");
		tf = new JTextArea();
		panel.add(tf);
		panel.add(btn);
		
		ButtonGroup font = new ButtonGroup();
		JRadioButton times = new JRadioButton("Times");
		times.setSelected(true);
		JRadioButton helvetica = new JRadioButton("helvetica");
		JRadioButton courier = new JRadioButton("courier");
		font.add(times);
		font.add(helvetica);
		font.add(courier);
		JPanel fPane = new JPanel();
		fPane.setLayout(new GridLayout(3,1));
		fPane.add(times);
		fPane.add(helvetica);
		fPane.add(courier);
		JLabel lbl = new JLabel("message");
		panel.add(lbl);
		
		panel.add(fPane);
		btn.addActionListener(new myButtonListener(this));
		times.addItemListener(new RadioListener(lbl));
		btn.addMouseListener(new MseListener(lbl));
		
		window.setSize(500, 200);
		window.setVisible(true);
		
		}
	public void add1(){
		tf.append((++num).toString());
	}
	
	public void take1(){
		tf.append((--num).toString());
	}
	
	public static void main(String[] args) {
		ALFrame frame1 = new ALFrame();

	}

}

class MseListener implements MouseListener{
	
	JLabel lbl;
	
	public MseListener(JLabel frm){
		this.lbl = frm;
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		int x = arg0.getX();
		int y = arg0.getY();
		lbl.setText("mouse entred at " + x + "," +y);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		lbl.setText("mouse exited");
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		lbl.setText("mouse released");
		
	}
	
	
}

class RadioListener implements ItemListener {
	
	JLabel lbl;
	
	public RadioListener(JLabel frm){
		this.lbl = frm;
		
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		lbl.setText("changed");
		
	}
	
}

class myButtonListener implements ActionListener, Runnable {

	ALFrame frame;
	
	public myButtonListener(ALFrame frm){
		frame = frm;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		frame.add1();
		try {
			Thread.sleep(5000);
			frame.take1();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
