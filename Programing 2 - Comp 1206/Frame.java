import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Frame extends JFrame {
	
	JPanel panel;
	JFrame window;
	JTextArea tf;
	JButton inc;
	JButton reset;
	Integer number;
	
	public void increment(){
		number = number + 1;
		tf.setText(number.toString());
	}
	
	public void reset(){
		number = 0;
		tf.setText(number.toString());
	}
	
	public Frame(){
		number = 0;
	}
	
	public void init(){
		window = new JFrame("ui");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		window.setContentPane(panel);
		panel.setLayout(new FlowLayout());
		
		inc = new JButton("Increment");
		reset = new JButton("Reset");
		tf = new JTextArea();

		panel.add(inc);
		panel.add(reset);
		panel.add(tf);
	}
	
	public void go(){
		reset.addActionListener(new BtnListener());
		inc.addActionListener(new BtnListener());
		
		window.setSize(500, 200);
		window.setVisible(true);
	}
	
	public class BtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(reset))
				 reset();
			else if (e.getSource().equals(inc))
				increment();
		}
		
	}
	
	public static void main(String[] args) {
		Frame myFrame = new Frame();
		myFrame.init();
		myFrame.go();
		while(true){
			
		}

	}

}
