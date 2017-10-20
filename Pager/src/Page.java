import java.util.Date;

public class Page implements Notification {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	Date date;
	String Sender;

	public Page(Date date, String message, String sender) {
		this.date = date;
		this.message = message;
		this.Sender = sender;
	}

}
