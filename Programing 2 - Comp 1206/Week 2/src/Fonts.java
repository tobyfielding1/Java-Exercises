import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;


public class Fonts  extends JFrame {
	
	Boolean valueEntered = false;
	JComboBox fontList;
	ButtonGroup style;
	JTextField text;
	JButton ok;
	ButtonGroup font;
	JRadioButton helvetica;
	JRadioButton courier;
	JRadioButton times;
	JCheckBox bold;
	JCheckBox italic;
	
	JFrame window;
	private JRadioButton ttthelvetica;
	public Fonts(){
		
	}
	
	public void init(){
		window = new JFrame("Font Chooser");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		window.setContentPane(panel);
		
		panel.setLayout( new FlowLayout());
		
		style = new ButtonGroup();
		bold = new JCheckBox("Bold");
		italic = new JCheckBox("Italic");
		style.add(bold);
		style.add(italic);
		JPanel sPane = new JPanel();
		sPane.setLayout(new GridLayout(2,1));
		sPane.add(bold);
		sPane.add(italic);
		bold.setName("bold");
		italic.setName("italic");
		
		font = new ButtonGroup();
		times = new JRadioButton("Times");
		times.setSelected(true);
		helvetica = new JRadioButton("helvetica");
		courier = new JRadioButton("courier");
		font.add(times);
		font.add(helvetica);
		font.add(courier);
		times.setName("times");
		helvetica.setName("helvetica");
		courier.setName("courier");
		
		JPanel fPane = new JPanel();
		fPane.setLayout(new GridLayout(3,1));
		fPane.add(times);
		fPane.add(helvetica);
		fPane.add(courier);
		
		panel.add(sPane);
		panel.add(fPane);
		JPanel pane3 = new JPanel();
		pane3.setLayout(new GridLayout());
		text = new JTextField("text here ...				");
		
		panel.add(pane3.add(text));
		
		JPanel pane4 = new JPanel();
		pane4.setLayout(new GridLayout());
		ok = new JButton("OK");
		panel.add(pane4.add(ok));
	}
	
	public void display(){
		int styleInt= Font.PLAIN;
		String fontString = null;
		
		if (style.getSelection() != null){
			if (bold.isSelected())
				styleInt = Font.BOLD;
			if (italic.isSelected())
				styleInt = Font.ITALIC;	
		}
		
		
		if (courier.isSelected())
			fontString = "courier";
		if (times.isSelected())
			fontString = "times";
		if (helvetica.isSelected())
			fontString = "helvetica";
		
		text.setFont(new Font(fontString, styleInt,15));
		text.setText("font: " +  fontString);
	}	
	
	public void go(){
		ok.addActionListener(new GetStateListener());
		courier.addActionListener(new GetStateListener());
		times.addActionListener(new GetStateListener());
		helvetica.addActionListener(new GetStateListener());
		bold.addActionListener(new GetStateListener());
		italic.addActionListener(new GetStateListener());
	
		window.setSize(700, 200);
		window.setVisible(true);
	}
		
	class GetStateListener implements ActionListener, ItemListener {
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			display();
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			display();
			
		}
	}

	public static void main(String[] args) {
		Fonts myFrame = new Fonts();
		myFrame.init();
		myFrame.go();
		while(true){
			
		}

	}
}
