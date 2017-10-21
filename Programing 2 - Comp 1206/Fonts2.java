import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

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


public class Fonts2  extends JFrame {
	
	JComboBox<String> fontList;
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
	Font[] fonts;
	String[] fontStrings;
	
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
		
		// gets fonts from system, so it will be different on different machines.
		fonts  = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		fontStrings = new String[fonts.length];	
		
		for (int i=0;i<fonts.length;i++){
			fontStrings[i]= fonts[i].getFontName();
		}
	
		fontList = new JComboBox(fontStrings);
		
		JPanel pane3 = new JPanel();
		pane3.setLayout(new GridLayout());
		text = new JTextField("text here ...				");
		
		JPanel pane4 = new JPanel();
		pane4.setLayout(new GridLayout());
		ok = new JButton("OK");
		
		panel.add(sPane);
		panel.add(fontList);
		panel.add(sPane);
		panel.add(pane3.add(text));
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
		
		
		text.setFont(fonts[fontList.getSelectedIndex()].deriveFont(styleInt, 15));
		text.setText("font: " + fontStrings[fontList.getSelectedIndex()]);
		
	}	
	
	public void go(){
		ok.addActionListener(new GetStateListener());
		bold.addActionListener(new GetStateListener());
		italic.addActionListener(new GetStateListener());
	
		window.setSize(700, 200);
		window.setVisible(true);
	}
		
	class GetStateListener implements ActionListener, ItemListener {
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			display();
			if(e.getSource().equals(ok))
				style.clearSelection();
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			display();
			
		}
	}

	public static void main(String[] args) {
		Fonts2 myFrame = new Fonts2();
		myFrame.init();
		myFrame.go();
		while(true){
			
		}

	}
}
