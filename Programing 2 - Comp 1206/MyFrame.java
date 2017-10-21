import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.JTextField;

public class MyFrame extends JFrame {
	
	JFrame window;
	
	public MyFrame(){
	window = new JFrame("Simple submit cancel form");
	window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
	
	JPanel panel = new JPanel();
	window.setContentPane(panel);
	
	panel.setLayout( new GridLayout(2,1));
	
	JButton submit = new JButton("Submit");
	JButton cancel = new JButton("Cancel");
	
	JPanel top = new JPanel();
	top.setLayout(new FlowLayout());
	
	top.add(new JTextField("text here ...				"));
	panel.add(top);
	JPanel bottom = new JPanel();
	bottom.setLayout(new FlowLayout());
	panel.add(bottom);
	bottom.add(submit);
	bottom.add(cancel);
	
	cancel.addActionListener(new MyCancelListener(this));
	window.setSize(500, 200);
	window.setVisible(true);
	}
	
	public void hide(){
		window.setVisible(false);
	}
	
	class MyCancelListener implements ActionListener {
		MyFrame frm;
		public MyCancelListener(MyFrame frame){
			frm = frame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			frm.hide();
		}
	}

}
