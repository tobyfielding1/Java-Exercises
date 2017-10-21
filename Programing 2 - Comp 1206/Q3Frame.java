import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;



public class Q3Frame  extends JFrame {
	
	Boolean valueEntered = false;
	JComboBox fontList;
	ButtonGroup style;
	JTextField text;
	
	public Q3Frame(){
		JFrame window = new JFrame("Font Chooser");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		window.setContentPane(panel);
		
		panel.setLayout( new FlowLayout());
		
		
		style = new ButtonGroup();
		
		JCheckBox bold = new JCheckBox("Bold");
		JCheckBox italic = new JCheckBox("Italic");
		style.add(bold);
		style.add(italic);
		JPanel sPane = new JPanel();
		sPane.setLayout(new GridLayout(2,1));
		sPane.add(bold);
		sPane.add(italic);
		
		String[] fonts = {"Abadi MT Condensed Light","Albertus Extra Bold ","Albertus Medium ","Antique Olive ","Arial ","Arial Black ","Arial MT ","Arial Narrow ","Bazooka"}; 

		fontList = new JComboBox(fonts);
		
		panel.add(sPane);
		panel.add(fontList);
		JPanel pane3 = new JPanel();
		pane3.setLayout(new GridLayout());
		text = new JTextField("text here ...				");
		panel.add(pane3.add(text));
		
		JPanel pane4 = new JPanel();
		pane4.setLayout(new GridLayout());
		JButton ok = new JButton("OK");
		panel.add(pane4.add(ok));
		
		
		
		ok.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("font" + style.getSelection() +" "+ fontList.getSelectedItem() + "\n Text: " + text.getText() );	
			}
		});
		
		window.setSize(700, 200);
		window.setVisible(true);
		}

}
