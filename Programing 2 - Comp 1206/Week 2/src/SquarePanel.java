import javax.swing.JPanel;

public class SquarePanel extends JPanel {
	
	boolean readyToExit = false;
	
	public boolean isReadyToExit() {
		return readyToExit;
	}

	public void setReadyToExit(boolean readyToExit) {
		this.readyToExit = readyToExit;
	}

	private String cords="";
	
	public void setCords(String crd){
		cords = crd;
	}
	
	public String getCords(){
		return cords;
	}

}
