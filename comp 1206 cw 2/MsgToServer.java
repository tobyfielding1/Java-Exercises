

import java.util.Date;

/**
 * messages are expected to be created, read, and thrown away. This enables use of final fields
 * use of final fields where possible makes things fast, safe and succinct
 * the constructors are specified to make communication foolproof; if the parameters aren't right, no message created;
 */
public abstract class MsgToServer extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8106990079801384706L;
	
	private byte[] cypherKey;
	
	public byte[] getKey() {
		return cypherKey;
	}

	public void setKey(byte[] key) {
		this.cypherKey = key;
	}

	static class requestItems extends MsgToServer{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @param userID
		 * @param sellerID
		 * @param cat
		 * @param itemID
		 * @param createdSince
		 * @param includeSold
		 */
		public requestItems(int userID, Integer sellerID, Category cat, Integer itemID, Date createdSince, boolean includeSold) {
			super();
			this.userID = userID;
			this.sellerID = sellerID;
			this.cat = cat;
			this.itemID = itemID;
			this.createdSince = createdSince;
			this.includeSold = includeSold;
		}
		final Integer sellerID;
		final Category cat;
		final Integer itemID;
		final Date createdSince;
		final boolean includeSold;
		
	}
	static class requestBidOnItems extends requestItems{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public requestBidOnItems(int userID, Integer sellerID, Category cat, Integer itemID, Date createdSince,
				boolean includeSold) {
			super(userID, sellerID, cat, itemID, createdSince, includeSold);
		}
	}
	
	static class Login extends MsgToServer{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		final String pwd;
		/**
		 * @param userID
		 * @param pwd
		 */
		public Login(int userID, String pwd){
			this.userID = userID;
			this.pwd = pwd;
		}
	}
	
	static class EndSession extends MsgToServer{
		

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EndSession(Integer userID){
			this.userID = userID;
		}
	}
	
	static class Register extends MsgToServer{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		final String first;
		final String family;
		final String pwd;
		
		/**
		 * @param first
		 * @param family
		 * @param pwd
		 */
		public Register(String first, String family, String pwd) {
			this.first = first;
			this.family = family;
			this.pwd = pwd;
		}
	}
	
	static class SellItem extends MsgToServer{
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * @param title
		 * @param description
		 * @param category
		 * @param start
		 * @param end
		 * @param reserve
		 * @param startPrice
		 */
		public SellItem(int userID, String title, String description, Category category, Date start, Date end, double reserve, double startPrice) {
			this.userID = userID;
			this.title = title;
			this.description = description;
			this.cat = category;
			this.start = start;
			this.end = end;
			this.reserve = reserve;
			this.startPrice = startPrice;
		}
		final String title;
		final String description;
		final Category cat;
		final Date start;
		final Date end;
		final double reserve;
		final double startPrice;
	
	}
	
	static class PlaceBid extends MsgToServer{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public PlaceBid(Integer userID, Bid b) {
			super();
			this.userID = userID;
			this.bid = b;
		}
		final Bid bid;
		
		
	}

}
