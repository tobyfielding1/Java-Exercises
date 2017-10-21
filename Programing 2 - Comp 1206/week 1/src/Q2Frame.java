import java.awt.FlowLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;



public class Q2Frame extends JFrame {
	public Q2Frame(){
		JFrame window = new JFrame("Font Chooser");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		window.setContentPane(panel);
		
		panel.setLayout( new FlowLayout());
		
		
		ButtonGroup style = new ButtonGroup();
		
		JCheckBox bold = new JCheckBox("Bold");
		JCheckBox italic = new JCheckBox("Italic");
		style.add(bold);
		style.add(italic);
		JPanel sPane = new JPanel();
		sPane.setLayout(new GridLayout(2,1));
		sPane.add(bold);
		sPane.add(italic);
		
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
		
		
		panel.add(sPane);
		panel.add(fPane);
		JPanel pane3 = new JPanel();
		pane3.setLayout(new GridLayout());
		panel.add(pane3.add(new JTextField("text here ...				")));
		
		JPanel pane4 = new JPanel();
		pane4.setLayout(new GridLayout());
		JButton ok = new JButton("OK");
		panel.add(pane4.add(ok));
		
		
		
		ok.addActionListener(new MyOkListener());
		window.setSize(700, 200);
		window.setVisible(true);
		}
		
		class MyOkListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		}

}
