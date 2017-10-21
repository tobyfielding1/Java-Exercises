

import java.io.Serializable;
import java.util.Date;



public class Bid implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4679293996554669707L;
	/**
	 * @param amount
	 * @param userID
	 * @param itemID
	 * @param date
	 */
	public Bid(double amount, int userID, int itemID, Date date) {
		this.amount = amount;
		this.userID = userID;
		this.itemID = itemID;
		this.date = date;
	}
	final double amount;
	final int userID;
	final int itemID;
	final Date date;//date of bid creation
	
}
