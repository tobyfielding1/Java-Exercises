

import java.util.Date;


/**
 * a type of message used by client outside of message passing and so warrants its own class
 *
 */
public class Notification extends MsgToClient implements Comparable<Notification>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3107386977625679568L;


	public static enum TYPE{WELCOME, AUCTION_CREATED, SUCCESSFUL_BUY, SUCCESSFUL_SALE, FAILED_SALE, FAILED_BUY, OUTBID}
	/**
	 * @param type
	 * @param content
	 */
	public Notification(int userID, TYPE type, String content) {
		super(userID);
		this.type = type;
		this.content = content;
		this.date = new Date();
	}
	
	final TYPE type;
	final String content;
	final Date date;
	
	
	@Override
    public String toString() {
        return content;
    }


	public int compareTo(Notification arg1) {
		if(this.date.before(arg1.date))
			return 1;
		else
			return -1;
	}



}	